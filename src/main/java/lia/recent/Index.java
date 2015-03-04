package lia.recent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Index {
  
  private Index() {}

  /** Index all text files under a directory. */
  public static void main(String[] args) {
    String indexPath = "index";
    String docsPath = "data/technology/computers/programming/tia.properties";
    
    final File file = new File(docsPath);
    try {
      System.out.println("Indexing to directory '" + indexPath + "'...");
      FileInputStream fis;
      try {
        fis = new FileInputStream(file);
      
      } catch (FileNotFoundException fnfe) {
        // at least on windows, some temporary files raise this exception with an "access denied" message
        // checking if the file can be read doesn't help
    	  System.err.println("파일이 존재하지 않습니다.");
    	  return;
      }
      Document doc = new Document();
      Field pathField = new StringField("path", file.getPath(), Field.Store.YES);
      doc.add(pathField);
      doc.add(new LongField("modified", file.lastModified(), Field.Store.NO));
      doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(fis, "UTF-8"))));
      
      Directory dir = FSDirectory.open(new File(indexPath));
      
      Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
      IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_45, analyzer);
      IndexWriter writer = new IndexWriter(dir, iwc);
      System.out.println("adding " + file);
      writer.addDocument(doc);
      writer.close();
    } catch (IOException e) {
      System.err.println(" caught a " + e.getClass() +
       "\n with message: " + e.getMessage());
    }
  }
  
}
