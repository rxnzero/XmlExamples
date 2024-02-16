package com.dhlee.xsd;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlValidator {
    public static void main(String[] args) {
    	testFile();
//    	testData();        
    }

    public static void testFile() {
        String xmlFile = "src/resources/sample.xml";
        String xsdFile = "src/resources/sample.xsd";
        boolean isValid = validate(xmlFile, xsdFile, false);
        System.out.println("Is the XML file valid? " + isValid);
    }
    
    private static boolean validate(String xmlFile, String xsdFile, boolean isFullValidation) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            
            // Read and print XML file content
            String xmlContent = readFileContent(xmlFile);
            System.out.println("XML File Content:\n" + xmlContent);
            // Read and print XSD file content
            String xsdContent = readFileContent(xsdFile);
            System.out.println("XSD File Content:\n" + xsdContent);
            
            Source source = new StreamSource(xmlFile);
            File schemaFile = new File(xsdFile);
            Schema schema = factory.newSchema(schemaFile);
            
            Validator validator = schema.newValidator();
            if(isFullValidation) {
            	SimpleErrorHandler errorHandler = new SimpleErrorHandler();
            	errorHandler.setDebug(false);
            	validator.setErrorHandler(errorHandler);
            
	            validator.validate(source);
	            List<String> errorMessages = errorHandler.getErrorMessages();
	            if(errorMessages.size() > 0) {
	            	System.	out.println("Validation errors : \n" + errorMessages.stream().collect(Collectors.joining("\n")) );
	            	return false;
	            }
	            return true;
            }
            else {
            	validator.validate(source);
            	return true;
            }
        } catch (SAXParseException e) {
            System.out.println("Validation failed at line " + e.getLineNumber() + ", column " + e.getColumnNumber() + ": " + e.getMessage());
            return false;
        } catch (SAXException e) {
            System.out.println("Validation failed: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
    }
    
    private static String readFileContent(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
    
    public static void testData() {
        String xmlString = "<root><element>value</element><elementInt>10s</elementInt><SSN>A23-12-1234</SSN></root>";
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
                "                <xsd:element name=\"SSN\">\n" +
                "                <xsd:simpleType>\n" +
                "                <xsd:restriction base=\"xsd:token\">\n" +
                "                    <xsd:pattern value=\"[0-9]{3}-[0-9]{2}-[0-9]{4}\"/>\n" +
                "                </xsd:restriction>\n" +
                "                </xsd:simpleType>\n" +
                "                </xsd:element>\n" +
                "            </xsd:sequence>\n" +
                "        </xsd:complexType>\n" +
                "    </xsd:element>\n" +
                "</xsd:schema>";
        boolean isValid = validateWithData(xmlString, xsdString, true);
        System.out.println("Is the XML string valid? " + isValid);
        
        isValid = validateWithData(xmlString, xsdString, false);
        System.out.println("Is the XML string valid? " + isValid);
    }

    private static boolean validateWithData(String xmlString, String xsdString, boolean isFullValidation) {
        try {
        	System.out.println(">> XML\n" + xmlString);
        	System.out.println(">> XSD\n" + xsdString);
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Source schemaSource = new StreamSource(new java.io.StringReader(xsdString));
            Schema schema = factory.newSchema(schemaSource);
            Validator validator = schema.newValidator();
            Source xmlSource = new StreamSource(new java.io.StringReader(xmlString));
            
            if(isFullValidation) {
            	SimpleErrorHandler errorHandler = new SimpleErrorHandler();
            	errorHandler.setDebug(false);
            	validator.setErrorHandler(errorHandler);
            
	            validator.validate(xmlSource);
	            List<String> errorMessages = errorHandler.getErrorMessages();
	            if(errorMessages.size() > 0) {
	            	System.	out.println("Validation errors : \n" + errorMessages.stream().collect(Collectors.joining("\n")) );
	            	return false;
	            }
	            return true;
            }
            else {
            	validator.validate(xmlSource);
            	return true;
            }
        } catch (SAXParseException e) {
            System.out.println("Validation failed at line " + e.getLineNumber() + ", column " + e.getColumnNumber() + ": " + e.getMessage());
            return false;
        } catch (SAXException e) {
            System.out.println("Validation failed: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
    }

}
