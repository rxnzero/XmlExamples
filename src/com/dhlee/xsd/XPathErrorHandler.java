package com.dhlee.xsd;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XPathErrorHandler extends DefaultHandler {
	private Stack<String> elementStack = new Stack<>();
    private StringBuilder xpathBuilder = new StringBuilder();
    private List<String> errorMessages = new ArrayList<>();;
    private boolean isError = false;
    private boolean fullParsing = false;
    
    boolean debug = false;
	
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	
	public boolean isFullParsing() {
		return fullParsing;
	}

	public void setFullParsing(boolean fullParsing) {
		this.fullParsing = fullParsing;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
        elementStack.push(qName);
        updateXPath();         
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        elementStack.pop();
        updateXPath();
    }

    private void updateXPath() {
        xpathBuilder.setLength(0);
        for (String element : elementStack) {
            xpathBuilder.append('/').append(element);
        }
    }

    private String getXPath() {
        return xpathBuilder.toString();
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        String message = printExceptionDetails("Error", exception);
        if(!fullParsing) throw new SAXException(message);
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
    	String message = printExceptionDetails("Fatal Error", exception);
        if(!fullParsing) throw new SAXException(message);
    }

    private String printExceptionDetails(String severity, SAXParseException exception) {
    	isError = true;
        StringBuilder sb = new StringBuilder();
        sb.append( severity + " at " );
        sb.append( "Line: " + exception.getLineNumber() + ", " );
        sb.append( "Column: " + exception.getColumnNumber() );
        sb.append( "\nXPath: " + getXPath() );;
        sb.append( "\nMessage: " + exception.getMessage());
        String message = sb.toString();
        errorMessages.add(message);
        if(debug) System.out.println(message);
        return message;
    }
}

