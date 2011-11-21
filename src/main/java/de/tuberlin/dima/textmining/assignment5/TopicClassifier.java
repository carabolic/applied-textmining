package de.tuberlin.dima.textmining.assignment5;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;


/**
 * Assignment 5 of the Course Applied Text Mining Ws 2011/12 TU Berlin DIMA
 *
 * In this assignment you will have to implememnt a topic classifier
 */
public class TopicClassifier {

    private final static StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);


    /**
     * This function estimates the parameters of the classifier based on the training set
     *
     * @param trainingDocs
     */
    public void train(List<labeledDocument> trainingDocs) throws IOException {

        // traverse training collection
        for(labeledDocument currentDoc : trainingDocs){

              // The class label of the current document (e.g."rec.autos"
              String documentLabel = currentDoc.getLabel();

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

                  // TODO: Implement the appropriate parameter estimation for the classifier here







                }
              }
        }
    }



    public String classifyDocument(String document){

        //TODO: Implement the classification of the given Document here

        return "alt.atheism";
    }







}
