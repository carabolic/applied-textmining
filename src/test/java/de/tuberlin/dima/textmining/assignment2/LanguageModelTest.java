package de.tuberlin.dima.textmining.assignment2;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LanguageModelTest {

  LanguageModel languageModel = new UnigramModel();

  String trainingSentencesFile = "/europarl-v6.de-en.en";
  String testSentencesFile = "/europarl-v6.de-en.en";

  public Collection<List<String>> getSentences(String corpus){
    Collection<List<String>> sentences = new ArrayList<List<String>>();
    for(String rawSentence: corpus.split("\n")){
        List<String> sentence = new ArrayList<String>();
        for (String word : rawSentence.split("\\s+"))
            sentence.add(word.toLowerCase());
        sentences.add(sentence);
    }
    return sentences;
  }

  @Before
  public void setup() throws IOException {
    String trainingCorpus = Resources.toString(Resources.getResource("assignment2" + trainingSentencesFile), Charsets.UTF_8);
    languageModel.train(getSentences(trainingCorpus));
    System.out.println("Model successfully trained. (" + trainingCorpus.length() + ")");
  }

  @Test
  public void perplexity() throws IOException {
    String testCorpus = Resources.toString(Resources.getResource("assignment2" + testSentencesFile), Charsets.UTF_8);

    double logProbability = 0.0;
    double numSymbols = 0.0;
    for (List<String> sentence : getSentences(testCorpus)) {
      logProbability += languageModel.sentenceLogProbability(sentence) / Math.log(2.0);
      numSymbols += sentence.size();
    }
    double avgLogProbability = logProbability / numSymbols;
    double perplexity = Math.pow(0.5, avgLogProbability);

    System.out.println("Calculated perplexity: "  + perplexity);
  }

  @Test
  public void generateSentences() {
    System.out.println("Generating sentences:\n\n");

    for (int n = 0; n < 10; n++) {
      System.out.println(Joiner.on(' ').join(languageModel.generateSentence()));
    }
  }


}
