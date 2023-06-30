package com.dhlee.xsd;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;

public class XmlValidator {
    public static void main(String[] args) {
        testData();
    }

    public static void testFile() {
        String xmlFile = "path/to/xml/file.xml";
        String xsdFile = "path/to/xsd/schema.xsd";
        boolean isValid = validate(xmlFile, xsdFile);
        System.out.println("Is the XML file valid? " + isValid);
    }
    
    private static boolean validate(String xmlFile, String xsdFile) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            File schemaFile = new File(xsdFile);
            Schema schema = factory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(xmlFile);
            validator.validate(source);
            return true;
        } catch (SAXException e) {
            System.out.println("Validation failed due to " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("IO error occurred while validating " + e.getMessage());
            return false;
        }
    }
        
    public static void testData() {
        String xmlString = "<root><element>value</element><elementInt>10</elementInt></root>";
        String xsdString = "<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "    <xsd:element name=\"root\">\n" +
                "        <xsd:complexType>\n" +
                "            <xsd:sequence>\n" +
                "                <xsd:element name=\"element\" type=\"xsd:string\"/>\n" +
                "            </xsd:sequence>\n" +
                "        </xsd:complexType>\n" +
                "    </xsd:element>\n" +
                "</xsd:schema>";
        
        xsdString = "<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "    <xsd:element name=\"root\">\n" +
                "        <xsd:complexType>\n" +
                "            <xsd:sequence>\n" +
                "                <xsd:element name=\"element\" type=\"xsd:string\"/>\n" +
                "                <xsd:element name=\"elementInt\" type=\"xsd:int\" minOccurs=\"0\"/>\n" +
                "            </xsd:sequence>\n" +
                "        </xsd:complexType>\n" +
                "    </xsd:element>\n" +
                "</xsd:schema>";
        boolean isValid = validateWithData(xmlString, xsdString);
        System.out.println("Is the XML string valid? " + isValid);
    }

    private static boolean validateWithData(String xmlString, String xsdString) {
        try {
        	System.out.println(">> XML\n" + xmlString);
        	System.out.println(">> XSD\n" + xsdString);
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Source schemaSource = new StreamSource(new java.io.StringReader(xsdString));
            Schema schema = factory.newSchema(schemaSource);
            Validator validator = schema.newValidator();
            Source xmlSource = new StreamSource(new java.io.StringReader(xmlString));
            validator.validate(xmlSource);
            return true;
        } catch (SAXException e) {
            System.out.println("Validation failed due to " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("IO error occurred while validating " + e.getMessage());
            return false;
        }
    }

}
