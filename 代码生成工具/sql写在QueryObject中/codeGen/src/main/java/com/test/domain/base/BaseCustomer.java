package com.test.domain.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCustomer<M extends BaseCustomer<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return get("id");
	}

	public void setIdNum(java.lang.String idNum) {
		set("idNum", idNum);
	}

	public java.lang.String getIdNum() {
		return get("idNum");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setImage1(java.lang.String image1) {
		set("image1", image1);
	}

	public java.lang.String getImage1() {
		return get("image1");
	}

	public void setImage2(java.lang.String image2) {
		set("image2", image2);
	}

	public java.lang.String getImage2() {
		return get("image2");
	}

}
