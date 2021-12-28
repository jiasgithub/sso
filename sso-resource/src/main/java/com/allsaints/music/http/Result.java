package com.allsaints.music.http;

import java.io.Serializable;

public class Result implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 	返回结果码
	 */
	private String code;
	/**
	 * 	返回结果描述
	 */
	private String message;
	/**
	 * 	返回数据
	 */
	private Object data;

	public Result() {
		super();
	}

	public Result(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public Result(String code, String message, Object data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public Result(ResultCodeTemplate resultCodeTemplate) {
		super();
		this.code = resultCodeTemplate.getCode();
		this.message = resultCodeTemplate.getMessage();
	}

	public Result(ResultCodeTemplate resultCodeTemplate, Object data) {
		super();
		this.code = resultCodeTemplate.getCode();
		this.message = resultCodeTemplate.getMessage();
		this.data = data;
	}

	public Result(ResultCodeTemplate resultCodeTemplate, String message, Object data) {
		super();
		this.code = resultCodeTemplate.getCode();
		this.message = message;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
