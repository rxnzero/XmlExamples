package com.dhlee.xsd;

import org.apache.ws.commons.schema.*;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.namespace.QName;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XsdSampleXmlGeneratorSafeCombo {

    public static void main(String[] args) throws Exception {
        File xsdFile = new File("src/resources/iso20022.xsd");
        List<String> samples = XsdSampleXmlGeneratorSafeCombo.generateSampleXml(xsdFile);

        for (String xml : samples) {
            System.out.println("=== Sample XML ===");
            System.out.println(xml);
        }
    }
    
    private static final int MAX_REPEAT = 3;
    private static final int MAX_CHOICE_COMBO = 1;

    public static List<String> generateSampleXml(File xsdFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xsdFile);

        XmlSchemaCollection schemaCol = new XmlSchemaCollection();
        XmlSchema schema = schemaCol.read(doc, null);

        List<String> result = new ArrayList<>();
        for (XmlSchemaObject obj : schema.getItems()) {
            if (obj instanceof XmlSchemaElement) {
                result.addAll(generateElement((XmlSchemaElement) obj, 0, schema));
            }
        }
        return result;
    }
    
    private static String getIndent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("  "); // °ø¹é 2Ä­
        }
        return sb.toString();
    }

    private static List<String> generateElement(XmlSchemaElement element, int level, XmlSchema schema) {
        List<String> result = new ArrayList<>();
        String name = element.getName();
        
        String indent = getIndent(level);
        
        long minOccurs = element.getMinOccurs();
        long maxOccurs = element.getMaxOccurs();
        if (maxOccurs == Long.MAX_VALUE) maxOccurs = Math.max(minOccurs, 1);
        int repeatCount = (int) Math.max(minOccurs, 1);
        repeatCount = Math.min(repeatCount, MAX_REPEAT);

        for (int i = 0; i < repeatCount; i++) {
            String attrStr = getAttributesString(element, schema);

            XmlSchemaType type = element.getSchemaType();
            if (type == null && element.getSchemaTypeName() != null) {
                Map<QName, XmlSchemaType> types = schema.getSchemaTypes();
                type = types.get(element.getSchemaTypeName());
            }

            if (type instanceof XmlSchemaComplexType) {
                XmlSchemaComplexType complexType = (XmlSchemaComplexType) type;
                List<String> childCombinations = new ArrayList<>();
                childCombinations.add(indent + "");
                if (complexType.getParticle() != null) {
                    childCombinations = processParticleSafeCombo(complexType.getParticle(), level + 1, schema);
                }

                for (String child : childCombinations) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(indent).append("<").append(name).append(attrStr).append(">\n");
                    sb.append(child);
                    sb.append(indent).append("</").append(name).append(">\n");
                    result.add(sb.toString());
                }

            } else {
                QName typeName = element.getSchemaTypeName();
                result.add(indent + "<" + name + ">" + getSampleValue(typeName) + "</" + name + ">\n");
            }
        }

        return result;
    }

    private static String getAttributesString(XmlSchemaElement element, XmlSchema schema) {
        StringBuilder sb = new StringBuilder();

        XmlSchemaType type = element.getSchemaType();
        if (type == null && element.getSchemaTypeName() != null) {
            type = schema.getSchemaTypes().get(element.getSchemaTypeName());
        }

        if (type instanceof XmlSchemaComplexType) {
            XmlSchemaComplexType complexType = (XmlSchemaComplexType) type;

            for (XmlSchemaAttributeOrGroupRef attrObj : complexType.getAttributes()) {
                if (attrObj instanceof XmlSchemaAttribute) {
                    XmlSchemaAttribute attr = (XmlSchemaAttribute) attrObj;
                    sb.append(" ").append(attr.getName())
                      .append("=\"").append(getSampleValue(attr.getSchemaTypeName())).append("\"");
                } else if (attrObj instanceof XmlSchemaAttributeGroupRef) {
                    XmlSchemaAttributeGroupRef groupRef = (XmlSchemaAttributeGroupRef) attrObj;
                    XmlSchemaAttributeGroup group = schema.getAttributeGroups().get(groupRef.getRef());
                    if (group != null) {
                    	for (XmlSchemaAttributeGroupMember member : group.getAttributes()) {
                            if (member instanceof XmlSchemaAttribute) {
                                XmlSchemaAttribute attr = (XmlSchemaAttribute) member;
                                sb.append(" ").append(attr.getName())
                                  .append("=\"").append(getSampleValue(attr.getSchemaTypeName())).append("\"");
                            }
                        }
                    }
                }
            }
        }

        return sb.toString();
    }

    private static List<String> processParticleSafeCombo(XmlSchemaParticle particle, int level, XmlSchema schema) {
        List<String> result = new ArrayList<>();

        if (particle instanceof XmlSchemaSequence) {
            result.addAll(processSequence((XmlSchemaSequence) particle, level, schema));
        } else if (particle instanceof XmlSchemaAll) {
            result.addAll(processAll((XmlSchemaAll) particle, level, schema));
        } else if (particle instanceof XmlSchemaChoice) {
            XmlSchemaChoice choice = (XmlSchemaChoice) particle;
            List<List<String>> optionLists = new ArrayList<>();
            for (XmlSchemaChoiceMember member : choice.getItems()) {
                if (member instanceof XmlSchemaElement) {
                    optionLists.add(generateElement((XmlSchemaElement) member, level, schema));
                }
            }
            result.addAll(combineOptionsLimited(optionLists));
        }

        return result;
    }

    private static List<String> processSequence(XmlSchemaSequence seq, int level, XmlSchema schema) {
        List<String> result = new ArrayList<>();
        result.add("");
        for (XmlSchemaSequenceMember member : seq.getItems()) {
            if (member instanceof XmlSchemaElement) {
                List<String> childList = generateElement((XmlSchemaElement) member, level, schema);
                result = combineSequential(result, childList);
            }
        }
        return result;
    }

    private static List<String> processAll(XmlSchemaAll all, int level, XmlSchema schema) {
        List<String> result = new ArrayList<>();
        result.add("");
        for (XmlSchemaAllMember member : all.getItems()) {
            if (member instanceof XmlSchemaElement) {
                List<String> childList = generateElement((XmlSchemaElement) member, level, schema);
                result = combineSequential(result, childList);
            }
        }
        return result;
    }

    private static List<String> combineSequential(List<String> base, List<String> additions) {
        List<String> result = new ArrayList<>();
        for (String b : base) {
            for (String a : additions) {
                if (b.endsWith("\n") && !a.startsWith("\n")) {
                    result.add(b + a); // ÁÙ¹Ù²Þ À¯Áö
                } else {
                    result.add(b + a);
                }
            }
        }
        return result;
    }

    private static List<String> combineOptionsLimited(List<List<String>> optionLists) {
        List<String> result = new ArrayList<>();
        int n = optionLists.size();
        int combos = (1 << n) - 1;
        int count = 0;
        for (int mask = 1; mask <= combos && count < MAX_CHOICE_COMBO; mask++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    List<String> options = optionLists.get(i);
                    sb.append(options.get(0));
                }
            }
            result.add(sb.toString());
            count++;
        }
        return result;
    }

    private static String getSampleValue(QName typeName) {
        if (typeName == null) return "?";
        switch (typeName.getLocalPart()) {
            case "string": return "sample";
            case "int":
            case "integer": return "0";
            case "long": return "123456789";
            case "boolean": return "true";
            case "date": return "2025-01-01";
            case "dateTime": return "2025-01-01T00:00:00";
            case "decimal":
            case "double":
            case "float": return "0.0";
            default: return "?";
        }
    }
}