# Orbeon Forms integration

## Architecture 
The architecture of Qlack Business Engine forms module is described in the following diagram.

![qbe-forms-arch](https://github.com/qlack/QLACK-BusinessEngine/tree/master/qlack-be-forms/qlack-be-forms-server/src/main/resources/images/qbe-forms-arch.png)




## Running orbeon under an uber-jar file of a Spring-boot app.

To run orbeon-forms combined with a spring boot app the following requirements are to be met:

1) The folders `apps`, `config` and file `web.xml` found in the `orbeon.war/WEB-INF` folder should be placed under `resources` folder of our Spring boot app.

2) The `DispatcherServlet` should be declared in the web.xml. Its mapping will be the context-path of our spring boot app. Orbeon-forms is set by default to run on root `/` context path.

```xml
<servlet-mapping>
    <servlet-name>test</servlet-name>
    <url-pattern>/test/*</url-pattern>
</servlet-mapping>

<servlet>
    <servlet-name>test</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value></param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
```

3) In case of an angular app change the <base href=""> attribute to your spring boot app context. i.e.

```html 
  <base href="/test/">
```

**If `proxy.conf.json` is used make sure to change the context there as well.** 

4) A `bridge` should be used to register all `web.xml` servlets, filters, mappings etc to the spring boot app servlet context. You can find an example at the end of this file (see `WebXmlBridge.java`)

5) Declare the `bridge` bean in your Spring application main class:

``` java
 @Bean
  public WebXmlBridge webXmlBridge() {
    return new WebXmlBridge();
  }
```



## Custom persistence implementation

Since orbeon Community Edition (CE) does not support Oracle database as a predefined Persistence API implementation we implement and use our own custom persistence API. 

To have orbeon call our custom persistence implementation endpoints for all CRUD and Search operations we need to enable custom persistence:

**1)** Disable default integrated persistence provider for eXist database. The steps: 

* Disable the embedded eXist database by adding the following to properties-local.xml :

```xml
<property
    as="xs:boolean"
    name="oxf.fr.persistence.exist.active"
    value="false"/>
```

* Delete all exist-* jar files (the embedded eXist implementation and its dependencies) from `/resources/lib/exist-*.jar`

* Remove all `<servlet>`, `<servlet-mapping>`, `<filter>`, and `<filter-mapping>` eXist references in `web.xml`.

**2)** Include the following properties to our `properties-local.xml` file under `/resources/config` folder of our application.

```xml
    <!-- Tell orbeon our custom implementation provider name -->
    <property as="xs:string" name="oxf.fr.persistence.provider.*.*.*"
              value="qbe"/>
    <!-- Tell orbeon our custom implementation provider is enabled -->
    <property as="xs:boolean"
              name="oxf.fr.persistence.qbe.active"
              value="true"/>
    <!-- Set the base uri of our app here. This must be set so orbeon-forms and our custom 		 persistence implementation run together at the same host -->
    <property as="xs:anyURI"
              name="oxf.url-rewriting.service.base-uri"
              value="http://localhost:8080/"/>
    <!-- Set the url of our custom implementation peristence endpoints -->
    <property as="xs:anyURI" name="oxf.fr.persistence.qbe.uri"
              value="test/api/forms"/>
```

The custom persistence API implementation lies within QBE and is served via REST services endpoints. This includes the endpoints that save form data and file attachments or uploaded files.

Our custom persistence API implementation is described by the following endpoints:

```java
  // Retrieves form metadata and templating information
  @GetMapping({"/form/{app}", "/form/{app}/{form}"})

  // Retrieves stored form data.
  @GetMapping("/crud/{app}/{form}/data/{documentId}/data.xml")
  
  // Retrieves an attachment for a draft form definition or for a published form saved data.
  @GetMapping(value = "/crud/{app}/{form}/data/{documentId}/{attachmentName:[0-9a-z]+.bin}", produces = "application/octet-stream")
 
  // Stores or updates an attachment for a draft form definition or for a published form saved data.
  @PutMapping(value = "/crud/{app}/{form}/data/{documentId}/{attachmentName:[0-9a-z]+.bin}")

  // Retrieves a published form XML definition.
  @GetMapping("/crud/{app}/{form}/form/form.xhtml")
  
  // Saves the data the user fills in a rendered published form.
  @PutMapping("/crud/{app}/{form}/data/{documentId}/data.xml")
  
  // Publishes a form by saving it as a published form definition.
  @PutMapping(value = "/crud/{app}/{form}/form/form.xhtml", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
  
  // Retrieves an attachment for a published form definition.
  @GetMapping(value = "/crud/{app}/{form}/form/{attachmentName:[0-9a-z]+.bin}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  
  // Stores an attachment for a published form definition.
  @PutMapping(value = "/crud/{app}/{form}/form/{attachmentName:[0-9a-z]+.bin}", produces = MediaType.APPLICATION_XHTML_XML_VALUE)
```

In general orbeon consists of two basic modules. Forms builder (fb) and forms runner (fr). Orbeon Persistence API endpoints consist of one or more of the following parameters. An application name, a form name, a document id, an attachment name. Although these are self-explanatory, the application name is the name of the application your forms will be associated with. The form name is the name of each form. The document id is a unique id that refers to forms draft definitions or saved form data records. The attachment name is only found in case of attachments and it is a randomly generated alpharithmetic string that ends with the ".bin" file extension as it is a binary file. The `data.xml` refers to a draft form definition or saved form data, whereas the `form.xhtml` refers to a published form definition.

## Run JavaScript function through orbeon form runner button triggered actions

You can execute custom js code by using the navigate action in the `properties-local.xml` to declare the name of the callback function. This function must be declared as a child script node in your form definition xml ` <head>` element.

**properties-local.xml**

```  xml
<property as="xs:string" name="oxf.fr.detail.process.save.*.*">  
    save
    then navigate(uri="javascript:notifyParentWindow()")
    recover error-message(message="Save Failed")
</property>
```

**Form definition:**

```xml
...
    <script type="text/javascript">   function notifyParentWindow() {
          var id = ORBEON.xforms.Document.getValue("fr-parameters-instance-document");
          parent.submitDone(id);
        };
    </script>
</xh:head>
```

In an end application, forms should be rendered in an iframe object. To notify the parent window on data submission of a rendered form you should declare the `submitDone()` function in the global window object as shown above. If your end application is an Angular application you could do the following and bind `submitDone` function to your own custom function in your controller:

```typescript
constructor(public http: HttpClient, @Inject(WINDOW) public window: Window) {
    // Register function
    (<any>window).submitDone = this.submitDone.bind(this);
  }

  ngOnDestroy() {
    // Unregister function
    delete (<any>window).submitDone;
  }

  submitDone(id: string) {
    alert("Bound function: Form submitted, id: " + id);
  }
```



## Appearance

It is possible to change the appearance of orbeon-forms builder and runner using  the `oxf.fr.css.custom.uri.*.*` property in your `properties-local.xml` file. The `qbe.css` is at `/resources/forms/qbe/assets/qbe.css` in the following example.

```xml
<property as="xs:string" name="oxf.fr.css.custom.uri.*.*">
        /forms/qbe/assets/qbe.css
    </property>
```



You can change  the layout of the detail page to `fluid` / `fixed` using the following:

```xml
<!-- Change detail page layout to fluid/fixed -->
<property
            as="xs:string"
            name="oxf.fr.detail.html-page-layout.*.*"
            value="fluid"/>
```

You can change the logo of the forms builder:

```xml
 <property as="xs:anyURI" name="oxf.fr.default-logo.uri.*.*" value=""/>
```



You can show / hide buttons on form builder and runner pages: 

```xml 
<!-- Choose which buttons will be shown in form builder details page -->
    <!-- i.e. summary new test publish  -->
    <property as="xs:string" name="oxf.fr.detail.buttons.orbeon.builder">
        save summary new test publish
    </property>

<!-- Choose which buttons will be shown in form runner published form details page -->
    <!-- i.e. close clear print pdf save submit  -->
    <property
            as="xs:string"
            name="oxf.fr.detail.buttons.*.*"
            value="close clear print pdf save submit"/>
```



To avoid css being minified during development use:

```xml 
 <property
            as="xs:boolean"
            name="oxf.xforms.minimal-resources"
            value="false"/>

    <property
            as="xs:boolean"
            name="oxf.xforms.combine-resources"
            value="false"/>
```



## Logging configuration for orbeon-forms (properties-local.xml)

```xml 
<!-- Most comprehensive logging configuration -->
    <property as="xs:NMTOKENS" name="oxf.xforms.logging.debug">
        document
        model
        submission
        control
        event
        action
        analysis
        server
        server-body
        html
        process
        submission-details
        submission-body
    </property>
```



## Orbeon forms core dependencies

It seems that the core project dependencies are all in one file used for dependency management in Scala named  build.sbt. Scala has a class called make-pom that generates a pom.xml for maven publishing.

## Qlack 2 Business Engine implementation for orbeon-forms 

Qlack 2 BE used orbeon-forms by deploying it in a war file together with a custom persistence implementation. The custom persistence implementation (CPI) is based on REST API endpoints according to orbeon-forms requirements and documentation at the time. Qlack 2 BE uses only the basics of the builder and runner features. A Qlack Webdesktop app  opens a popup window that loads orbeon-forms builder. The user then builds the desired form and when done closes the window. The produced xml is copied to the Webdesktop app and saved to the database. The forms versioning, management etc is handled exclusively by the  Webdesktop app. When a user requests a form, the CPI is looking for  the requested form and before serving it to the user it enriches the form xml with translation values by substituting predefined markers.

### Translations 

If a word needs to be translated to different languages the user must predefine a marker in its place while building the form in orbeon-forms builder i.e. `%translation.formTitle%`. QBE uses the form version name to find the equivalent form Qlack Lexicon group since for each form version a lexicon group is created. Using the group id, QBE finds the children `keys` and returns their translations in a list. QBE then uses this list to create a pattern of the format "%(cat|beverage)%" for the keys and tries to match this with the predefined markers. If any match is found it replaces the marker with the appropriate value.

Orbeon forms supports form localization only in the Professional Edition (PE)

### Integration with qbe-rules

Execution of rules is supported. A `RulesRuntimeManagementService` can be set so that pre-condition, validation and post-condition rules be executed before the form is submitted or retrieved.

**Difference between validation and default validation rules**

It seems that during validations rules execution qbe always take into account both types (defined as enum) of  validation rules, so the only difference between them would be semantic.

## Feedback on Qlack2 qbe

1) Repeated rows in lists didn't work.
2) Columns over 4 not supported. **Need to confirm for latest version.**
3) XSD schema bind with elements didn't work, elements were introduced in the model inside the code.
4) Latest version functionality unclear. What we really need is an "Active version" feature. "Finalised version" should be kept since it is needed to distinguish customer translated features.
5) The textarea for forms, workflow, rules code in our webdesktop app should be bigger.
6) Integration between workflow and rules should be available with a specific version of rules. Up to now the "latest version" of rules is used.
7) Subflows were not supported in workflows. Each time a workflow should call a subflow, it would be re-introduced in the code.
8) Review import/export process. It was too complicated for the client.

## Deployment solutions

### Dockerized Uber jar  (preferred) 

In this scenario Orbeon forms is deployed bundled with a Spring boot application in an Uber-jar file. Main advantage here is the possibility to `dockerize` the application allowing for a quick, safe and scalable deployment solution.

### War

1. **Add ServletInitializer class. Probably should be enabled under a @Profile.**
2. **Enable the following resources plugin configuration for maven-resources plugin. Probably should be under a maven profile.**

```xml
<plugin>
                <artifactId>maven-resources-plugin</artifactId>
                <configuration>
                    <nonFilteredFileExtensions>
                        <nonFilteredFileExtension>jar</nonFilteredFileExtension>
                    </nonFilteredFileExtensions>
                </configuration>
                <executions>
                    <execution>
                        <id>default-copy-resources</id>
                        <phase>process-resources</phase>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <overwrite>true</overwrite>
                            <outputDirectory>
                                ${project.build.directory}/${project.artifactId}-${project.version}/WEB-INF/lib
                            </outputDirectory>
                            <resources>
                                <resource>
                                    <directory>${project.basedir}/src/main/resources/lib</directory>
                                    <filtering>false</filtering>
                                </resource>
                            </resources>
                        </configuration>

                    </execution>
                    <execution>
                        <id>default-copy-resources2</id>
                        <phase>process-resources</phase>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <overwrite>false</overwrite>
                            <outputDirectory>
                                ${project.build.directory}/${project.artifactId}-${project.version}/WEB-INF/forms
                            </outputDirectory>
                            <resources>
                                <resource>
                                    <directory>${project.basedir}/src/main/resources/forms</directory>
                                    <filtering>false</filtering>
                                </resource>
                            </resources>
                        </configuration>

                    </execution>
                    <execution>
                        <configuration>
                            <outputDirectory>${project.build.directory}/classes/resources/</outputDirectory>
                            <resources>
                                <resource>
                                    <directory>${project.parent.basedir}/qlack-base-application-ui/target/dist
                                    </directory>
                                </resource>
                            </resources>
                        </configuration>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <id>copy-resources</id>
                        <phase>validate</phase>
                    </execution>
                </executions>
            </plugin>
```

We can either run orbeon on root path by adding this entry to server.xml on Tomcat. This way we don't need to change  the custom persistence implementation uri and the base href path.  Or we can run orbeon in /orbeon and change these properties accordingly.

```xml
 <Host name="localhost"  appBase="webapps"
            unpackWARs="true" autoDeploy="true">
		<Context path="" docBase="orbeon">
    <WatchedResource>WEB-INF/web.xml</WatchedResource>

    <Resource
        name="jdbc/mysql"
        driverClassName="com.mysql.jdbc.Driver"

        auth="Container"
        type="javax.sql.DataSource"

        initialSize="3"
        maxActive="10"
        maxIdle="10"
        maxWait="30000"

        poolPreparedStatements="true"

        testOnBorrow="true"
        validationQuery="select 1"

        username="root"
        password="root"
        url="jdbc:mysql://localhost:3306/qbe"/>

</Context>
          <!-- SingleSignOn valve, share authentication between web applications
             Documentation at: /docs/config/valve.html 
    
        <Valve className="org.apache.catalina.authenticator.SingleSignOn" />
        -->

        <!-- Access log processes all example.
             Documentation at: /docs/config/valve.html
             Note: The pattern used is equivalent to using pattern="common" -->
        <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
               prefix="localhost_access_log" suffix=".txt"
               pattern="%h %l %u %t &quot;%r&quot; %s %b" />

      </Host>
```


# APPENDIX A

#### WebXmlBridge.java

```java 
package com.eurodyn.qlack.be.forms.management;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.apache.tomcat.util.descriptor.web.ServletDef;
import org.apache.tomcat.util.descriptor.web.WebXml;
import org.apache.tomcat.util.descriptor.web.WebXmlParser;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link ServletContextInitializer} to register the servlets, listeners and
 * filters defined in the application's web.xml on startup.
 * <p>
 * TODO servlets' init-params are not supported TODO filters are not registered
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
      log.debug("Servlet: [{}] --- URL Pattern Mappings: [{}]", f.getName(),
          f.getUrlPatternMappings());
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

      log.debug("Registered servlet with name [{}] Class[{}] Mappings[{}] and init params [{}]",
          servletName, def.getServletClass(),
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
          registeredFilter = (FilterRegistration.Dynamic) servletContext
              .getFilterRegistration(def.getFilterName());
        }

        registeredFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true,
            mapping.getURLPatterns());
        registeredFilter.setInitParameters(def.getParameterMap());
      }

      log.debug("Registered filter with name [{}] , Init params[{}] and Mappings[{}] ", filterName,
          def.getParameterMap().values().toString(), mappings);


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

```



#### properties-local.xml

```xml 
<properties xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:oxf="http://www.orbeon.com/oxf/processors">

  <property as="xs:string" name="oxf.fr.detail.process.save.*.*">
    save
    then navigate(uri="javascript:notifyParentWindow()")
    recover error-message(message="Save Failed")
  </property>

  <!-- Choose which buttons will be shown in form builder details page -->
  <!-- i.e. summary new test publish  -->
  <property as="xs:string" name="oxf.fr.detail.buttons.orbeon.builder">
    save summary new test publish
  </property>

  <property as="xs:anyURI" name="oxf.fr.default-logo.uri.*.*" value=""/>

  <!-- Choose which buttons will be shown in form runner published form details page -->
  <!-- i.e. close clear print pdf save submit  -->
  <property
    as="xs:string"
    name="oxf.fr.detail.buttons.*.*"
    value="submit"/>

  <property
    as="xs:boolean"
    name="oxf.xforms.combine-resources"
    value="true"/>

  <!-- Choose which buttons will be shown in form runner published form details page -->
  <!-- i.e. close clear print pdf save submit  -->
  <property
    as="xs:string"
    name="oxf.fr.detail.buttons.*.*"
    value="save"/>

  <property as="xs:string" name="oxf.fr.css.custom.uri.*.*">
    /forms/qbe/assets/qbe.css
  </property>


  <!--    <property as="xs:boolean" name="oxf.epilogue.xforms.inspector"-->
  <!--              value="true"/>-->

  <property as="xs:string"
    name="oxf.fr.detail.html-page-layout.*.*"
    value="fluid"/>

  <!-- Tell orbeon your custom implementation provider name -->
  <property as="xs:string" name="oxf.fr.persistence.provider.*.*.*"
    value="qbe"/>
  <!-- Tell orbeon your custom implementation provider is enabled -->
  <property as="xs:boolean"
    name="oxf.fr.persistence.qbe.active"
    value="true"/>

  <property
    as="xs:string"
    name="oxf.fr.persistence.qbe.datasource"
    value="qbe"/>

  <property
    as="xs:anyURI"
    name="oxf.fr.persistence.qbe.versioning"
    value="true"/>


  <!-- Set the base uri of your app here. This must be set so orbeon-forms and your custom persistence implementation run together at the same host -->
  <property
    as="xs:anyURI"
    name="oxf.url-rewriting.service.base-uri"
    value="http://localhost:8080/"/>

  <!-- Set the url of your custom implementation peristence endpoints -->
  <property as="xs:anyURI" name="oxf.fr.persistence.qbe.uri"
    value="test/api/forms"/>

  <property as="xs:boolean"
    name="oxf.fr.persistence.exist.active"
    value="false"/>

  <property
    as="xs:string"
    processor-name="oxf:page-flow"
    name="page-public-methods"
    value="GET HEAD POST PUT"/>

  <property
    as="xs:string"
    processor-name="oxf:page-flow"
    name="service-public-methods"
    value="GET HEAD POST PUT"/>


  <!-- Most comprehensive logging configuration -->
  <property as="xs:NMTOKENS" name="oxf.xforms.logging.debug">
    document
    model
    submission
    control
    event
    action
    analysis
    server
    server-body
    html
    process
    submission-details
    submission-body
  </property>

</properties>


```



#### web.xml

```xml 
<?xml version="1.0" encoding="UTF-8"?>
<web-app
    xmlns="http://xmlns.jcp.org/xml/ns/javaee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
    version="3.1">

    <display-name>Orbeon Forms</display-name>
    <description>
        Orbeon Forms is an open source, standard-based web forms solution, which
        includes Form Builder, a WYSIWYG browser-based authoring tool, and Form
        Runner, a runtime environment which facilitates the deployment and
        integration of a large number of complex forms. Orbeon Forms implements
        different technologies, such as XForms and Ajax, with no need for
        client-side software or plug-ins.
    </description>
    <!-- "A web application that is written so that it can be deployed in a web container distributed
         across multiple Java virtual machines running on the same host or different hosts." -->
    <!--Distributable when replication is enabled-->
    <distributable/>
    <!--Initialize main resource manager-->
    <context-param>
        <param-name>oxf.resources.factory</param-name>
        <param-value>org.orbeon.oxf.resources.PriorityResourceManagerFactory</param-value>
    </context-param>
    <!--Web application resource manager for resources-->
    <context-param>
        <param-name>oxf.resources.priority.2</param-name>
        <param-value>org.orbeon.oxf.resources.WebAppResourceManagerFactory</param-value>
    </context-param>
    <context-param>
        <param-name>oxf.resources.priority.2.oxf.resources.webapp.rootdir</param-name>
        <param-value>/WEB-INF/resources</param-value>
    </context-param>
    <!--Classloader resource manager-->
    <context-param>
        <param-name>oxf.resources.priority.6</param-name>
        <param-value>org.orbeon.oxf.resources.ClassLoaderResourceManagerFactory</param-value>
    </context-param>
    <!--Set run mode ("dev" or "prod")-->
    <context-param>
        <param-name>oxf.run-mode</param-name>
        <param-value>prod</param-value>
    </context-param>
    <!--Set location of properties.xml-->
    <context-param>
        <param-name>oxf.properties</param-name>
        <param-value>oxf:/config/properties-${oxf.run-mode}.xml</param-value>
    </context-param>
    <!--Determine whether logging initialization must take place-->
    <context-param>
        <param-name>oxf.initialize-logging</param-name>
        <param-value>true</param-value>
    </context-param>
    <!--Set context listener processors-->
    <!-- Uncomment this for the context listener processors -->
    <!--
    <context-param>
        <param-name>oxf.context-initialized-processor.name</param-name>
        <param-value>{http://www.orbeon.com/oxf/processors}pipeline</param-value>
    </context-param>
    <context-param>
        <param-name>oxf.context-initialized-processor.input.config</param-name>
        <param-value>oxf:/apps/context/context-initialized.xpl</param-value>
    </context-param>
    <context-param>
        <param-name>oxf.context-destroyed-processor.name</param-name>
        <param-value>{http://www.orbeon.com/oxf/processors}pipeline</param-value>
    </context-param>
    <context-param>
        <param-name>oxf.context-destroyed-processor.input.config</param-name>
        <param-value>oxf:/apps/context/context-destroyed.xpl</param-value>
    </context-param>-->
    <!-- End context listener processors -->
    <!--Set session listener processors-->
    <!-- Uncomment this for the session listener processors -->
    <!--
    <context-param>
        <param-name>oxf.session-created-processor.name</param-name>
        <param-value>{http://www.orbeon.com/oxf/processors}pipeline</param-value>
    </context-param>
    <context-param>
        <param-name>oxf.session-created-processor.input.config</param-name>
        <param-value>oxf:/apps/context/session-created.xpl</param-value>
    </context-param>
    <context-param>
        <param-name>oxf.session-destroyed-processor.name</param-name>
        <param-value>{http://www.orbeon.com/oxf/processors}pipeline</param-value>
    </context-param>
    <context-param>
        <param-name>oxf.session-destroyed-processor.input.config</param-name>
        <param-value>oxf:/apps/context/session-destroyed.xpl</param-value>
    </context-param>-->
    <!-- End session listener processors -->

    <!--Limit concurrent access to Form Runner-->
    <filter>
        <filter-name>orbeon-limiter-filter</filter-name>
        <filter-class>org.orbeon.oxf.servlet.LimiterFilter</filter-class>
        <!--Include Form Runner pages and XForms Ajax requests-->
        <init-param>
            <param-name>include</param-name>
            <param-value>(/fr/.*)|(/xforms-server)</param-value>
        </init-param>
        <!--Exclude resources not produced by services-->
        <init-param>
            <param-name>exclude</param-name>
            <param-value>(?!/([^/]+)/service/).+\.(gif|css|pdf|json|js|coffee|map|png|jpg|xsd|htc|ico|swf|html|htm|txt)</param-value>
        </init-param>
        <!--Minimum, requested, and maximum number of concurrent threads allowed-->
        <!--The `x` prefix specifies a multiple of the number of CPU cores reported by the JVM-->
        <init-param>
            <param-name>min-threads</param-name>
            <param-value>1</param-value>
        </init-param>
        <init-param>
            <param-name>num-threads</param-name>
            <param-value>x1</param-value>
        </init-param>
        <init-param>
            <param-name>max-threads</param-name>
            <param-value>x1</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>orbeon-limiter-filter</filter-name>
        <url-pattern>/*</url-pattern>
        <dispatcher>REQUEST</dispatcher>
    </filter-mapping>
    <!--Add internal Orbeon-* headers for auth-->
    <filter>
        <filter-name>orbeon-form-runner-auth-servlet-filter</filter-name>
        <filter-class>org.orbeon.oxf.servlet.FormRunnerAuthFilter</filter-class>
        <!--
        <init-param>
            <param-name>content-security-policy</param-name>
            <param-value>default-src 'self'</param-value>
        </init-param>
        -->
    </filter>
    <filter-mapping>
        <filter-name>orbeon-form-runner-auth-servlet-filter</filter-name>
        <url-pattern>/*</url-pattern>
        <dispatcher>REQUEST</dispatcher>
        <dispatcher>FORWARD</dispatcher>
    </filter-mapping>
    <!--All JSP files under /xforms-jsp go through the XForms filter-->
    <filter>
        <filter-name>orbeon-xforms-filter</filter-name>
        <filter-class>org.orbeon.oxf.servlet.OrbeonXFormsFilter</filter-class>

    </filter>
    <filter-mapping>
        <filter-name>orbeon-xforms-filter</filter-name>
        <url-pattern>/xforms-jsp/*</url-pattern>
        <!--Servlet 2.4 configuration allowing the filter to run upon forward in addition to request-->
        <dispatcher>REQUEST</dispatcher>
        <dispatcher>FORWARD</dispatcher>
    </filter-mapping>
    <!--Orbeon context listener-->
    <listener>
        <listener-class>org.orbeon.oxf.webapp.OrbeonServletContextListener</listener-class>
    </listener>
    <!--Context listener for deployment with replication-->
    <listener>
        <listener-class>org.orbeon.oxf.xforms.ReplicationServletContextListener</listener-class>
    </listener>
    <!--XForms session listener-->
    <listener>
        <listener-class>org.orbeon.oxf.xforms.XFormsServletContextListener</listener-class>
    </listener>
    <!--General-purpose session listener-->
    <listener>
        <listener-class>org.orbeon.oxf.webapp.OrbeonSessionListener</listener-class>
    </listener>
    <!--Ehcache shutdown listener-->
    <listener>
        <listener-class>net.sf.ehcache.constructs.web.ShutdownListener</listener-class>
    </listener>
    <!--This is the main Orbeon Forms servlet-->
    <servlet>
        <servlet-name>orbeon-main-servlet</servlet-name>
        <servlet-class>org.orbeon.oxf.servlet.OrbeonServlet</servlet-class>
        <!--Set main processor-->
        <init-param>
            <param-name>oxf.main-processor.name</param-name>
            <param-value>{http://www.orbeon.com/oxf/processors}pipeline</param-value>
        </init-param>
        <init-param>
            <param-name>oxf.main-processor.input.config</param-name>
            <param-value>oxf:/config/prologue-servlet.xpl</param-value>
        </init-param>
        <!--Set error processor-->
        <init-param>
            <param-name>oxf.error-processor.name</param-name>
            <param-value>{http://www.orbeon.com/oxf/processors}page-flow</param-value>
        </init-param>
        <init-param>
            <param-name>oxf.error-processor.input.controller</param-name>
            <param-value>oxf:/config/error-page-flow.xml</param-value>
        </init-param>
        <!--Set supported methods-->
        <init-param>
            <param-name>oxf.http.accept-methods</param-name>
            <param-value>get,post,head,put,delete,lock,unlock</param-value>
        </init-param>
        <!--Set servlet initialization and destruction listeners-->
        <!-- Uncomment this for the servlet listener processors -->
        <!--
    <init-param>
        <param-name>oxf.servlet-initialized-processor.name</param-name>
        <param-value>{http://www.orbeon.com/oxf/processors}pipeline</param-value>
    </init-param>
    <init-param>
        <param-name>oxf.servlet-initialized-processor.input.config</param-name>
        <param-value>oxf:/apps/context/servlet-initialized.xpl</param-value>
    </init-param>
    <init-param>
        <param-name>oxf.servlet-destroyed-processor.name</param-name>
        <param-value>{http://www.orbeon.com/oxf/processors}pipeline</param-value>
    </init-param>
    <init-param>
        <param-name>oxf.servlet-destroyed-processor.input.config</param-name>
        <param-value>oxf:/apps/context/servlet-destroyed.xpl</param-value>
    </init-param>-->
        <!-- End servlet listener processors -->
    </servlet>
    <!--This is the XForms Renderer servlet, used to deploy Orbeon Forms as a separate WAR-->
    <servlet>
        <servlet-name>orbeon-renderer-servlet</servlet-name>
        <servlet-class>org.orbeon.oxf.servlet.OrbeonServlet</servlet-class>
        <!--Set main processor-->
        <init-param>
            <param-name>oxf.main-processor.name</param-name>
            <param-value>{http://www.orbeon.com/oxf/processors}page-flow</param-value>
        </init-param>
        <init-param>
            <param-name>oxf.main-processor.input.controller</param-name>
            <param-value>oxf:/ops/xforms/xforms-renderer-page-flow.xml</param-value>
        </init-param>
        <!--Set error processor-->
        <init-param>
            <param-name>oxf.error-processor.name</param-name>
            <param-value>{http://www.orbeon.com/oxf/processors}pipeline</param-value>
        </init-param>
        <init-param>
            <param-name>oxf.error-processor.input.config</param-name>
            <param-value>oxf:/config/error.xpl</param-value>
        </init-param>
    </servlet>

    <servlet-mapping>
        <servlet-name>orbeon-main-servlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
        <servlet-name>orbeon-renderer-servlet</servlet-name>
        <url-pattern>/xforms-renderer</url-pattern>
    </servlet-mapping>
    <!-- Uncomment this for the eXist XMLRPC support -->
    <!--
    <servlet-mapping>
        <servlet-name>exist-xmlrpc-servlet</servlet-name>
        <url-pattern>/exist/xmlrpc/*</url-pattern>
    </servlet-mapping>-->
    <!-- End eXist XMLRPC support -->
    <!-- Uncomment this for the relational persistence, and change oracle if necessary -->
    <!--
    <resource-ref>
        <description>DataSource</description>
        <res-ref-name>jdbc/oracle</res-ref-name>
        <res-type>javax.sql.DataSource</res-type>
        <res-auth>Container</res-auth>
    </resource-ref>-->
    <!-- End relational persistence, and change oracle if necessary -->
    <!-- Form Runner authentication -->
    <!-- Require the security role on /fr/auth by default. To protect everything this must be changed. -->
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Form Runner</web-resource-name>
            <url-pattern>/fr/auth</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>orbeon-user</role-name>
        </auth-constraint>
    </security-constraint>
    <!-- The following pages and services are allowed without constraints by default -->
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Form Runner services and public pages and resources</web-resource-name>
            <url-pattern>/fr/service/*</url-pattern>
            <url-pattern>/fr/style/*</url-pattern>
            <url-pattern>/fr/not-found</url-pattern>
            <url-pattern>/fr/error</url-pattern>
            <url-pattern>/fr/login</url-pattern>
            <url-pattern>/fr/login-error</url-pattern>
        </web-resource-collection>
    </security-constraint>
    <!-- This will cause redirect from HTTP to HTTPS but not for eXist -->
    <!--
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Entire Application</web-resource-name>
            <url-pattern>/*</url-pattern>
        </web-resource-collection>
        <user-data-constraint>
            <transport-guarantee>CONFIDENTIAL</transport-guarantee>
        </user-data-constraint>
    </security-constraint>
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>eXist</web-resource-name>
            <url-pattern>/exist/*</url-pattern>
        </web-resource-collection>
        <user-data-constraint>
            <transport-guarantee>NONE</transport-guarantee>
        </user-data-constraint>
    </security-constraint>
    -->
    <!-- Use the form-based method by default -->
    <login-config>
        <auth-method>FORM</auth-method>
        <form-login-config>
            <form-login-page>/fr/login</form-login-page>
            <form-error-page>/fr/login-error</form-error-page>
        </form-login-config>
    </login-config>
    <!-- Default security role, which can be changed as needed -->
    <security-role>
        <role-name>orbeon-user</role-name>
    </security-role>
    <session-config>
        <session-timeout>60</session-timeout>
    </session-config>

    <!--    <listener>-->
    <!--        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>-->
    <!--    </listener>-->

    <!--    <context-param>-->
    <!--        <param-name>contextConfigLocation</param-name>-->
    <!--        <param-value>/WEB-INF/app-context.xml</param-value>-->
    <!--    </context-param>-->
    <servlet-mapping>
        <servlet-name>test</servlet-name>
        <url-pattern>/test/*</url-pattern>
    </servlet-mapping>

    <servlet>
        <servlet-name>test</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value></param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
</web-app>

```





#### A typical log4j configuration for debugging orbeon-forms 

```xml 
<!--
  Copyright (C) 2009 Orbeon, Inc.

  This program is free software; you can redistribute it and/or modify it under the terms of the
  GNU Lesser General Public License as published by the Free Software Foundation; either version
  2.1 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU Lesser General Public License for more details.

  The full text of the license is available at http://www.gnu.org/copyleft/lesser.html
  -->
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/" xmlns="http://jakarta.apache.org/log4j/">

    <!-- This is the standard log appender to the console (System.out) -->
    <appender name="ConsoleAppender" class="org.apache.log4j.ConsoleAppender">
        <param name="Target" value="System.out"/>
        <param name="Encoding" value="UTF-8"/>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{ISO8601} %-5p %c{1} %x - %m%n"/>
        </layout>
        <filter class="org.apache.log4j.varia.LevelRangeFilter">
            <param name="LevelMin" value="debug"/>
        </filter>
    </appender>

    <!-- Logging to a single file, typically used in development when you don't want
         to be dealing with multiple files generated by the RollingFileAppender. -->
    <appender name="SingleFileAppender" class="org.apache.log4j.FileAppender">
        <param name="File" value="../logs/orbeon.log"/>
        <param name="Append" value="false" />
        <param name="Encoding" value="UTF-8"/>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{ISO8601} %-5p %c{1} %x - %m%n"/>
        </layout>
    </appender>

    <!-- Logging to a rolling files. Every time the file exceeds a certain size, a backup
         is created and the file used for logging is truncated. -->
    <appender name="RollingFileAppender" class="org.apache.log4j.RollingFileAppender">
        <param name="File" value="../logs/orbeon.log"/>
        <param name="MaxFileSize" value="5MB"/>
        <param name="maxBackupIndex" value="200"/>
        <param name="Append" value="false" />
        <param name="Encoding" value="UTF-8"/>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{ISO8601} %-5p %c{1} %x - %m%n"/>
        </layout>
    </appender>

    <!-- XForms engine activity, see https://doc.orbeon.com/configuration/advanced/xforms-logging.html -->
    <!--
    <category name="org.orbeon.oxf.xforms.processor.XFormsServer">
        <priority value="debug"/>
    </category>
    -->

    <!-- To enable logging for any of the sub-systems below, copy that section outside of the comment block -->
    <!--
    <category name="org.orbeon.filter.limiter">
        <priority value="debug"/>
    </category>

    <category name="org.orbeon.auth">
        <priority value="debug"/>
    </category>

    <category name="org.orbeon.fr.data-migration">
        <priority value="debug"/>
    </category>

    <category name="org.orbeon.xforms.analysis.calculate">
        <priority value="debug"/>
    </category>

    <category name="org.orbeon.lifecycle">
        <priority value="debug"/>
    </category>

    <category name="org.orbeon.xbl">
        <priority value="debug"/>
    </category>

    <category name="org.orbeon.properties">
        <priority value="debug"/>
    </category>

    <category name="org.orbeon.oxf.processor.pipeline.TeeProcessor">
        <priority value="debug"/>
    </category>

    <category name="org.orbeon.oxf.processor.pipeline.choose.ConcreteChooseProcessor">
        <priority value="debug"/>
    </category>

    <category name="org.orbeon.oxf.processor.generator.URLGenerator">
        <priority value="debug"/>
    </category>

    <category name="org.orbeon.oxf.processor.sql.SQLProcessor">
        <priority value="debug"/>
    </category>

    <category name="org.orbeon.oxf.controller.PageFlowControllerProcessor">
        <priority value="debug"/>
    </category>

    <category name="org.orbeon.oxf.processor.generator.RequestGenerator">
        <priority value="debug"/>
    </category>

    <category name="org.orbeon.oxf.webapp.OrbeonSessionListener">
        <priority value="info"/>
    </category>

    <category name="org.orbeon.oxf.webapp.OrbeonServletContextListener">
        <priority value="info"/>
    </category>
    -->

    <!-- Prevent extra display of eXist paging activity -->
    <category name="org.exist.storage.btree.Paged">
        <priority value="warn"/>
    </category>

    <category name="org.exist.storage.DBBroker">
        <priority value="warn"/>
    </category>

    <category name="org.exist.storage.BrokerPool">
        <priority value="warn"/>
    </category>

    <!-- You decide here which one of the loggers listed above you want to use. -->
    <root>
        <priority value="info"/>
        <!--<appender-ref ref="ConsoleAppender"/>-->
        <appender-ref ref="SingleFileAppender"/>
        <!--<appender-ref ref="RollingFileAppender"/>-->
    </root>

</log4j:configuration>
```





