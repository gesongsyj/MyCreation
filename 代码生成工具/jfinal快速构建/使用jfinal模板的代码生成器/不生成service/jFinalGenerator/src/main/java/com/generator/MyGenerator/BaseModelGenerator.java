package com.generator.MyGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.generator.HelperClass.TableMeta;
import com.jfinal.kit.JavaKeyword;
import com.jfinal.kit.Kv;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;

public class BaseModelGenerator implements IGenerator{
	protected String template ="/com/generator/MyGenerator/templates/BaseModel.jf";

	protected String baseModelPackageName;
	protected String baseModelOutputDir;
	protected String superModelClassName = "SuperModel";
	protected boolean generateChainSetter = true;//默认使用链式setter

	protected JavaKeyword javaKeyword = JavaKeyword.me;
	/**
	 * 针对 Model 中七种可以自动转换类型的 getter 方法，调用其具有确定类型返回值的 getter 方法
	 * 享用自动类型转换的便利性，例如 getInt(String)、getStr(String)
	 * 其它方法使用泛型返回值方法： get(String)
	 * 注意：jfinal 3.2 及以上版本 Model 中的六种 getter 方法才具有类型转换功能
	 */
	@SuppressWarnings("serial")
	protected Map<String, String> getterTypeMap = new HashMap<String, String>() {{
		put("java.lang.String", "getStr");
		put("java.lang.Integer", "getInt");
		put("java.lang.Long", "getLong");
		put("java.lang.Double", "getDouble");
		put("java.lang.Float", "getFloat");
		put("java.lang.Short", "getShort");
		put("java.lang.Byte", "getByte");
	}};

	public BaseModelGenerator(String baseModelPackageName, String baseModelOutputDir) {
		if (StrKit.isBlank(baseModelPackageName)) {
			throw new IllegalArgumentException("baseModelPackageName can not be blank.");
		}
		if (baseModelPackageName.contains("/") || baseModelPackageName.contains("\\")) {
			throw new IllegalArgumentException("baseModelPackageName error : " + baseModelPackageName);
		}
		if (StrKit.isBlank(baseModelOutputDir)) {
			throw new IllegalArgumentException("baseModelOutputDir can not be blank.");
		}

		this.baseModelPackageName = baseModelPackageName;
		this.baseModelOutputDir = baseModelOutputDir;
	}

	/**
	 * 使用自定义模板生成 base model
	 */
	public void setTemplate(String template) {
		this.template = template;
	}
	
	public void setGenerateChainSetter(boolean generateChainSetter) {
		this.generateChainSetter = generateChainSetter;
	}
	
	public void generate(List<TableMeta> tableMetas) {
		System.out.println("Generate base model ...");
		System.out.println("Base Model Output Dir: " + baseModelOutputDir);

		Engine engine = Engine.create("forBaseModel");
		engine.setSourceFactory(new ClassPathSourceFactory());
		engine.addSharedMethod(new StrKit());
		engine.addSharedObject("getterTypeMap", getterTypeMap);
		engine.addSharedObject("javaKeyword", javaKeyword);

		for (TableMeta tableMeta : tableMetas) {
			genBaseModelContent(tableMeta);
		}
		writeToFile(tableMetas);
	}

	protected void genBaseModelContent(TableMeta tableMeta) {
		Kv data = Kv.by("baseModelPackageName", baseModelPackageName);
		data.set("superModelClassName", superModelClassName);
		data.set("generateChainSetter", generateChainSetter);
		data.set("tableMeta", tableMeta);

		Engine engine = Engine.use("forBaseModel");
		tableMeta.baseModelContent = engine.setSourceFactory(new ClassPathSourceFactory()).getTemplate(template).renderToString(data);
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
	 * base model 覆盖写入
	 */
	protected void writeToFile(TableMeta tableMeta) throws IOException {
		File dir = new File(baseModelOutputDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String target = baseModelOutputDir + File.separator + tableMeta.baseModelName + ".java";
		FileWriter fw = new FileWriter(target);
		try {
			fw.write(tableMeta.baseModelContent);
		} finally {
			fw.close();
		}
	}
}
