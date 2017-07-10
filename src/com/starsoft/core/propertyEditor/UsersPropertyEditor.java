package com.starsoft.core.propertyEditor;

import java.beans.PropertyEditorSupport;

import com.starsoft.core.entity.Users;

public class UsersPropertyEditor extends PropertyEditorSupport {
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if(text!=null&&!"".equals(text)){
				Users user=new Users();
				user.setId(text);
				this.setValue(user);
			}else{
				this.setValue(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
