package com.drkiettran.text.loader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drkiettran.text.model.Document;
import com.drkiettran.text.model.Page;

public class DocumentLoaderText implements DocumentLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentLoaderText.class);
	private String lastLine = null;

	@Override
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
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			return new Document(getPages(br));
		} catch (InvalidPasswordException e) {
			LOGGER.error("Invalid password: {}", e);
		} catch (IOException e) {
			LOGGER.error("IO exception: {}", e);
		}

		return null;
	}

	private List<Page> getPages(BufferedReader br) throws IOException {
		List<Page> pages = new ArrayList<Page>();
		int pageNo = 1;

		for (;;) {
			String pageText = getPage(br);
			if (pageText.isEmpty()) {
				break;
			}
			Page page = new Page(pageText);
			page.setPageNumber(pageNo++);
			pages.add(page);
		}

		return pages;
	}

	private String getPage(BufferedReader br) throws IOException {
		StringBuilder page = new StringBuilder();
		if (lastLine != null) {
			page.append(lastLine);
			lastLine = null;
		}

		for (;;) {
			String line = br.readLine();
			if (line == null) {
				break;
			}
			if (line.startsWith("*** chapter")) {
				lastLine = line;
				return page.toString();
			} else {
				page.append(line).append('\n');
			}
		}
		return page.toString();
	}

}
