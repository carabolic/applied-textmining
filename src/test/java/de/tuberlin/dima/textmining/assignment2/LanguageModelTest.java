package de.tuberlin.dima.textmining.assignment2;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LanguageModelTest {

  LanguageModel languageModel = new LanguageModel();

  @Before
  public void setup() throws IOException {
    String corpus = Resources.toString(Resources.getResource("assignment2/corpus.txt"), Charsets.UTF_8);
    languageModel.train(corpus);
  }

  @Test
  public void perplexity() throws IOException {

    List<List<String>> sentences = Arrays.asList(
        Arrays.asList("I", "am", "happy"),
        Arrays.asList("Englands", "sucks", "ass"));


    double logProbability = 0.0;
    double numSymbols = 0.0;
    for (List<String> sentence : sentences) {
      logProbability += Math.log(languageModel.sentenceProbability(sentence)) / Math.log(2.0);
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
