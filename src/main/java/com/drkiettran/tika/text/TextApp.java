package com.drkiettran.tika.text;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class TextApp {

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
}
