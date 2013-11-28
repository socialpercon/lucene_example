package lia.recent;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/** Simple command-line based search demo. */
public class CopyOfSearch {

  private CopyOfSearch() {}

  /** Simple command-line based search demo. */
  public static void main(String[] args) throws Exception {

	  System.out.println("Hello");
	  KoreanAnalyzer ka = new KoreanAnalyzer();
	  TokenStream ts = ka.tokenStream("", new java.io.StringReader("과학기술이 정말 I an Hello"));
	  System.out.println(ts.toString());
	  try{
	  while (ts.incrementToken()){
	  org.apache.lucene.analysis.tokenattributes.TermAttribute ta = ts.getAttribute( org.apache.lucene.analysis.tokenattributes.TermAttribute.class);
	  System.out.println("adf"+ta.term());
	  }
	  }catch (Exception e){System.out.println(e.toString());}


	  }
  }
}
