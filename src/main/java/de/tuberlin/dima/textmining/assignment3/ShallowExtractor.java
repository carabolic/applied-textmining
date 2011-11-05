package de.tuberlin.dima.textmining.assignment3;

import java.util.Collection;
import com.google.common.collect.Lists;

public class ShallowExtractor {

	private static boolean DEBUG = false;

	private int currentPosition = 0;

	/**
	 * Find quotes.
	 * <br />
	 * Applied patterns:<br />
	 * <code>$TOKEN* "$TOKEN $TOKEN?" NNP? NNP</code>
	 * 
	 * @param sentences
	 *            the sentences
	 * @return the list
	 */
	public Iterable<SimpleRelation> findQuotes(Iterable<ShallowSentence> sentences) {
		Collection<SimpleRelation> quotes = Lists.newLinkedList();
		Collection<ShallowToken> quoteCandidate = Lists.newArrayList();
		Collection<ShallowToken> speakerCandidate = Lists.newArrayList();

		for (ShallowSentence sentence : sentences) {
			this.currentPosition = 0;
			int length = sentence.size();

			quoteCandidate = this.getNextQuote(sentence, 2);

			// Continue with next sentence if there is only one token left after the
			// quote candidate or if there is no quote candidate (collection of word
			// within " ")
			if (this.currentPosition >= length - 1 || quoteCandidate == null) {
				continue;
			}

			speakerCandidate = this.getNextNounChunk(sentence, 2);

			// Continue with next sentence if there is no speaker candidate (noun chunk)
			if (speakerCandidate == null) {
				continue;
			}

			String quote = ShallowExtractor.tokenCollectionToString(quoteCandidate);
			String speaker = ShallowExtractor.tokenCollectionToString(speakerCandidate);

			if (ShallowExtractor.DEBUG) System.out.println("Quote: " + quote + " " + speaker);
			quotes.add(new SimpleRelation(RelationType.QUOTE_SPEAKER, speaker, quote));
		}

		return quotes;
	}

	/**
	 * Find apposition.
	 * <br />
	 * Applied patterns:<br />
	 * <code>NNP NNP? , NNP NNP?</code>
	 * 
	 * @param sentences
	 *            the sentences
	 * @return the list
	 */
	public Iterable<SimpleRelation> findApposition(Iterable<ShallowSentence> sentences) {
		Collection<SimpleRelation> appositions = Lists.newArrayList();
		Collection<ShallowToken> entityCandidate = Lists.newArrayList();
		Collection<ShallowToken> appositionCandidate = Lists.newArrayList();
		boolean hasComma = false;

		for (ShallowSentence sentence : sentences) {
			this.currentPosition = 0;
			int length = sentence.size();

			entityCandidate = this.getNextNounChunk(sentence, 2);

			// Continue with next sentence if there is only one token left after the
			// entity candidate or if there is no entity candidate (noun chunk)
			if (this.currentPosition >= length - 1 || entityCandidate == null) {
				continue;
			}

			hasComma = this.checkNextTokenForComma(sentence);

			// Continue with next sentence if the token following a noun chunk is
			// not a comma
			if (!hasComma) {
				continue;
			}

			appositionCandidate = this.getNextNounChunk(sentence, 2);

			// Continue with next sentence if there is no apposition candidate (noun chunk)
			if (appositionCandidate == null) {
				continue;
			}

			String entity = ShallowExtractor.tokenCollectionToString(entityCandidate);
			String apposition = ShallowExtractor.tokenCollectionToString(appositionCandidate);

			if (ShallowExtractor.DEBUG) System.out.println("Apposition: " + entity + ", " + apposition);
			appositions.add(new SimpleRelation(RelationType.APPOSITION, entity, apposition));
		}		

		return appositions;
	}

	/**
	 * Checks whether the given ShallowToken contains a quotation mark or not.
	 * @param token
	 * @return
	 * 	True if the given token contains a quotation mark, false otherwise.
	 */
	private boolean containsQuotationMark(ShallowToken token) {
		String[] quotes = {"\""};
		String text = token.getText();

		for (String quote : quotes) {
			if (text.contains(quote)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Parses the given sentence for a phrase enclosed in quotation marks, using the current position.
	 * @param sentence
	 * 	The sentence to parse
	 * @param minSize
	 * 	Minimum length (number of tokens) of the quote
	 * @return
	 * 	The quote as an ordered list of ShallowToken.
	 */
	private Collection<ShallowToken> getNextQuote(ShallowSentence sentence, int minSize) {
		Collection<ShallowToken> quoteCandidate = Lists.newArrayList();
		ShallowToken token = null;
		int length = sentence.size();
		boolean openQuote = false;

		for (; this.currentPosition < length; this.currentPosition++) {
			token = sentence.get(this.currentPosition);

			// Current token contains first quotation mark
			if (!openQuote && this.containsQuotationMark(token)) {
				quoteCandidate.add(token);
				openQuote = true;
			}
			// All tokens after the opening quotation mark
			else if (openQuote && !this.containsQuotationMark(token)){
				quoteCandidate.add(token);
			}
			// Current token contains closing quotation mark
			else {
				quoteCandidate.add(token);
				this.currentPosition++;
				break;
			}
		}
		if (quoteCandidate.size() < minSize) {
			return null;
		}

		return quoteCandidate;
	}

	/**
	 * Parses the given sentence for a group of nouns, using the current position.
	 * @param sentence
	 * 	The sentence to parse.
	 * @param minSize
	 * 	Minimum size of the noun chunk (number of succeeding nouns).
	 * @return
	 * 	The noun chunk as on ordered list of ShallowTokens.
	 */
	private Collection<ShallowToken> getNextNounChunk(ShallowSentence sentence, int minSize) {
		Collection<ShallowToken> nounChunk = Lists.newArrayList();
		ShallowToken token = null;
		int length = sentence.size();

		for (; this.currentPosition < length; this.currentPosition++) {
			token = sentence.get(this.currentPosition);

			if (this.isNoun(token)) {
				nounChunk.add(token);
			}
			else {
				break;
			}
		}

		if (nounChunk.size() < minSize) {
			return null;
		}

		return nounChunk;
	}

	private boolean checkNextTokenForComma(ShallowSentence sentence) {
		return sentence.get(this.currentPosition++).getText().equals(",");
	}

	private boolean isNoun(ShallowToken token) {
		String[] nounTags = {"NN", "NNP", "NNPS", "NNS"};
		String tag = token.getTag();

		for (String nounTag : nounTags) {
			if (nounTag.equals(tag)) {
				return true;
			}
		}

		return false;
	}

	private static String tokenCollectionToString(Collection<ShallowToken> tokens) {
		StringBuilder stringBuilder = new StringBuilder();

		for (ShallowToken token : tokens) {
			stringBuilder.append(token.getText());
			stringBuilder.append(" ");
		}

		return stringBuilder.toString();
	}

}
