package com.osiris.pandomiumbuilder;


import com.osiris.dyml.exceptions.NotLoadedException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class XMLTest {

    @Test
    void test() throws ParserConfigurationException, NotLoadedException, TransformerException, IOException, SAXException, XPathExpressionException {
        Document pandomiumPom = U.toXML("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <parent>\n" +
                "        <groupId>org.panda-lang</groupId>\n" +
                "        <artifactId>pandomium-parent</artifactId>\n" +
                "        <version>67.0.7</version> <!-- Pandonium-Builder.jar sets this to the latest version, before publishing. -->\n" +
                "    </parent>\n" +
                "\n" +
                "    <artifactId>pandomium</artifactId>\n" +
                "\n" +
                "    <dependencies>\n" +
                "    </dependencies>\n" +
                "\n" +
                "</project>");


        Document pandomiumParentPom = U.toXML("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "\n" +
                "    <groupId>org.panda-lang</groupId>\n" +
                "    <artifactId>pandomium-parent</artifactId>\n" +
                "    <packaging>pom</packaging>\n" +
                "    <version>67.0.7</version> <!-- Pandonium-Builder.jar sets this to the latest version, before publishing. -->\n" +
                "\n" +
                "    <dependencyManagement>\n" +
                "        <dependencies>\n" +
                "        </dependencies>\n" +
                "    </dependencyManagement>\n" +
                "</project>");

        // Update the projects versions in its pom.xml files
        U.findNodes(pandomiumPom, "//version").item(0)
                .setTextContent("LATEST VERSION HERE");
        U.findNodes(pandomiumParentPom, "//version").item(0)
                .setTextContent("LATEST VERSION HERE");

        // Remove old <dependency> nodes:
        for (Node oldGroupId :
                U.findNodesWithText(U.findNodes(pandomiumPom, "/project/dependencies/groupId"), "org.panda-lang.pandomium-fat-jars")) {
            pandomiumPom.removeChild(oldGroupId.getParentNode());
        }
        for (Node oldGroupId :
                U.findNodesWithText(U.findNodes(pandomiumParentPom, "/project/dependencies/groupId"), "org.panda-lang.pandomium-fat-jars")) {
            pandomiumParentPom.removeChild(oldGroupId.getParentNode());
        }

        printXMLDoc(pandomiumPom);
        printXMLDoc(pandomiumParentPom);

        // Append new:

        for (File fatJar :
                Arrays.asList(new File("win32-fat.jar"), new File("win64-fat.jar"))) {
            try{
                Node dependencies = U.findNodes(pandomiumPom, "/project/dependencies").item(0);
                Node dependency = null;
                try {
                    dependency = U.findNodes(pandomiumPom, "/project/dependencies[groupId==org.panda-lang.pandomium-fat-jars]").item(0);
                } catch (Exception ignored){}

                if (dependency != null)
                    pandomiumPom.removeChild(dependency);

                dependency = pandomiumPom.createElement( "dependency");
                dependencies.appendChild(dependency);

                Node groupId = pandomiumPom.createElement("groupId");
                groupId.setTextContent("org.panda-lang.pandomium-fat-jars");
                Node artifactId = pandomiumPom.createElement("artifactId");
                artifactId.setTextContent(U.getFileNameWithoutExt(fatJar));
                Node version = pandomiumPom.createElement("version");
                version.setTextContent("${project.version}");
                dependency.appendChild(pandomiumPom.createComment("Auto-generated by Pandomium-Builder.jar"));
                dependency.appendChild(groupId);
                dependency.appendChild(artifactId);
                dependency.appendChild(version);
            } catch (Exception exception) {
                throw exception;
            }

            // Do almost the same stuff for the pandonium parent pom.xml
            try{
                Node dependencies = U.findNodes(pandomiumParentPom, "/project/dependencyManagement/dependencies").item(0);
                Node dependency = null;
                try {
                    dependency = U.findNodes(pandomiumParentPom, "/project/dependencyManagement/dependencies[groupId==org.panda-lang.pandomium-fat-jars]").item(0);
                } catch (Exception ignored){}

                if (dependency != null)
                    pandomiumParentPom.removeChild(dependency);

                dependency = pandomiumParentPom.createElement( "dependency");
                dependencies.appendChild(dependency);

                Node groupId = pandomiumParentPom.createElement("groupId");
                groupId.setTextContent("org.panda-lang.pandomium-fat-jars");
                Node artifactId = pandomiumParentPom.createElement("artifactId");
                artifactId.setTextContent(U.getFileNameWithoutExt(fatJar));
                dependency.appendChild(pandomiumParentPom.createComment("Auto-generated by Pandomium-Builder.jar"));
                dependency.appendChild(groupId);
                dependency.appendChild(artifactId);
            } catch (Exception exception) {
                throw exception;
            }
        }

        printXMLDoc(pandomiumPom);
        printXMLDoc(pandomiumParentPom);
    }

    private void printXMLDoc(Document doc) throws TransformerException, IOException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        System.out.println(writer.getBuffer().toString());
        writer.close();
    }
}