
package com.starsoft.core.util;
import java.io.BufferedReader;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.net.URL;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
import net.sf.json.JSONArray;  
import net.sf.json.JSONObject;  
public class JsonTools {  
  
    public static List<Map<String, Object>> parseJSON2List(String jsonStr){  
        JSONArray jsonArr = JSONArray.fromObject(jsonStr);  
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
        Iterator<JSONObject> it = jsonArr.iterator();  
        while(it.hasNext()){  
            JSONObject json2 = it.next();  
            list.add(parseJSON2Map(json2.toString()));  
        }  
        return list;  
    }  
      
     
    public static Map<String, Object> parseJSON2Map(String jsonStr){  
        Map<String, Object> map = new HashMap<String, Object>();  
        //最外层解析  
        JSONObject json = JSONObject.fromObject(jsonStr);  
        for(Object k : json.keySet()){  
            Object v = json.get(k);   
            //如果内层还是数组的话，继续解析  
            if(v instanceof JSONArray){  
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
                Iterator<JSONObject> it = ((JSONArray)v).iterator();  
                while(it.hasNext()){  
                    JSONObject json2 = it.next();  
                    list.add(parseJSON2Map(json2.toString()));  
                }  
                map.put(k.toString(), list);  
            } else {  
                map.put(k.toString(), v);  
            }  
        }  
        return map;  
    }  
      
     
//    public static List<Map<String, Object>> getListByUrl(String url){  
//        try {  
//            //通过HTTP获取JSON数据  
//            InputStream in = new URL(url).openStream();  
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
//            StringBuilder sb = new StringBuilder();  
//            String line;  
//            while((line=reader.readLine())!=null){  
//                sb.append(line);  
//            }  
//            return parseJSON2List(sb.toString());  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }  
//        return null;  
//    }  
//      
//     
//    public static Map<String, Object> getMapByUrl(String url){  
//        try {  
//            //通过HTTP获取JSON数据  
//            InputStream in = new URL(url).openStream();  
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
//            StringBuilder sb = new StringBuilder();  
//            String line;  
//            while((line=reader.readLine())!=null){  
//                sb.append(line);  
//            }  
//            return parseJSON2Map(sb.toString());  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }  
//        return null;  
//    }  
//      
      
    //test  
    public static void main(String[] args) {  
        String url = "http://...";  
//        List<Map<String,Object>> list = getListByUrl(url);  
//        System.out.println(list);  
    }  
}  
