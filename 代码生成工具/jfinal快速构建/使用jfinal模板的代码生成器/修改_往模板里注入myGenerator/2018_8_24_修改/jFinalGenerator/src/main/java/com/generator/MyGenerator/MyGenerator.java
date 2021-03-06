package com.generator.MyGenerator;

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
	public static final String WEBXMLDIR_WEBAPP = "/src/main/webapp/WEB-INF";
	public static final String WEBXMLDIR_WEBROOT = "/WebRoot/WEB-INF";
	// jdbc配置的key值
	protected String jdbcUrl = "jdbcUrl";
	protected String username = "username";
	protected String password = "password";
	/**
	 * 项目目录包名及类名
	 */
	protected String basePackageName;
	protected String commonPackageName;
	protected String modelPackageName;
	protected String baseModelPackageName;// superModelPackageName
	protected String baseControllerPackageName;// superModelPackageName
	protected String superModelClassName = "SuperModel";
	protected String baseControllerClassName = "BaseController";
	protected String jfinalPackageName;
	protected String coreConfigName = "CoreConfig";
	protected String coreRoutesName = "CoreRoutes";
	protected String queryPackageName;
	protected String queryObjectName = "QueryObject";
	protected String servicePackageName;
	protected String controllerPackageName;
	protected String mappingKitPackageName;
	protected String mappingKitClassName = "_MappingKit";
	protected String allSqlName = "all";
	protected String webXmlName = "web";
	protected String webXMLOutputDir = WEBXMLDIR_WEBROOT;

	protected String alittle_config_file_name;
	protected boolean onlyBaseModel;
	protected boolean hasService;
	protected boolean hasQueryObj;

	protected Dialect dialect = null;
	protected MetaBuilder metaBuilder;

	protected BaseModelGenerator baseModelGenerator;
	protected BaseControllerlGenerator baseControllerGenerator;
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
	public MyGenerator(String basePackageName, String commonPackageName, String configName, boolean onlyBaseModel,
			boolean hasService, boolean hasQueryObj) {
		this.basePackageName = basePackageName;
		this.commonPackageName = commonPackageName;
		this.modelPackageName = basePackageName + ".model";
		this.baseModelPackageName = basePackageName + ".model.base";
		this.baseControllerPackageName = commonPackageName + ".web";
		this.jfinalPackageName = commonPackageName + ".jfinal";
		this.queryPackageName = basePackageName + ".query";
		this.servicePackageName = basePackageName + ".service";
		this.controllerPackageName = basePackageName + ".web";
		this.mappingKitPackageName = commonPackageName + ".jfinal";

		this.alittle_config_file_name = configName;
		this.onlyBaseModel = onlyBaseModel;
		this.hasService = hasService;
		this.hasQueryObj = hasQueryObj;
		// init(basePackageName, onlyBaseModel);
	}

	public boolean hasService() {
		return hasService;
	}

	public void setHasService(boolean hasService) {
		this.hasService = hasService;
	}

	public boolean hasQueryObj() {
		return hasQueryObj;
	}

	public void setHasQueryObj(boolean hasQueryObj) {
		this.hasQueryObj = hasQueryObj;
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
			this.baseModelGenerator = new BaseModelGenerator(baseModelPackageName, baseModelOutputDir, this);
			generators.add(baseModelGenerator);
		} else {
			String baseControllerPackageName = this.baseControllerPackageName;
			String baseControllerOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ baseControllerPackageName.replace(".", "/");
			BaseControllerlGenerator baseControllerlGenerator = new BaseControllerlGenerator(baseControllerPackageName, baseControllerOutputDir, this);
			generators.add(baseControllerlGenerator);
			// =============================
			String baseModelPackageName = this.baseModelPackageName;
			String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ baseModelPackageName.replace(".", "/");
			this.baseModelGenerator = new BaseModelGenerator(baseModelPackageName, baseModelOutputDir, this);
			generators.add(baseModelGenerator);
			// =============================
			String modelPackageName = this.modelPackageName;
			String modelOutputDir = PathKit.getWebRootPath() + "/src/main/java/" + modelPackageName.replace(".", "/");
			this.modelGenerator = new ModelGenerator(modelPackageName, modelOutputDir, this);
			generators.add(modelGenerator);
			// =============================
			String mappingKitPackageName = this.mappingKitPackageName;
			String mappingKitOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ mappingKitPackageName.replace(".", "/");
			this.mappingKitGenerator = new MappingKitGenerator(mappingKitPackageName, mappingKitOutputDir, this);
			generators.add(mappingKitGenerator);
			// =============================
			String allSqlOutputDir = PathKit.getWebRootPath() + "/src/main/resources/sql";
			this.allSqlGenerator = new AllSqlGenerator(allSqlOutputDir, this);
			generators.add(allSqlGenerator);
			// =============================
			String controllerPackageName = this.controllerPackageName;
			String controllerOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ controllerPackageName.replace(".", "/");
			this.controllerGenerator = new ControllerGenerator(controllerPackageName, controllerOutputDir, this);
			generators.add(controllerGenerator);
			// =============================
			String coreConfigPackageName = this.jfinalPackageName;
			String coreConfigOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ coreConfigPackageName.replace(".", "/");
			this.coreConfigGenerator = new CoreConfigGenerator(coreConfigPackageName, coreConfigOutputDir, this);
			generators.add(coreConfigGenerator);
			// =============================
			String coreRoutesPackageName = this.jfinalPackageName;
			String coreRoutesOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ coreRoutesPackageName.replace(".", "/");
			this.coreRoutesGenerator = new CoreRoutesGenerator(coreRoutesPackageName, coreRoutesOutputDir, this);
			generators.add(coreRoutesGenerator);
			// =============================
			if (hasService) {
				String servicePackageName = this.servicePackageName;
				String serviceOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
						+ servicePackageName.replace(".", "/");
				this.serviceGenerator = new ServiceGenerator(servicePackageName, serviceOutputDir, this);
				generators.add(serviceGenerator);
			}
			// =============================
			if (hasQueryObj) {
				String objQueryObjectPackageName = this.queryPackageName;
				String objQueryObjectOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
						+ objQueryObjectPackageName.replace(".", "/");
				this.objQueryObjectGenerator = new ObjQueryObjectGenerator(objQueryObjectPackageName,
						objQueryObjectOutputDir, this);
				generators.add(objQueryObjectGenerator);
				// =============================
				String queryObjectPackageName = this.queryPackageName;
				String queryObjectOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
						+ queryObjectPackageName.replace(".", "/");
				this.queryObjectGenerator = new QueryObjectGenerator(queryObjectPackageName, queryObjectOutputDir,
						this);
				generators.add(queryObjectGenerator);
			}
			// =============================
			String sqlOutputDir = PathKit.getWebRootPath() + "/src/main/resources/sql";
			this.sqlGenerator = new SqlGenerator(sqlOutputDir, this);
			generators.add(sqlGenerator);
			// =============================
			String superModelPackageName = this.baseModelPackageName;
			String superModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/"
					+ superModelPackageName.replace(".", "/");
			this.superModelGenerator = new SuperModelGenerator(superModelPackageName, superModelOutputDir, this);
			generators.add(superModelGenerator);
			// =============================
			String webXMLOutputDir = PathKit.getWebRootPath() + this.webXMLOutputDir;
			this.webGenerator = new WebGenerator(webXMLOutputDir, this);
			generators.add(webGenerator);
			// =============================
			String pomXMLOutputDir = PathKit.getWebRootPath();
			// =============================
			String jfinalPropOutputDir = PathKit.getWebRootPath() + "/src/main/resources";
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
		getMetaBuilder().setRemovedTableNamePrefixes(removedTableNamePrefixes);
	}

	/**
	 * 添加不需要处理的数据表
	 */
	public void addExcludedTable(String... excludedTables) {
		getMetaBuilder().addExcludedTable(excludedTables);
	}

	/**
	 * 添加需要处理的数据表,如果设置了该项,则只处理添加的数据表
	 */
	public void addIncludedTable(String... includedTables) {
		getMetaBuilder().addIncludeTables(includedTables);
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
			getMetaBuilder().setDialect(dialect);
		}

		long start = System.currentTimeMillis();
		List<TableMeta> tableMetas = getMetaBuilder().build();
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
	 * 默认值为username
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

	public String getBasePackageName() {
		return basePackageName;
	}

	public void setBasePackageName(String basePackageName) {
		this.basePackageName = basePackageName;
	}

	public String getModelPackageName() {
		return modelPackageName;
	}

	public void setModelPackageName(String modelPackageName) {
		this.modelPackageName = modelPackageName;
	}

	public String getBaseModelPackageName() {
		return baseModelPackageName;
	}

	public void setBaseModelPackageName(String baseModelPackageName) {
		this.baseModelPackageName = baseModelPackageName;
	}

	public String getSuperModelClassName() {
		return superModelClassName;
	}

	public void setSuperModelClassName(String superModelClassName) {
		this.superModelClassName = superModelClassName;
	}

	public String getJfinalPackageName() {
		return jfinalPackageName;
	}

	public void setJfinalPackageName(String jfinalPackageName) {
		this.jfinalPackageName = jfinalPackageName;
	}

	public String getCoreConfigName() {
		return coreConfigName;
	}

	public void setCoreConfigName(String coreConfigName) {
		this.coreConfigName = coreConfigName;
	}

	public String getCoreRoutesName() {
		return coreRoutesName;
	}

	public void setCoreRoutesName(String coreRoutesName) {
		this.coreRoutesName = coreRoutesName;
	}

	public String getQueryPackageName() {
		return queryPackageName;
	}

	public void setQueryPackageName(String queryPackageName) {
		this.queryPackageName = queryPackageName;
	}

	public String getQueryObjectName() {
		return queryObjectName;
	}

	public void setQueryObjectName(String queryObjectName) {
		this.queryObjectName = queryObjectName;
	}

	public String getServicePackageName() {
		return servicePackageName;
	}

	public void setServicePackageName(String servicePackageName) {
		this.servicePackageName = servicePackageName;
	}

	public String getControllerPackageName() {
		return controllerPackageName;
	}

	public void setControllerPackageName(String controllerPackageName) {
		this.controllerPackageName = controllerPackageName;
	}

	public String getMappingKitPackageName() {
		return mappingKitPackageName;
	}

	public void setMappingKitPackageName(String mappingKitPackageName) {
		this.mappingKitPackageName = mappingKitPackageName;
	}

	public String getMappingKitClassName() {
		return mappingKitClassName;
	}

	public void setMappingKitClassName(String mappingKitClassName) {
		this.mappingKitClassName = mappingKitClassName;
	}

	public String getAllSqlName() {
		return allSqlName;
	}

	public void setAllSqlName(String allSqlName) {
		this.allSqlName = allSqlName;
	}

	public String getWebXmlName() {
		return webXmlName;
	}

	public void setWebXmlName(String webXmlName) {
		this.webXmlName = webXmlName;
	}

	public String getAlittle_config_file_name() {
		return alittle_config_file_name;
	}

	public void setAlittle_config_file_name(String alittle_config_file_name) {
		this.alittle_config_file_name = alittle_config_file_name;
	}

	public String getWebXMLOutputDir() {
		return webXMLOutputDir;
	}

	public void setWebXMLOutputDir(String webXMLOutputDir) {
		this.webXMLOutputDir = webXMLOutputDir;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public String getCommonPackageName() {
		return commonPackageName;
	}

	public void setCommonPackageName(String commonPackageName) {
		this.commonPackageName = commonPackageName;
	}

	public String getBaseControllerPackageName() {
		return baseControllerPackageName;
	}

	public void setBaseControllerPackageName(String baseControllerPackageName) {
		this.baseControllerPackageName = baseControllerPackageName;
	}

	public String getBaseControllerClassName() {
		return baseControllerClassName;
	}

	public void setBaseControllerClassName(String baseControllerClassName) {
		this.baseControllerClassName = baseControllerClassName;
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
	public MetaBuilder getMetaBuilder() {
		if (this.metaBuilder == null) {
			this.metaBuilder = new MetaBuilder(getDataSource());
		}
		return this.metaBuilder;
	}

	/**
	 * 设置自定义的metaBuilder,扩展部分功能
	 * 
	 * @param metaBuilder
	 */
	public void setMetaBuilder(MetaBuilder metaBuilder) {
		this.metaBuilder = metaBuilder;
	}

}
