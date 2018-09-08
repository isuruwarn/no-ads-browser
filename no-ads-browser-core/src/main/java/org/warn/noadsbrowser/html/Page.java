package org.warn.noadsbrowser.html;

import org.jsoup.nodes.Document;

public class Page {
	
	private String title;
	private String message;
	private StringBuilder inputHTML;
	private StringBuilder outputHTML;
	private StringBuilder outputText;
	private Document document;
	
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
	public StringBuilder getInputHTML() {
		return inputHTML;
	}
	public void setInputHTML(StringBuilder inputHTML) {
		this.inputHTML = inputHTML;
	}
	public StringBuilder getOutputHTML() {
		return outputHTML;
	}
	public void setOutputHTML(StringBuilder outputHTML) {
		this.outputHTML = outputHTML;
	}
	public StringBuilder getOutputText() {
		return outputText;
	}
	public void setOutputText(StringBuilder outputText) {
		this.outputText = outputText;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	
}
