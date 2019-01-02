package com.drkiettran.text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiettran.text.model.SearchResult;
import com.drkiettran.text.model.Word;

public class ReadingTextManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadingTextManager.class);

	private static final char LF = '\n';
	private String text;
	private List<Word> words;
	private int caret = 0;
	private int wordIdx = 0;
	private Word currentWord;
	private int numPauses = 0;

	public ReadingTextManager(String text) {
		LOGGER.debug("*** text: \n{}", text);
		this.text = text;
		initializeWordList();
	}

	/**
	 * create a list of Words and maintain internally. wordIdx is the location of a
	 * word in the list.
	 * 
	 */
	public void initializeWordList() {
		LOGGER.debug("initializing word list");
		makeParagraph();
		insertExtraLF();
		String workingText = text.replaceAll("\n", " ");
		words = new ArrayList<Word>();

		int startIdx = 0;
		int foundIdx = 0;
		for (;;) {
			startIdx = skipBlanks(startIdx);

			if (startIdx < 0) {
				break;
			}

			foundIdx = workingText.indexOf(' ', startIdx);

			if (foundIdx < 0) {
				break;
			}

			String token = workingText.substring(startIdx, foundIdx + 1);
			Word word = extractWord(token);
			if (word.isWord()) {
				word.setIndexOfText(startIdx);
			}
			startIdx = foundIdx + 1;
		}

		if (foundIdx < 0 && startIdx > 0) {
			String token = workingText.substring(startIdx);
			Word word = extractWord(token);
			if (word.isWord()) {
				word.setIndexOfText(startIdx);
			}
		}

		LOGGER.info("found: {} words", words.size());
	}

	/**
	 * Extract a word from a token.
	 * 
	 * @param token
	 * @return
	 */
	public Word extractWord(String token) {
		Word word = new Word(token);

		if (word.isWord()) {
			words.add(word);
		}

		LOGGER.debug("word: /{}/{}/", word.getOriginalWord(), word.getTransformedWord());
		return word;
	}

	public int skipBlanks(int idx) {
		while (true) {
			if (idx >= text.length() - 1) {
				idx = -1;
				break;
			}
			if (text.charAt(idx) == ' ') {
				idx++;
			} else {
				break;
			}
		}
		return idx;
	}

	/**
	 * Making a viewable paragraph
	 */
	private void makeParagraph() {
		char lastChar = 'x';
		StringBuilder sb = new StringBuilder();

		for (int idx = 0; idx < text.length(); idx++) {
			char curChar = text.charAt(idx);

			if (notFirstNotLastChar(idx)) {
				char nextChar = text.charAt(idx + 1);
				if (singleLF(text, idx) && !Character.isUpperCase(nextChar) && !Character.isDigit(nextChar)
						&& nextChar != ' ' && nextChar != '•') {
					sb.append(' ');
					continue;
				}

			}

			// Before adding a digit, make sure it has space in front.
			if (!Character.isDigit(lastChar) && Character.isDigit(curChar) && sb.length() > 0 && lastChar != ' ') {
				sb.append(' ');
			}
			if (curChar == '—') {
				curChar = '-';
			} else if (curChar == '\t') {
				curChar = ' ';
			}
			sb.append(curChar);
			lastChar = curChar;
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
			caret = currentWord.getIndexOfText();
			currentWord.getOriginalWord().length();
//			return currentWord.getTransformedWord();
			return currentWord.getOriginalWord();
		}

		return null;
	}

	public void setCurrentCaret(int caretPosition) {
		for (int idx = 0; idx < words.size(); idx++) {
			if (caretPosition <= words.get(idx).getIndexOfText()) {
				wordIdx = idx;
				currentWord = words.get(wordIdx);
				break;
			}
		}
	}

	public SearchResult search(String searchText) {
		SearchResult sr = new SearchResult();

		StringTokenizer st = new StringTokenizer(searchText, " ");
		while (st.hasMoreTokens()) {
			String token = st.nextToken().toLowerCase();
			for (Word word : words) {
				if (word.getTransformedWord().toLowerCase().contains(token)) {
					sr.addFoundWord(word);
				}
			}
		}
		LOGGER.info("{} words '{}' found", sr.getNumberMatchedWords(), searchText);
		return sr;
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

	public Word getWordAt(int caretPosition) {
		for (int idx = 0; idx < words.size(); idx++) {
			if (caretPosition <= words.get(idx).getIndexOfText()) {
				LOGGER.info("found *** {}", words.get(idx - 1).getTransformedWord());
				return words.get(idx - 1).clone();
			}
		}
		return null;
	}
}
