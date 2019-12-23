package com.how2java.tmall.util;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.service.impl.CategoryServiceImpl;

public class Test {
//	static int i;

	public static void main(String[] args) {
		System.out.println(123);
		System.out.println(CategoryServiceImpl.class.getName());
		System.out.println(CategoryServiceImpl.class.getSimpleName());
		System.out.println(CategoryServiceImpl.class.getPackage().getName());
		System.out.println(CategoryServiceImpl.class.getPackage().getName().replaceAll(".service.impl", ".pojo")+"."+CategoryServiceImpl.class.getSimpleName().replaceAll("ServiceImpl", ""));
	}
}
