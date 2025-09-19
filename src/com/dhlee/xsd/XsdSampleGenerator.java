package com.dhlee.xsd;

import org.apache.ws.commons.schema.*;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.namespace.QName;
import java.io.File;

public class XsdSampleGenerator {

    public static void main(String[] args) throws Exception {
        File schemaFile = new File("src/resources/iso20022.xsd");

        // 1. XSD 파일 → DOM Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(schemaFile);

        // 2. XmlSchemaCollection으로 읽기
        XmlSchemaCollection schemaCol = new XmlSchemaCollection();
        XmlSchema schema = schemaCol.read(doc, null);

        // 3. 최상위 element에서 샘플 XML 생성
        for (XmlSchemaObject obj : schema.getItems()) {
            if (obj instanceof XmlSchemaElement) {
                XmlSchemaElement element = (XmlSchemaElement) obj;
                String xml = generateElement(element, "  ");
                System.out.println(xml);
            }
        }
    }

    private static String generateElement(XmlSchemaElement element, String indent) {
        StringBuilder sb = new StringBuilder();
        String name = element.getName();

        // 반복 요소 처리 (minOccurs, maxOccurs)
        long minOccurs = element.getMinOccurs();
        long maxOccurs = element.getMaxOccurs();
        if (maxOccurs == Long.MAX_VALUE) maxOccurs = minOccurs > 0 ? minOccurs : 1;

        int repeatCount = (int) Math.max(minOccurs, 1); // 최소 1개
        repeatCount = Math.min(repeatCount, 3); // 최대 3개 제한

        for (int i = 0; i < repeatCount; i++) {
            sb.append(indent).append("<").append(name).append(">");

            if (element.getSchemaType() instanceof XmlSchemaComplexType) {
                sb.append("\n");
                XmlSchemaComplexType complexType = (XmlSchemaComplexType) element.getSchemaType();

                if (complexType.getParticle() instanceof XmlSchemaSequence) {
                    XmlSchemaSequence seq = (XmlSchemaSequence) complexType.getParticle();
                    for (XmlSchemaSequenceMember member : seq.getItems()) {
                        if (member instanceof XmlSchemaElement) {
                            XmlSchemaElement child = (XmlSchemaElement) member;
                            sb.append(generateElement(child, indent + "  ")).append("\n");
                        }
                    }
                }
                sb.append(indent).append("</").append(name).append(">");
            } else {
                // 단순 타입 → 샘플 값 매핑
                QName typeName = element.getSchemaTypeName();
                sb.append(getSampleValue(typeName));
                sb.append("</").append(name).append(">");
            }
        }
        return sb.toString();
    }

    private static String getSampleValue(QName typeName) {
        if (typeName == null) return "?";

        switch (typeName.getLocalPart()) {
            case "string":   return "sample";
            case "int":
            case "integer":  return "0";
            case "long":     return "123456789";
            case "boolean":  return "true";
            case "date":     return "2025-01-01";
            case "dateTime": return "2025-01-01T00:00:00";
            case "decimal":
            case "double":
            case "float":    return "0.0";
            default:         return "?";
        }
    }
}
