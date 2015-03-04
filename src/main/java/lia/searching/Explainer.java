package lia.searching;

/**
 * Copyright Manning Publications Co.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific lan      
*/

import java.io.File;
import java.util.Arrays;
import junit.framework.TestCase;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

// From chapter 3
public class Explainer {
  public static void main(String[] args) throws Exception {
/*    if (args.length != 2) {
      System.err.println("Usage: Explainer <index dir> <query>");
      System.exit(1);
    }

    String indexDir = args[0];
    String queryExpression = args[1];
*/
    RAMDirectory dir = new RAMDirectory();
    IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(
      Version.LUCENE_46, new StandardAnalyzer(Version.LUCENE_46)));

    char[] chars = new char[IndexWriter.MAX_TERM_LENGTH];
    Arrays.fill(chars, 'x');
    Document doc = new Document();
    final String bigTerm = new String(chars);

    // This produces a too-long term:
    String contents = "abc xyz x" + bigTerm + " another term";
    doc.add(new TextField("content", contents, Field.Store.NO));
    writer.addDocument(doc);

    // Make sure we can add another normal document
    doc = new Document();
    doc.add(new TextField("content", "abc bbb ccc", Field.Store.YES));
    writer.addDocument(doc);
    writer.close();

	IndexReader reader = IndexReader.open(dir);

    // Make sure all terms < max size were indexed
	
    System.out.println(reader.docFreq(new Term("content", "abc")));
    System.out.println(reader.docFreq(new Term("content", "bbb")));
    System.out.println(reader.docFreq(new Term("content", "term")));
    System.out.println(reader.docFreq(new Term("content", "another")));

    // Make sure position is still incremented when
    // massive term is skipped:
    DocsAndPositionsEnum tps = MultiFields.getTermPositionsEnum(reader,
                                                                MultiFields.getLiveDocs(reader),
                                                                "content",
                                                                new BytesRef("another"));
    System.out.println(tps.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
    System.out.println(tps.freq());
    System.out.println(tps.nextPosition());

    // Make sure the doc that has the massive term is in
    // the index:
    System.out.println("document with wicked long term should is not in the index!"+reader.numDocs());

    reader.close();

    // Make sure we can add a document with exactly the
    // maximum length term, and search on that term:
    doc = new Document();
    doc.add(new TextField("content", bigTerm, Field.Store.YES));
    ClassicAnalyzer sa = new ClassicAnalyzer(Version.LUCENE_46);
    sa.setMaxTokenLength(100000);
    writer  = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_46, sa));
    writer.addDocument(doc);
    writer.close();
    reader = IndexReader.open(dir);
    System.out.println(reader.docFreq(new Term("content", bigTerm)));

    QueryParser parser = new QueryParser(Version.LUCENE_46,
                                         "content", new StandardAnalyzer(Version.LUCENE_46));
    String queryExpression = "abc";
    
    Query query = parser.parse(queryExpression);

    System.out.println("Query: " + queryExpression);

    IndexSearcher searcher = new IndexSearcher(reader);
    TopDocs topDocs = searcher.search(query, 10);

    System.out.println(topDocs.totalHits);
    
    for (ScoreDoc match : topDocs.scoreDocs) {
      Explanation explanation
         = searcher.explain(query, match.doc);     //#A
      System.out.println(match.doc);
      System.out.println("----------");
      Document doc2 = searcher.doc(match.doc);
      System.out.println(doc2.get("content"));
      System.out.println(explanation.toString());  //#B
    }
    
    reader.close();
  }
}
/*
#A Generate Explanation
#B Output Explanation
*/
