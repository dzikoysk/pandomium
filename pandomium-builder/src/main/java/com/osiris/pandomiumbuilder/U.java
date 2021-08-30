package com.osiris.pandomiumbuilder;

import com.osiris.dyml.exceptions.NotLoadedException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Utils
 */
public class U {

    public static String getFileNameWithoutExt(File file) throws NotLoadedException {
        return getFileNameWithoutExt(file.getName());
    }

    public static String getFileNameWithoutExt(String fileNameWithExt) throws NotLoadedException {
        return fileNameWithExt.replaceFirst("[.][^.]+$", ""); // Removes the file extension
    }

    public static Document toXML(File file) throws IOException, SAXException, ParserConfigurationException {
        FileInputStream fileIS = new FileInputStream(file);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return  builder.parse(fileIS);
    }

    public static Document toXML(String fileAsString) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(fileAsString.getBytes(StandardCharsets.UTF_8)));
    }

    public static NodeList findNodes(Document doc, String exp) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        return (NodeList) xPath.compile(exp).evaluate(doc, XPathConstants.NODESET);
    }

    public static List<Node> findNodesWithText(NodeList nodesList, String text){
        List<Node> results = new ArrayList<>();
        Node n = null;
        for (int i = 0; i < nodesList.getLength(); i++) {
            n = nodesList.item(i);
            if(n.getTextContent()!=null && n.getTextContent().equals(text))
                results.add(n);
        }
        return results;
    }

    public static void saveXMLToFile(Document xml, File file) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        DOMSource source = new DOMSource(xml);
        transformer.transform(source, new StreamResult(file));
    }
}
