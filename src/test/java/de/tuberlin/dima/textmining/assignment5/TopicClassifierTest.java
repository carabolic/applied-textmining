package de.tuberlin.dima.textmining.assignment5;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import de.tuberlin.dima.textmining.assignment1.KeywordFinder;
import junit.framework.TestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Christoph Boden
 * Date: 11.11.11
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class TopicClassifierTest extends TestCase{

    public static final String DATA_INDEX = "assignment5/minidata.txt";
    public static final String DATA_DIR = "assignment5/mini_newsgroups/";
    private List<labeledDocument> documents = new ArrayList<labeledDocument>();

    private static final long  DEFAULT_RANDOM_SEED = 0L;
    static Random              random              = new Random(DEFAULT_RANDOM_SEED);

    private static final int CROSSVALIDATON_NUM_FOLDS = 10;




    @Test
    public void testTopicClassifier() throws IOException {
        System.out.print("Loading documents ...");
        loadDocumentCollection();
        System.out.println(" done.");

        System.out.println("Just added " + documents.size() + " documents to the dataset.");

        long startTime = System.nanoTime();
        long endTime;

        String[] predictions = crossValidate(CROSSVALIDATON_NUM_FOLDS);
        double cv_accuracy = getAccuracy(predictions);

        endTime = System.nanoTime();
        double duration = ((double)(endTime - startTime)) * Math.pow(10,-9);

        assertTrue("The accuracy of your classifier is not sufficient. Yours achieved " + cv_accuracy * 100 + " % accuracy, but should be at least 50%!", cv_accuracy >= 50);
        System.out.println("Your topic classifier achieved an accuracy of " + cv_accuracy * 100 + " % with " + CROSSVALIDATON_NUM_FOLDS + " fold cross-validation in  " + duration + " s.");
    }





        /**
         * computes the accuracy of the predictions
         *
         * @param predictions
         * @return
         */

    private double getAccuracy(String[] predictions){
        double accurateClassifications = 0.0;

        for(int i = 0; i < predictions.length; i++){
            if(predictions[i].equals(documents.get(i).getLabel())){
                accurateClassifications += 1.0;
            }
        }

        return accurateClassifications / (double)(predictions.length);
    }


    /**
    * Loads the documents and labels into memory
    *
    * @throws IOException
    */
    private void loadDocumentCollection() throws IOException {

        String[] doc_ids = Resources.toString(Resources.getResource(DATA_INDEX), Charsets.UTF_8).split("\n");
        for(String doc_id : doc_ids){
            String[] data = doc_id.split(" ");
            String documentText = Resources.toString(Resources.getResource(DATA_DIR + data[0].trim() + "/" + data[1].trim()), Charsets.UTF_8);
            documents.add(new labeledDocument(documentText, data[0]));
        }
    }


    private String[] crossValidate(int nr_fold) throws IOException {

        // counter
        int i;

        //  get the number of training data
        int num_training_data = documents.size();
        String[] predictions = new String[num_training_data];

        // fold offsets
        int[] fold_start = new int[nr_fold + 1];
        for (i = 0; i <= nr_fold; i++)
        fold_start[i] = i * num_training_data / nr_fold;
        int[] perm = new int[num_training_data];


        // generate a random order of training data indices
        for (i = 0; i < num_training_data; i++)
        perm[i] = i;
        for (i = 0; i < num_training_data; i++) {
        int j = i + random.nextInt(num_training_data - i);
        swap(perm, i, j);
        }

        System.out.println("Starting " + nr_fold + "fold crossvalidation ...");

        // for each fold
        for (i = 0; i < nr_fold; i++) {
            int begin = fold_start[i];
            int end = fold_start[i + 1];
            int j, k;
            List<labeledDocument> subDocList = new ArrayList<labeledDocument>();

            System.out.print("Validating fold " + i + " ... ");

            // fill sub problem with training data - fold i
            k = 0;
            for (j = 0; j < begin; j++) {
               subDocList.add(documents.get(perm[j]));
               ++k;
            }
            for (j = end; j < num_training_data; j++) {
               subDocList.add(documents.get(perm[j]));
               ++k;
            }

            // train model on subproblem
            TopicClassifier myClassifier = new TopicClassifier();
            System.out.print(" training ...");
            long startTime = System.nanoTime();
            long endTime;
            myClassifier.train(subDocList);
            endTime = System.nanoTime();
            double duration = ((double)(endTime - startTime)) * Math.pow(10,-9);
            System.out.print(" done training  after " + duration + " s ...");

            // predict fold i
            startTime = System.nanoTime();
            System.out.print(" predicting ...");
            for (j = begin; j < end; j++){
             predictions[perm[j]] = myClassifier.classifyDocument(documents.get(perm[j]).getText());
            }
            endTime = System.nanoTime();
            duration = ((double)(endTime - startTime)) * Math.pow(10,-9);
            System.out.println(" done predicting  after " + duration + " s.");
        }
    return predictions;
    }

       /**
     * swaps elements A and B of an array
     *
     * @param array
     * @param idxA
     * @param idxB
     */
    static void swap(int[] array, int idxA, int idxB) {
        int temp = array[idxA];
        array[idxA] = array[idxB];
        array[idxB] = temp;
    }

}
