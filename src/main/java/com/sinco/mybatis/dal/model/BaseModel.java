/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sinco.mybatis.dal.model;

import java.io.Serializable;

/**
 * Base model支持类
 * @author ThinkGem
 * @version 2013-01-15
 */
public abstract class BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前操作用户id,该字段不为数据库字段，具体使用请看 DataModel 
	 */
	private String currentUserId;

	/**
	 * 当前操作用户id,该字段不为数据库字段，
	 */
	protected String getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

}
