package de.tuberlin.dima.textmining.assignment3;

/**
 * The Enum RelationType. Currently, two types of relations are defined: QUOTE
 * and APPOSITION. Additional types may be defined here if necessary.
 */
public enum RelationType {

	/**
	 * The QUOTE_SPEAKER Relation. Example ["Aye Caramba!" (quote)
	 * "Bart Simpson" (speaker)]
	 */
	QUOTE_SPEAKER,

	/**
	 * The APPOSITION Relation. Example "German Chancellor" (apposition) and
	 * "Angela Merkel" (entity).
	 */
	APPOSITION

}
