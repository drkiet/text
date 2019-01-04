package com.drkiettran.text.loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiettran.text.model.Document;
import com.drkiettran.text.model.Page;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;

public class DocumentLoaderEpub implements DocumentLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentLoaderEpub.class);

	@Override
	public Document getPages(String fileName) {
		try (InputStream is = new FileInputStream(fileName)) {
			return getPages(is);
		} catch (FileNotFoundException e) {
			LOGGER.error("File not found: {}", e);
		} catch (IOException e) {
			LOGGER.error("IO exception: {}", e);
		}
		return null;
	}

	@Override
	public Document getPages(InputStream is) {
		EpubReader reader = new EpubReader();
		try {
			Book book = reader.readEpub(is);
			return new Document(getPages(book));
		} catch (IOException e) {
			LOGGER.error("IO exception: {}", e);
		}
		return null;
	}

	private List<Page> getPages(Book book) throws IOException {
		List<Page> pages = new ArrayList<Page>();
		Spine spine = book.getSpine();
		List<SpineReference> spineReferences = spine.getSpineReferences();

		for (int idx = 0; idx < spineReferences.size(); idx++) {
			LOGGER.info("EPUB loads page # {}", idx + 1);
			Page page = new Page(getPage(spineReferences.get(idx), idx + 1));
			page.setPageNumber(idx + 1);
			pages.add(page);
		}
		return pages;
	}

	private static final String[] SUPPORTING_TAGS = { "p", "h1", "h2", "h3", "table", "thead", "tr", "td" };
	private static final List<String> TAGS = Arrays.asList(SUPPORTING_TAGS);

	private String getPage(SpineReference spineReference, int pageNo) throws IOException {
		org.jsoup.nodes.Document doc = Jsoup.parse(new String(spineReference.getResource().getData()), "ISO-8859-1");
		Elements elements = doc.getAllElements();

		StringBuilder sb = new StringBuilder("Title: ").append(doc.title()).append('\n');
		boolean lastTd = false;
		for (Element element : elements) {
			if (TAGS.contains(element.tagName().toLowerCase())) {
				if ("table".equalsIgnoreCase(element.tagName())) {
					System.out.println("Table:");
				} else if ("tr".equalsIgnoreCase(element.tagName())) {
					sb.append('\n');
				} else if ("td".equalsIgnoreCase(element.tagName())) {
					sb.append(element.text()).append(" --- ");
					lastTd = true;
				} else {
					if (lastTd) {
						sb.append('\n');
						lastTd = false;
					}
					sb.append(element.text()).append('\n');
				}

			} else {
				LOGGER.debug("**** huh? {}", element.tagName());
			}
		}
//		System.out.println("text: " + sb.toString());
		return sb.toString();
	}

}
