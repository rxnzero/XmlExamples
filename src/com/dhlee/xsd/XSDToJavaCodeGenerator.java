package com.dhlee.xsd;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.*;

import java.io.StringReader;
import java.io.StringWriter;

public class XSDToJavaCodeGenerator {
    public static void main(String[] args) {
        try {
            // Load the XSD schema as a string
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

            // Generate Java code from the XSD
//            JCodeModel codeModel = CodeModelLanguage.XSD.generateCode(null, null, new InputSource(new StringReader(xsdSchema)), null);
//            StringWriter writer = new StringWriter();
//            codeModel.build(new XJCListener() {
//                @Override
//                public void generated(ClassOutline classOutline, CClassInfo classInfo, CPropertyInfo propertyInfo) {
//                    // Optionally perform any custom logic upon generation of each class
//                }
//            }, new JCodeModel().rootPackage("com.example.generated"), writer);
//
//            // Print the generated Java code
//            System.out.println(writer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

