package com.how2java.tmall.service;

import org.springframework.stereotype.Service;

import com.how2java.tmall.pojo.Product;

public interface PropertyValueService extends BaseService {
	public void init(Product product);
}
