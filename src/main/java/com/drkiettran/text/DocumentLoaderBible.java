package com.drkiettran.text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiettran.scriptureinaction.model.BibleBook;
import com.drkiettran.scriptureinaction.model.Chapter;
import com.drkiettran.scriptureinaction.model.Verse;
import com.drkiettran.scriptureinaction.repository.BibleRepositoryViaFile;
import com.drkiettran.text.loader.DocumentLoader;
import com.drkiettran.text.model.Document;
import com.drkiettran.text.model.Page;

public class DocumentLoaderBible implements DocumentLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentLoaderBible.class);

	private String translation = null;
	private BibleRepositoryViaFile repo;
	private BibleBook book;

	public DocumentLoaderBible(String translation) {
		this.translation = translation;

	}

	@Override
	public Document getPages(String bookName) {
		repo = new BibleRepositoryViaFile();
		book = repo.load(translation, bookName);
		Document doc = new Document(getPages(book.getChapters()));
		doc.setBibleBook(book);

		return doc;
	}

	@Override
	public Document getPages(InputStream is) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Page> getPages(List<Chapter> chapters) {
		List<Page> pages = new ArrayList<Page>();

		for (int idx = 0; idx < chapters.size(); idx++) {
			Page page = new Page(getPage(chapters.get(idx).getVerses()));
			page.setPageNumber(idx + 1);
			pages.add(page);
		}
		return pages;
	}

	private String getPage(List<Verse> verses) {
		StringBuilder sb = new StringBuilder();
		for (int idx = 0; idx < verses.size(); idx++) {
			Verse verse = verses.get(idx);
			sb.append(verse.getText().trim()).append('\n');
		}
		return sb.toString();
	}

}
