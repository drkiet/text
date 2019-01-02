package com.drkiettran.text.loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiettran.text.model.Document;
import com.drkiettran.text.model.Page;

public class DocumentLoaderPdf implements DocumentLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentLoaderPdf.class);

	public Document getPages(String fileName) {
		try (InputStream is = new FileInputStream(fileName)) {
			return getPages(is);
		} catch (FileNotFoundException e) {
			LOGGER.error("File not found: {}", e);
		} catch (IOException e) {
			LOGGER.error("IO exception: {}", e);
		}
		return null;
	}

	@Override
	public Document getPages(InputStream is) {
		try (PDDocument document = PDDocument.load(is)) {
			if (!document.isEncrypted()) {
				return new Document(getPages(document));
			}
		} catch (InvalidPasswordException e) {
			LOGGER.error("Invalid password: {}", e);
		} catch (IOException e) {
			LOGGER.error("IO exception: {}", e);
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
