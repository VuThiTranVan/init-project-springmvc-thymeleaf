package com.spring.sample.util;

import java.util.List;

public class CommonUtil {

	public static boolean isEmpty(List<?> list) {
		return list == null || list.size() == 0;
	}

	public static boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}

}
