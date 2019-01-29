package com.generator.MyGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.generator.HelperClass.TableMeta;
import com.jfinal.kit.Kv;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;

public class JfinalPropGenerator implements IGenerator{
	protected String template ="/com/generator/MyGenerator/templates/jfinal.jf";

	protected String jfinalPropOutputDir;
	protected String jfinalPropFileName="jfianl.properties";
	
	public JfinalPropGenerator(String jfinalPropOutputDir) {
		if (StrKit.isBlank(jfinalPropOutputDir)) {
			throw new IllegalArgumentException("jfinalPropOutputDir can not be blank.");
		}
		
		this.jfinalPropOutputDir = jfinalPropOutputDir;
	}
	
	public JfinalPropGenerator(String jfinalPropOutputDir,String jfinalPropFileName) {
		if (StrKit.isBlank(jfinalPropOutputDir)) {
			throw new IllegalArgumentException("jfinalPropOutputDir can not be blank.");
		}
		if (StrKit.isBlank(jfinalPropFileName)) {
			throw new IllegalArgumentException("jfinalPropFileName can not be blank.");
		}

		this.jfinalPropOutputDir = jfinalPropOutputDir;
		this.jfinalPropFileName = jfinalPropFileName;
	}

	public void setTemplate(String template) {
		if(StrKit.notBlank(template)){
			this.template = template;
		}
	}

	public void setJfinalPropOutputDir(String jfinalPropOutputDir) {
		if(StrKit.notBlank(jfinalPropOutputDir)){
			this.jfinalPropOutputDir = jfinalPropOutputDir;
		}
	}

	public void setJfinalPropName(String jfinalPropFileName) {
		if(StrKit.notBlank(jfinalPropFileName)){
			this.jfinalPropFileName = jfinalPropFileName;
		}
	}
	
	public void generate(List<TableMeta> tableMetas) {
		System.out.println("Generate jfinalProp file ...");
		System.out.println("JfinalProp Output Dir: " + jfinalPropOutputDir);
		
		Engine engine = Engine.create("forJfinalProp");
		engine.setSourceFactory(new ClassPathSourceFactory());
		engine.addSharedMethod(new StrKit());
		
		Kv data = Kv.by("tableMetas", tableMetas);
		
		String ret = engine.setSourceFactory(new ClassPathSourceFactory()).getTemplate(template).renderToString(data);
		writeToFile(ret);
	}
	
	/**
	 * jfinal.properties 不覆盖
	 */
	protected void writeToFile(String ret) {
		FileWriter fw = null;
		try {
			File dir = new File(jfinalPropOutputDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			String target = jfinalPropOutputDir + File.separator + jfinalPropFileName;
			File file = new File(target);
			if (file.exists()) {
				return; // 若 存在，不覆盖
			}
			fw = new FileWriter(file);
			fw.write(ret);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			if (fw != null) {
				try {fw.close();} catch (IOException e) {LogKit.error(e.getMessage(), e);}
			}
		}
	}
}
