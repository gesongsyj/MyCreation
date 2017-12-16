package com.test.web.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import com.test.query.CustomerQueryObject;
import com.test.service.CustomerService;

public class CustomerController extends Controller{
	CustomerService customerService=new CustomerService();
	
	public void testSave(){
		for (int i = 0; i <20; i++) {
			Record record=new Record().set("id", 100+i);
			customerService.save(record);
			
		}
		renderText("添加");
	}
	public void testPage(){
		CustomerQueryObject qo=new CustomerQueryObject();
		qo.setKeyword("18");
		//qo.setSqlSelectFrom("select u.*,d.* from customer u join department d on u.id=d.customer_id");
		Page<Record> paginate = customerService.paginate(qo);
		System.out.println(paginate.getList());
		renderText("查询");
	}
}
