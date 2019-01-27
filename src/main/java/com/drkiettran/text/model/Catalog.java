package com.drkiettran.text.model;

/**
 * A catalog
 * 
 * @author ktran
 *
 */
public class Catalog {
	private int catalogId;
	private String fileName;
	private Document document;

	public int getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
