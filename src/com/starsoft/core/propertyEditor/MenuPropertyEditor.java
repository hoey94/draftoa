package com.starsoft.core.propertyEditor;

import java.beans.PropertyEditorSupport;

import com.starsoft.core.entity.Menu;

public class MenuPropertyEditor extends PropertyEditorSupport {
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if(text!=null&&!"".equals(text)){
				Menu menu=new Menu();
				menu.setId(text);
				this.setValue(menu);
			}else{
				this.setValue(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
