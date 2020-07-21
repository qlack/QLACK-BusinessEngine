package com.eurodyn.qlack.be.forms.management;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.descriptor.web.*;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.xml.sax.InputSource;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Implementation of {@link ServletContextInitializer} to register the servlets, listeners and filters defined
 * in the application's web.xml on startup.
 * <p>
 * TODO servlets' init-params are not supported
 * TODO filters are not registered
 */
@Slf4j
public class WebXmlBridge implements ServletContextInitializer {

    private static final String WEB_XML_PATH = "/web.xml";

    @Override
    public void onStartup(ServletContext servletContext) {

        WebXml webXml = parseWebXml();

        registerContextParams(webXml, servletContext);
        registerServlets(webXml, servletContext);
        registerFilters(webXml, servletContext);
        registerListeners(webXml, servletContext);

        log.debug("****************************************************************");
        log.debug("Registered Servlets: ");
        for (ServletRegistration s : servletContext.getServletRegistrations().values()) {
            log.debug("Servlet: [{}] --- Mappings: [{}]", s.getName(), s.getMappings());
        }
        log.debug("Registered Filters: " + servletContext.getFilterRegistrations().values().toString());
        for (FilterRegistration f : servletContext.getFilterRegistrations().values()) {
            log.debug("Servlet: [{}] --- URL Pattern Mappings: [{}]", f.getName(), f.getUrlPatternMappings());
        }
        log.debug("Registered Init Params: " + servletContext.getInitParameterNames().toString());
        log.debug("****************************************************************");
    }

    private void registerContextParams(WebXml webXml, ServletContext servletContext) {
        webXml.getContextParams().forEach((k, v) -> {
            servletContext.setInitParameter(k, v);
            log.debug("Registered context param with name [{}] and value [{}]", k, v);
        });
    }

    private WebXml parseWebXml() {
        URL resource;

        try {
            resource = WebXmlBridge.class.getResource(WEB_XML_PATH);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        WebXml webXml = new WebXml();

        WebXmlParser parser = new WebXmlParser(false, false, false);
        parser.setClassLoader(WebXmlBridge.class.getClassLoader());

        try (InputStream is = resource.openStream()) {
            boolean success = parser.parseWebXml(new InputSource(is), webXml, false);
            if (!success) {
                throw new IllegalStateException("Error parsing " + WEB_XML_PATH);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error reading " + WEB_XML_PATH, e);
        }

        return webXml;
    }

    /**
     * Reads servlets defined in web.xml and registers them into the application.
     *
     * @param webXml a web.xml object
     * @param servletContext the servlet context
     */
    private void registerServlets(WebXml webXml, ServletContext servletContext) {
        Map<String, String> servletMappings = webXml.getServletMappings();

        for (ServletDef def : webXml.getServlets().values()) {
            String servletName = def.getServletName();

            List<String> mappings = findMappings(servletMappings, servletName);

            if (mappings.isEmpty()) {
                throw new IllegalStateException("Not mapping defined for " + servletName);
            }

            for (String mapping : mappings) {
                ServletRegistration.Dynamic reg;

                if (servletContext.getServletRegistration(servletName) == null) {

                    reg = servletContext.addServlet(servletName, def.getServletClass());
                } else {
                    reg = (ServletRegistration.Dynamic) servletContext.getServletRegistration(servletName);
                }

                reg.addMapping(mapping);

                reg.setInitParameters(def.getParameterMap());
            }

            log.debug("Registered servlet with name [{}] Class[{}] Mappings[{}] and init params [{}]", servletName, def.getServletClass(),
                    mappings, def.getParameterMap().values().toString());
        }
    }

    private List<String> findMappings(Map<String, String> servletMappings, String servletName) {
        List<String> mappings = new ArrayList<>();

        for (Map.Entry<String, String> mapping : servletMappings.entrySet()) {
            if (mapping.getValue().equals(servletName)) {
                mappings.add(mapping.getKey());
            }
        }

        return mappings;
    }

    private List<FilterMap> findFilterMappings(Set<FilterMap> filterMappings, String filterName) {
        List<FilterMap> mappings = new ArrayList<>();

        for (FilterMap mapping : filterMappings) {
            if (mapping.getFilterName().equals(filterName)) {
                mappings.add(mapping);
            }
        }

        return mappings;
    }

    /**
     * Method to read filters defined in web.xml and to register them into the application.
     */
    private void registerFilters(WebXml webXml, ServletContext servletContext) {
        Set<FilterMap> filterMappings = webXml.getFilterMappings();

        for (FilterDef def : webXml.getFilters().values()) {
            String filterName = def.getFilterName();

            List<FilterMap> mappings = findFilterMappings(filterMappings, filterName);

            if (mappings.isEmpty()) {
                throw new IllegalStateException("Not mapping defined for " + filterName);
            }


            for (FilterMap mapping : mappings) {
                FilterRegistration.Dynamic registeredFilter;
                if (servletContext.getFilterRegistration(filterName) == null) {
                    registeredFilter = servletContext.addFilter(def.getFilterName(), def.getFilterClass());
                } else {
                    registeredFilter = (FilterRegistration.Dynamic) servletContext.getFilterRegistration(def.getFilterName());
                }

                registeredFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, mapping.getURLPatterns());
                registeredFilter.setInitParameters(def.getParameterMap());
            }


            log.debug("Registered filter with name [{}] , Init params[{}] and Mappings[{}] ", filterName, def.getParameterMap().values().toString(), mappings);


        }
    }

    /**
     * It reads listeners defined in web.xml and registers them into the application.
     */
    private void registerListeners(WebXml webXml, ServletContext servletContext) {
        for (String listener : webXml.getListeners()) {
            servletContext.addListener(listener);
            log.debug("Registered listener class [{}]", listener);
        }
    }

}
