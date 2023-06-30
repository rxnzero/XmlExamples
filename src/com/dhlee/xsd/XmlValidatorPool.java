package com.dhlee.xsd;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XmlValidatorPool {

	public XmlValidatorPool() {
		// TODO Auto-generated constructor stub
	}

	private static final int POOL_SIZE = 10;
    private static final BlockingQueue<Validator> validatorPool = new ArrayBlockingQueue<>(POOL_SIZE);

    private static void createValidators(String xsdString) {
        try {
        	SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new java.io.StringReader(xsdString)));
            for (int i = 0; i < POOL_SIZE; i++) {
                Validator validator = schema.newValidator();
                validatorPool.put(validator);
            }
        } catch (SAXException | InterruptedException e) {
            throw new IllegalStateException("Failed to initialize Validator pool", e);
        }        
    }

    public static void main(String[] args) {
    	String xsdString = "<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "    <xsd:element name=\"root\">\n" +
                "        <xsd:complexType>\n" +
                "            <xsd:sequence>\n" +
                "                <xsd:element name=\"element\" type=\"xsd:string\"/>\n" +
                "                <xsd:element name=\"elementInt\" type=\"xsd:int\" minOccurs=\"0\"/>\n" +
                "            </xsd:sequence>\n" +
                "        </xsd:complexType>\n" +
                "    </xsd:element>\n" +
                "</xsd:schema>";
        
    	createValidators(xsdString);
    	
        String xmlString = "<root><element>value</element></root>";
        boolean isValid = validate(xmlString);
        System.out.println("Is the XML string valid? " + isValid);
    }

    private static boolean validate(String xmlString) {
        Validator validator = null;
        try {
            validator = validatorPool.take();
            Source xmlSource = new StreamSource(new java.io.StringReader(xmlString));
            validator.validate(xmlSource);
            return true;
        } catch (SAXException e) {
            System.out.println("Validation failed due to " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println("IO error occurred while validating " + e.getMessage());
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            if (validator != null) {
                validator.reset();
                validatorPool.offer(validator);
            }
        }
    }
}
