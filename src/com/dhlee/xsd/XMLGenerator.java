package com.dhlee.xsd;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XMLGenerator {
	public static void main(String[] args) {
        try {
            // Create a JAXBContext for the generated classes
            JAXBContext jaxbContext = JAXBContext.newInstance(SampleObject.class); // Replace with the generated root element class

            // Create a Marshaller
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Create an instance of the root element class
            SampleObject rootElement = new SampleObject(); // Replace with the actual root element class

            // Set values or populate attributes in the root element or its child elements as needed

            // Marshal the JAXB object to XML and output to file
//            marshaller.marshal(rootElement, new File("path/to/output.xml"));
            StringWriter output = new StringWriter();
            marshaller.marshal(rootElement, output);
            System.out.println(output.toString());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
