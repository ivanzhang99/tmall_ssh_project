package com.how2java.tmall.action;

import org.apache.struts2.convention.annotation.Action;

public class PropertyValueAction extends Action4Result {
	@Action("admin_propertyValue_edit")
	public String edit() {
		t2p(product);
		propertyValueService.init(product);
		propertyValues = propertyValueService.listByParent(product);
//		System.out.println(1232);
		return "editPropertyValue";
	}

	@Action("admin_propertyValue_update")
	public String update() {
		String value = propertyValue.getValue();
		t2p(propertyValue);
		propertyValue.setValue(value);
		propertyValueService.update(propertyValue);
		return "success.jsp";
	}
}
