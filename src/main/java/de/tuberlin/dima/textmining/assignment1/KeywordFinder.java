package de.tuberlin.dima.textmining.assignment1;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;


public class KeywordFinder {
	
	// Custom stop word list copied from
	// http://armandbrahaj.blog.al/2009/04/14/list-of-english-stop-words/
	// and added "ext" and "int"
	private final static String STOP_WORDS_FILE = "assignment1/stop-words.txt";
	private final static Version LUCENE_VERSION = Version.LUCENE_33;

  public List<String> keywords(String text, int howMany) {
    List<String> keywords = Lists.newArrayList();

    // Get the custom stop word list
    File stopwords = new File(Resources.getResource(STOP_WORDS_FILE).getPath());

    try {
    	// Use lucene standard analyzer in order to tokenize and filter the whole text
    	StandardAnalyzer stdAnal = new StandardAnalyzer(LUCENE_VERSION, stopwords);
    	Map<String,Integer> termCounts = new HashMap<String,Integer>();
    	TokenStream stream = stdAnal.tokenStream(null, new StringReader(text));
    	CharTermAttribute attr = stream.addAttribute(CharTermAttribute.class);

    	while (stream.incrementToken()) {
    		if (attr.length() > 0) {
    			String term = new String(attr.buffer(), 0, attr.length());
    			int count = termCounts.containsKey(term) ? termCounts.get(term) : 0;
    			termCounts.put(term, ++count);
    		}
    	}

    	// Copy word, frequency mapping into a TreeMap and sort it based on the values
    	TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(new ValueComparator(termCounts));
    	sortedMap.putAll(termCounts);

    	// Reverse the order of the TreeMap to get the words with the highest frequency
    	NavigableMap<String, Integer> descSortedMap = sortedMap.descendingMap();
    	Iterator<String> wordsIter = descSortedMap.keySet().iterator();
    	int i = 0;

    	while (wordsIter.hasNext() && i < howMany) {
    		keywords.add(wordsIter.next());
    		i++;
    	}

    } catch (IOException e) {
    	e.printStackTrace();
    	System.out.println("Could not open file " + STOP_WORDS_FILE);
    }

    return keywords;
  }
  
  /**
   * Custom Comparator in order to sort a tree based on its values.
   * 
   * @author Christoph Bruecke <christoph.bruecke@campus.tu-berlin.de>
   *
   */
  static class ValueComparator implements Comparator<String> {

	  private Map<String, Integer> origin = null;

	  public ValueComparator(Map<String, Integer> origin) {
		  super();
		  this.origin = origin;
	  }

	  @Override
	  public int compare(String o1, String o2) {
		  Integer value1 = origin.get(o1);
		  Integer value2 = origin.get(o2);
		  
		  return value1.compareTo(value2);
	  }

  }
}
