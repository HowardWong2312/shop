package io.dubai.common.exception;

import io.dubai.common.enums.ResponseStatusEnum;

/**
 * 自定义异常
 *
 * @author howard
 */
public class RRException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg = ResponseStatusEnum.SYSTEM_ERROR.msg;
    private int code = ResponseStatusEnum.SYSTEM_ERROR.code;

	public RRException() {
		super(ResponseStatusEnum.SYSTEM_ERROR.msg);
	}

    public RRException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public RRException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public RRException(ResponseStatusEnum rse) {
		super(rse.msg);
		this.msg = rse.msg;
		this.code = rse.code;
	}
	
	public RRException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public RRException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
}
