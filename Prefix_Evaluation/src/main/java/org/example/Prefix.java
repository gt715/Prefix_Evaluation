package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.net.URL;


public class Prefix {
    //  to get prefix expression from XML file
    public static String getPrefix(String fileName) {
        File file = getFile(fileName); // Get the XML file
        File xsd = getFile("test.xsd"); // Get the XSD file
        if (file != null) {
            try {
                // Validate the XML file against the XSD file
                boolean value = validateXmlAgainstXsd(file, xsd);
                // If valid, parse the XML to prefix expression
                return (value) ? parseXmlToPrefix(file).trim() : null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //  to get a file from the resource path
    private static File getFile(String fileName) {
        URL resourceUrl = Prefix.class.getClassLoader().getResource(fileName);

        if (resourceUrl != null) {
            File file = new File(resourceUrl.getFile());

            if (file.exists())
                return file;
            else {
                System.out.println("File not found!");
                return null;
            }
        } else {
            System.out.println("Resource not found!");
            return null;
        }
    }

    //  to validate an XML file against an XSD file
    private static boolean validateXmlAgainstXsd(File xmlFile, File xsdFile) {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Source schemaSource = new StreamSource(xsdFile);
            Schema schema = schemaFactory.newSchema(schemaSource);
            Validator validator = schema.newValidator();

            Source xmlFileSource = new StreamSource(xmlFile);
            validator.validate(xmlFileSource);

            System.out.println(xmlFile.getName() + " is valid according to the schema.");
            return true;
        } catch (Exception e) {
            System.out.println(xmlFile.getName() + " is not valid according to the schema.");
            System.out.println("Reason: " + e.getMessage());
            return false;
        }
    }

    //  to parse an XML file to prefix expression
    private static String parseXmlToPrefix(File xmlFile) throws Exception{
        Document document = parseXmlFile(xmlFile); // Parse the XML file to a Document
        Element root = document.getDocumentElement(); // Get the root element
        return parseExpressionToPrefix(root); // Parse the expression to prefix
    }

    //  to parse an XML file to a Document
    private static Document parseXmlFile(File xmlFile) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(xmlFile);
    }

    //  to parse an expression to prefix
    private static String parseExpressionToPrefix(Node node) {
        StringBuilder result = new StringBuilder();

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String operator = getOperatorValue(element);
            if (!operator.isEmpty()) {
                result.append(operator).append(" ");
            }

            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                result.append(parseExpressionToPrefix(childNodes.item(i)));
            }
        } else if ("atom".equals(node.getNodeName())) {
            Element atomElement = (Element) node;
            String atomValue = getAtomValue(atomElement);
            if (!atomValue.isEmpty()) {
                result.append(atomValue).append(" ");
            }
        }

        return result.toString().stripLeading();
    }

    //  to get the operator value from an element
    private static String getOperatorValue(Element element) {
        return element.getAttribute("value");
    }

    //  to get the atom value from an element
    private static String getAtomValue(Element element) {
        return element.getAttribute("value");
    }

    //  to validate a prefix expression
    private static boolean validatePrefixExpression(String prefix) {
        int numberOfOperators = 0, numberOfOperands = 0;
        String[] values = prefix.split(" ");
        for(String value: values)
            if (value.matches("-?\\d+(\\.\\d+)?"))
                numberOfOperands++;
            else
                numberOfOperators++;
        boolean value = numberOfOperands == numberOfOperators + 1;
        if (!value)
            System.out.println("This prefix in this xml is not valid");
        return value;
    }


}


