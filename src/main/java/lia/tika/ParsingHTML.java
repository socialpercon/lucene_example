package lia.tika;

import java.io.InputStream;
import java.net.URL;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.xml.sax.ContentHandler;


public class ParsingHTML {
	
    public static void main (String args[]) throws Exception {
        URL url = new URL("http://www.aladin.co.kr/search/wsearchresult.aspx?SearchTarget=All&SearchWord=%BF%A4%B8%AE%B8%E0%C6%B2+%B5%F0%C0%DA%C0%CE+%C6%D0%C5%CF+Elemental+Design+Patterns+&x=20&y=21");
        InputStream input = url.openStream();
        LinkContentHandler linkHandler = new LinkContentHandler();
        ContentHandler textHandler = new BodyContentHandler();
        ToHTMLContentHandler toHTMLHandler = new ToHTMLContentHandler();
        TeeContentHandler teeHandler = new TeeContentHandler(linkHandler, textHandler, toHTMLHandler);
        Metadata metadata = new Metadata();
        HtmlParser parser = new HtmlParser();
        parser.parse(input, teeHandler, metadata);
        System.out.println("title:\n" + metadata.get("title"));
        System.out.println("links:\n" + linkHandler.getLinks());
        System.out.println("text:\n" + textHandler.toString());
        System.out.println("html:\n" + toHTMLHandler.toString());
    }

}
