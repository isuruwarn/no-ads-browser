package org.warn.noadsbrowser.http;

public class HttpDTO {
	
	private int statusCode;
	private String status;
	private StringBuilder responseBody;
	
	public HttpDTO( int statusCode, String status, StringBuilder responseBody ) {
		this.statusCode = statusCode;
		this.status = status;
		this.responseBody = responseBody;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public StringBuilder getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(StringBuilder responseBody) {
		this.responseBody = responseBody;
	}

}
