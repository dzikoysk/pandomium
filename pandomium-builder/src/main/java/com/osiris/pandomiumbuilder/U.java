package com.osiris.pandomiumbuilder;

import com.osiris.dyml.exceptions.NotLoadedException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.ls.LSSerializerFilter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Utils class.
 */
public class U {

    public static boolean deleteDirectoryRecursively(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectoryRecursively(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

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
        return builder.parse(fileIS);
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

    public static List<Node> findNodesWithText(NodeList nodesList, String text) {
        List<Node> results = new ArrayList<>();
        Node n = null;
        for (int i = 0; i < nodesList.getLength(); i++) {
            n = nodesList.item(i);
            if (n.getTextContent() != null && n.getTextContent().equals(text))
                results.add(n);
        }
        return results;
    }

    public static void printXML(Document xml) throws TransformerException, IOException, ParserConfigurationException, ClassNotFoundException, InstantiationException, IllegalAccessException, SAXException {
        // Since the provided xml may be not formatted correctly 100%,
        // we need to first convert it into one line, otherwise the pretty print won't work correctly.
        StringWriter writer = new StringWriter();
        TransformerFactory.newInstance()
                .newTransformer().transform(new DOMSource(xml), new StreamResult(writer));
        BufferedReader br = new BufferedReader(new StringReader(writer.getBuffer().toString()));
        String line;
        StringBuilder sb = new StringBuilder();
        while((line=br.readLine())!= null){
            sb.append(line.trim());
        }
        writer.close();
        // Now that the xml document is one line, we pretty format it
        // so that its more readable.
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        StringWriter writer2 = new StringWriter();
        transformer.transform(new DOMSource(toXML(sb.toString())), new StreamResult(writer2));
        System.out.println(writer2.getBuffer().toString());
        writer2.close();
    }

    public static void saveXMLToFile(Document xml, File file) throws TransformerException, IOException, ParserConfigurationException, SAXException {
        // Since the provided xml may be not formatted correctly 100%,
        // we need to first convert it into one line, otherwise the pretty print won't work correctly.
        StringWriter writer = new StringWriter();
        TransformerFactory.newInstance()
                .newTransformer().transform(new DOMSource(xml), new StreamResult(writer));
        BufferedReader br = new BufferedReader(new StringReader(writer.getBuffer().toString()));
        String line;
        StringBuilder sb = new StringBuilder();
        while((line=br.readLine())!= null){
            sb.append(line.trim());
        }
        writer.close();
        // Now that the xml document is one line, we pretty format it
        // so that its more readable.
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        StringWriter writer2 = new StringWriter();
        transformer.transform(new DOMSource(toXML(sb.toString())), new StreamResult(writer2));
        Files.write(file.toPath(), writer2.getBuffer().toString().getBytes(StandardCharsets.UTF_8));
        writer2.close();
    }
}
