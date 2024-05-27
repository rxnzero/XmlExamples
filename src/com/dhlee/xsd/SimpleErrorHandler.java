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
		String errorMessage = printExceptionDetails("Warning", e);
		if(debug) System.out.println(errorMessage);
        errorMessages.add(errorMessage);
        throw e;
    }
	
	private String printExceptionDetails(String severity, SAXParseException exception) {
        String message = severity + " at " +
                           "Line: " + exception.getLineNumber() + ", " +
                           "Column: " + exception.getColumnNumber() + ", " +
                           "Public ID: " + exception.getPublicId() + ", " +
                           "System ID: " + exception.getSystemId() +
                           ", getMessage - " + exception.getMessage();
//        System.out.println(message);
        return message;
    }
	
    public void error(SAXParseException e) throws SAXException {
    	String errorMessage = printExceptionDetails("error", e);
//    	String errorMessage = "Error at line " + e.getLineNumber() + ", column " + e.getColumnNumber() + ": " + e.getMessage();
//        if(debug) System.out.println(errorMessage);
        errorMessages.add(errorMessage);
//        throw e;
    }

    public void fatalError(SAXParseException e) throws SAXException {
    	String errorMessage = printExceptionDetails("fatalError", e);
//    	String errorMessage = "Fatal error at line " + e.getLineNumber() + ", column " + e.getColumnNumber() + ": " + e.getMessage();
//        if(debug) System.out.println(errorMessage);
        errorMessages.add(errorMessage);
//        throw e;
    }
}
