package com.drkiettran.text.model;

import com.drkiettran.text.ReadingTextManager;

public class Page {
	private ReadingTextManager rtm;

	private int pageIdx;
	private int pageNumber;

	public Page(String text) {
		rtm = new ReadingTextManager(text);
		rtm.getText();
	}

	public int getPageIdx() {
		return pageIdx;
	}

	public void setPageIdx(int pageIdx) {
		this.pageIdx = pageIdx;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public ReadingTextManager getRtm() {
		return rtm;
	}

}
