package com.drkiettran.text;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.drkiettran.text.loader.DocumentLoaderEpub;
import com.drkiettran.text.loader.DocumentLoaderPdf;
import com.drkiettran.text.loader.DocumentLoaderText;
import com.drkiettran.text.loader.DocumentLoaderWebsite;
import com.drkiettran.text.model.Document;

public class TextApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(TextApp.class);
	private String translation = null;
	private String newPageIndicator = "~~~";

	public TextApp(String translation) {
		this.translation = translation;
	}

	public void setNewPageIndicator(String newPageIndicator) {
		this.newPageIndicator = newPageIndicator;
	}

	public TextApp() {
		// TODO Auto-generated constructor stub
	}

	public String parseToString(InputStream is) throws IOException, TikaException, SAXException {
		BodyContentHandler handler = new BodyContentHandler(-1);
		AutoDetectParser parser = new AutoDetectParser();
		Metadata metadata = new Metadata();
		parser.parse(is, handler, metadata);
		return handler.toString();
	}

	public String parseToString2(InputStream is) {
		Tika tika = new Tika();
		try {
			return tika.parseToString(is);
		} catch (IOException | TikaException e) {
			LOGGER.error("IO Error: {}", e);
		}
		return "";
	}

	public Document getPages(String fileName) {
		LOGGER.info("getting pages for {}", fileName);
		if (fileName.endsWith(".pdf")) {
			return new DocumentLoaderPdf().getPages(fileName);
		} else if (fileName.endsWith(".epub")) {
			return new DocumentLoaderEpub().getPages(fileName);
		} else if (fileName.endsWith(".txt")) {
			return new DocumentLoaderText(newPageIndicator).getPages(fileName);
		} else if (translation != null) {
			DocumentLoaderBible doc = new DocumentLoaderBible(translation);
			return doc.getPages(fileName);
		} else if (fileName.startsWith("http")) {
			return new DocumentLoaderWebsite().getPages(fileName);
		} else {
			LOGGER.error("Unable to load {} - file type NOT SUPPORTED.", fileName);
		}
		return null;
	}

}
