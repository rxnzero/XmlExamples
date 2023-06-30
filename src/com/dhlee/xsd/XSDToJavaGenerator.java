package com.dhlee.xsd;

import javax.xml.bind.*;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class XSDToJavaGenerator {
    public static void main(String[] args) throws IOException {
        try {
            // Create a JAXBContext for the classes you want to generate
            JAXBContext jaxbContext = JAXBContext.newInstance(SampleObject.class);

            // Create a SchemaOutputResolver to specify the output location for generated XSD
            SchemaOutputResolver outputResolver = new SchemaOutputResolver() {
                @Override
                public Result createOutput(String namespaceURI, String suggestedFileName) {
                    StringWriter writer = new StringWriter();
                    StreamResult result = new StreamResult(writer);
                    result.setSystemId(namespaceURI);
                    return result;
                }
            };

            // Generate the XSD from JAXBContext and save it to a string
            jaxbContext.generateSchema(outputResolver);
            String generatedXsd = outputResolver.createOutput(null, null).getSystemId();

            // Print the generated XSD
            System.out.println(generatedXsd);

            // Perform marshalling and unmarshalling using the generated classes
            Marshaller marshaller = jaxbContext.createMarshaller();
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            // Create an instance of YourRootElementClass
            SampleObject instance = new SampleObject();
            instance.setName("John");
            instance.setAge(25);

            // Marshalling to XML
            StringWriter xmlWriter = new StringWriter();
            marshaller.marshal(instance, xmlWriter);
            String xmlOutput = xmlWriter.toString();
            System.out.println("Marshalled XML:\n" + xmlOutput);

            // Unmarshalling from XML
            SampleObject unmarshalledInstance = (SampleObject) unmarshaller.unmarshal(new StringReader(xmlOutput));
            System.out.println("Unmarshalled Name: " + unmarshalledInstance.getName());
            System.out.println("Unmarshalled Age: " + unmarshalledInstance.getAge());

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}

