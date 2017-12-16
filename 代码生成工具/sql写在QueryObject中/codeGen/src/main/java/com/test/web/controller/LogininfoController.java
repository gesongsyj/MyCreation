package com.test.web.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import com.test.query.LogininfoQueryObject;
import com.test.service.LogininfoService;

public class LogininfoController extends Controller{
	LogininfoService logininfoService=new LogininfoService();
	
	public void testSave(){
		for (int i = 0; i <20; i++) {
			Record record=new Record().set("id", 100+i);
			logininfoService.save(record);
			
		}
		renderText("添加");
	}
	public void testPage(){
		LogininfoQueryObject qo=new LogininfoQueryObject();
		qo.setKeyword("18");
		//qo.setSqlSelectFrom("select u.*,d.* from logininfo u join department d on u.id=d.logininfo_id");
		Page<Record> paginate = logininfoService.paginate(qo);
		System.out.println(paginate.getList());
		renderText("查询");
	}
}
