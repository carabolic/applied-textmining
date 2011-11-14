package de.tuberlin.dima.textmining.assignment3;


/**
 * The Class SimpleRelation.
 */
public class SimpleRelation {

	/** The subject. */
	private String subject;

	/** The predicate. */
	private String predicate;

	/** The object. */
	private String object;

	/**
	 * Instantiates a new simple relation.
	 * 
	 * @param predicate
	 *            the predicate, aka the relation type
	 * @param subject
	 *            the subject
	 * @param object
	 *            the object
	 */
	public SimpleRelation(RelationType predicate, String subject, String object) {
		super();
		this.subject = subject;
		this.predicate = predicate.name();
		this.object = object;
	}

	/**
	 * Instantiates a new simple relation.
	 * 
	 * @param predicate
	 *            the predicate, aka the relation type
	 * @param subject
	 *            the subject
	 * @param object
	 *            the object
	 */
	public SimpleRelation(String genericPredicate, String subject, String object) {
		super();
		this.subject = subject;
		this.predicate = genericPredicate;
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
	public String getPredicate() {
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
