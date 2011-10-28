package de.tuberlin.dima.textmining.assignment3;

/**
 * The Class SimpleRelation.
 */
public class SimpleRelation {

	/** The subject. */
	private String subject;
	
	/** The predicate. */
	private RelationType predicate;
	
	/** The object. */
	private String object;

	/**
	 * Instantiates a new simple relation.
	 *
	 * @param predicate the predicate, aka the relation type
	 * @param subject the subject
	 * @param object the object
	 */
	public SimpleRelation(RelationType predicate, String subject, String object) {
		super();
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}
	
	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Gets the predicate.
	 *
	 * @return the predicate
	 */
	public RelationType getPredicate() {
		return predicate;
	}

	/**
	 * Gets the object.
	 *
	 * @return the object
	 */
	public String getObject() {
		return object;
	}

}
