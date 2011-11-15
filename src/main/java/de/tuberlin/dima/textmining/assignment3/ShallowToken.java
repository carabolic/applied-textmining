package de.tuberlin.dima.textmining.assignment3;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

import edu.stanford.nlp.ling.HasWord;

/**
 * The Class ShallowToken hold a shallow parsed token consisting of the token
 * text, the POS tag and the lemma.
 */
public class ShallowToken implements Serializable, HasWord {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The text. */
	private String text;

	/** The tag. */
	private String tag;

	/** The lemma. */
	private String lemma;
	
	private String ner;

	/**
	 * Instantiates a new token with all shallow information. Use this
	 * constructor.
	 * 
	 * @param text
	 *            the text
	 * @param tag
	 *            the tag
	 * @param lemma
	 *            the lemma
	 */
	public ShallowToken(String text, String tag, String lemma) {
		super();
		this.text = text;
		this.tag = tag;
		this.lemma = lemma;
		this.ner = null;
	}

	/**
	 * Instantiates a new token.
	 * 
	 * @param text
	 *            the text
	 * @param tag
	 *            the tag
	 */
	public ShallowToken(String text, String tag) {
		super();
		this.text = text;
		this.tag = tag;
		this.lemma = null;
		this.ner = null;
	}

	/**
	 * Instantiates a new token.
	 * 
	 * @param text
	 *            the text
	 */
	public ShallowToken(String text) {
		super();
		this.text = text;
		this.tag = null;
		this.lemma = null;
		this.ner = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		String result = this.text;

		if (this.tag != null && this.lemma != null) {

			result += " (" + this.tag + " " + this.lemma + ")";

		} else if (this.tag != null) {
			result += " (" + this.tag + ")";

		} else if (this.lemma != null) {
			result += " (" + this.lemma + ")";

		}
		return result;
	}

	/**
	 * Returns this object as JSON.
	 * 
	 * @return the jSON object
	 */
	public JSONObject toJson() {

		JSONObject job = new JSONObject();
		try {
			job.put("text", text);
			job.put("tag", tag);
			job.put("lemma", lemma);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return job;
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the tag.
	 * 
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Sets the tag.
	 * 
	 * @param tag
	 *            the new tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Gets the lemma.
	 * 
	 * @return the lemma
	 */
	public String getLemma() {
		return lemma;
	}

	/**
	 * Sets the lemma.
	 * 
	 * @param lemma
	 *            the new lemma
	 */
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	@Override
	public String word() {
		return this.text;
	}

	@Override
	public void setWord(String word) {
		this.setText(word);		
	}
	
	public String ner() {
		return this.ner;
	}
	
	public void setNer(String ner) {
		this.ner = ner;
	}

}
