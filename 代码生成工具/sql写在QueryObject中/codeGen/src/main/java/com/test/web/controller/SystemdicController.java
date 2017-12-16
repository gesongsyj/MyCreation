package com.test.web.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import com.test.query.SystemdicQueryObject;
import com.test.service.SystemdicService;

public class SystemdicController extends Controller{
	SystemdicService systemdicService=new SystemdicService();
	
	public void testSave(){
		for (int i = 0; i <20; i++) {
			Record record=new Record().set("id", 100+i);
			systemdicService.save(record);
			
		}
		renderText("添加");
	}
	public void testPage(){
		SystemdicQueryObject qo=new SystemdicQueryObject();
		qo.setKeyword("18");
		//qo.setSqlSelectFrom("select u.*,d.* from systemdic u join department d on u.id=d.systemdic_id");
		Page<Record> paginate = systemdicService.paginate(qo);
		System.out.println(paginate.getList());
		renderText("查询");
	}
}
