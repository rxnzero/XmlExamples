package com.dhlee.xsd;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

// Custom error handler to handle validation errors
public class SimpleErrorHandler implements ErrorHandler {
	List<String> errorMessages = new ArrayList<>();; 
	boolean debug = false;
	
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void warning(SAXParseException e) throws SAXException {
        String errorMessage = "Warning: " + e.getMessage();
		if(debug) System.out.println(errorMessage);
        errorMessages.add(errorMessage);
        throw e;
    }

    public void error(SAXParseException e) throws SAXException {
        String errorMessage = "Error at line " + e.getLineNumber() + ", column " + e.getColumnNumber() + ": " + e.getMessage();
        if(debug) System.out.println(errorMessage);
        errorMessages.add(errorMessage);
//        throw e;
    }

    public void fatalError(SAXParseException e) throws SAXException {
        String errorMessage = "Fatal error at line " + e.getLineNumber() + ", column " + e.getColumnNumber() + ": " + e.getMessage();
        if(debug) System.out.println(errorMessage);
        errorMessages.add(errorMessage);
//        throw e;
    }
}
