package com.drkiettran.text.model;

import java.util.List;

public class Document {
	private List<Page> pages;
	private int idx = 0;

	public Document(List<Page> pages) {
		this.pages = pages;
	}

	public Page nextPage() {
		Page page = pages.get(idx);
		if (idx < pages.size() - 1) {
			idx++;
		}
		return page;
	}

	public Page previousPage() {
		Page page = pages.get(idx);
		if (idx > 0) {
			idx--;
		}
		return page;
	}

	public void setIdx(int idx) {
		if (idx - 1 >= 0 && idx - 1 <= pages.size() - 1) {
			this.idx = idx;
		}
	}

	public int getPageCount() {
		return pages.size();
	}

	public int getCurrentPageNumber() {
		return idx;
	}
}
