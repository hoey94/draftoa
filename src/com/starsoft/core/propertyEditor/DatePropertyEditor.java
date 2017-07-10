package com.starsoft.core.propertyEditor;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePropertyEditor extends PropertyEditorSupport {
	public void setAsText(String text) throws IllegalArgumentException {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(text!=null&&text.length()==10){
				if(text.indexOf("/")>0){//10/14/2010
					text=text.substring(6,10)+"-"+text.substring(0,2)+"-"+text.substring(3,5);
				}
				Date date = format1.parse(text);
				this.setValue(date);
				
			}else if(text!=null&&text.length()==19){
				Date date = format2.parse(text);
				this.setValue(date);
			}else{
				this.setValue(null);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
