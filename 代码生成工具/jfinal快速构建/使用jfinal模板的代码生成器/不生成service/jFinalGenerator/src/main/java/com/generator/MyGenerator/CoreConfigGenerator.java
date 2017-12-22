package com.generator.MyGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.generator.HelperClass.TableMeta;
import com.jfinal.kit.Kv;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;

public class CoreConfigGenerator implements IGenerator{
	protected String template ="com/generator/MyGenerator/templates/CoreConfig.jf";

	protected String coreConfigPackageName;
	protected String mappingKitPackageName;
	protected String mappingKitClassName="_MappingKit";
	protected String coreConfigOutputDir;
	protected String coreConfigClassName = "CoreConfig";
	protected String jfinalPropFileName="jfianl.properties";
	
	public String getCoreConfigClassName() {
		return coreConfigClassName;
	}

	public CoreConfigGenerator(String coreConfigPackageName, String mappingKitPackageName,String coreConfigOutputDir) {
		this.coreConfigPackageName = coreConfigPackageName;
		this.mappingKitPackageName = mappingKitPackageName;
		this.coreConfigOutputDir = coreConfigOutputDir;
	}
	public CoreConfigGenerator(String coreConfigPackageName, String mappingKitPackageName,String coreConfigOutputDir,String jfinalPropFileName) {
		this.coreConfigPackageName = coreConfigPackageName;
		this.mappingKitPackageName = mappingKitPackageName;
		this.coreConfigOutputDir = coreConfigOutputDir;
		this.jfinalPropFileName = jfinalPropFileName;
	}

	/**
	 * 使用自定义模板生成 CoreConfig
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	public void setCoreConfigOutputDir(String coreConfigOutputDir) {
		if (StrKit.notBlank(coreConfigOutputDir)) {
			this.coreConfigOutputDir = coreConfigOutputDir;
		}
	}

	public void setCoreConfigPackageName(String coreConfigPackageName) {
		if (StrKit.notBlank(coreConfigPackageName)) {
			this.coreConfigPackageName = coreConfigPackageName;
		}
	}

	public void setMappingKitPackageName(String mappingKitPackageName) {
		if (StrKit.notBlank(mappingKitPackageName)) {
			this.mappingKitPackageName = mappingKitPackageName;
		}
	}

	public void setCoreConfigClassName(String coreConfigClassName) {
		if (StrKit.notBlank(coreConfigClassName)) {
			this.coreConfigClassName = StrKit.firstCharToUpperCase(coreConfigClassName);
		}
	}
	
	public void setJfinalPropFileName(String jfinalPropFileName) {
		if (StrKit.notBlank(jfinalPropFileName)) {
			this.jfinalPropFileName = StrKit.firstCharToUpperCase(jfinalPropFileName);
		}
	}

	public void generate(List<TableMeta> tableMetas) {
		System.out.println("Generate CoreConfig file ...");
		System.out.println("CoreConfig Output Dir: " + coreConfigOutputDir);

		Engine engine = Engine.create("forCoreConfig");
		engine.setSourceFactory(new ClassPathSourceFactory());
		engine.addSharedMethod(new StrKit());

		Kv data = Kv.by("coreConfigPackageName", coreConfigPackageName);
		data.set("coreConfigClassName", coreConfigClassName);
		data.set("mappingKitPackageName", mappingKitPackageName);
		data.set("mappingKitClassName", mappingKitClassName);
		data.set("jfinalPropFileName", jfinalPropFileName);
		data.set("tableMetas", tableMetas);

		String ret = engine.setSourceFactory(new ClassPathSourceFactory()).getTemplate(template).renderToString(data);
		writeToFile(ret);
	}

	/**
	 * CoreConfig.java 不覆盖写入
	 */
	protected void writeToFile(String ret) {
		FileWriter fw = null;
		try {
			File dir = new File(coreConfigOutputDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String target = coreConfigOutputDir + File.separator + coreConfigClassName + ".java";
			File file = new File(target);
			if (file.exists()) {
				return; // 若 存在，不覆盖
			}
			fw = new FileWriter(file);
			fw.write(ret);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					LogKit.error(e.getMessage(), e);
				}
			}
		}
	}
}
