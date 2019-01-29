package build;

import com.generator.MyGenerator.MyGenerator;

public class Build {
	public static void main(String[] args) {
		/**
		 * 第一个参数:生成的代码放置的位置:基础包名
		 * 第二个参数:生成的代码放置的位置:公共包名
		 * 第三个参数:数据库配置文件名称,如果传null或""则默认为jfinal.properties,文件可以不存在,若不存在需要配置JdbcUrl,User,Password三个属性值,见示例.
		 * 			如果文件不存在,会生成一份配置文件,且该传入的名称就是生成文件的名称
		 * 第四个参数:初次生成配置为false,生成所有要生成的文件,以后如果数据库有变更,将该参数改为true,则只修改baseMode,同步数据库的变更
		 * 第五个参数:是否生成service对象,小型web项目可设置成false,直接在Controller中完成业务代码
		 * 第六个参数:是否生成queryObj对象,供查询使用,本生成器生成的代码中有baseController及一个拦截器,可将请求参数注入到baseController中的一个map对象里,小型web项目可不生成queryObj对象
		 */
		MyGenerator myGenerator = new MyGenerator("com.testGen.system", "com.testGen.common", "jfinal.properties", false, false, false);
		/**
		 * 设置需要跳过的表
		 */
		//myGenerator.addExcludedTable("tableName1","tableName2");
		/**
		 * 设置只需要生成的某些表,设置了该项,addExcludedTable()方法就不起作用了
		 */
		//myGenerator.addIncludedTable("tableName1","tableName2");
		
		/**
		 * 数据库信息配置,如果有配置文件,可在构造器第三个参数中传入配置文件名称,以下三个参数即可不设置
		 */
		myGenerator.setJdbcUrlValue("111.111.22.22/dbName");
		myGenerator.setUserValue("root");
		myGenerator.setPasswordValue("1234");
		
		/**
		 * 生成代码
		 */
		myGenerator.generate();
	}
}
