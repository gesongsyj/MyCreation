package com.junyi.utils.generator;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * 表示数据模型对象,封装了模板中占位符的数据
 * @author Administrator
 *
 */
@Getter
public class ClassInfo {
	private String basePkg;//基础包名
	private String className;//简单类名
	private String classNameToLower;//简单类名全小写
	private String objName;//对象名称,即简单类名首字母小写
	private List<String> propsName=new ArrayList<String>(); //当前对象中所有的属性名称
	
	public ClassInfo(Class<?> clazz) throws Exception {
		String pkgName=clazz.getPackage().getName();
		basePkg=pkgName.substring(0, pkgName.lastIndexOf("."));
		className=clazz.getSimpleName();
		classNameToLower=className.toLowerCase();
		objName=className.substring(0, 1).toLowerCase()+className.substring(1);
		//使用内省机制获得属性名称
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz,Object.class);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor pd : pds) {
			propsName.add(pd.getName());
		}
	}
}
