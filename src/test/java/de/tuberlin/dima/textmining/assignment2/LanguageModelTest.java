package de.tuberlin.dima.textmining.assignment2;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LanguageModelTest {

  String trainingSentencesFile = "assignment2/corpus.txt";
  String testSentencesFile = "assignment2/corpus.txt";

  Collection<List<String>> getSentences(String corpus){
    Collection<List<String>> sentences = new ArrayList<List<String>>();
    for (String rawSentence : corpus.split("\n")) {
        List<String> sentence = Lists.newArrayList();
        for (String word : rawSentence.split("\\s+")) {
            sentence.add(word.toLowerCase());
        }
        sentences.add(sentence);
    }
    return sentences;
  }

  @Test
  public void testUnigramModel() throws Exception {
    System.out.println("\nTESTING UNIGRAM MODEL\n");
    LanguageModel languageModel = new UnigramModel();
    train(languageModel);
    calculatePerplexity(languageModel);
    generateSentences(languageModel);
  }

  @Test
  public void testNGramModel() throws Exception {
    System.out.println("\nTESTING NGRAM MODEL\n");
    LanguageModel languageModel = new NGramModel();
    train(languageModel);
    calculatePerplexity(languageModel);
    generateSentences(languageModel);
  }

  @Test
  public void testSmoothedNGramModel() throws Exception {
    System.out.println("\nTESTING SMOOTHED NGRAM MODEL\n");
    LanguageModel languageModel = new SmoothedNGramModel();
    train(languageModel);
    calculatePerplexity(languageModel);
    generateSentences(languageModel);
  }


  void train(LanguageModel languageModel) throws IOException {
    String trainingCorpus = Resources.toString(Resources.getResource(trainingSentencesFile), Charsets.UTF_8);
    languageModel.train(getSentences(trainingCorpus));
  }

  void calculatePerplexity(LanguageModel languageModel) throws IOException {
    String testCorpus = Resources.toString(Resources.getResource(testSentencesFile), Charsets.UTF_8);

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

 void generateSentences(LanguageModel languageModel) {
    System.out.println("Generating sentences:\n");
    for (int n = 1; n <= 10; n++) {
      System.out.println("[" + n + "]\t" + Joiner.on(' ').join(languageModel.generateSentence()));
    }
  }


}
