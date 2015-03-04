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

import java.io.File;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/** Simple command-line based search demo. */
public class SearchWithQueryParser {

  private SearchWithQueryParser() {}

  /** Simple command-line based search demo. */
  public static void main(String[] args) throws Exception {
		String index = "index";
		String field = "contents";
		String queries = "Tapestry";
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
		
		QueryParser parser = new QueryParser(Version.LUCENE_45, field, analyzer);
		Query query = parser.parse(queries);
		System.out.println("Searching for: " + query.toString(field));
		Date start = new Date();
		TopDocs results = searcher.search(query, 100);
		Date end = new Date();
		System.out.println(results.totalHits);
		System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
		reader.close();
  }
}