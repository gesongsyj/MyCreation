package com.test.web.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import com.test.query.ManagerQueryObject;
import com.test.service.ManagerService;

public class ManagerController extends Controller{
	ManagerService managerService=new ManagerService();
	
	public void testSave(){
		for (int i = 0; i <20; i++) {
			Record record=new Record().set("id", 100+i);
			managerService.save(record);
			
		}
		renderText("添加");
	}
	public void testPage(){
		ManagerQueryObject qo=new ManagerQueryObject();
		qo.setKeyword("18");
		//qo.setSqlSelectFrom("select u.*,d.* from manager u join department d on u.id=d.manager_id");
		Page<Record> paginate = managerService.paginate(qo);
		System.out.println(paginate.getList());
		renderText("查询");
	}
}
