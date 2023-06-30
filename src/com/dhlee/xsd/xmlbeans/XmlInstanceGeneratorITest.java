package com.dhlee.xsd.xmlbeans;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class XmlInstanceGeneratorITest {

	public XmlInstanceGeneratorITest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
        // XML Schema definition (XSD) as a String
        String xsd = "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "  <xs:element name=\"root\">\n" +
                "    <xs:complexType>\n" +
                "      <xs:sequence>\n" +
                "        <xs:element name=\"child1\" type=\"xs:string\"/>\n" +
                "        <xs:element name=\"child2\" type=\"xs:int\"/>\n" +
                "      </xs:sequence>\n" +
                "    </xs:complexType>\n" +
                "  </xs:element>\n" +
                "</xs:schema>";
        
        try {
			xsd = readClasspathFile("resources/iso20022.xsd");
	        XmlInstanceGeneratorImpl generator = new XmlInstanceGeneratorImpl();
	        
	        String xml = generator.generateXmlInstance(xsd, "Document"); //root
	        //System.out.println("XSD :\n" + xsd);
	        System.out.println("XML :\n" + xml);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

	}

	private static String readClasspathFile(String filePath) throws IOException, URISyntaxException {
        // Read the file from the classpath
        byte[] fileBytes = Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(filePath).toURI()));

        // Convert the file bytes to a string using UTF-8 encoding
        return new String(fileBytes, StandardCharsets.UTF_8);
    }
}
