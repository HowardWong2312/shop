package io.dubai.common.utils;

import io.dubai.common.enums.ResponseStatusEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author mother fucker
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public R() {
		put("code", ResponseStatusEnum.SUCCESS.code);
		put("msg", ResponseStatusEnum.SUCCESS.msg);
	}
	
	public static R error() {
		return error(ResponseStatusEnum.SYSTEM_ERROR);
	}
	
	public static R error(String msg) {
		return error(ResponseStatusEnum.SYSTEM_ERROR.code, msg);
	}

	public static R error(ResponseStatusEnum rse) {
		R r = new R();
		r.put("code", rse.code);
		r.put("msg", rse.msg);
		return r;
	}

	public static R ok(ResponseStatusEnum rse) {
		R r = new R();
		r.put("code", rse.code);
		r.put("msg", rse.msg);
		return r;
	}
	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}

	public static R ok(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}
	
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
