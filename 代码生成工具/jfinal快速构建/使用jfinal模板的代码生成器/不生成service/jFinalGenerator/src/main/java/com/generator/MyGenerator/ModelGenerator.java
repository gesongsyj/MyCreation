package com.generator.MyGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.generator.HelperClass.TableMeta;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;

public class ModelGenerator implements IGenerator{
	protected String template = "/com/generator/MyGenerator/templates/Model.jf";

	protected String modelPackageName;
	protected String baseModelPackageName;
	protected String objQueryObjectPackageName;
	protected String modelOutputDir;
	protected boolean generateDaoInModel = true;// 默认在在Model中生成dao

	public ModelGenerator(String modelPackageName, String baseModelPackageName, String objQueryObjectPackageName,String modelOutputDir) {
		if (StrKit.isBlank(modelPackageName)) {
			throw new IllegalArgumentException("modelPackageName can not be blank.");
		}
		if (modelPackageName.contains("/") || modelPackageName.contains("\\")) {
			throw new IllegalArgumentException("modelPackageName error : " + modelPackageName);
		}
		if (StrKit.isBlank(baseModelPackageName)) {
			throw new IllegalArgumentException("baseModelPackageName can not be blank.");
		}
		if (baseModelPackageName.contains("/") || baseModelPackageName.contains("\\")) {
			throw new IllegalArgumentException("baseModelPackageName error : " + baseModelPackageName);
		}
		if (StrKit.isBlank(objQueryObjectPackageName)) {
			throw new IllegalArgumentException("objQueryObjectPackageName can not be blank.");
		}
		if (objQueryObjectPackageName.contains("/") || objQueryObjectPackageName.contains("\\")) {
			throw new IllegalArgumentException("objQueryObjectPackageName error : " + objQueryObjectPackageName);
		}
		if (StrKit.isBlank(modelOutputDir)) {
			throw new IllegalArgumentException("modelOutputDir can not be blank.");
		}

		this.modelPackageName = modelPackageName;
		this.baseModelPackageName = baseModelPackageName;
		this.objQueryObjectPackageName = objQueryObjectPackageName;
		this.modelOutputDir = modelOutputDir;
	}

	/**
	 * 使用自定义模板生成 model
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	public void setGenerateDaoInModel(boolean generateDaoInModel) {
		this.generateDaoInModel = generateDaoInModel;
	}

	public void generate(List<TableMeta> tableMetas) {
		System.out.println("Generate model ...");
		System.out.println("Model Output Dir: " + modelOutputDir);

		Engine engine = Engine.create("forModel");
		engine.setSourceFactory(new ClassPathSourceFactory());
		engine.addSharedMethod(new StrKit());

		for (TableMeta tableMeta : tableMetas) {
			genModelContent(tableMeta);
		}
		writeToFile(tableMetas);
	}

	protected void genModelContent(TableMeta tableMeta) {
		Kv data = Kv.by("modelPackageName", modelPackageName);
		data.set("baseModelPackageName", baseModelPackageName);
		data.set("objQueryObjectPackageName", objQueryObjectPackageName);
		data.set("generateDaoInModel", generateDaoInModel);
		data.set("tableMeta", tableMeta);

		String ret = Engine.use("forModel").setSourceFactory(new ClassPathSourceFactory()).getTemplate(template).renderToString(data);
		tableMeta.modelContent = ret;
	}

	protected void writeToFile(List<TableMeta> tableMetas) {
		try {
			for (TableMeta tableMeta : tableMetas) {
				writeToFile(tableMeta);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 若 model 文件存在，则不生成，以免覆盖用户手写的代码
	 */
	protected void writeToFile(TableMeta tableMeta) throws IOException {
		File dir = new File(modelOutputDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String target = modelOutputDir + File.separator + tableMeta.modelName + ".java";

		File file = new File(target);
		if (file.exists()) {
			return; // 若 Model 存在，不覆盖
		}

		FileWriter fw = new FileWriter(file);
		try {
			fw.write(tableMeta.modelContent);
		} finally {
			fw.close();
		}
	}
}
