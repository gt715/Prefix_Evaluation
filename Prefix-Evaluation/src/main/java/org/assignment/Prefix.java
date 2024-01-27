package org.assignment;

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
    public static String getPrefix(String fileName) {
        File file = getFile(fileName);
        File xsd = getFile("test.xsd");
        if (file != null) {
            try {
                boolean value = validations(file, xsd);
                return (value) ? parseXmlToPrefix(file).trim() : null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

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

    private static String parseXmlToPrefix(File xmlFile) throws Exception{
        Document document = parseXmlFile(xmlFile);
        Element root = document.getDocumentElement();
        return parseExpressionToPrefix(root);
    }

    private static Document parseXmlFile(File xmlFile) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(xmlFile);
    }

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

    private static String getOperatorValue(Element element) {
        return element.getAttribute("value");
    }

    private static String getAtomValue(Element element) {
        return element.getAttribute("value");
    }


    private static boolean validatePrefixExpression(String prefix) {
        int numberOfOperators = 0, numberOfOperands = 0;
        String[] values = prefix.split(" ");
        for(String value: values)
            if (value.matches("\\d+"))
                numberOfOperands++;
            else
                numberOfOperators++;
        boolean value = numberOfOperands == numberOfOperators + 1;
        if (!value)
            System.out.println("This prefix in this xml is not valid");
        return value;
    }

    private static boolean validations(File xmlFile, File xsdFile) throws Exception {
        return validateXmlAgainstXsd(xmlFile, xsdFile) && validatePrefixExpression(parseXmlToPrefix(xmlFile).trim());
    }
}



