package com.drkiettran.tika.text;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SearchResult {
	List<Word> matchedWords = null;

	public SearchResult() {
		matchedWords = new ArrayList<Word>();
	}

	public void addFoundWord(Word word) {
		matchedWords.add(word);
	}

	public Hashtable<Integer, String> getMatchedWords() {
		Hashtable<Integer, String> foundMatchedWords = new Hashtable<Integer, String>();

		for (Word word : matchedWords) {
			foundMatchedWords.put(word.getIndexOfText(), word.getTransformedWord());
		}

		return foundMatchedWords;
	}

	public int getNumberMatchedWords() {
		return matchedWords.size();
	}

}
