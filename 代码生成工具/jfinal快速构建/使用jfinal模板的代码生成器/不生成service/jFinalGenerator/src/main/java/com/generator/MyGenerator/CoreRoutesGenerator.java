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

public class CoreRoutesGenerator implements IGenerator {
	protected String template = "/com/generator/MyGenerator/templates/CoreRoutes.jf";

	protected String coreRoutesPackageName;
	protected String controllerPackageName;
	protected String coreRoutesOutputDir;
	protected String coreRoutesClassName = "CoreRoutes";

	public CoreRoutesGenerator(String coreRoutesPackageName, String controllerPackageName, String coreRoutesOutputDir) {
		this.coreRoutesPackageName = coreRoutesPackageName;
		this.controllerPackageName = controllerPackageName;
		this.coreRoutesOutputDir = coreRoutesOutputDir;
	}

	/**
	 * 使用自定义模板生成 CoreRoutes
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	public void setCoreRoutesOutputDir(String coreRoutesOutputDir) {
		if (StrKit.notBlank(coreRoutesOutputDir)) {
			this.coreRoutesOutputDir = coreRoutesOutputDir;
		}
	}

	public void setControllerPackageName(String controllerPackageName) {
		if (StrKit.notBlank(controllerPackageName)) {
			this.controllerPackageName = controllerPackageName;
		}
	}
	
	public void setCoreRoutesPackageName(String coreRoutesPackageName) {
		if (StrKit.notBlank(coreRoutesPackageName)) {
			this.coreRoutesPackageName = coreRoutesPackageName;
		}
	}

	public void setCoreConfigClassName(String coreRoutesClassName) {
		if (StrKit.notBlank(coreRoutesClassName)) {
			this.coreRoutesClassName = StrKit.firstCharToUpperCase(coreRoutesClassName);
		}
	}

	public void generate(List<TableMeta> tableMetas) {
		System.out.println("Generate CoreRoutes file ...");
		System.out.println("CoreRoutes Output Dir: " + coreRoutesOutputDir);

		Engine engine = Engine.create("forCoreRoutes");
		engine.setSourceFactory(new ClassPathSourceFactory());
		engine.addSharedMethod(new StrKit());

		Kv data = Kv.by("coreRoutesPackageName", coreRoutesPackageName);
		data.set("coreRoutesClassName", coreRoutesClassName);
		data.set("controllerPackageName", controllerPackageName);
		data.set("tableMetas", tableMetas);

		String ret = engine.setSourceFactory(new ClassPathSourceFactory()).getTemplate(template).renderToString(data);
		writeToFile(ret);
	}

	/**
	 * CoreRoutes.java 不覆盖写入
	 */
	protected void writeToFile(String ret) {
		FileWriter fw = null;
		try {
			File dir = new File(coreRoutesOutputDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String target = coreRoutesOutputDir + File.separator + coreRoutesClassName + ".java";
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
