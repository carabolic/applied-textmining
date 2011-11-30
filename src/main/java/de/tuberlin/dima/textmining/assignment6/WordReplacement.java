package de.tuberlin.dima.textmining.assignment6;

import com.google.common.collect.Lists;
import rita.wordnet.RiWordnet;

import java.util.List;

public class WordReplacement {
	
	public Iterable<ShallowSentence> findWords(Iterable<ShallowSentence> sentences, int replaceInterval) {

    List<ShallowSentence> words = Lists.newArrayList();
		// TODO: implement a method, which replaces words in a sentence based 
		//on a synonym mapping to Wordnet

		return words;
	}
	
	/**
	 * Generates all synonyms for a given word
	 *
	 * @return array of synonyms  
	 */
	public String[] getSynonyms(String word){
		
		RiWordnet wordnet = new RiWordnet();
		String[] synonyms = wordnet.getAllSynonyms(word, "n", 15);
		
		return synonyms;		
	}

}
