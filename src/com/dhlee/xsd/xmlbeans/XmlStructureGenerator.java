package com.dhlee.xsd.xmlbeans;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xsd2inst.SampleXmlUtil;

public class XmlStructureGenerator {
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
            // Create an XmlObject from the XSD
            XmlObject schema = XmlObject.Factory.parse(xsd);

            // Set indentation options for pretty printing
            XmlOptions xmlOptions = new XmlOptions();
            xmlOptions.setSavePrettyPrint();
            xmlOptions.setSavePrettyPrintIndent(2);

            // Convert the XmlObject to a formatted XML string
            String formattedXml = schema.xmlText(xmlOptions);

            // Print the XML structure
            System.out.println(formattedXml);
        } catch (XmlException e) {
            e.printStackTrace();
        }
    }
}
