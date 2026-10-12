package com.ficklebytes.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse {

	protected int statusCode;
	protected String clientMessage;
	protected Object body;
	
	public void setAll(int statusCode,  String clientMessage, Object body) {
		this.statusCode = statusCode;
		this.clientMessage = clientMessage;
		this.body = body;
	}
	
	public void setCodeAndMsg(int statusCode,  String clientMessage) {
		this.statusCode = statusCode;
		this.clientMessage = clientMessage;
	}
}
