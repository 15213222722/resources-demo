package com.resourses.tool.xml;

import java.io.StringReader;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtils {
    
    private static final String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
    
    public static String convertXmlForMap(Map<String, Object> params) {
        if (params != null && params.size() > 0) {
            Document _document = DocumentHelper.createDocument();
            Element _root = _document.addElement("xml");
            Set<Map.Entry<String, Object>> sets = params.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = sets.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String tableKey = entry.getKey();
                Element clElement = _root.addElement(tableKey);
                clElement.addText(entry.getValue().toString());
            }
            return _document.asXML();
        }
        return "";
    }
    
    public static Document convertXmlForString(String xmlString) {
        Document doc;
        
        try {
            
            SAXReader reader = new SAXReader();
            reader.setFeature(FEATURE ,true);
            String encoding = getEncoding(xmlString);
            
            InputSource source = new InputSource(new StringReader(xmlString));
            source.setEncoding(encoding);
            
            doc = reader.read(source);
            
            if (doc.getXMLEncoding() == null) {
                doc.setXMLEncoding(encoding);
            }
        } catch (DocumentException | SAXException e) {
            throw new RuntimeException("转换xml字符串异常,XML[:" + xmlString + "]", e);
        }
        return doc;
    }


    /**
     * 将返回的信息变为map集合
     *
     * @param doc
     * @return 2015年12月10日上午9:21:58
     */
    public static Map<String, Object> getReturnContentByMap(Document doc) {
        Map<String, Object> sortMap = new TreeMap<String, Object>();
        Element element = doc.getRootElement();
        recursiveConvertMap(element, sortMap);
        return sortMap;
    }

    private static void recursiveConvertMap(Element element, Map<String, Object> map) {
        Iterator<Element> subElements = element.elementIterator();
        if (null != subElements && subElements.hasNext()) {
            while (subElements.hasNext()) {
                recursiveConvertMap(subElements.next(), map);
            }
        } else {
            map.put(element.getName(), element.getText());
        }
    }
    
    private static String getEncoding(String text) {
        String result = null;
        
        String xml = text.trim();
        
        if (xml.startsWith("<?xml")) {
            int end = xml.indexOf("?>");
            String sub = xml.substring(0, end);
            StringTokenizer tokens = new StringTokenizer(sub, " =\"\'");
            
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                
                if ("encoding".equals(token)) {
                    if (tokens.hasMoreTokens()) {
                        result = tokens.nextToken();
                    }
                    
                    break;
                }
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        String text = "<alipay>"
                + "<is_success>T</is_success>"
                + "<request>"
                + "<param name=" + "'service'" + ">single_trade_query</param>"
                + "<param name=" + "'_input_charset'" + ">utf-8</param>"
                + "</request>"
                + "<response>"
                + "<trade>"
                + "<additional_trade_status>DAEMON_CONFIRM_CLOSE</additional_trade_status>"
                + "<buyer_email>18523286913</buyer_email>"
                + "</trade>"
                + "</response>"
                + "</alipay>";
        Map<String, Object> params = getReturnContentByMap(convertXmlForString(text));
        System.out.println(params);
        for (String string : params.keySet()) {
            System.out.println(string + "------" + params.get(string));
        }
    }
}
