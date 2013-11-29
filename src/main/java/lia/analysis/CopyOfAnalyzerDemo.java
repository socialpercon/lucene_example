package lia.analysis;

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

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.Version;

// From chapter 4

/**
 * Adapted from code which first appeared in a java.net article
 * written by Erik
 */
public class CopyOfAnalyzerDemo {
  private static final String[] examples = {

    "Make the best for smart business"
  };
  private static Analyzer[] analyzers;

  
  public static void main(String[] args) throws IOException {
	  analyzers = new Analyzer[] { 
			    new WhitespaceAnalyzer(Version.LUCENE_46),
			    new SimpleAnalyzer(Version.LUCENE_46),
			    new StopAnalyzer(Version.LUCENE_46),
			    new StandardAnalyzer(Version.LUCENE_46),
			    new KeywordAnalyzer(),
			    new KoreanAnalyzer(Version.LUCENE_46)
	  };	    

    String[] strings = examples;

    for (String text : strings) {
      analyze(text);
    }
  }

  private static void analyze(String text) throws IOException {
    System.out.println("Analyzing \"" + text + "\"");
    for (Analyzer analyzer : analyzers) {
    	String name = analyzer.getClass().getSimpleName();
    	 System.out.println(name);
        TokenStream stream = analyzer.tokenStream("dummy", text);
        stream.reset();

        CharTermAttribute termAttr = stream.addAttribute(CharTermAttribute.class);
        OffsetAttribute offsetAttr = stream.addAttribute(OffsetAttribute.class);
        TypeAttribute typeAttr = stream.addAttribute(TypeAttribute.class);
        PositionIncrementAttribute positionAttr = stream.addAttribute(PositionIncrementAttribute.class);

    	    while (stream.incrementToken()) {

    	      System.out.print(
    	          "[" + termAttr + "] "
    	      );
    	    }
    	 System.out.println("");
    }
  }
}

// #A Analyze command-line strings, if specified
// #B Real work done in here
