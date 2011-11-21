package de.tuberlin.dima.textmining.assignment5;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import edu.stanford.nlp.stats.ClassicCounter;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Assignment 5 of the Course Applied Text Mining Ws 2011/12 TU Berlin DIMA
 *
 * In this assignment you will have to implememnt a topic classifier
 */
public class TopicClassifier {

		private static final double SMOOTHING_PARAM = 0.1;

    private final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
    private final ClassicCounter<String> labelCounter = new ClassicCounter<String>();
    private final Map<String, ClassicCounter<String>> model = new HashMap<String, ClassicCounter<String>>();

    /**
     * This function estimates the parameters of the classifier based on the training set
     *
     * @param trainingDocs
     */
    public void train(List<labeledDocument> trainingDocs) throws IOException {
    		ClassicCounter<String> termCounter = null;

        // traverse training collection
        for(labeledDocument currentDoc : trainingDocs){

              // The class label of the current document (e.g."rec.autos")
              String documentLabel = currentDoc.getLabel();
              labelCounter.incrementCount(documentLabel);

              // Get the counter for the current label if exist, create new otherwise
              termCounter = this.model.containsKey(documentLabel) ? this.model.get(documentLabel) : new ClassicCounter<String>();

              /* Note: if you want to tune your smoothing parameters on held-out data, you'll have to
               * split the training set yourself
               *
               * e.g.:

                      double sample = Math.random();
                        if(sample <= 0.80){
                            //  add doc to training data
                        }
                        else(sample > 0.80 ){
                            // add doc to held out data
                        }
               */

              // utilize lucene token stream to transform text into tokens (removes stop words etc.)
              TokenStream stream = analyzer.tokenStream(null, new StringReader(currentDoc.getText()));
              CharTermAttribute attr = stream.addAttribute(CharTermAttribute.class);

              // for each term in document
              while (stream.incrementToken()) {
                if (attr.length() > 0) {
                  String term = new String(attr.buffer(), 0, attr.length());
                  termCounter.incrementCount(term);
                }
              }
            this.model.put(documentLabel, termCounter);  
        }
    }

    public String classifyDocument(String document){
    	String bestLabel = "NONE";
    	double bestProb = Double.NEGATIVE_INFINITY;

    	for (String label : this.model.keySet()) {
    		double prob = this.getClassificationLogProbability(document, label);

    		if (prob > bestProb) {
    			bestLabel = label;
    			bestProb = prob;
    		}
    	}

    	return bestLabel;
    }

    private double getLabelProbability(String label) {
    	double prob = 0;

    	double cnt = this.labelCounter.getCount(label);
    	prob = cnt / this.labelCounter.totalCount();

    	return prob;
    }

    private double getConditionalTermProbability(String term, String label) {
    	double prob = 0;

    	double count = this.model.get(label).getCount(term);
    	double total = this.model.get(label).totalCount();

     	prob = (count + TopicClassifier.SMOOTHING_PARAM) / (total + TopicClassifier.SMOOTHING_PARAM * this.model.keySet().size());

    	return prob;
    }

    private double getClassificationLogProbability(String document, String label) {
    	double prob = 0;

    	TokenStream stream = analyzer.tokenStream(null, new StringReader(document));
      CharTermAttribute attr = stream.addAttribute(CharTermAttribute.class);
      double sum = 0;

      try {
				while (stream.incrementToken()) {
				  if (attr.length() > 0) {
				    String term = new String(attr.buffer(), 0, attr.length());
				    sum += Math.log(this.getConditionalTermProbability(term, label));
				  }
				}
			} catch (IOException e) {
				System.err.println("IOException withing string reader ... u serious?");
				e.printStackTrace();
			}

      prob = Math.log(this.getLabelProbability(label)) + sum;

    	return prob;
    }
}
