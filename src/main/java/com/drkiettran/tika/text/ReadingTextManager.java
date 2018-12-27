package com.drkiettran.tika.text;

public class ReadingTextManager {

	private String readingText;
	private int currentCaret = 0;
	private int wordsFromBeginning = 0;
	private int totalWords = 0;
	private boolean firstWordRetrieved;

	public int getWordsFromBeginning() {
		return wordsFromBeginning;
	}

	public int getTotalWords() {
		return totalWords;
	}

	public ReadingTextManager(String readingText) {
		this.readingText = spacingText(readingText);
		countTotalWords();
		currentCaret = 0;
		wordsFromBeginning = 0;
		firstWordRetrieved = false;
	}

	private void countTotalWords() {
		while (getNextWord() != null) {
			totalWords++;
		}
	}

	public int getCurrentCaret() {
		return currentCaret;
	}

	public String getReadingText() {
		return readingText;
	}

	private String spacingText(String text) {

		StringBuilder sb = new StringBuilder();
		char[] charsInText = text.toCharArray();

		for (int i = 0; i < charsInText.length; i++) {
			sb.append(charsInText[i]);
			if (textNeedsASpace(charsInText, i)) {
				sb.append(' ');
			}
		}
		return sb.toString();
	}

	private boolean textNeedsASpace(char[] charsInText, int i) {
		return isSeparator(charsInText[i]) && isNotAtTheLastChar(charsInText, i) && charsInText[i + 1] != ' ';
	}

	private boolean isNotAtTheLastChar(char[] charsInText, int i) {
		return i < charsInText.length - 1;
	}

	private static final char[] SEPARATORS = new char[] { '?', '.', ',', ';', '!', '-', '_', '/', '\n', '\r' };

	private boolean isSeparator(char c) {
		for (char separator : SEPARATORS) {
			if (c == separator) {
				return true;
			}
		}
		return false;
	}

	public String getNextWord() {
		String nextWord = "";

		for (;;) {
			if (currentCaret >= readingText.length()) {
				break;
			}

			if (!firstWordRetrieved) {
				firstWordRetrieved = true;
			} else {
				moveCaretToNextSpace();
				moveCaretToNextNonSpace();
			}

			nextWord = extractNextWord();

			if (!nextWord.isEmpty()) {
				wordsFromBeginning++;
				break;
			}
		}

		return nextWord.isEmpty() ? null : nextWord;
	}

	private String extractNextWord() {
		int saveCurrentCaret = currentCaret;
		moveCaretToNextSpace();
		int newCurrentCaret = currentCaret;
		currentCaret = saveCurrentCaret;
		return readingText.substring(currentCaret, newCurrentCaret).trim();
	}

	private void moveCaretToNextNonSpace() {
		while (currentCaret < readingText.length()) {
			if (readingText.charAt(currentCaret) != ' ') {
				break;
			}
			currentCaret++;
		}
	}

	private void moveCaretToNextSpace() {
		while (currentCaret < readingText.length()) {
			if (readingText.charAt(currentCaret) == ' ') {
				break;
			}
			currentCaret++;
		}
	}

	public void setCurrentCaret(int newCaretPosition) {
		wordsFromBeginning = 0;
		currentCaret = 0;
		int lastCaret = 0;

		while (getNextWord() != null) {
			if (newCaretPosition < currentCaret) {
				currentCaret = lastCaret;
				break;
			}
			lastCaret = currentCaret;
		}
	}

	public int search(String searchText) {
		int newCaret = readingText.toLowerCase().indexOf(searchText.toLowerCase(), currentCaret + 1);

		if (newCaret > 0) {
			setCurrentCaret(newCaret);
		}

		return newCaret;
	}

}
