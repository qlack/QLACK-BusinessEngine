package com.eurodyn.qlack.be.forms.management.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLUtil {

    public static void addScriptToDocumentHead(Document xmlDocument, Element appendingEl, String script) {
        Element scriptNode = xmlDocument.createElement("script");
        scriptNode.setAttribute("type", "text/javascript");

        Node jsFunction = xmlDocument.createTextNode(script);

        scriptNode.appendChild(jsFunction);
        appendingEl.appendChild(scriptNode);
    }

    public static String getNodeString(Node node) {
        try {
            StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            String output = writer.toString();
            return output.substring(output.indexOf("?>") + 2);//remove <?xml version="1.0" encoding="UTF-8"?>
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return node.getTextContent();
    }

    public static Document getDocumentFromInputStream(String content)
            throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(content)));
        doc.getDocumentElement().normalize();
        return doc;
    }
}
