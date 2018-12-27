package com.drkiettran.tika.text;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ReadingTextManagerTest {

	@Parameters
	public static Collection<Object[]> getTestParameters() {
		return Arrays.asList(new Object[][] { { "", 0, 0, null }, { "word", 1, 1, "word" },
				{ "challenging conversations I've had with Craig Larman,"
						+ "Tom and Mary Poppendieck, Dan North, Gordon Weir, Jeff Patton "
						+ " and Matthias Edinger - (in no particular order).", 25, 7, "Larman," } });
	}

	private String readingText;
	private Integer totalWords;
	private Integer wordsFromBeginning;
	private String expectedWord;

	public ReadingTextManagerTest(String readingText, Integer totalWords, Integer wordsFromBeginning,
			String expectedWord) {
		this.readingText = readingText;
		this.totalWords = totalWords;
		this.wordsFromBeginning = wordsFromBeginning;
		this.expectedWord = expectedWord;
	}

	@Test
	public void should_get_the_right_total_words_count() {
		ReadingTextManager readingTextManager = new ReadingTextManager(readingText);
		assertThat(readingTextManager.getTotalWords(), equalTo(totalWords));
	}

	@Test
	public void should_get_the_right_word() {
		ReadingTextManager readingTextManager = new ReadingTextManager(readingText);
		String word = null;
		for (int i = 0; i < wordsFromBeginning; i++) {
			word = readingTextManager.getNextWord();
		}
		assertThat(word, equalTo(expectedWord));
	}

	@Test
	public void should_get_the_right_word_at_a_location() {
		ReadingTextManager readingTextManager = new ReadingTextManager(
				"challenging conversations I've had with Craig Larman,");
		readingTextManager.setCurrentCaret(11);
		assertThat(readingTextManager.getNextWord(), equalTo("conversations"));
		readingTextManager.setCurrentCaret(12);
		assertThat(readingTextManager.getNextWord(), equalTo("I've"));
	}
}
