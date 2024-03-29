package de.tuberlin.dima.textmining.assignment2;

import java.util.Collection;
import java.util.List;

/**
 * In this class you should implement your language model.  *
 */

public interface  LanguageModel {

	static String SENTENCE_START_TAG = "<s>";
	static String SENTENCE_END_TAG = "</s>";

    /**
     * This function takes the provided training corpus and estimates the model probabilities by processing it.
     *
     * @param corpus
     */
    void train(Collection<List<String>> corpus);

     /**
     * computes the probability of an individual word in a sentence at position i
     *
     * @param sentence
     * @param index
     * @return
     */
    double getWordProbability(List<String> sentence, int index);


    /**
     * This function returns the probability of a sentence as scored by the language model
     *
     * e.g. P(I am here) = P(I|<s>)*P(am|I)*P(here|am)P(</s>|here)
     *
      * @param sentence
     * @return
     */
    double sentenceLogProbability(List<String> sentence);



    /**
     * This function implements the 'shanon game'. A sentence is assembled by randomly sampling from the language model
     * until the stop token has been sampled.
     *
      * @return
     */
    Iterable<String> generateSentence();

}
