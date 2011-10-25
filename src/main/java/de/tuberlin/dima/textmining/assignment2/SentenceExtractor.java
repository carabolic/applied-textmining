package de.tuberlin.dima.textmining.assignment2;

import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SentenceExtractor {
	
	private static final TokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;
	private static final SentenceModel SENTENCE_MODEL = new MedlineSentenceModel();
	private List<String> tokenList;
	private List<String> whiteList;
	private Tokenizer tokenizer;
	private String[] tokens;
	private String[] whites;
	private int[] sentenceBoundaries;
	
	public SentenceExtractor() {
		tokenList = new ArrayList<String>();
		whiteList = new ArrayList<String>();	
	}
	
	/**
	 * 
	 * @param documentText
	 * 			the document text
	 */
	public void generateSetences(String documentText) {
		tokenizer = TOKENIZER_FACTORY.tokenizer(documentText.toCharArray(),0,documentText.length());
		tokenizer.tokenize(tokenList,whiteList);

		tokens = new String[tokenList.size()];
		whites = new String[whiteList.size()];
		tokenList.toArray(tokens);
		whiteList.toArray(whites);
		sentenceBoundaries = SENTENCE_MODEL.boundaryIndices(tokens,whites);
	}
	
	/**
	 * 
	 * @param number
	 * 			number of the sentence e.g. 0 extracts the first sentence
	 * @return sentence from the text
	 */
	public String extractSentence(Integer number) {
			
		if (sentenceBoundaries.length < 1 || number < 0 || number > sentenceBoundaries.length - 1) {
		    return "";
		}
		
		int sentStartTok;
		int sentEndTok;
		String sentence = "";
		   
		if(number == 0) 
			sentStartTok = 0;
		else 
			sentStartTok = sentenceBoundaries[number - 1] + 1;
		sentEndTok = sentenceBoundaries[number];
	    
	    //generate sentence
	    for (int j=sentStartTok; j<=sentEndTok; j++) {
	    	sentence += tokens[j]+whites[j+1];
	    }
	   
	    return sentence.trim();
		
	}
	
	/**
	 * 
	 * @return number of sentence for the given text
	 */
	public Integer getNumberOfSentences () {
		return sentenceBoundaries.length;
	}
	
	
	public List<String> getSentences(){
		
		List<String> sentenceList = new ArrayList<String>();
		for(int i = 0; i < sentenceBoundaries.length; i++){
			String s = extractSentence(i);
			s = s.replaceAll("\n", "");
			if(!s.equals("")){
				sentenceList.add(s);
			}
		}
		
		return sentenceList; 
		
	}


     /**
     * Splits an incoming string into sentences, returns a collection of lists of strings
     *
     * @param corpus
     * @return
     */
    public Collection<List<String>> getSentenceCollection(String corpus){

      // extract sentences from text
      SentenceExtractor extractor= new SentenceExtractor();
      extractor.generateSetences(corpus);

      // assemble return collection
      Collection<List<String>> returnCollection = new ArrayList<List<String>>();
      for(int i = 0; i < extractor.getNumberOfSentences(); i++)
          returnCollection.add(Arrays.asList(extractor.extractSentence(i).split(" ")));

      return returnCollection;
    }



}
