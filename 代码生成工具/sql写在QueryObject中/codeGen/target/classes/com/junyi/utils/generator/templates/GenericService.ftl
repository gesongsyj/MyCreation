package ${basePkg}.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import ${basePkg}.query.QueryObject;
import lombok.Setter;

public class GenericService<T> {
	@Setter
	private Class<T> targetType;
	private String targetTypeName;// 类名全小写

	public GenericService() {
		// 得到带泛型父类类型
		ParameterizedType pType = (ParameterizedType) this.getClass().getGenericSuperclass();
		// 得到泛型参数类型
		targetType = (Class<T>) pType.getActualTypeArguments()[0];
		//得到泛型参数类型的名称小写
		targetTypeName = targetType.getSimpleName().toLowerCase();
	}

	/**
	 * CRUD
	 */
	public void save(Record record) {
		Db.save(targetTypeName, record);
	}

	public Record queryById(Long id) {
		return Db.findById(targetTypeName, id);
	}

	public void update(Record record) {
		Db.update(targetTypeName, record);
	}

	public void delete(Long id) {
		Db.deleteById(targetTypeName, id);
	}

	public List<Record> listAll() {
		String sql = "select * from " + targetTypeName;
		return Db.find(sql);
	}

	/**
	 * 分页
	 */
	public Page<Record> paginate(QueryObject qo) {
		if(qo.getParams().size()>0){
			return Db.paginate(qo.getCurrentPage(), qo.getPageSize(), qo.getSqlSelect(),
					qo.getSqlFrom() + qo.getQuery(), qo.getParams().toArray());
		}else{
			return Db.paginate(qo.getCurrentPage(), qo.getPageSize(), qo.getSqlSelect(),
					qo.getSqlFrom());
		}
	}
}
