package com.how2java.tmall.service;

import java.util.List;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductImage;

public interface ProductImageService extends BaseService {
	public static String type_single = "type_single";
	public static String type_detail = "type_detail";

//	public List<ProductImage> list(String key_product, Product product, String key_type, String type);

	public void setFirstProdutImage(Product product);
}
