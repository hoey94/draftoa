package com.starsoft.core.propertyEditor;

import java.beans.PropertyEditorSupport;

import com.starsoft.core.entity.Appes;

public class AppesPropertyEditor extends PropertyEditorSupport {
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if(text!=null&&!"".equals(text)){
				Appes appes=new Appes();
				appes.setId(text);
				this.setValue(appes);
			}else{
				this.setValue(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
