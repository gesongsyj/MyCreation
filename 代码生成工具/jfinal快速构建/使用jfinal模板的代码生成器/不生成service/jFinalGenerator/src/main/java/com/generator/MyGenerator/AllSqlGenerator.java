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

public class AllSqlGenerator implements IGenerator{
	protected String template ="/com/generator/MyGenerator/templates/all.jf";

	protected String allSqlOutputDir;
	protected String allSqlFileName="all";
	
	public AllSqlGenerator(String allSqlOutputDir) {
		if (StrKit.isBlank(allSqlOutputDir)) {
			throw new IllegalArgumentException("allSqlOutputDir can not be blank.");
		}

		this.allSqlOutputDir = allSqlOutputDir;
	}

	public void setTemplate(String template) {
		if(StrKit.notBlank(template)){
			this.template = template;
		}
	}

	public void setAllSqlOutputDir(String allSqlOutputDir) {
		if(StrKit.notBlank(allSqlOutputDir)){
			this.allSqlOutputDir = allSqlOutputDir;
		}
	}

	public void setAllSqlFileName(String allSqlFileName) {
		if(StrKit.notBlank(allSqlFileName)){
			this.allSqlFileName = allSqlFileName;
		}
	}
	
	public void generate(List<TableMeta> tableMetas) {
		System.out.println("Generate AllSql file ...");
		System.out.println("AllSql Output Dir: " + allSqlOutputDir);
		
		Engine engine = Engine.create("forAllSql");
		engine.setSourceFactory(new ClassPathSourceFactory());
		engine.addSharedMethod(new StrKit());
		
		Kv data = Kv.by("tableMetas", tableMetas);
		
		String ret = engine.setSourceFactory(new ClassPathSourceFactory()).getTemplate(template).renderToString(data);
		writeToFile(ret);
	}
	
	/**
	 * all.sql不覆盖写入
	 */
	protected void writeToFile(String ret) {
		FileWriter fw = null;
		try {
			File dir = new File(allSqlOutputDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			String target = allSqlOutputDir + File.separator + allSqlFileName + ".sql";
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
