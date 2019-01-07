package com.drkiettran.text.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiettran.scriptureinaction.model.BibleBook;
import com.drkiettran.scriptureinaction.model.Chapter;
import com.drkiettran.scriptureinaction.model.Commentary;
import com.drkiettran.scriptureinaction.model.Link;
import com.drkiettran.scriptureinaction.model.VersePointer;
import com.drkiettran.scriptureinaction.repository.BibleRepositoryViaFile;

public class Document {
	private static final String EMPTY_RESULT = "<html><font size=\"%d\" face=\"%s\">*** EMPTY ***</font></html>";
	private List<Page> pages;
	private Page currentPage;
	private BibleBook bibleBook;
	private BibleRepositoryViaFile repo = new BibleRepositoryViaFile();

	public void setBibleBook(BibleBook bibleBook) {
		this.bibleBook = bibleBook;
	}

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

	private static final Logger LOGGER = LoggerFactory.getLogger(Document.class);

	public String getLinkInfo(int verseNo) {
		Chapter chapter = getChapterWithLinksAndComments();
		List<Link> links = chapter.getLinks();

		if (invalidVerseNumber(verseNo, chapter) || noLinks(links)) {
			return EMPTY_RESULT;
		}

		Link link = null;
		boolean found = false;
		int startingVerse, endingVerse;
		StringBuilder sb = new StringBuilder();

		for (int idx = 0; idx < links.size(); idx++) {
			link = links.get(idx);
			String linkRef = link.getLinkRef().replaceAll("–", "-");

			if (linkRef.indexOf('-') > 0) {
				String[] verses = linkRef.split(":")[1].split("-");
				startingVerse = Integer.valueOf(verses[0]);
				endingVerse = Integer.valueOf(verses[1]);
			} else {
				startingVerse = endingVerse = Integer.valueOf(linkRef.split(":")[1]);
			}
			LOGGER.info("verseNo {} starting {} ending {}", verseNo, startingVerse, endingVerse);
			if (verseNo >= startingVerse && verseNo <= endingVerse) {
				found = true;
				sb.append(link.getLinkRef()).append("--> ");
				List<VersePointer> vps = link.getRelatedVerses();

				for (VersePointer vp : vps) {
					sb.append(vp.getText()).append(", ");
				}
			}
		}

		if (found) {
			return sb.toString();
		}
		return "";
	}

	public boolean noLinks(List links) {
		return links == null || links.size() == 0;
	}

	public boolean invalidVerseNumber(int verseNo, Chapter chapter) {
		return verseNo < 1 || verseNo > chapter.getVerses().size();
	}

	private Chapter getChapterNAB(String bookName, int chapterNumber) {
		return loadNABBook(bookName).getChapters().get(chapterNumber - 1);
	}

	private BibleBook loadNABBook(String bookName) {
		return repo.load(BibleBook.NAB, bookName);
	}

	public String getCommentInfo(Integer verseNo) {
		Chapter chapter = getChapterWithLinksAndComments();
		List<Commentary> comments = chapter.getComments();

		if (invalidVerseNumber(verseNo, chapter) || noLinks(comments)) {
			return EMPTY_RESULT;
		}

		Commentary comment = null;
		boolean found = false;
		StringBuilder sb = new StringBuilder("<html><font \" + \"size=\"%d\" face=\"%s\">");

		for (int idx = 0; idx < comments.size(); idx++) {
			comment = comments.get(idx);
			if (verseNo >= comment.getStartingVerseNumber() && verseNo <= comment.getEndingVerseNumber()) {
				found = true;
				StringBuilder text = new StringBuilder(comment.getText().replaceAll("â", "-"));
				text.insert(0, "<b>").insert(text.indexOf("]") + 1, "</b>");
				sb.append(text);
				sb.append("<br>*** ---- ***<br>");
			}
		}

		if (found) {
			sb.append("</font></html>");
			return sb.toString();
		}
		return EMPTY_RESULT;
	}

	public Chapter getChapterWithLinksAndComments() {
		Chapter chapter;
		if (bibleBook.getTranslation().equalsIgnoreCase(BibleBook.NAB)) {
			chapter = bibleBook.getChapters().get(currentPage.getPageNumber() - 1);
		} else {
			chapter = getChapterNAB(bibleBook.getName(), currentPage.getPageNumber());
		}
		return chapter;
	}

	public String getRelatedVerses(Integer verseNo) {
		Chapter chapter = getChapterWithLinksAndComments();
		List<Link> links = chapter.getLinks();

		if (invalidVerseNumber(verseNo, chapter) || noLinks(links)) {
			return EMPTY_RESULT;
		}

		Link link = null;
		boolean found = false;
		int startingVerse = 0, endingVerse = 0;
		StringBuilder sb = new StringBuilder("<html><font \" + \"size=\"%d\" face=\"%s\">");

		for (int idx = 0; idx < links.size(); idx++) {
			link = links.get(idx);
			String linkRef = link.getLinkRef().replaceAll("–", "-");

			if (linkRef.indexOf('-') > 0) {
				String[] verses = linkRef.split(":")[1].split("-");
				startingVerse = Integer.valueOf(verses[0]);
				endingVerse = Integer.valueOf(verses[1]);
			} else {
				startingVerse = endingVerse = Integer.valueOf(linkRef.split(":")[1]);
			}
			LOGGER.info("verseNo {} starting {} ending {}", verseNo, startingVerse, endingVerse);
			if (verseNo >= startingVerse && verseNo <= endingVerse) {
				sb.append(generateRelatedVersesText(link, startingVerse, endingVerse));
				sb.append("<br>*** ---- ***<br>");
				found = true;
			}
		}

		if (found) {
			sb.append("</font></html>");
			return sb.toString();
		}

		return EMPTY_RESULT;
	}

	public String generateRelatedVersesText(Link link, int startingVerse, int endingVerse) {
		StringBuilder sb = new StringBuilder();
		for (int idx = startingVerse; idx <= endingVerse; idx++) {
			// @formatter:off
			sb.append("<b>")
			  .append(link.getShortName()).append(' ')
			  .append(this.getCurrentPageNumber()).append(':')
			  .append(idx)
			  .append(" </b> = ");
			
			sb.append(
				bibleBook.getChapters().get(this.getCurrentPageNumber() - 1)
				         .getVerses().get(idx - 1)
				         .getText())
			  .append("<br>");
			// @formatter:on
		}

		sb.append("<br>");
		List<VersePointer> vps = link.getRelatedVerses();

		for (VersePointer vp : vps) {
			BibleBook book = repo.load(bibleBook.getTranslation(), vp.getBookName());
			LOGGER.info("*** vp: {}\n", vp.getText());

			int startingVerseNumber;
			int endingVerseNumber;

			if (vp.getText().indexOf(":") < 0) {
				startingVerseNumber = 1;
				endingVerseNumber = book.getChapters().get(vp.getChapterNumber() - 1).getVerses().size();
			} else {
				startingVerseNumber = vp.getStartingVerseNumber();
				endingVerseNumber = vp.getEndingVerseNumber();
			}

			for (int idx = startingVerseNumber; idx <= endingVerseNumber; idx++) {
				// @formatter:off
				sb.append("<b>")
				  .append(vp.getShortName()).append(' ')
				  .append(vp.getChapterNumber()) .append(':').append(idx)
				  .append("</b> = ")
				  .append(getVerseText(book, vp.getChapterNumber(), idx))
				  .append(book.getChapters().get(vp.getChapterNumber() - 1).getVerses().get(idx - 1).getText())
				  .append("<br>");
				// @formatter:on
			}
		}

		LOGGER.info("*** references: {}", sb.toString());
		return sb.toString();
	}

	private String getVerseText(BibleBook book, int chapterNo, int verseNo) {
		return book.getChapters().get(chapterNo - 1).getVerses().get(verseNo - 1).getText();
	}

	public Object getBookName() {
		return bibleBook.getName();
	}

	public String getTranslationTexts(Integer verseNo) {
		Chapter chapter = getChapterWithLinksAndComments();

		if (invalidVerseNumber(verseNo, chapter)) {
			return EMPTY_RESULT;
		}

		StringBuilder sb = new StringBuilder("<html><font \" + \"size=\"%d\" face=\"%s\">");
		String translations[] = { BibleBook.NAB, BibleBook.RSV, BibleBook.DR, BibleBook.LV };
		String translationShortNames[] = { "NAB", "RSV", "DR", "LV" };

		for (int idx = 0; idx < translations.length; idx++) {
			BibleBook book = repo.load(translations[idx], bibleBook.getName());
			sb.append("<b>").append(translationShortNames[idx]).append("</b>: ");
			sb.append(book.getVerseText(getCurrentPageNumber(), verseNo)).append("<br><br>");
		}

		sb.append("</font></html>");
		return sb.toString();
	}
}
