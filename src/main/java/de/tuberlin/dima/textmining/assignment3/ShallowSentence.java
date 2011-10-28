package de.tuberlin.dima.textmining.assignment3;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Class ShallowSentence is a shallow tagged sentence consisting of a Vector of ShallowToken.
 */
public class ShallowSentence extends Vector<ShallowToken> {

	/**
	 * Instantiates a new shallow sentence given a vector of ShallowToken.
	 *
	 * @param tokens the tokens
	 */
	public ShallowSentence(Vector<ShallowToken> tokens) {

		for (ShallowToken token : tokens) {
			this.add(token);
		}

	}

	/**
	 * Instantiates a new shallow sentence from a JSON object.
	 *
	 * @param sentenceJson the JSON object
	 */
	public ShallowSentence(JSONObject sentenceJson) {

		try {
			JSONArray instances = sentenceJson.getJSONArray("tokens");

			for (int c = 0; c < instances.length(); c++) {

				String lemma = ((JSONObject) instances.get(c))
						.getString("lemma");
				String text = ((JSONObject) instances.get(c)).getString("text");
				String tag = ((JSONObject) instances.get(c)).getString("tag");

				ShallowToken token = new ShallowToken(text, tag, lemma);

				this.add(token);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return job.toString();
	}

}
