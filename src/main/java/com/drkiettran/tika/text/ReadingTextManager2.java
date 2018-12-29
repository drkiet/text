package com.drkiettran.tika.text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadingTextManager2 {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadingTextManager2.class);

	private static final char LF = '\n';
	private String text;
	private List<Word> words;
	private int caret = 0;
	private int wordIdx = 0;
	private Word currentWord;
	private int numPauses = 0;

	private int lastWordLength = 0;

	public ReadingTextManager2(String text) {
		this.text = text;
		initializeWordList();
	}

	public void initializeWordList() {
		makeParagraph();
		insertExtraLF();
		words = new ArrayList<Word>();
		StringTokenizer st = new StringTokenizer(text, " ");

		while (st.hasMoreTokens()) {
			Word word = new Word(st.nextToken().trim());

			if (word.isWord()) {
				words.add(word);
			}
		}
	}

	private void makeParagraph() {

		StringBuilder sb = new StringBuilder();
		for (int idx = 0; idx < text.length(); idx++) {
			if (notFirstNotLastChar(idx)) {
				if (singleLF(text, idx) && !Character.isUpperCase(text.charAt(idx + 1))
						&& !Character.isDigit(text.charAt(idx + 1))) {
					sb.append(' ');
					continue;
				}

			}
			sb.append(text.charAt(idx));
		}
		text = sb.toString();

	}

	private void insertExtraLF() {
		StringBuilder sb = new StringBuilder();
		for (int idx = 0; idx < text.length(); idx++) {
			if (notFirstNotLastChar(idx)) {
				if (singleLF(text, idx)) {
					sb.append(LF);
				}
			}
			sb.append(text.charAt(idx));
		}
		text = sb.toString();
	}

	private boolean notFirstNotLastChar(int idx) {
		return idx > 0 && idx < text.length() - 2;
	}

	private boolean singleLF(String text, int idx) {
		return text.charAt(idx - 1) != LF && text.charAt(idx) == LF && text.charAt(idx + 1) != LF;
	}

	public String getText() {
		return text;
	}

	public List<Word> getWords() {
		return words;
	}

	public int getNumberOfWords() {
		return words.size();
	}

	public String getReadingText() {
		return text;
	}

	public int getCurrentCaret() {
		return caret;
	}

	public int getWordsFromBeginning() {
		return wordIdx;
	}

	public int getTotalWords() {
		return words.size();
	}

	/**
	 * Return the next word to be displayed. If previous word has a period: empty
	 * string is returned twice (2). If previous word has a comma, semicolon or a
	 * right parenthesis: an empty string is returned once (1). If no more word,
	 * null is returned.
	 * 
	 * @return a string contains a single word.
	 */
	public String getNextWord() {
		if (numPauses > 0) {
			numPauses--;
			return "";
		}

		if (wordIdx < words.size()) {
			currentWord = words.get(wordIdx++);
			numPauses = getNumberOfPauses();

			caret = text.indexOf(currentWord.getOriginalWord(), caret + lastWordLength);
			lastWordLength = currentWord.getOriginalWord().length();
			return currentWord.getTransformedWord();
		}

		return null;
	}

	public void setCurrentCaret(int caret) {
		this.caret = caret;
	}

	public int search(String searchText) {
		return 0;
	}

	private int getNumberOfPauses() {
		if (currentWord.endsWithPeriod()) {
			return 2;
		} else if (currentWord.endsWithComma() || currentWord.endsWithSemicolon()
				|| currentWord.endsWithRightParenthesis()) {
			return 1;
		}
		return 0;
	}
}
