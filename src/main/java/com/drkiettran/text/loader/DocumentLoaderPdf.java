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
		LOGGER.info("Getting pages from file ...");
		try (InputStream is = new FileInputStream(fileName)) {
			Document doc = getPages(is);
			is.close();
			return doc;
		} catch (FileNotFoundException e) {
			LOGGER.error("File not found: {}", e);
		} catch (IOException e) {
			LOGGER.error("IO exception: {}", e);
		}
		return null;
	}

	@Override
	public Document getPages(InputStream is) {
		LOGGER.info("Getting pages on inputstream ...");
		try (PDDocument document = PDDocument.load(is)) {
//			if (!document.isEncrypted()) {
				List<Page> pages = getPages(document);
				document.close();
				return new Document(pages);
//			}
//			document.close();
		} catch (InvalidPasswordException e) {
			LOGGER.error("Invalid password: {}", e);
		} catch (IOException e) {
			LOGGER.error("IO exception: {}", e);
		}

		return null;
	}

	private List<Page> getPages(PDDocument document) throws IOException {
		LOGGER.info("Getting pages on PDDocument ...");
		List<Page> pages = new ArrayList<Page>();

		for (int idx = 0; idx < document.getNumberOfPages(); idx++) {
			LOGGER.info("PDF loads page # {}", idx + 1);
			Page page = new Page(getPage(document, idx + 1));
			page.setPageNumber(idx + 1);
			pages.add(page);
		}
		return pages;
	}

	private String getPage(PDDocument document, int pageNo) throws IOException {
		LOGGER.info("Getting page on PDDocument ...");
		PDFTextStripper reader = new PDFTextStripper();
		reader.setStartPage(pageNo);
		reader.setEndPage(pageNo);
		return reader.getText(document);
	}
}
