package com.starsoft.cms.propertyEditor;

import java.beans.PropertyEditorSupport;

import com.starsoft.cms.entity.Columns;

public class ColumnsPropertyEditor extends PropertyEditorSupport {
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if(text!=null){
				Columns obj=new Columns();
				obj.setId(text);
				this.setValue(obj);
			}else{
				this.setValue(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	
}
