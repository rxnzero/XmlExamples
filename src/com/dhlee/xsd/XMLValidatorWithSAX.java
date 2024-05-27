package com.dhlee.xsd;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class XMLValidatorWithSAX {
    public static void main(String[] args) {
    	boolean isFullDebug = false;
        try {
            String xmlFile = "src/resources/sample.xml";
            String xsdFile = "src/resources/sample.xsd";
            // ��Ű�� ���丮�� ��Ű�� ����
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = factory.newSchema(new File(xsdFile));

            // SAXParserFactory ����
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setSchema(schema);
            saxParserFactory.setNamespaceAware(true);

            SAXParser saxParser = saxParserFactory.newSAXParser();
            
            boolean isFullParsing = true;
            // ErrorHandler ����
            XPathErrorHandler handler = new XPathErrorHandler();
            
            handler.setFullParsing(isFullParsing);
            
            // XML ���� �Ľ� �� ����
            saxParser.parse(new File(xmlFile), handler);
            if(isFullParsing) {
	            List<String> errorMessages = handler.getErrorMessages();
	            if(errorMessages.size() > 0) {
	            	String fullErrorMessage = String.format("Validation errors(%d) : \n %s", errorMessages.size(), errorMessages.stream().collect(Collectors.joining("\n"))) ;
	            	throw new SAXException(fullErrorMessage);
	            }
	            else {
	            	System.out.println("XML ������ ��ȿ�մϴ�.");
	            }
            }
        } catch (IOException e) {
        	System.out.println(">> IOError : ");
            e.printStackTrace();
        }catch (SAXException e) {
        	System.out.println(">> SAXException : ");
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
        	System.out.println(">> ParserConfigurationException : ");
			e.printStackTrace();
		} 
    }

}
