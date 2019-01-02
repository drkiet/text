package com.drkiettran.tika.text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public Document getPagesFromPdf(InputStream is) {

		try (PDDocument document = PDDocument.load(is)) {
			if (!document.isEncrypted()) {
				return new Document(getPages(document));
			}
		} catch (InvalidPasswordException e) {
			LOGGER.error("Error: {}", e);
		} catch (IOException e) {
			LOGGER.error("Error: {}", e);
		}

		return null;
	}

	private List<Page> getPages(PDDocument document) throws IOException {
		List<Page> pages = new ArrayList<Page>();

		for (int idx = 0; idx < document.getNumberOfPages(); idx++) {
			pages.add(new Page(getPage(document, idx + 1)));
		}
		return pages;
	}

	private String getPage(PDDocument document, int pageNo) throws IOException {
		PDFTextStripper reader = new PDFTextStripper();
		reader.setStartPage(pageNo);
		reader.setEndPage(pageNo);
		return reader.getText(document);
	}
}
