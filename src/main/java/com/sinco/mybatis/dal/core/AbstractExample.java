package com.sinco.mybatis.dal.core;

import com.sinco.data.core.Page;

/**
 * 添加分页
 * @author james
 *
 * @param <T>
 */
public abstract class AbstractExample<T> {

	private Page<T> page;

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}
}
