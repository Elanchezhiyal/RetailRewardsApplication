package com.crm.rewardshub.api;


public class ResponseJson<T> {
    private int code;
    private String message;
    private T payload;

    public ResponseJson(int code, String message, T payload) {
        this.code = code;
        this.message = message;
        this.payload = payload;
    }

    public static <T> ResponseJson<T> success(T payload) {
        return new ResponseJson<>(200, "SUCCESS", payload);
    }

    public static <T> ResponseJson<T> error(int code, String message) {
        return new ResponseJson<>(code, message, null);
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}
    
    
	
}
