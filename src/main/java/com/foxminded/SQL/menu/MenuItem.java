package com.foxminded.SQL.menu;

import java.lang.reflect.Method;

public class MenuItem {
	private final Object obj;
	private final String label;
	private final String target;
	private boolean isExitItem;

	public MenuItem(String label) {
		this(label, null, null);
	}
	public MenuItem(String label, Object obj, String target) {
		this.label = label;
		this.obj = obj;
		this.target = target;
	}
	
	public String getLabel() {
		return label;
	}

	void invoke() {
		if (target == null) return;
		
		try {
			Method method = obj.getClass().getMethod(target);
			method.invoke(obj);
		}
		catch (Exception ex) { ex.printStackTrace(); }
	}

	boolean isExitItem() {
		return isExitItem;
	}

	void setExitItem() {
		this.isExitItem = true;
	}
	
	public String toString() {
		return getLabel();
	}
}
