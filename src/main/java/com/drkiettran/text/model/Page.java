package com.drkiettran.text.model;

import java.util.List;

import com.drkiettran.text.ReadingTextManager;

public class Page {
	private ReadingTextManager rtm;

	private int pageIdx;
	private int pageNumber;

	@Override
	public Object clone() {
		Page clone = new Page();
		clone.pageIdx = this.pageIdx;
		clone.pageNumber = this.pageNumber;
		clone.rtm = this.rtm;
		return clone;
	}

	public Page(String text) {
		rtm = new ReadingTextManager(text);
		rtm.getText();
	}

	private Page() {

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

	public List<String> getWordsInPage() {
		return rtm.wordsInPage();
	}
}
