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

import junit.framework.TestCase;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

// From chapter 3
public class BasicSearchingTest extends TestCase {

  public void testTerm() throws Exception {
	  Directory dir = FSDirectory.open(new File("./index")); //A
    DirectoryReader dirr = DirectoryReader.open(dir);
    IndexSearcher searcher = new IndexSearcher(dirr);  //B

    Term t = new Term("subject", "ant");
    Query query = new TermQuery(t);
    TopDocs docs = searcher.search(query, 10);
    assertEquals("Ant in Action",                //C
                 1, docs.totalHits);                         //C

    t = new Term("subject", "junit");
    docs = searcher.search(new TermQuery(t), 10);
    assertEquals("Ant in Action, " +                                 //D
                 "JUnit in Action, Second Edition",                  //D
                 2, docs.totalHits);                                 //D

    dirr.close();
    dir.close();
  }

  /*
    #A Obtain directory from TestUtil
    #B Create IndexSearcher
    #C Confirm one hit for "ant"
    #D Confirm two hits for "junit"
  */

  public void testKeyword() throws Exception {
	 Directory dir = FSDirectory.open(new File("./index"));
    //Directory dir = TestUtil.getBookIndexDirectory();
    DirectoryReader dirr = DirectoryReader.open(dir);
    
    IndexSearcher searcher = new IndexSearcher(dirr);
    Term t = new Term("isbn", "9781935182023");
    Query query = new TermQuery(t);
    TopDocs docs = searcher.search(query, 10);
    System.out.println("TermQuery: " + docs.totalHits);
    assertEquals("JUnit in Action, Second Edition",
                 1, docs.totalHits);

    dirr.close();
    dir.close();
  }

  public void testWildQuery() throws Exception {
	 Directory dir = FSDirectory.open(new File("./index"));
    //Directory dir = TestUtil.getBookIndexDirectory();
    DirectoryReader dirr = DirectoryReader.open(dir);
    
    IndexSearcher searcher = new IndexSearcher(dirr);
    Term t = new Term("isbn", "9781*");
    Query query = new WildcardQuery(t);
    TopDocs docs = searcher.search(query, 10);
    System.out.println("WildcardQuery: " + docs.totalHits);
    assertEquals("JUnit in Action, Second Edition",
                 2, docs.totalHits);

    dirr.close();
    dir.close();
  }
  
  public void testPrefixQuery() throws Exception {
	 Directory dir = FSDirectory.open(new File("./index"));
    //Directory dir = TestUtil.getBookIndexDirectory();
    DirectoryReader dirr = DirectoryReader.open(dir);
    
    IndexSearcher searcher = new IndexSearcher(dirr);
    Term t = new Term("isbn", "9781");
    Query query = new PrefixQuery(t);
    TopDocs docs = searcher.search(query, 10);
    Document doc = searcher.doc(docs.scoreDocs[0].doc);
    System.out.println("PrefixQuery: " + docs.totalHits);
    System.out.println(doc.get("title"));
    assertEquals("JUnit in Action, Second Edition",
                 2, docs.totalHits);

    dirr.close();
    dir.close();
  }
  
  public void testPhraseQuery() throws Exception {
	 Directory dir = FSDirectory.open(new File("./index"));
    //Directory dir = TestUtil.getBookIndexDirectory();
    DirectoryReader dirr = DirectoryReader.open(dir);
    
    IndexSearcher searcher = new IndexSearcher(dirr);
    
    PhraseQuery query = new PhraseQuery();
    query.setSlop(2);
    query.add(new Term("title", "junit"));
    query.add(new Term("title", "action"));
    TopDocs docs = searcher.search(query, 10);
    Document doc = searcher.doc(docs.scoreDocs[0].doc);
    System.out.println("PhraseQuery: " + docs.totalHits);
    System.out.println(doc.get("title"));
    assertEquals("JUnit in Action, Second Edition",
                 1, docs.totalHits);

    dirr.close();
    dir.close();
  }
  
  public void testMultiPhraseQuery() throws Exception {
	 Directory dir = FSDirectory.open(new File("./index"));
    //Directory dir = TestUtil.getBookIndexDirectory();
    DirectoryReader dirr = DirectoryReader.open(dir);
    
    IndexSearcher searcher = new IndexSearcher(dirr);
    
    MultiPhraseQuery query = new MultiPhraseQuery();
    query.setSlop(2);
    query.add(new Term[] {new Term("title", "junit"), new Term("title", "action")});
    TopDocs docs = searcher.search(query, 10);
    Document doc = searcher.doc(docs.scoreDocs[0].doc);
    System.out.println("MultiPhraseQuery: " + docs.totalHits);
    System.out.println(doc.get("title"));
    assertEquals("JUnit in Action, Second Edition",
                 4, docs.totalHits);

    dirr.close();
    dir.close();
  }

  public void testFuzzyQuery() throws Exception {
	 Directory dir = FSDirectory.open(new File("./index"));
    //Directory dir = TestUtil.getBookIndexDirectory();
    DirectoryReader dirr = DirectoryReader.open(dir);
    
    IndexSearcher searcher = new IndexSearcher(dirr);
    
    FuzzyQuery query = new FuzzyQuery(new Term("title", "jnit"));
    
    TopDocs docs = searcher.search(query, 10);
    Document doc = searcher.doc(docs.scoreDocs[0].doc);
    System.out.println("FuzzyQuery: " + docs.totalHits);
    System.out.println(doc.get("title"));
    assertEquals("JUnit in Action, Second Edition",
                 2, docs.totalHits);

    dirr.close();
    dir.close();
  }  
  
  public void testRegexpQuery() throws Exception {
	 Directory dir = FSDirectory.open(new File("./index"));
    //Directory dir = TestUtil.getBookIndexDirectory();
    DirectoryReader dirr = DirectoryReader.open(dir);
    
    IndexSearcher searcher = new IndexSearcher(dirr);
    
    RegexpQuery query = new RegexpQuery(new Term("isbn", "[0-9]+"));
    
    TopDocs docs = searcher.search(query, 10);
    Document doc = searcher.doc(docs.scoreDocs[0].doc);
    System.out.println("RegexpQuery: " + docs.totalHits);
    System.out.println(doc.get("title"));
    assertEquals("JUnit in Action, Second Edition",
                 11, docs.totalHits);

    dirr.close();
    dir.close();
  }  
 
  
  public void testTermRangeQuery() throws Exception {
	 Directory dir = FSDirectory.open(new File("./index"));
    //Directory dir = TestUtil.getBookIndexDirectory();
    DirectoryReader dirr = DirectoryReader.open(dir);
    
    IndexSearcher searcher = new IndexSearcher(dirr);
    
    Query query = TermRangeQuery.newStringRange("title", "h", "k", false, false);
    
    TopDocs docs = searcher.search(query, 10);
    Document doc = searcher.doc(docs.scoreDocs[0].doc);
    System.out.println("TermRangeQuery: " + docs.totalHits);
    System.out.println(doc.get("title"));
    assertEquals("JUnit in Action, Second Edition",
                 4, docs.totalHits);

    dirr.close();
    dir.close();
  }  
  
  public void testNumericRangeQuery() throws Exception {
	 Directory dir = FSDirectory.open(new File("./index"));
    //Directory dir = TestUtil.getBookIndexDirectory();
    DirectoryReader dirr = DirectoryReader.open(dir);
    
    IndexSearcher searcher = new IndexSearcher(dirr);
    
    Query query = NumericRangeQuery.newIntRange("pubmonth", 201001, 201012, false, false);
    
    TopDocs docs = searcher.search(query, 10);
    Document doc = searcher.doc(docs.scoreDocs[0].doc);
    System.out.println("NumericRangeQuery: " + docs.totalHits);
    System.out.println(doc.get("title"));
    assertEquals("JUnit in Action, Second Edition",
                 2, docs.totalHits);

    dirr.close();
    dir.close();
  }  
  
  public void testConstantScoreQuery() throws Exception {
	 Directory dir = FSDirectory.open(new File("./index"));
    //Directory dir = TestUtil.getBookIndexDirectory();
    DirectoryReader dirr = DirectoryReader.open(dir);
    
    IndexSearcher searcher = new IndexSearcher(dirr);

    Query query = new ConstantScoreQuery(new TermQuery(new Term("title", "junit")));
    query.setBoost(50.0f);
    Query query2 = new ConstantScoreQuery(query);
        
    TopDocs docs = searcher.search(query2, 10);
    Document doc = searcher.doc(docs.scoreDocs[0].doc);
    System.out.println("ConstantScoreQuery: " + docs.getMaxScore());
    System.out.println(doc.get("title"));
    assertEquals("JUnit in Action, Second Edition",
                 2, docs.totalHits);

    dirr.close();
    dir.close();
  }  
  
  
  public void testQueryParser() throws Exception {
	  Directory dir = FSDirectory.open(new File("./index"));
    DirectoryReader dirr = DirectoryReader.open(dir);
    IndexSearcher searcher = new IndexSearcher(dirr);

    QueryParser parser = new QueryParser(Version.LUCENE_46,      //A
                                         "contents",                  //A
                                         new SimpleAnalyzer(Version.LUCENE_46));       //A

    Query query = parser.parse("+JUNIT +ANT -MOCK");                  //B
    TopDocs docs = searcher.search(query, 10);
    assertEquals(1, docs.totalHits);
    Document d = searcher.doc(docs.scoreDocs[0].doc);
    assertEquals("Ant in Action", d.get("title"));

    query = parser.parse("mock OR junit");                            //B
    docs = searcher.search(query, 10);
    assertEquals("Ant in Action, " + 
                 "JUnit in Action, Second Edition",
                 2, docs.totalHits);

    dirr.close();
    dir.close();
  }
  /*
#A Create QueryParser
#B Parse user's text
  */
}
