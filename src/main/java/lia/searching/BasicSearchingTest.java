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

import lia.common.TestUtil;

import org.apache.lucene.analysis.core.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;

import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
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
    System.out.println(docs.totalHits);
    assertEquals("JUnit in Action, Second Edition",
                 1, docs.totalHits);

    dirr.close();
    dir.close();
  }

  public void testQueryParser() throws Exception {
	  Directory dir = FSDirectory.open(new File("./index"));
    DirectoryReader dirr = DirectoryReader.open(dir);
    IndexSearcher searcher = new IndexSearcher(dirr);

    QueryParser parser = new QueryParser(Version.LUCENE_44,      //A
                                         "contents",                  //A
                                         new SimpleAnalyzer(Version.LUCENE_44));       //A

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
