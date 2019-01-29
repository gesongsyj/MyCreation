package com.junyi.utils.generator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 某些数据需要多个classInfo的数据时使用该类封装多个classInfo的信息
 * @author Administrator
 *
 */
@Getter@Setter
public class ClassInfos {
	private String basePkg;//基础包名
	private List<ClassInfo> classInfoList=new ArrayList<>();
}
