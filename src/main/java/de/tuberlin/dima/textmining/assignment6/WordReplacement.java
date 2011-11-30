package de.tuberlin.dima.textmining.assignment6;

import com.google.common.collect.Lists;
import rita.wordnet.RiWordnet;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class WordReplacement {

	private RiWordnet wordnet;
	private Random rand;

	public WordReplacement() {
		Locale.setDefault(Locale.ENGLISH);
		this.wordnet = new RiWordnet();
		this.rand = new Random();
	}

	public Iterable<ShallowSentence> findWords(Iterable<ShallowSentence> sentences, int replaceInterval) {

		List<ShallowSentence> words = Lists.newArrayList();
		int posInSentence = 0;
		boolean noSynonym = false;

		for (ShallowSentence sentence : sentences) {
			posInSentence = 0;
			noSynonym = false;

			for (ShallowToken token : sentence) {
				if (((posInSentence + 1) % replaceInterval == 0) || noSynonym) {
					String synonym = this.getRandSynonym(token);

					if (synonym != null) {
						token.setLemma(synonym);
						token.setText(synonym + "/syn ");
						noSynonym = false;
					}
					else {
						noSynonym = true;
					}
				}
				posInSentence++;
			}

			words.add(sentence);
		}

		return words;
	}

	/**
	 * Generates all synonyms for a given word
	 *
	 * @return array of synonyms  
	 */
	public String[] getSynonyms(String word){
		String[] synonyms = this.wordnet.getAllSynonyms(word, "n", 15);

		return synonyms;		
	}

	private String getRandSynonym(ShallowToken token) {
		String[] synonyms = null;
		int maxSyns = 15;

		if (WordReplacement.isNoun(token)) {
			synonyms = this.wordnet.getAllSynonyms(token.getLemma(), "n", maxSyns);
		}
		else if (WordReplacement.isVerb(token)) {
			synonyms = this.wordnet.getAllSynonyms(token.getLemma(), "v", maxSyns);
		}
		else if (WordReplacement.isAdjective(token)) {
			//synonyms = this.wordnet.getAllSynonyms(token.getLemma(), "a", maxSyns);
		}
		else if (WordReplacement.isAdverb(token)) {
			//synonyms = this.wordnet.getAllSynonyms(token.getLemma(), "r", maxSyns);
		}

		if (synonyms != null) {
			int len = synonyms.length;
			int index = this.rand.nextInt(len);
			return synonyms[index];
		}		

		return null;
	}

	public static boolean isVerb(ShallowToken token) {
		String[] tags = {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
		return WordReplacement.tokenContainsPosTag(token, tags);
	}

	public static boolean isNoun(ShallowToken token) {
		String[] tags = {"NN", "NNS", "NNP", "NNPS"};
		return WordReplacement.tokenContainsPosTag(token, tags);
	}

	public static boolean isAdjective(ShallowToken token) {
		String[] tags = {"JJ", "JJR", "JJS"};
		return WordReplacement.tokenContainsPosTag(token, tags);
	}

	public static boolean isAdverb(ShallowToken token) {
		String[] tags = {"RB", "RBR", "RBS", "WRB"};
		return WordReplacement.tokenContainsPosTag(token, tags);
	}

	public static boolean tokenContainsPosTag(ShallowToken token, String[] tags) {
		String tag = token.getTag();

		for (String t : tags) {
			if (t.equals(tag)) {
				return true;
			}
		}

		return false;
	}

}
