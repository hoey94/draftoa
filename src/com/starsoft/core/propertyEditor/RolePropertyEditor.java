package com.starsoft.core.propertyEditor;

import java.beans.PropertyEditorSupport;

import com.starsoft.core.entity.Role;

public class RolePropertyEditor extends PropertyEditorSupport {
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if(text!=null&&!"".equals(text)){
				Role role=new Role();
				role.setId(text);
				this.setValue(role);
			}else{
				this.setValue(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
