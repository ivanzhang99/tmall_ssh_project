package com.how2java.tmall.service;

import java.util.List;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.util.Page;

public interface PropertyService extends BaseService {
	public List listByCategory(Category category);

	public List list(Page page, Category category);

	public int total(Category category);
}
