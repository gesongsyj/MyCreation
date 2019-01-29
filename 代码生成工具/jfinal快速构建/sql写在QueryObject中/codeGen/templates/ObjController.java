package ${basePkg}.web.controller;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import ${basePkg}.query.${className}QueryObject;
import ${basePkg}.service.${className}Service;

public class ${className}Controller extends Controller{
	${className}Service ${objName}Service=new ${className}Service();
	
	public void testSave(){
		for (int i = 0; i <20; i++) {
			Record record=new Record().set("id", 100+i);
			${objName}Service.save(record);
			
		}
		renderText("添加");
	}
	public void testPage(){
		${className}QueryObject qo=new ${className}QueryObject();
		qo.setKeyword("18");
		//qo.setSqlSelectFrom("select u.*,d.* from ${classNameToLower} u join department d on u.id=d.${classNameToLower}_id");
		Page<Record> paginate = ${objName}Service.paginate(qo);
		System.out.println(paginate.getList());
		renderText("查询");
	}
}
