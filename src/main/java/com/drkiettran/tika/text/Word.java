package com.drkiettran.tika.text;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Word {
	// @formatter:off
	public static final String[] KNOWN_WORD_LIST = {
		"no.", "u.s.", "usa", "u.s.a" 
	};
	// @formatter:on

	public static final List<String> KNOWN_WORDS = Arrays.asList(KNOWN_WORD_LIST);

	private boolean word;
	private boolean wellKnownWord;
	private String originalWord;
	private String transformedWord;

	private boolean endsWithPeriod;
	private boolean endsWithComma;
	private boolean endsWithSemicolon;
	private boolean endsWithRightParenthesis;
	private int indexOfText;

	public Word(String token) {
		originalWord = token;
		token = token.replaceAll("\n", " ").trim();
		word = isItaWord(token);

		if (!isWord()) {
			return;
		}

		wellKnownWord = isWellKnown(token);

		if (wellKnownWord) {
			transformedWord = token;
			return;
		}

		getPunctuation(token);
		transformedWord = transform(token);
	}

	private Word() {
		// TODO Auto-generated constructor stub
	}

	private String transform(String token) {
		StringBuilder sb = new StringBuilder();

		for (int idx = 0; idx < token.length(); idx++) {
			char curChar = token.charAt(idx);
			if (isReadableCharacter(curChar)) {
				sb.append(curChar);
			}
		}

		return sb.toString();
	}

	private void getPunctuation(String token) {
		this.endsWithComma = lastCharIs(token, ',');
		this.endsWithSemicolon = lastCharIs(token, ';');
		this.endsWithPeriod = lastCharIs(token, '.');
		this.endsWithRightParenthesis = lastCharIs(token, ')');
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(Word.class);

	private boolean lastCharIs(String token, char expectedChar) {
//		LOGGER.info("token: {}; lastChar: {}", token, token.charAt(token.length() - 1));
		return token.charAt(token.length() - 1) == expectedChar;
	}

	private boolean isReadableCharacter(char curChar) {
		return curChar == '-' || curChar == '\'' || Character.isAlphabetic(curChar) || Character.isDigit(curChar);
	}

	public String getOriginalWord() {
		return originalWord;
	}

	public String getTransformedWord() {
		return transformedWord;
	}

	private boolean isWellKnown(String token) {
		return KNOWN_WORDS.contains(token.toLowerCase());
	}

	/*
	 * a word must have at least one alphabet or one digit.
	 */
	private boolean isItaWord(String token) {
		for (int idx = 0; idx < token.length(); idx++) {
			if (isAlphabetOrDigit(token.charAt(idx))) {
				return true;
			}
		}
		return false;
	}

	private boolean isAlphabetOrDigit(char ch) {
		return Character.isAlphabetic(ch) || Character.isDigit(ch);
	}

	public boolean isWord() {
		return word;
	}

	public boolean isWellKnownWord() {
		return wellKnownWord;
	}

	public boolean endsWithPeriod() {
		return endsWithPeriod;
	}

	public boolean endsWithComma() {
		return endsWithComma;
	}

	public boolean endsWithSemicolon() {
		return endsWithSemicolon;
	}

	public boolean endsWithRightParenthesis() {
		return endsWithRightParenthesis;
	}

	public void setIndexOfText(int indexOfText) {
		this.indexOfText = indexOfText;
	}

	@Override
	public Word clone() {
		Word copy = new Word();
		copy.endsWithComma = this.endsWithComma;
		copy.endsWithPeriod = this.endsWithPeriod;
		copy.endsWithRightParenthesis = this.endsWithRightParenthesis;
		copy.endsWithSemicolon = this.endsWithSemicolon;
		copy.indexOfText = this.indexOfText;
		copy.originalWord = this.originalWord;
		copy.transformedWord = this.transformedWord;
		copy.wellKnownWord = this.wellKnownWord;
		copy.word = this.word;
		return copy;
	}

	public int getIndexOfText() {
		return indexOfText;
	}

}
