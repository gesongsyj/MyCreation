package com.test.web.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import com.test.query.AuthrecordQueryObject;
import com.test.service.AuthrecordService;

public class AuthrecordController extends Controller{
	AuthrecordService authrecordService=new AuthrecordService();
	
	public void testSave(){
		for (int i = 0; i <20; i++) {
			Record record=new Record().set("id", 100+i);
			authrecordService.save(record);
			
		}
		renderText("添加");
	}
	public void testPage(){
		AuthrecordQueryObject qo=new AuthrecordQueryObject();
		qo.setKeyword("18");
		//qo.setSqlSelectFrom("select u.*,d.* from authrecord u join department d on u.id=d.authrecord_id");
		Page<Record> paginate = authrecordService.paginate(qo);
		System.out.println(paginate.getList());
		renderText("查询");
	}
}
