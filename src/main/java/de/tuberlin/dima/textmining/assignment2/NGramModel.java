package de.tuberlin.dima.textmining.assignment2;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

public class NGramModel implements LanguageModel {

	BigramCounter bigramCounter = new BigramCounter();

	@Override
	public void train(Collection<List<String>> corpus) {
		for (List<String> sentence : corpus) {
			// annotate sentence start and end with <s> or </s>
			List<String> stoppedSentence = Lists.newArrayList(sentence);
			stoppedSentence.add(0, SENTENCE_START_TAG);
			stoppedSentence.add(SENTENCE_END_TAG);

			// Count bigrams
			int length = stoppedSentence.size();

			if (length < 2) {
				bigramCounter.incrementCount(stoppedSentence.get(0));
				continue;
			}

			for (int i = 0; i < length - 1; i++) {
				bigramCounter.incrementCount(stoppedSentence.get(i), stoppedSentence.get(i + 1));
			}
		}

		System.out.println("[NGramModel.train()]: Model trained.");
	}

	@Override
	public double getWordProbability(List<String> sentence, int index) {
		// Get the probability of a word within a sentence using additive smoothing
		Preconditions.checkArgument(index > -1, "Sentence index has to larger or equal 0");
		double probability = 0.0;

		if (index == 0) {
			return 1.0;
		} else {
			// P(word|word-1) = P(word UNION word-1) / P(word-1) + 1
			String word = sentence.get(index - 1);
			probability = this.bigramCounter.getCount(word, sentence.get(index));
			if (probability == 0) {
				probability = 1;
			}
			probability /= this.bigramCounter.getCount(word) + 1;
		}

		return probability;
	}

	@Override
	public double sentenceLogProbability(List<String> sentence) {
		// annotate sentence start and end with <s> or </s>
		List<String> stoppedSentence = Lists.newArrayList(sentence);
		stoppedSentence.add(0, SENTENCE_START_TAG);
		stoppedSentence.add(SENTENCE_END_TAG);

		// Copy/ Paste ... no time waste ;D
		double probability = 0.0;

		for (int i = 0; i < stoppedSentence.size(); i++) {
			probability += Math.log(getWordProbability(stoppedSentence, i));
			Preconditions.checkArgument(!Double.isInfinite(probability), "MSG: word:" + stoppedSentence.get(i) + " - "
					+ getWordProbability(stoppedSentence, i) + " count:" + bigramCounter.getCount(stoppedSentence.get(i)));
		}
		return probability;
	}

	public String generateSuffix(String prefix) {
		return this.bigramCounter.getRandomSuffix(prefix);
	}

	@Override
	public Iterable<String> generateSentence() {
		List<String> sentence = Lists.newArrayList();

		String word = this.bigramCounter.getRandomSuffix(SENTENCE_START_TAG);
		while (!word.equals(SENTENCE_END_TAG)) {
			sentence.add(word);
			word = this.bigramCounter.getRandomSuffix(word);
		}

		return sentence;
	}

}
