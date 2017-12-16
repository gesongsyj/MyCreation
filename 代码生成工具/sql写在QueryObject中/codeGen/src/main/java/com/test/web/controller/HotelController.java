package com.test.web.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import com.test.query.HotelQueryObject;
import com.test.service.HotelService;

public class HotelController extends Controller{
	HotelService hotelService=new HotelService();
	
	public void testSave(){
		for (int i = 0; i <20; i++) {
			Record record=new Record().set("id", 100+i);
			hotelService.save(record);
			
		}
		renderText("添加");
	}
	public void testPage(){
		HotelQueryObject qo=new HotelQueryObject();
		qo.setKeyword("18");
		//qo.setSqlSelectFrom("select u.*,d.* from hotel u join department d on u.id=d.hotel_id");
		Page<Record> paginate = hotelService.paginate(qo);
		System.out.println(paginate.getList());
		renderText("查询");
	}
}
