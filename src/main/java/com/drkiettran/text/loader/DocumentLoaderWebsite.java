package com.drkiettran.text.loader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.drkiettran.text.model.Document;
import com.drkiettran.text.model.Page;

public class DocumentLoaderWebsite implements DocumentLoader {

	private String url;

	@Override
	public Document getPages(String fileName) {
		this.url = fileName;
		return getPages((InputStream) null);
	}

	@Override
	public Document getPages(InputStream is) {
		return new Document(getUrlPage(url));
	}

	private List<Page> getUrlPage(String url) {
		String content = "";
		WebDriver driver = new HtmlUnitDriver();
		List<Page> pages = new ArrayList<Page>();

		driver.get(url);
		Page page = new Page(getUrlPage(Jsoup.parse(driver.getPageSource())));
		page.setPageNumber(1);
		pages.add(page);

		return pages;
	}

	private int indent = 0;

	private String getUrlPage(org.jsoup.nodes.Document doc) {
		StringBuilder sb = new StringBuilder();

		for (Element child : doc.children()) {
			sb.append(getChildText(child)).append('\n');
		}

		System.out.println(sb.toString());
		return sb.toString();
	}

	private String getChildText(Element childElement) {
		StringBuilder sb = new StringBuilder();

		if (hasNoChildren(childElement)) {
			return childElement.text();
		}

		indent += 2;
		for (Element child : childElement.children()) {
			String childText = getChildText(child).trim();
			if (!childText.isEmpty()) {
				for (int idx = 0; idx < indent; idx++) {
					sb.append(' ');
				}
				sb.append(getChildText(child)).append('\n');
			}
		}

		indent -= 2;
		return sb.toString();
	}

	public boolean hasNoChildren(Element childElement) {
		return childElement.children().isEmpty();
	}

}
