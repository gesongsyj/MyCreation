package ${basePkg}.jfinal;

import com.jfinal.config.Routes;
<#list classInfoList as item>
import ${basePkg}.web.controller.${item.className}Controller;
</#list>
public class CoreRoutes extends Routes{

	@Override
	public void config() {
		<#list classInfoList as item>
		add("/${item.objName}", ${item.className}Controller.class);
		</#list>
	}

}
