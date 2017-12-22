package com.generator.MyGenerator;

import java.io.ObjectInputStream.GetField;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import com.generator.HelperClass.MetaBuilder;
import com.generator.HelperClass.TableMeta;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.druid.DruidPlugin;

public class MyGenerator {
	// jdbc配置的key值
	protected String jdbcUrl = "jdbcUrl";
	protected String username = "user";
	protected String password = "password";
	/**
	 * 项目目录包名
	 */
	protected String basePackageName;
	protected String modelPackageName;
	protected String baseModelPackageName;
	protected String jfinalPackageName;
	protected String queryPackageName;
	protected String servicePackageName;
	protected String controllerPackageName;
	protected String mappingKitPackageName;

	protected String alittle_config_file_name;
	protected boolean onlyBaseModel;

	protected Dialect dialect = null;
	protected MetaBuilder metaBuilder;

	protected BaseModelGenerator baseModelGenerator;
	protected ModelGenerator modelGenerator;
	protected MappingKitGenerator mappingKitGenerator;
	protected AllSqlGenerator allSqlGenerator;
	protected ControllerGenerator controllerGenerator;
	protected HtmlGenerator htmlGenerator;
	protected ServiceGenerator serviceGenerator;
	protected QueryObjectGenerator queryObjectGenerator;
	protected ObjQueryObjectGenerator objQueryObjectGenerator;
	protected SqlGenerator sqlGenerator;
	protected SuperModelGenerator superModelGenerator;
	protected CoreConfigGenerator coreConfigGenerator;
	protected CoreRoutesGenerator coreRoutesGenerator;
	protected FakeBaseModelGenerator fakeBaseModelGenerator;
	protected WebGenerator webGenerator;
	protected PomGenerator pomGenerator;
	protected JfinalPropGenerator jfinalPropGenerator;

	protected Set<IGenerator> generators = new HashSet<IGenerator>();

	/**
	 * 
	 * @param basePackageName
	 *            基础包名
	 * @param onlyBaseModel
	 *            是否只生成aseModel
	 */
	public MyGenerator(String basePackageName, String configName, boolean onlyBaseModel) {
		this.basePackageName = basePackageName;
		this.modelPackageName = basePackageName + ".model";
		this.baseModelPackageName = basePackageName + ".model.base";
		this.jfinalPackageName = basePackageName + ".jfinal";
		this.queryPackageName = basePackageName + ".query";
		this.servicePackageName = basePackageName + ".service";
		this.controllerPackageName = basePackageName + ".web";
		this.mappingKitPackageName = basePackageName + ".model.base";

		this.alittle_config_file_name = configName;
		this.onlyBaseModel = onlyBaseModel;
		// init(basePackageName, onlyBaseModel);
	}

	/**
	 * 根据基础包名初始化默认的各个包名以及输出路径
	 * 
	 * @param basePackageName2
	 */
	private void init(String basePackageName, boolean onlyBaseModel) {
		if (generators.size() > 0) {
			generators.clear();
		}
		if (onlyBaseModel) {
			String baseModelPackageName = this.baseModelPackageName;
			String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ baseModelPackageName.replace(".", "/");
			this.baseModelGenerator = new BaseModelGenerator(baseModelPackageName, baseModelOutputDir);
			generators.add(baseModelGenerator);
		} else {
			String baseModelPackageName = this.baseModelPackageName;
			String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ baseModelPackageName.replace(".", "/");
			// =============================
			String modelPackageName = this.modelPackageName;
			String modelOutputDir = PathKit.getWebRootPath() + "/src/main/java/" + modelPackageName.replace(".", "/");
			// =============================
			String mappingKitPackageName = this.mappingKitPackageName;
			String mappingKitOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ mappingKitPackageName.replace(".", "/");
			// =============================
			String allSqlOutputDir = PathKit.getWebRootPath() + "/src/main/resources";
			// =============================
			String controllerPackageName = this.controllerPackageName;
			String controllerOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ controllerPackageName.replace(".", "/");
			// =============================
			String coreConfigPackageName = this.jfinalPackageName;
			String coreConfigOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ coreConfigPackageName.replace(".", "/");
			// =============================
			String coreRoutesPackageName = this.jfinalPackageName;
			String coreRoutesOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ coreRoutesPackageName.replace(".", "/");
			// =============================
			/*
			 * String servicePackageName = getServicePackageName(); String
			 * serviceOutputDir = PathKit.getWebRootPath() + "/src/main/java/" +
			 * servicePackageName.replace(".", "/");
			 */
			// =============================
			String objQueryObjectPackageName = this.queryPackageName;
			String objQueryObjectOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ objQueryObjectPackageName.replace(".", "/");
			// =============================
			String queryObjectPackageName = this.queryPackageName;
			String queryObjectOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ queryObjectPackageName.replace(".", "/");
			// =============================
			String sqlOutputDir = PathKit.getWebRootPath() + "/src/main/resources";
			// =============================
			String superModelPackageName = this.baseModelPackageName;
			String superModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ superModelPackageName.replace(".", "/");
			;
			// =============================
			String webXMLOutputDir = PathKit.getWebRootPath() + "/src/main/webapp/WEB-INF";
			// =============================
			String pomXMLOutputDir = PathKit.getWebRootPath();
			// =============================
			String jfinalPropOutputDir = PathKit.getWebRootPath() + "/src/main/resources";
			// =============================
			this.allSqlGenerator = new AllSqlGenerator(allSqlOutputDir);
			generators.add(allSqlGenerator);
			this.baseModelGenerator = new BaseModelGenerator(baseModelPackageName, baseModelOutputDir);
			generators.add(baseModelGenerator);
			this.controllerGenerator = new ControllerGenerator(controllerPackageName, modelPackageName,
					objQueryObjectPackageName, controllerOutputDir);
			generators.add(controllerGenerator);
			this.coreRoutesGenerator = new CoreRoutesGenerator(coreRoutesPackageName, controllerPackageName,
					coreRoutesOutputDir);
			generators.add(coreRoutesGenerator);
			this.mappingKitGenerator = new MappingKitGenerator(mappingKitPackageName, modelPackageName,
					mappingKitOutputDir);
			generators.add(mappingKitGenerator);
			if (alittle_config_file_name != null || StrKit.notBlank(alittle_config_file_name)) {
				this.coreConfigGenerator = new CoreConfigGenerator(coreConfigPackageName, mappingKitPackageName,
						coreConfigOutputDir, alittle_config_file_name);
				generators.add(coreConfigGenerator);
			} else {
				this.coreConfigGenerator = new CoreConfigGenerator(coreConfigPackageName, mappingKitPackageName,
						coreConfigOutputDir);
				generators.add(coreConfigGenerator);
			}
			this.modelGenerator = new ModelGenerator(modelPackageName, baseModelPackageName, objQueryObjectPackageName,
					modelOutputDir);
			generators.add(modelGenerator);
			this.objQueryObjectGenerator = new ObjQueryObjectGenerator(objQueryObjectPackageName,
					objQueryObjectOutputDir);
			generators.add(objQueryObjectGenerator);
			/*
			 * this.serviceGenerator = new ServiceGenerator(servicePackageName,
			 * modelPackageName, objQueryObjectPackageName, serviceOutputDir);
			 * generators.add(serviceGenerator);
			 */
			this.queryObjectGenerator = new QueryObjectGenerator(queryObjectPackageName, modelPackageName,
					queryObjectOutputDir);
			generators.add(queryObjectGenerator);
			this.sqlGenerator = new SqlGenerator(sqlOutputDir);
			generators.add(sqlGenerator);
			this.superModelGenerator = new SuperModelGenerator(superModelPackageName, superModelOutputDir);
			generators.add(superModelGenerator);
			this.webGenerator = new WebGenerator(webXMLOutputDir, coreConfigPackageName,
					this.coreConfigGenerator.getCoreConfigClassName());
			generators.add(webGenerator);
			// =============================
			// new HtmlGenerator();
			// new PomGenerator();
			// new FakeBaseModelGenerator();
			// =============================

		}
	}

	/**
	 * 添加定制generator 默认在构造器中会初始化generator集合,使用set集合,可以添加或覆盖已有generator
	 * 
	 * @param allSqlGenerator
	 */
	public void addGenerator(IGenerator customizeGenerator) {
		generators.add(customizeGenerator);
	}

	/**
	 * 设置数据库方言，默认为 MysqlDialect
	 */
	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	/**
	 * 设置 BaseModel 是否生成链式 setter 方法
	 */
	public void setGenerateChainSetter(boolean generateChainSetter) {
		baseModelGenerator.setGenerateChainSetter(generateChainSetter);
	}

	/**
	 * 设置需要被移除的表名前缀，仅用于生成 modelName 与 baseModelName 例如表名 "osc_account"，移除前缀
	 * "osc_" 后变为 "account"
	 */
	public void setRemovedTableNamePrefixes(String... removedTableNamePrefixes) {
		getMetaBuilser().setRemovedTableNamePrefixes(removedTableNamePrefixes);
	}

	/**
	 * 添加不需要处理的数据表
	 */
	public void addExcludedTable(String... excludedTables) {
		getMetaBuilser().addExcludedTable(excludedTables);
	}

	/**
	 * 设置是否在 Model 中生成 dao 对象，默认生成
	 */
	public void setGenerateDaoInModel(boolean generateDaoInModel) {
		if (modelGenerator != null) {
			modelGenerator.setGenerateDaoInModel(generateDaoInModel);
		}
	}

	public void generate() {
		init(basePackageName, onlyBaseModel);
		if (dialect != null) {
			getMetaBuilser().setDialect(dialect);
		}

		long start = System.currentTimeMillis();
		List<TableMeta> tableMetas = getMetaBuilser().build();
		if (tableMetas.size() == 0) {
			System.out.println("TableMeta 数量为 0，不生成任何文件");
			return;
		}

		if (generators.size() == 0) {
			System.out.println("generators 数量为0,不生产任何文件");
		}
		for (IGenerator generator : generators) {
			generator.generate(tableMetas);
		}
		long usedTime = (System.currentTimeMillis() - start);
		System.out.println("Generate complete in " + usedTime + " milliseconds.");
	}

	/**
	 * 默认值为jdbcUrl
	 * 
	 * @param jdbcUrl
	 */
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * 默认值为user
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 默认值为password
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 默认值为basePackageName+".domain"
	 * 
	 * @param domainPackageName
	 */
	public void setModelPackageName(String modelPackageName) {
		this.modelPackageName = modelPackageName;
	}

	/**
	 * 默认值为basePackageName+".domain.base"
	 * 
	 * @param baseDomainPackageName
	 */
	public void setBaseModelPackageName(String baseModelPackageName) {
		this.baseModelPackageName = baseModelPackageName;
	}

	/**
	 * 默认值为basePackageName+".jfinal"
	 * 
	 * @param jfinalPackageName
	 */
	public void setJfinalPackageName(String jfinalPackageName) {
		this.jfinalPackageName = jfinalPackageName;
	}

	/**
	 * 默认值为basePackageName+".query"
	 * 
	 * @param queryPackageName
	 */
	public void setQueryPackageName(String queryPackageName) {
		this.queryPackageName = queryPackageName;
	}

	/**
	 * 默认值为basePackageName+".service"
	 * 
	 * @param servicePackageName
	 */
	public void setServicePackageName(String servicePackageName) {
		this.servicePackageName = servicePackageName;
	}

	/**
	 * 默认值为basePackageName+".web"
	 * 
	 * @param controllerPackageName
	 */
	public void setControllerPackageName(String controllerPackageName) {
		this.controllerPackageName = controllerPackageName;
	}

	/**
	 * 默认值为和baseDomainPackage相同
	 * 
	 * @param mappingKitPackageName
	 */
	public void setMappingKitPackageName(String mappingKitPackageName) {
		this.mappingKitPackageName = mappingKitPackageName;
	}

	/**
	 * 获取datasource对象
	 * 
	 * @return
	 */
	public DataSource getDataSource() {
		if (alittle_config_file_name != null || StrKit.notBlank(alittle_config_file_name)) {
			PropKit.use(alittle_config_file_name);
		} else {
			PropKit.use("jfinal.properties");
		}
		System.out.println(PropKit.get(jdbcUrl));
		DruidPlugin druidPlugin = new DruidPlugin(PropKit.get(jdbcUrl), PropKit.get(username),
				PropKit.get(password).trim());
		druidPlugin.start();
		DataSource dataSource = druidPlugin.getDataSource();
		return dataSource;
	}

	/**
	 * 获取metaBuilder对对象,如果为空,new一个,如果不为空直接返回
	 * 
	 * @return
	 */
	public MetaBuilder getMetaBuilser() {
		if (this.metaBuilder == null) {
			this.metaBuilder = new MetaBuilder(getDataSource());
		}
		return this.metaBuilder;
	}
}
