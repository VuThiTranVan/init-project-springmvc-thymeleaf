package com.spring.sample.model;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public abstract class BaseModel {
	private Sort sort;
	private Integer page = 1;
	private Integer perPage = 3;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPerPage() {
		return perPage;
	}

	public void setPerPage(Integer perPage) {
		this.perPage = perPage;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public Pageable getPageable() {
		if (sort == null) {
			return PageRequest.of(page - 1, perPage);
		} else {
			return PageRequest.of(page - 1, perPage, sort);
		}
	}
}
