package com.dhlee.xsd.xmlbeans;

import javax.xml.crypto.dsig.XMLObject;
import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;


//<dependencies>
//<dependency>
//    <groupId>org.apache.xmlbeans</groupId>
//    <artifactId>xmlbeans</artifactId>
//    <version>x.x.x</version> <!-- Replace with the desired version -->
//</dependency>
//<dependency>
//    <groupId>org.apache.xmlbeans</groupId>
//    <artifactId>xbean</artifactId>
//    <version>x.x.x</version> <!-- Replace with the desired version -->
//</dependency>
//</dependencies>


public class XSDToXMLGenerator {
    public static void main(String[] args) {
        String xsdSchema = "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "    <xs:element name=\"YourRootElementClass\">\n" +
                "        <xs:complexType>\n" +
                "            <xs:sequence>\n" +
                "                <xs:element name=\"name\" type=\"xs:string\"/>\n" +
                "                <xs:element name=\"age\" type=\"xs:int\"/>\n" +
                "            </xs:sequence>\n" +
                "        </xs:complexType>\n" +
                "    </xs:element>\n" +
                "</xs:schema>";
//        parseXsdWithData(xsdSchema);
        parseXsdStructure(xsdSchema);
    }
    
    public static void parseXsdStructure(String xsdSchema) {
    	try {
            // Create a new XMLBeans SchemaTypeSystem from the XSD schema string
            SchemaTypeSystem schemaTypeSystem = XmlBeans.compileXsd(new XmlObject[]{XmlObject.Factory.parse(xsdSchema)}, XmlBeans.getBuiltinTypeSystem(), null);

            // Create a new instance of the generated XMLBeans type
            SchemaType[] sTypes = schemaTypeSystem.documentTypes();
            for(int i=0; i< sTypes.length; i++) {
            	System.out.println(sTypes[i].toString() + " -> " + sTypes[i].getElementProperties() );
            }
//            SchemaType rootElementType = schemaTypeSystem.documentTypes()[0];
//            System.out.println("Generated XML:\n" + xmlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void parseXsdWithData(String xsdSchema) {
        try {
            // Load the XSD schema as a string
    
            // Create an XmlObject instance from the XSD string
            XmlObject schemaObject = XmlObject.Factory.parse(xsdSchema);

            // Generate an example XML instance from the XSD
            XmlOptions options = new XmlOptions();
            options.setLoadLineNumbers();
            options.setDocumentType(schemaObject.schemaType());
            XmlObject exampleObject = XmlObject.Factory.newInstance(options);
            XmlCursor cursor = exampleObject.newCursor();
            cursor.toNextToken();
            cursor.beginElement("YourRootElementClass");
            cursor.insertElementWithText("name", "John");
            cursor.insertElementWithText("age", "25");
            cursor.dispose();

            // Print the generated XML
            System.out.println(exampleObject.xmlText());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
