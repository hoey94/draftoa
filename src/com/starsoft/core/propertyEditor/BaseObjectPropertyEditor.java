package com.starsoft.core.propertyEditor;

import java.beans.PropertyEditorSupport;

import com.starsoft.core.entity.BaseObject;

public class  BaseObjectPropertyEditor extends PropertyEditorSupport {
	private BaseObject obj;
	public BaseObjectPropertyEditor(BaseObject object){
		this.obj=object;
	}
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if(text!=null&&!"".equals(text)){
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
