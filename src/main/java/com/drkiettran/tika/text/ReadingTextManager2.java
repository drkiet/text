package com.drkiettran.tika.text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ReadingTextManager2 {

	private String text;
	private List<String> words;
	private int caret = 0;
	private int wordIdx = 0;
	private String currentWord = "";

	public ReadingTextManager2(String text) {
		this.text = text;
		words = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(text, " ");

		while (st.hasMoreTokens()) {
			words.add(st.nextToken());
		}
	}

	public String getText() {
		return text;
	}

	public List<String> getWords() {
		return words;
	}

	public int getNumberOfWords() {
		return words.size();
	}

	public String getTransformed() {
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			sb.append(word).append(' ');
		}
		return sb.toString();
	}

	public String getReadingText() {
		return text;
	}

	public int getCurrentCaret() {
		return caret;
	}

	public int getWordsFromBeginning() {
		return caret;
	}

	public int getTotalWords() {
		return words.size();
	}

	public String getNextWord() {
		if (wordIdx < words.size()) {
			currentWord = words.get(wordIdx++);
			caret = text.indexOf(currentWord, caret);
		} else {
			currentWord = null;
		}
		return currentWord;
	}

	public void setCurrentCaret(int caret) {
		this.caret = caret;
	}

	public int search(String searchText) {
		return 0;
	}

}
