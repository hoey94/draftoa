package com.starsoft.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


import com.starsoft.core.entity.BaseObject;

public class BeanUtil {

	/**
	 * Copy all basic properties (boolean, int, String, Date) but ignore
	 * reference properties.
	 * 
	 * @param srcObj
	 * @param destObj
	 */
	public static void copyProperties(Object srcObj, Object destObj) {
		Class dest = destObj.getClass();
		Class src = srcObj.getClass();
		Method[] ms = dest.getMethods();
		for (int i = 0; i < ms.length; i++) {
			String name = ms[i].getName();
			if (name.startsWith("set")) {
				Class[] cc = ms[i].getParameterTypes();
				if (cc.length == 1) {
					String type = cc[0].getName(); // parameter type
					if (type.equals("java.lang.String") || type.equals("int")
							|| type.equals("java.lang.Integer")
							|| type.equals("long")
							|| type.equals("java.lang.Long")
							|| type.equals("boolean")
							|| type.equals("java.lang.Boolean")
							|| type.equals("java.util.Date")) {
						try {
							// get property name:
							String getMethod = "";
							if (type.equals("boolean")
									|| type.equals("java.lang.Boolean")) {
								getMethod = "is" + name.substring(3);
							} else {
								getMethod = "g" + name.substring(1);
							}

							// call get method:
							Method getM = src
									.getMethod(getMethod, new Class[0]);
							Object ret = getM.invoke(srcObj, new Object[] {});
							if (ret != null) {
								ms[i].invoke(destObj, new Object[] { ret });
							}
						} catch (Exception e) {
							System.out.println("Invoke method "
									+ ms[i].getName() + " failed: "
									+ e.getMessage());
						}
					}
				}
			}
		}
	}
	/**
	 * Copy all basic properties (boolean, int, String, Date) but ignore
	 * reference properties.
	 * 
	 * @param srcObj
	 * @param destObj
	 */
	public static String getProperty(Object bean, String fieldName) {
			Field[] fields = bean.getClass().getDeclaredFields();  
			Field.setAccessible(fields, true);     
			Object obj = null;  
			for (int i = 0; i < fields.length; i++) {  
			        Field field = fields[i];		          
			        if (fieldName.equals(field.getName())) {  
			            try {  
			               obj = field.get(bean);
			               if(obj!=null&&obj instanceof BaseObject){
			            	   BaseObject objt=(BaseObject)obj;
			            	   return objt.getTname();
			               }
			            } catch (Exception e) { 
			            	System.out.println("编译出错！不存在现有类！");
			            }  
			        }  
			    }  
			return "";
	}
	/***
	 * 判断一个对象是否包含属性
	 * @param bean
	 * @param fieldName
	 * @return
	 */
	public static boolean containProperty(BaseObject bean, String fieldName){
		Field[] fields = bean.getClass().getDeclaredFields();  
//		Field.setAccessible(fields, true);  
		for (int i = 0; i < fields.length; i++) {  
	        Field field = fields[i];		          
	        if (fieldName.equals(field.getName())) {  
	        	return true;
        	}
        }
		return false;
	}
}
