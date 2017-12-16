package com.test.web.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import com.test.query.SystemdicitemQueryObject;
import com.test.service.SystemdicitemService;

public class SystemdicitemController extends Controller{
	SystemdicitemService systemdicitemService=new SystemdicitemService();
	
	public void testSave(){
		for (int i = 0; i <20; i++) {
			Record record=new Record().set("id", 100+i);
			systemdicitemService.save(record);
			
		}
		renderText("添加");
	}
	public void testPage(){
		SystemdicitemQueryObject qo=new SystemdicitemQueryObject();
		qo.setKeyword("18");
		//qo.setSqlSelectFrom("select u.*,d.* from systemdicitem u join department d on u.id=d.systemdicitem_id");
		Page<Record> paginate = systemdicitemService.paginate(qo);
		System.out.println(paginate.getList());
		renderText("查询");
	}
}
