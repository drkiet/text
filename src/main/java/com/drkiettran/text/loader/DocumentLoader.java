package com.drkiettran.text.loader;

import java.io.InputStream;

import com.drkiettran.text.model.Document;

public interface DocumentLoader {
	Document getPages(String fileName);
	Document getPages(InputStream is);

}