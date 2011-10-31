package de.tuberlin.dima.textmining.assignment3;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Class ShallowSentence is a shallow tagged sentence consisting of a Vector of ShallowToken.
 */
public class ShallowSentence implements Iterable<ShallowToken> {

  private final List<ShallowToken> tokens;
	/**
	 * Instantiates a new shallow sentence given a vector of ShallowToken.
	 *
	 * @param tokens the tokens
	 */
	public ShallowSentence(Iterable<ShallowToken> tokens) {
    this.tokens = Lists.newArrayList(tokens);
	}

	/**
	 * Instantiates a new shallow sentence from a JSON object.
	 *
	 * @param sentenceJson the JSON object
	 */
	public ShallowSentence(JSONObject sentenceJson) {

    this.tokens = Lists.newArrayList();

		try {
			JSONArray instances = sentenceJson.getJSONArray("tokens");

			for (int c = 0; c < instances.length(); c++) {

				String lemma = ((JSONObject) instances.get(c)).getString("lemma");
				String text = ((JSONObject) instances.get(c)).getString("text");
				String tag = ((JSONObject) instances.get(c)).getString("tag");

				ShallowToken token = new ShallowToken(text, tag, lemma);

				tokens.add(token);
			}

		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Print this object as JSON
	 *
	 * @return the JSON string
	 */
	public String toJson() {

		JSONArray ja = new JSONArray();

		for (ShallowToken token : this) {
			ja.put(token.toJson());
		}

		JSONObject job = new JSONObject();
		try {
			job.put("tokens", ja);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		return job.toString();
	}

  @Override
  public Iterator<ShallowToken> iterator() {
    return tokens.iterator();
  }
}
