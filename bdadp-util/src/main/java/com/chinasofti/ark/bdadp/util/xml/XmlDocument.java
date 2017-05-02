package com.chinasofti.ark.bdadp.util.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XmlDocument {

    private final Document xml;

    public XmlDocument(File file) {
        this.xml = loadXml(file);
    }

    public static String getText(Element parent, String childName) {
        return parent.elementText(childName);
    }

    public static String getAttribute(Element element, String attrName) {
        if (element.attribute(attrName) == null) {
            return null;
        }
        return element.attribute(attrName).getValue();
    }

    private Document loadXml(File file) {
        DocumentBuilder builder;
        org.w3c.dom.Document dom;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            dom = builder.parse(file);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Load xml failed, please check the xml file " + file.getName() + ": " + e.getMessage());
        }
        DOMReader reader = new DOMReader();
        return reader.read(dom);
    }

    public Element root() {
        return this.xml.getRootElement();
    }

    public Element element(String name) {
        return this.xml.getRootElement().element(name);
    }

    public Element element(Element parent, String name) {
        return parent.element(name);
    }

}
