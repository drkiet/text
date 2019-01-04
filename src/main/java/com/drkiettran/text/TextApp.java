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
import com.drkiettran.text.model.Document;

public class TextApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(TextApp.class);

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
		if (fileName.endsWith(".pdf")) {
			return new DocumentLoaderPdf().getPages(fileName);
		} else if (fileName.endsWith(".epub")) {
			return new DocumentLoaderEpub().getPages(fileName);
		} else if (fileName.endsWith(".txt")) {
			return new DocumentLoaderText().getPages(fileName);
		} else {
			LOGGER.error("Unable to load {} - file type NOT SUPPORTED.", fileName);
		}
		return null;
	}

}
