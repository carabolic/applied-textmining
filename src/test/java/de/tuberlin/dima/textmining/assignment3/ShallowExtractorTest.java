package de.tuberlin.dima.textmining.assignment3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * The Class ShallowExtractorTest.
 */
public class ShallowExtractorTest {

	ShallowExtractor extractor = new ShallowExtractor();

	/**
	 * Gets the parsed sentences from file resource.
	 * 
	 * @return the sentences from file
	 */
	public Vector<ShallowSentence> getSentencesFromFile() {

		Vector<ShallowSentence> sentences = new Vector<ShallowSentence>();

		Scanner scan;
		try {
			scan = new Scanner(new File(
					"src/test/resources/assignment3/sentences-json"));

			while (scan.hasNextLine()) {

				try {

					JSONObject job = new JSONObject(scan.nextLine());

					ShallowSentence readSentence = new ShallowSentence(job);

					sentences.add(readSentence);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sentences;

	}

	/**
	 * Test extract quotes.
	 */
	@Test
	public void testExtractQuotes() {

		/*
		 * first get sentences from file
		 */
		Vector<ShallowSentence> sentences = this.getSentencesFromFile();
				
		/*
		 * TODO: make this function solve the problem
		 */
		extractor.findQuotes(sentences);
	}

	/**
	 * Test extract apposition.
	 */
	@Test
	public void testExtractApposition() {
		/*
		 * first get sentences from file
		 */
		Vector<ShallowSentence> sentences = this.getSentencesFromFile();

		/*
		 * TODO: make this function solve the problem
		 */
		extractor.findApposition(sentences);
	}

}
