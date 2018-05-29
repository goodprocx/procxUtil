package com.procx.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

public class XMLUtil {

	/**
	 * 节点下层节点或节点属性设置方法,处理结果在parentEl上
	 * @param map 子成员或属性map
	 * @param parentEl 父级节点或属性归属节点
	 * @param pakType 打包方式：0-设置子节点；1-设置节点属性
	 * @throws Exception
	 */
	public void xmlPacker(Map map, Element parentEl, String pakType)
			throws Exception {

		Iterator leaves = map.keySet().iterator();
		while (leaves.hasNext()) {
			String key = (String) leaves.next();
			String value = (String) map.get(key);
			if ("1".equals(pakType)) {
				Element ele = new Element(key);
				ele.setText(value);
				parentEl.addContent(ele);
			} else if ("2".equals(pakType)) {
				parentEl.setAttribute(key, value);
			}
		}
	}

	/**
	 * ELEMENT转换为字符串方法
	 * @param contractRoot 根节点ELEMENT
	 * @return 转换后字符串
	 */
	public String toStr(Element contractRoot) {
		Element el = (Element) contractRoot.clone();
		Format format = Format.getCompactFormat();
		format.setEncoding("UTF-8"); //设置xml文件的字符
		format.setIndent("	"); //设置xml文件的缩进
		Document docResp = new Document(el);
		el = docResp.getRootElement();
		XMLOutputter XMLOut = new XMLOutputter(format); //在元素后换行，每一层元素缩排四格
		return XMLOut.outputString(docResp);
	}

	/**
	 * 根据Element取下层节点Map
	 * @param el 父节点
	 * @return 子节点Map
	 * @throws Exception
	 */
	public Map getLeafMap(Element el) throws Exception {
		Map leafMap = new HashMap();
		List childrenList = el.getChildren();
		for (int i = 0; i < childrenList.size(); i++) {
			Element leafEl = (Element) childrenList.get(i);
			String name = leafEl.getName();
			String val = leafEl.getText();
			if ("ATTR".equals(name)) {
				name = "ATTR_" + leafEl.getAttributeValue("CD");
				val = leafEl.getAttributeValue("VAL");
			}

			leafMap.put(name, val);
		}
		return leafMap;
	}

	public Map getAttrMap(Element el) throws Exception {
		Map attrMap = new HashMap();
		List childrenList = el.getChildren();
		for (int i = 0; i < childrenList.size(); i++) {
			Element attrEl = (Element) childrenList.get(i);
			String name = "PARAM_".concat(attrEl.getAttributeValue("PARAM_CD"));
			String val = attrEl.getAttributeValue("PARAM_VALUE");
			attrMap.put(name, val);
		}
		return attrMap;
	}

	public String getTagValue(String tabName, String xml) throws Exception {
		String tagValue = null;
		if (xml.indexOf(tabName) < 0) {
			throw new Exception(tabName + "在xml中没有定义");
		} else if (xml.indexOf("<" + tabName + "/>") != -1) {
			throw new Exception(tabName + "在xml中非存值节点");
		} else {
			String preT = "<" + tabName + ">";
			String lasT = "</" + tabName + ">";
			int tagLen = preT.length();
			int start = xml.indexOf(preT);
			int end = xml.indexOf(lasT);
			tagValue = xml.substring(start + tagLen, end);
		}
		return tagValue;
	}
	
	/**
	 * 将XML转换为Map，每个节点对应1个Map，子节点作为嵌套Map
	 * @param element
	 * @param out
	 */
    private static void createMap(Element element, Map<String,Object> out)
    {
    	if (element == null) {
            throw new IllegalArgumentException("element is null");
        }

        if (out == null) {
            throw new IllegalArgumentException("out is null");
        }

        if (element.getChildren().isEmpty()&&element.getAttributes().isEmpty()) {//不带属性叶子节点
            put(out, element.getName(),(element.getText() != null) ? element.getText().trim(): "");
            
        } else {//非叶子节点 
            HashMap<String,Object> m = new HashMap<String,Object>();

            if(!element.getChildren().isEmpty()){
            	for (Iterator<Element> i = element.getChildren().iterator(); i.hasNext();)
            		createMap(i.next(), m);
            }
           
            put(out, element.getName(), m);
    	
        }
    }
    
    private static  void put(Map<String,Object> m, String key, Object value)
    {
        Object o = m.get(key);
        if (o == null) {
            m.put(key, value);
            return;
        }
        if (o instanceof List) {
            ((List) o).add(value);
            return;
        }
        ArrayList l = new ArrayList();
        l.add(o);
        l.add(value);
        m.put(key, l);
    }
	
	public static Map<String,Object> parseXmlToMap(String xml){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			StringReader reader = new StringReader(xml);
			InputSource in = new InputSource(reader);
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(in);
			Element e = doc.getRootElement();
			createMap(e,map);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("解析报文出错");
		}
		return map;
	}
	
	public static synchronized String xmlFormat(Document document) throws IOException {
		Format format = Format.getPrettyFormat(); 
		XMLOutputter xmlout = new XMLOutputter(format);  
		StringWriter writer = new StringWriter();
		xmlout.output(document, writer);
		return writer.toString().trim();
	}
	
	/**
	 * 
	 * map解析成xml字符串
	 * @param dataMap：value的值有3种对象，1.String,2.list(List的元素有两种String或者map),3.map，因为map对象同个key不能存两个值，所以用了list
	 * @return
	 * @throws IOException
	 */
	public static String parseMapToXml(Map<String,Object> dataMap,String rootName) throws Exception{
		Element root = new Element(rootName);
		createXml(dataMap, root);
		Document d = new Document();
		d.addContent(root);
		return xmlFormat(d);
		
	}
	
	/**
	 * 
	 * 迭代方法，建xml
	 * @param map
	 * @return
	 */
	public static void createXml(Map<String,Object> map,Element parent){
		for(String key:map.keySet()){
			Object o = map.get(key);
			if(o==null){
				o = "";
			}
			if(o instanceof String){
				Element e = new Element(key).setText((String)o);
				parent.addContent(e);
			}
			else if(o instanceof List){
				List<Object> list = (List<Object>)o;
				for(Object o1:list){
					if(o1 instanceof Map){
						Element e = new Element(key);
						parent.addContent(e);
						createXml((Map)o1, e);
					}else if(o1 instanceof String){
						Element e = new Element(key).setText((String) o1);
						parent.addContent(e);
					}
				}
			}
			else{
				Element e = new Element(key);
				parent.addContent(e);
				createXml((Map)o, e);
			}
		}
	}
}
