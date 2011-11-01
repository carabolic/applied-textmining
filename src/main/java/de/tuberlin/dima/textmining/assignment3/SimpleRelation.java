package de.tuberlin.dima.textmining.assignment3;

public class SimpleRelation {

	/** The subject. */
	private final String subject;
	
	/** The predicate. */
	private final RelationType predicate;
	
	/** The object. */
	private final String object;

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

	public String getSubject() {
		return subject;
	}

	public RelationType getPredicate() {
		return predicate;
	}

	public String getObject() {
		return object;
	}

}
