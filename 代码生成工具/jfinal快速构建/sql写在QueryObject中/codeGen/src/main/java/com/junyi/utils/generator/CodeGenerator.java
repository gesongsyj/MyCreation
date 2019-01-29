package com.junyi.utils.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.List;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class CodeGenerator {
	private static Configuration configuration;
	static {
		configuration = new Configuration();
		configuration.setClassForTemplateLoading(CodeGenerator.class, "templates");
	}

	public static void main(String[] args) throws Exception {
		ClassInfos classInfos = new ClassInfos();
		List<ClassInfo> classInfoList = classInfos.getClassInfoList();
		List<Class<?>> classes = ClassUtil.getClasses("com.test.domain", false);
		for (Class<?> clazz : classes) {
			classInfoList.add(new ClassInfo(clazz));
		}
		classInfos.setBasePkg(classInfos.getClassInfoList().get(0).getBasePkg());
		generatorCode(classInfos);
		System.out.println("生成完毕");
	}

	public static void generate(String basePkg) throws Exception {
		ClassInfos classInfos = new ClassInfos();
		List<ClassInfo> classInfoList = classInfos.getClassInfoList();
		List<Class<?>> classes = ClassUtil.getClasses(basePkg, false);
		for (Class<?> clazz : classes) {
			classInfoList.add(new ClassInfo(clazz));
		}
		classInfos.setBasePkg(classInfos.getClassInfoList().get(0).getBasePkg());
		generatorCode(classInfos);
		System.out.println("生成完毕");
	}

	/**
	 * 生成代码
	 * 
	 * @throws Exception
	 */
	public static void generatorCode(ClassInfos classInfos) throws Exception {
		boolean isFirst = true;

		for (ClassInfo classInfo : classInfos.getClassInfoList()) {
			if (isFirst) {
				/*
				 * 上面的这些只需要生成一次
				 */
				// 生成CoreConfig
				createFile(classInfo, "CoreConfig.ftl", "src/main/java/{0}/jfinal/CoreConfig.java");
				// 生成GenericService
				createFile(classInfo, "GenericService.java", "src/main/java/{0}/service/GenericService.java");
				// 生成QueryObject
				createFile(classInfo, "QueryObject.java", "src/main/java/{0}/query/QueryObject.java");
				// 生成jfinal.properties
				createFile(classInfo, "jfinal.properties", "src/main/resources/jfinal.properties");
				// 生成web.xml
				createFile(classInfo, "web.xml", "src/main/webapp/WEB-INF/web.xml");
				// ===============================================================================
				// 生成ObjController
				createFile(classInfo, "ObjController.java", "src/main/java/{0}/web/controller/{1}Controller.java");
				// 生成ObjService.java
				createFile(classInfo, "ObjService.java", "src/main/java/{0}/service/{1}Service.java");
				// 生成对应的ObjQueryObject.java
				createFile(classInfo, "ObjQueryObject.java", "src/main/java/{0}/query/{1}QueryObject.java");
				//pom文件添加jfinal的依赖
				appendFile(classInfo, "pom.xml", "src/main/java/{0}/mapper/application.xml");
			} else {
				// 生成ObjController
				createFile(classInfo, "ObjController.java", "src/main/java/{0}/web/controller/{1}Controller.java");
				// 生成ObjService.java
				createFile(classInfo, "ObjService.java", "src/main/java/{0}/service/{1}Service.java");
				// 生成对应的ObjQueryObject.java
				createFile(classInfo, "ObjQueryObject.java", "src/main/java/{0}/query/{1}QueryObject.java");
				// appendFile(classInfo,
				// "application-dao.xml","src/main/java/{0}/mapper/application.xml");
			}
			// 生成CoreRoutes
			createFileByAll(classInfos, "CoreRoutes.java", "src/main/java/{0}/jfinal/CoreRoutes.java");
		}
	}

	/**
	 * 以多个classInfo对象的数据生成文件
	 * 
	 * @param classInfo
	 * @param templateName
	 * @param fileName
	 * @throws Exception
	 */
	public static void createFileByAll(ClassInfos classInfos, String templateName, String fileName) throws Exception {
		// 获取模板对象
		Template template = configuration.getTemplate(templateName);
		// 设置{0}和{1}的值
		fileName = MessageFormat.format(fileName, classInfos.getBasePkg().replace(".", "/"));
		File file = new File(fileName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		// 合并模板和数据
		template.process(classInfos, new FileWriter(file));
	}

	/**
	 * 往现有文件中添加内容
	 * 
	 * @param classInfo
	 * @param templateName
	 * @param targetFile
	 * @throws Exception
	 */
	public static void appendFile(ClassInfo classInfo, String templateName, String targetFile) throws Exception {
		Template template = configuration.getTemplate(templateName);
		// 设置{0}和{1}的值
		targetFile = MessageFormat.format(targetFile, classInfo.getBasePkg().replace(".", "/"),
				classInfo.getClassName());
		StringWriter out = new StringWriter();
		template.process(classInfo, out);
		String appendingXml = out.toString();
		XmlUtil.mergeXML(new File(targetFile), appendingXml);
	}

	/**
	 * 生成文件
	 * 
	 * @param classInfo
	 *            要生成代码的domain对象的class
	 * @param templateName
	 *            使用的模板名称
	 * @param fileName
	 *            生成文件的全路径名
	 * @throws Exception
	 */
	public static void createFile(ClassInfo classInfo, String templateName, String fileName) throws Exception {
		// 获取模板对象
		Template template = configuration.getTemplate(templateName);
		// 设置{0}和{1}的值
		fileName = MessageFormat.format(fileName, classInfo.getBasePkg().replace(".", "/"), classInfo.getClassName());
		File file = new File(fileName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		// 合并模板和数据
		template.process(classInfo, new FileWriter(file));
	}

}
