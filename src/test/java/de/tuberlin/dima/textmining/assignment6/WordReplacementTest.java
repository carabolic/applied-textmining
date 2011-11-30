/**
 * 
 */
package de.tuberlin.dima.textmining.assignment6;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.google.common.collect.Lists;

public class WordReplacementTest {
	
	WordReplacement wordR = new WordReplacement();
	ShallowSentence shallS = new ShallowSentence(); 
	
	/**
	 * Gets the parsed sentences from file resource.
	 * 
	 * @return the sentences from file
	 */
	public Iterable<ShallowSentence> getSentencesFromFile() throws IOException {

		List<ShallowSentence> sentences = Lists.newArrayList();

		Scanner scan;
		try {
			scan = new Scanner(new StringReader(Resources.toString(Resources.getResource("assignment3/sentences-json"),
          Charsets.UTF_8)));

			while (scan.hasNextLine()) {
				try {
					JSONObject job = new JSONObject(scan.nextLine());
					ShallowSentence readSentence = new ShallowSentence(job);
					sentences.add(readSentence);
				} catch (JSONException e) {
					throw new RuntimeException(e);
				}
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return sentences;
	}
	

	@Test
	public void getNewTextTest() throws IOException {
		
		int replaceInterval = 5;
		Iterable<ShallowSentence> sentences = this.getSentencesFromFile();
		Iterable<ShallowSentence> changedSentences = wordR.findWords(sentences, replaceInterval);
		
		String newText = shallS.transformSentences(changedSentences);
		System.out.println(newText);
	}

}
