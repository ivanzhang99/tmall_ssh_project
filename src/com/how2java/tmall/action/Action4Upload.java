package com.how2java.tmall.action;

import java.io.File;

public class Action4Upload {
	protected File img;
	protected String imgfileName;
	protected String imgContentType;

	public File getImg() {
		return img;
	}

	public void setImg(File img) {
		this.img = img;
	}

	public String getImgfileName() {
		return imgfileName;
	}

	public void setImgfileName(String imgfileName) {
		this.imgfileName = imgfileName;
	}

	public String getImgContentType() {
		return imgContentType;
	}

	public void setImgContentType(String imgContentType) {
		this.imgContentType = imgContentType;
	}
}
