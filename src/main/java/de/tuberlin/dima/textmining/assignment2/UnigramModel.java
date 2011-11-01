package de.tuberlin.dima.textmining.assignment2;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import edu.stanford.nlp.stats.ClassicCounter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** Implementation of a plain & simple unigram language model */
public class UnigramModel implements LanguageModel {

  /** data structure for the counts of words. note that we do not pre-compute the probabilities but rather hold the raw counts */
  private ClassicCounter<String> unigramCounter = new ClassicCounter<String>();
  private double totalSum = 0.0;

  UnigramModel(){
    unigramCounter.setDefaultReturnValue(0.0);
  }


  /** takes the provided training corpus and estimates the model probabilities by processing it. */
  public void train(Collection<List<String>> corpus) {
      for (List<String> sentence : corpus) {
          List<String> stoppedSentence = Lists.newArrayList(sentence);
          stoppedSentence.add(SENTENCE_END_TAG);
          for (String word : stoppedSentence) {
              unigramCounter.incrementCount(word);
          }
      }
      totalSum = unigramCounter.totalCount();
      System.out.println("[UnigramModel.train()]: Model trained. Contains " + unigramCounter.keySet().size() +
          " n-grams. Total count:" + unigramCounter.totalCount() +". totalSum = " + totalSum);
  }



  /** computes the probability of an individual word in a sentence at position i */
  public double getWordProbability(List<String> sentence, int position) {
    String word = sentence.get(position);
    double probability = unigramCounter.getCount(word);

    if (probability == 0.0) {
        probability = 1.0;
    }

    double wordProbability = probability / (totalSum + 1.0);
    Preconditions.checkArgument(wordProbability > 0);
    return wordProbability;
  }


  /** returns the probability of a sentence as scored by the language model
   *
   * e.g. P(I am here) = P(I|<s>)*P(am|I)*P(here|am)P(</s>|here)
   */
  public double sentenceLogProbability(List<String> sentence) {
      List<String> mySentence = Lists.newArrayList(sentence);
      mySentence.add(SENTENCE_END_TAG);
      double probability = 0.0;

      for (int i = 0; i < mySentence.size(); i++) {
          probability += Math.log(getWordProbability(mySentence, i));
          Preconditions.checkArgument(!Double.isInfinite(probability), "MSG: word:" + mySentence.get(i) + " - " +
              getWordProbability(mySentence, i) + " count:" + unigramCounter.getCount(mySentence.get(i)));
      }
      return probability;
  }

  /**  randomly samples the language model for a word */
  private String generateWord() {
    double sample = Math.random();
    double sum = 0.0;
    for (String word : unigramCounter.keySet()) {
      sum += unigramCounter.getCount(word) / totalSum;
      if (sum > sample) {
        return word;
      }
   }
  return "<unk>";
}

  /**
   *  implements the 'shannon game'. A sentence is assembled by randomly sampling from the language model until the stop token
   *  has been sampled.
   */
  public Iterable<String> generateSentence() {
    List<String> sentence = new ArrayList<String>();
    String word = generateWord();
    while (!word.equals(SENTENCE_END_TAG)) {
      sentence.add(word);
      word = generateWord();
    }
    return sentence;
  }
}

