package com.project.tool;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.project.constans.wecharConstans;



/**
 * <p>Title: XMLUtil</p>
 * <p>Discription: 微信支付 <--> XML工具类 </p>
 * @author 吴敏明
 * @date 2017年12月10日 下午4:10:57
 */
public class XMLUtil {
	
	
	/** 
     * xml转map 不带属性 
     * @param xmlStr 
     * @param needRootKey 是否需要在返回的map里加根节点键 
     * @return 
     * @throws DocumentException 
     */  
    public static Map<String, Object> xml2map(String xmlStr, boolean needRootKey) throws DocumentException {  
        Document doc = DocumentHelper.parseText(xmlStr);
        Element root = doc.getRootElement();  
        Map<String, Object> map = (Map<String, Object>) xml2map(root);  
        if(root.elements().size()==0 && root.attributes().size()==0){  
            return map;  
        }  
        if(needRootKey){  
            //在返回的map里加根节点键（如果需要）  
            Map<String, Object> rootMap = new HashMap<String, Object>();  
            rootMap.put(root.getName(), map);  
            return rootMap;  
        }  
        return map;  
    }  
	

    /** 
     * xml转map 不带属性 
     * @param e 
     * @return 
     */  
    @SuppressWarnings("unchecked")
	private static Map<String, Object> xml2map(Element e) {  
        Map<String, Object> map = new LinkedHashMap<String, Object>();  
        List<Element> list = e.elements();  
        if (list.size() > 0) {  
            for (int i = 0; i < list.size(); i++) {  
                Element iter = (Element) list.get(i);  
                List<Object> mapList = new ArrayList<Object>();  
  
                if (iter.elements().size() > 0) {  
                    Map<String, Object> m = xml2map(iter);  
                    if (map.get(iter.getName()) != null) {  
                        Object obj = map.get(iter.getName());  
                        if (!(obj instanceof List)) {  
                            mapList = new ArrayList<Object>();  
                            mapList.add(obj);  
                            mapList.add(m);  
                        }  
                        if (obj instanceof List) {  
                            mapList =  (List<Object>) obj;  
                            mapList.add(m);  
                        }  
                        map.put(iter.getName(), mapList);  
                    } else  
                        map.put(iter.getName(), m);  
                } else {  
                    if (map.get(iter.getName()) != null) {  
                        Object obj = map.get(iter.getName());  
                        if (!(obj instanceof List)) {  
                            mapList = new ArrayList<Object>();  
                            mapList.add(obj);  
                            mapList.add(iter.getText());  
                        }  
                        if (obj instanceof List) {  
                            mapList = (List<Object>) obj;  
                            mapList.add(iter.getText());  
                        }  
                        map.put(iter.getName(), mapList);  
                    } else  
                        map.put(iter.getName(), iter.getText());  
                }  
            }  
        } else  
            map.put(e.getName(), e.getText());  
        return map;  
    } 
    
    /**
     * map转换成xml
     * @param param
     * @return
     */
    public static String map2Xml(Map<Object, Object> param) {
        Set<Entry<Object, Object>> set = param.entrySet();
        Iterator<Entry<Object, Object>> it = set.iterator();
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<xml>");
        while (it.hasNext()) {
            Entry<Object, Object> entry = it.next();
            sBuilder.append("<" + entry.getKey() + ">");
            sBuilder.append(entry.getValue());
            sBuilder.append("</" + entry.getKey() + ">");
        }
        sBuilder.append("</xml>");
        try {
        	return new String(sBuilder.toString().getBytes(wecharConstans.CHARSETS),wecharConstans.CHARSETS);
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }
}
