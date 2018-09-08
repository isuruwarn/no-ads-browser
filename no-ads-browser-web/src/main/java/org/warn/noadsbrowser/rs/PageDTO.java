package org.warn.noadsbrowser.rs;

public class PageDTO {

	private String url;
	private String title;
	private String message;
	private StringBuilder output;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public StringBuilder getOutput() {
		return output;
	}
	public void setOutput(StringBuilder output) {
		this.output = output;
	}
	
}
