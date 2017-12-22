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

public class WebGenerator implements IGenerator {
	protected String template = "/com/generator/MyGenerator/templates/web.jf";

	protected String coreConfigPackageName;
	protected String webXMLOutputDir;
	protected String webXMLName = "web";
	protected String coreConfigClassName = "CoreConfig";

	public WebGenerator(String webXMLOutputDir, String coreConfigPackageName, String coreConfigClassName) {
		this.coreConfigPackageName = coreConfigPackageName;
		this.webXMLOutputDir = webXMLOutputDir;
		this.coreConfigClassName = coreConfigClassName;
	}

	/**
	 * 使用自定义模板生成 SuperModel
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	public void setWebXMLOutputDir(String webXMLOutputDir) {
		if (StrKit.notBlank(webXMLOutputDir)) {
			this.webXMLOutputDir = webXMLOutputDir;
		}
	}

	public void setWebXMLName(String webXMLName) {
		if (StrKit.notBlank(webXMLName)) {
			this.webXMLName = StrKit.firstCharToUpperCase(webXMLName);
		}
	}

	public void generate(List<TableMeta> tableMetas) {
		System.out.println("Generate web.xml file ...");
		System.out.println("web.xml Output Dir: " + webXMLOutputDir);

		Engine engine = Engine.create("forWebXML");
		engine.setSourceFactory(new ClassPathSourceFactory());
		engine.addSharedMethod(new StrKit());

		Kv data = Kv.by("webXMLName", webXMLName);
		data.set("coreConfigPackageName", coreConfigPackageName);
		data.set("coreConfigClassName", coreConfigClassName);
		data.set("tableMetas", tableMetas);

		String ret = engine.setSourceFactory(new ClassPathSourceFactory()).getTemplate(template).renderToString(data);
		writeToFile(ret);
	}

	/**
	 * web.xml 不覆盖写入
	 */
	protected void writeToFile(String ret) {
		FileWriter fw = null;
		try {
			File dir = new File(webXMLOutputDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String target = webXMLOutputDir + File.separator + webXMLName + ".xml";
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
