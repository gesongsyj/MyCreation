package ${basePkg}.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
abstract public class QueryObject {
	private Integer currentPage = 1;
	private Integer pageSize = 5;

	private List<String> conditions = new ArrayList<String>();
	private List<Object> params = new ArrayList<Object>();
	
	private String sqlSelectFrom="select * from "+ this.getClass().getSimpleName().toLowerCase().substring(0, this.getClass().getSimpleName().indexOf("QueryObject"));
	public String getSqlSelect(){
		String[] arr=sqlSelectFrom.split("from");
		if (sqlSelectFrom != null && !"".equals(sqlSelectFrom) && arr.length==2) {
			System.out.println(arr[0]);
			return arr[0];
		}else{
			throw new RuntimeException("SQL格式有误!");
		}
	}
	public String getSqlFrom(){
		System.out.println(sqlSelectFrom);
		String[] arr=sqlSelectFrom.split("from");
		if (sqlSelectFrom != null && !"".equals(sqlSelectFrom) && arr.length==2) {
			System.out.println("from "+arr[1]);
			return "from "+arr[1];
		}else{
			throw new RuntimeException("SQL格式有误!");
		}
	}
	
	private boolean build = false;// 是否已经拼接sql
	// 初始化操作,把查询条件拼接成sql.这个的作用是防止多次调用拼接sql方法时,会拼接多次条件

	private void init() {
		if (!build) {
			this.customizedQuery();
			build = true;
		}
	}

	// 返回查询条件
	public String getQuery() {
		init();
		if (conditions.size() == 0) {
			return "";
		}
		return " where " + StringUtils.join(conditions, " and ");
	}

	// 返回查询条件的参数集合
	public List<Object> getParams() {
		init();
		return this.params;
	}

	// 添加查询条件和设置参数
	protected void addQuery(String condition, Object... args) {
		conditions.add(condition);
		params.addAll(Arrays.asList(args));
	}

	protected void customizedQuery() {
	}

	protected boolean hasLength(String str) {
		return str != null && !"".equals(str.trim());
	}

	// 返回分页的起点
	public Integer getStart() {
		return (currentPage - 1) * pageSize;
	}
}