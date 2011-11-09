package de.tuberlin.dima.textmining.assignment4;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.tuberlin.dima.textmining.assignment3.ShallowSentence;
import de.tuberlin.dima.textmining.assignment3.SimpleRelation;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.StringUtils;

public class OpenInformationExtractor {

	public List<SimpleRelation> findGenericRelations(String text) {

		List<SimpleRelation> foundRelations = new ArrayList<SimpleRelation>();

		// TODO: implement open information extraction method here or in other
		// method

		return foundRelations;

	}

	private void tagNamedEntities(String input) {

		// TODO: if you need it, use the Named Entity recognizer
		AbstractSequenceClassifier<Annotation> classifier = CRFClassifier
				.getClassifierNoExceptions("assignment4/all.3class.distsim.crf.ser.gz");

	}

	public List<SimpleRelation> findGenericRelations(
			Vector<ShallowSentence> sentences) {

		List<SimpleRelation> foundRelations = new ArrayList<SimpleRelation>();

		// TODO: implement open information extraction method here or in other
		// method

		return foundRelations;
	}

}
