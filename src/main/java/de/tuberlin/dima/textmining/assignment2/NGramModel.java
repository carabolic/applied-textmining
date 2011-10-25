package de.tuberlin.dima.textmining.assignment2;

import java.util.Collection;
import java.util.List;

/**

 */
public class NGramModel implements LanguageModel {
    @Override
    public void train(Collection<List<String>> corpus) {
        //TODO: INSERT CODE HERE
    }

    @Override
    public double getWordProbability(List<String> sentence, int index) {

        //TODO: INSERT CODE HERE

        return 0;
    }

    @Override
    public double sentenceLogProbability(List<String> sentence) {

        //TODO: INSERT CODE HERE

        return 0;
    }

    @Override
    public Iterable<String> generateSentence() {

        //TODO: INSERT CODE HERE

        return null;
    }

}
