package com.drkiettran.tika.text;

import java.util.Arrays;
import java.util.List;

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

	public Word(String token) {
		originalWord = token;
		word = isWord(token);

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

	private boolean lastCharIs(String token, char expectedChar) {
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
	private boolean isWord(String token) {
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
}
