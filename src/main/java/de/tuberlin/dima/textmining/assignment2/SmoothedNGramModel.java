package de.tuberlin.dima.textmining.assignment2;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

public class SmoothedNGramModel implements LanguageModel {

	private LanguageModel unigramModel = new UnigramModel();
	private LanguageModel bigramModel = new NGramModel();
	private double lambda = 0.5;

	@Override
	public void train(Collection<List<String>> corpus) {
		System.out.println("Training unigram model");
		this.unigramModel.train(corpus);
		System.out.println("Training bigram model");
		this.bigramModel.train(corpus);
	}

	@Override
	public double getWordProbability(List<String> sentence, int index) {
		// Get the probability of a word within a sentence using linear
		// interpolation
		Preconditions.checkArgument(index > -1, "Sentence index has to larger or equal 0");

		double bigramProb = bigramModel.getWordProbability(sentence, index);
		double unigramProb = unigramModel.getWordProbability(sentence, index);
		double probability = lambda * bigramProb + (1 - lambda) * unigramProb;

		return probability;
	}

	@Override
	public double sentenceLogProbability(List<String> sentence) {
		List<String> stoppedSentence = Lists.newArrayList(sentence);
		stoppedSentence.add(0, SENTENCE_START_TAG);
		stoppedSentence.add(SENTENCE_END_TAG);

		double probability = 0.0;

		for (int i = 0; i < stoppedSentence.size(); i++) {
			probability += Math.log(getWordProbability(stoppedSentence, i));
			Preconditions.checkArgument(!Double.isInfinite(probability));
		}
		return probability;
	}

	@Override
	public Iterable<String> generateSentence() {
		List<String> sentence = Lists.newArrayList();
		NGramModel model = (NGramModel) this.bigramModel;
		String word = model.generateSuffix(SENTENCE_START_TAG);
		while (!word.equals(SENTENCE_END_TAG)) {
			sentence.add(word);
			word = model.generateSuffix(word);
		}

		return sentence;
	}

}
