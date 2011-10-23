package de.tuberlin.dima.textmining.assignment1;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordFinder {

	/*
	 * TODO: make better
	 */
	private String cheapPullStopWords(String inputText) {

		inputText = inputText.replaceAll("The", "");
		inputText = inputText.replaceAll("As", "");
		inputText = inputText.replaceAll("Another", "");
		inputText = inputText.replaceAll("After", "");

		return inputText;

	}

	@SuppressWarnings("rawtypes")
	public LinkedHashMap sortHashMapByValues(HashMap passedMap,
			boolean ascending) {

		List mapKeys = new ArrayList(passedMap.keySet());
		List mapValues = new ArrayList(passedMap.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		if (!ascending)
			Collections.reverse(mapValues);

		LinkedHashMap someMap = new LinkedHashMap();
		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();
			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				if (passedMap.get(key).toString().equals(val.toString())) {
					passedMap.remove(key);
					mapKeys.remove(key);
					someMap.put(key, val);
					break;
				}
			}
		}
		return someMap;
	}

	public List<String> keywords(String text, int howMany) {
		List<String> keywords = Lists.newArrayList();

		Pattern pattern = Pattern.compile("\\b([A-Z]{2,})\\b");

		Matcher matcher = pattern.matcher(text);

		HashMap<String, Integer> keywordCount = new HashMap<String, Integer>();

		/*
		 * get bold keys (main actors short)
		 */
		while (matcher.find()) {

			if (keywordCount.containsKey(matcher.group())) {
				keywordCount.put(matcher.group(),
						keywordCount.get(matcher.group()) + 1);
			} else {
				keywordCount.put(matcher.group(), 1);
			}

		}

		text = this.cheapPullStopWords(text);

		/*
		 * get common bigrams
		 */
		pattern = Pattern.compile("\\b[A-Z][a-z]+ [A-Z][a-z]+\\b");
		matcher = pattern.matcher(text);
		HashMap<String, Integer> bigrams = new HashMap<String, Integer>();

		while (matcher.find()) {

			if (bigrams.containsKey(matcher.group())) {
				bigrams.put(matcher.group(), bigrams.get(matcher.group()) + 1);
			} else {
				bigrams.put(matcher.group(), 1);
			}

		}

		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Integer> sortedBold = sortHashMapByValues(
				keywordCount, false);
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Integer> sortedBi = sortHashMapByValues(bigrams,
				false);

		/*
		 * now find common bigrams that contain main actors (for full name)
		 */

		for (String key : sortedBold.keySet()) {

			// break when howMany are found
			if (keywords.size() == howMany)
				break;

			// System.out.println(key + " " + sortedBold.get(key));

			for (String bigram : sortedBi.keySet()) {

				// System.out.println(bigram + " : " + sortedBi.get(bigram));

				if (bigram.toLowerCase().contains(key.toLowerCase())) {
					// System.out.println("FOUND! + " + bigram + " : "
					// + sortedBi.get(bigram));

					if (!keywords.contains(bigram))
						keywords.add(bigram);

					break;
				}

			}

		}

		// keywords.addAll(foundKeywords);

		return keywords;
	}
}
