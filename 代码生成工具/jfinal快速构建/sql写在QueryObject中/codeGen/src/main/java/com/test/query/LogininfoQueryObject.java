package com.test.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogininfoQueryObject extends QueryObject {
	private String keyword;// 关键字

	// 定制查询条件
	public void customizedQuery() {
		if (hasLength(keyword)) {
			String key = "%" + keyword + "%";
			addQuery("(username like ? )", key);
		}
	}
}
