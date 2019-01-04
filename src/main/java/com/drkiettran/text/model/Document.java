package com.drkiettran.text.model;

import java.util.ArrayList;
import java.util.List;

public class Document {
	private List<Page> pages;
	private Page currentPage;

	public Document(List<Page> pages) {
		this.pages = pages;
		currentPage = pages.get(0);
	}

	public Page getCurrentPage() {
		return (Page) currentPage.clone();
	}

	public Page nextPage() {
		if (currentPage.getPageNumber() < pages.size()) {
			currentPage = pages.get(currentPage.getPageNumber());
		}

		return getCurrentPage();
	}

	public Page previousPage() {
		if (currentPage.getPageNumber() > 1) {
			currentPage = pages.get(currentPage.getPageNumber() - 2);
		}
		return getCurrentPage();
	}

	public void setPageNo(int pageNo) {
		if (pageNo >= 1 && pageNo <= pages.size()) {
			currentPage = pages.get(pageNo - 1);
		}
	}

	public int getPageCount() {
		return pages.size();
	}

	public int getCurrentPageNumber() {
		return currentPage.getPageNumber();
	}

	public List<SearchResult> search(String searchText) {
		List<SearchResult> searchResults = new ArrayList<SearchResult>();

		for (int idx = 0; idx < pages.size(); idx++) {
			searchResults.add(pages.get(idx).getRtm().search(searchText));
		}

		return searchResults;
	}
}
