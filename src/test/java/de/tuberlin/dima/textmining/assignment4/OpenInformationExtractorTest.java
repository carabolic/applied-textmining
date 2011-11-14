package de.tuberlin.dima.textmining.assignment4;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import de.tuberlin.dima.textmining.assignment3.ShallowSentence;
import de.tuberlin.dima.textmining.assignment3.SimpleRelation;

public class OpenInformationExtractorTest {

	OpenInformationExtractor extractor = new OpenInformationExtractor();

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
	public void testExtractGenericRelations() {

		/*
		 * first get sentences from file. if you don't need the ShallowSentence
		 * object, you can also use plain text sentences
		 */
		Vector<ShallowSentence> sentences = this.getSentencesFromFile();

		/*
		 * TODO: make this function solve the problem
		 */
		List<SimpleRelation> relations = extractor
				.findGenericRelations(sentences);

		for (SimpleRelation rel : relations) {

			System.out.println("found relation: " + rel);
		}
	}
}
