package com.drkiettran.tika.text;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tika.exception.TikaException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TextAppTest {

	private static final String TIKA_EXAMPLES_DOCX = "/Tika-examples.docx";
	private static final String EXPECTED_STRING = "This page provides";
	private static final String TIKA_EXAMPLES_PDF = "/Tika-examples.pdf";
	private TextApp textApp = null;

	@Before
	public void setUp() {
		textApp = new TextApp();
	}

	@Test
	public void given_a_pdf_content_i_should_get_a_main_text() throws IOException, TikaException, SAXException {
		String text = null;

		try (InputStream is = TextApp.class.getResourceAsStream(TIKA_EXAMPLES_PDF)) {
			text = textApp.parseToString(is);
		}

		assertThat(text, not(isEmptyOrNullString()));
		assertThat(text, containsString(EXPECTED_STRING));
	}

	@Test
	public void given_a_worddocx_content_i_should_get_a_main_text() throws IOException, TikaException, SAXException {
		String text = null;

		try (InputStream is = TextApp.class.getResourceAsStream(TIKA_EXAMPLES_DOCX)) {
			text = textApp.parseToString(is);
		}

		assertThat(text, not(isEmptyOrNullString()));
		assertThat(text, containsString(EXPECTED_STRING));
	}

	@Test
	public void loadTextFromFile() {

	}
}
