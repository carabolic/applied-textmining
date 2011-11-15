package de.tuberlin.dima.textmining.assignment4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import de.tuberlin.dima.textmining.assignment3.ShallowSentence;
import de.tuberlin.dima.textmining.assignment3.ShallowToken;
import de.tuberlin.dima.textmining.assignment3.SimpleRelation;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.ling.CoreLabel;

public class OpenInformationExtractor {
	
	private static final boolean DEBUG = false;
	
	private AbstractSequenceClassifier<CoreLabel> classifier = null;
	private int currentPosition = 0;
	
	@SuppressWarnings("unchecked")
	public OpenInformationExtractor() {
		this.classifier = CRFClassifier.getClassifierNoExceptions("assignment4/all.3class.distsim.crf.ser.gz");
	}

	/**
	 * Parses the given sentence in order to find named entities.
	 * @param sentence
	 * 	The sentence to be parsed.
	 * @return
	 * 	<code><b>true</b></code> if the sentence contains at least one named entity, or <code><b>false</b></code> other wise.
	 */
	private boolean tagNamedEntities(ShallowSentence sentence) {
		boolean hasNer = false;
		List<CoreLabel> annotations = this.classifier.classifySentence(sentence);
		Iterator<CoreLabel> annonIter = annotations.iterator();
		Iterator<ShallowToken> tokenIter = sentence.iterator();

		while (annonIter.hasNext() && tokenIter.hasNext()) {
			CoreLabel label = annonIter.next();
			ShallowToken token = tokenIter.next();
			String ner = label.get(AnswerAnnotation.class);
			if (ner != null && !ner.equals("O")) {
				token.setNer(ner);
				hasNer = true;
			}
		}

		return hasNer;
	}

	/**
	 * Parses the given sentence in order to find a generic relation following the pattern
	 * <code>NamedEntity VerbPhrase NamedEntity</code>.
	 * @param sentences
	 * 	The sentence to be parsed.
	 * @return
	 * 	The found relations.
	 */
	public List<SimpleRelation> findGenericRelations(Vector<ShallowSentence> sentences) {

		List<SimpleRelation> foundRelations = new ArrayList<SimpleRelation>();

		for (ShallowSentence sentence : sentences) {
			this.currentPosition = 0;
			boolean containsNamedEntity = this.tagNamedEntities(sentence);
			if (containsNamedEntity) {
				// Searches for relations following the pattern NE VERB NE
				Collection<ShallowToken> namedEntity1 = this.getNextNER(sentence, true, 1);
				if (namedEntity1 == null) {
					continue;
				}
				Collection<ShallowToken> verb = this.getNextVerb(sentence, false, 1);
				if (verb == null) {
					continue;
				}
				Collection<ShallowToken> namedEntity2 = this.getNextNER(sentence, false, 1);
				if (namedEntity2 == null) {
					continue;
				}

				String ne1 = OpenInformationExtractor.tokenCollectionToString(namedEntity1);
				String v = OpenInformationExtractor.tokenCollectionToString(verb);
				String ne2 = OpenInformationExtractor.tokenCollectionToString(namedEntity2);

				if (OpenInformationExtractor.DEBUG) System.out.println(ne1 + " " + v + " " + ne2);
				foundRelations.add(new SimpleRelation(v, ne1, ne2));
			}
		}		

		return foundRelations;
	}

	/**
	 * Parses the given sentence from the current position onwards in order to find a named entity.
	 * @param sentence
	 * 	The sentence to parse.
	 * @param search
	 * 	Indicates whether only the next word should be considered or the whole remainder of the sentence should be parsed.
	 * @param minSize
	 * 	Minimum size of the returned named entity
	 * @return
	 * 	The named entity.
	 */
	private Collection<ShallowToken> getNextNER(ShallowSentence sentence, boolean search, int minSize) {
		Collection<ShallowToken> namedEntity = new ArrayList<ShallowToken>();
		ShallowToken token = null;
		boolean lastWasNE = false;
		int length = sentence.size();

		for (; this.currentPosition < length; this.currentPosition++) {
			token = sentence.get(this.currentPosition);

			if (token.ner() != null) {
				namedEntity.add(token);
				lastWasNE = true;
			}
			else if (!search || lastWasNE) {
				break;
			}
		}

		if (namedEntity.size() < minSize) {
			return null;
		}

		return namedEntity;
	}

	/**
	 * Parses the given sentence from the current position onwards in order to find a verb.
	 * @param sentence
	 * 	The sentence to parse.
	 * @param search
	 * 	Indicates whether only the next word should be considered or the whole remainder of the sentence should be parsed.
	 * @param minSize
	 * 	Minimum size of the returned verb phrase/
	 * @return
	 * 	The verb phrase.
	 */
	private Collection<ShallowToken> getNextVerb(ShallowSentence sentence, boolean search, int minSize) {
		Collection<ShallowToken> verb = new ArrayList<ShallowToken>();
		ShallowToken token = null;
		boolean lastWasVerb = false;
		int length = sentence.size();

		for (; this.currentPosition < length; this.currentPosition++) {
			token = sentence.get(this.currentPosition);

			if (this.isVerb(token)) {
				verb.add(token);
				lastWasVerb = true;
			}
			else if (!search || lastWasVerb) {
				break;
			}
		}

		if (verb.size() < minSize) {
			return null;
		}

		return verb;
	}

	private boolean isVerb(ShallowToken token) {
		String[] verbTags = {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
		String tag = token.getTag();

		if (token.getText().equals("(")) {
			return false;
		}

		for (String verbTag : verbTags) {
			if (tag.equals(verbTag)) {
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