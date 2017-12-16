package ${basePkg}.jfinal;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.OrderedFieldContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;

public class CoreConfig extends JFinalConfig{

	@Override
	public void configConstant(Constants me) {
		loadPropertyFile("jfinal.properties");
		me.setEncoding("UTF-8");
		me.setDevMode(true);
		me.setViewType(ViewType.JSP);
	}

	@Override
	public void configRoute(Routes me) {
		me.add(new CoreRoutes());
	}

	@Override
	public void configPlugin(Plugins me) {
		//读取jdbc配置  
		final String url=getProperty("jdbcUrl");
		final String username=getProperty("user");
		final String password=getProperty("password");
		final Integer initialSize=Integer.parseInt(getProperty("initialSize"));
		final Integer minIdle=Integer.parseInt(getProperty("minIdle"));
		final Integer maxActive=Integer.parseInt(getProperty("maxActive"));
		final String driverClass=getProperty("driverClass");
		
		DruidPlugin druidPlugin = new DruidPlugin(url, username, password, driverClass);
		druidPlugin.set(initialSize, minIdle, maxActive);
		druidPlugin.setFilters("stat,wall");//监控统计："stat" ;防SQL注入："wall" 
		me.add(druidPlugin);
		//实体映射  
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		arp.setShowSql(true);
		arp.setContainerFactory(new OrderedFieldContainerFactory());//字段有序，保持和查询的顺序一致
		me.add(arp);
		//DB映射
		_MappingKit.mapping(arp);
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {  
        JFinal.start("src/main/webapp", 8080, "/", 5);//启动
    }

}
