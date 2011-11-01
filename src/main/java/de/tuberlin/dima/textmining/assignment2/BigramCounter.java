package de.tuberlin.dima.textmining.assignment2;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class BigramCounter {

	private Map<String, Gram> prefixes;

	public BigramCounter() {
		this.prefixes = new HashMap<String, Gram>();
	}

	/**
	 * Returns the count of the prefix
	 */
	public double getCount(String prefix) {
		if (this.prefixes.containsKey(prefix)) {
			// Returns the count of the prefix (only)
			return this.prefixes.get(prefix).getCount();
		} else {
			return 0;
		}
	}

	/**
	 * Returns the count of the whole bigram
	 * 
	 * @param prefix
	 *          Prefix (first word) of the bigram
	 * @param suffix
	 *          Suffix (second/ last word) of the bigram
	 * @return Occurrences of the bigram
	 */
	public double getCount(String prefix, String suffix) {
		if (this.prefixes.containsKey(prefix)) {
			// Returns the count of the whole bigram (prefix + suffix)
			return this.prefixes.get(prefix).getCount(suffix);
		} else {
			return 0;
		}
	}

	/**
	 * Increment the counter of the given word by 1.0
	 * 
	 * @param prefix
	 *          Prefix (first part) of a bigram
	 */
	public void incrementCount(String prefix) {
		Gram gram = this.prefixes.get(prefix);

		if (gram == null) {
			gram = new Gram();
		}

		gram.incrementCount();
		this.prefixes.put(prefix, gram);
	}

	/**
	 * Increment the prefix and suffix counter of the given bigram by 1.0.
	 * 
	 * @param prefix
	 *          Prefix (first part) of the bigram
	 * @param suffix
	 *          Suffix (last/ second part) of the bigram
	 */
	public void incrementCount(String prefix, String suffix) {
		Gram gram = this.prefixes.get(prefix);

		if (gram == null) {
			gram = new Gram();
		}

		gram.incrementCount(suffix);
		this.prefixes.put(prefix, gram);
	}

	/**
	 * Retrieve a random suffix from the given prefix according its probabilities
	 * 
	 * @param prefix
	 *          The word to retrieve the suffix for
	 * @return Random suffix or <code>&lt;UNK&gt;</code> if there is no word
	 */
	public String getRandomSuffix(String prefix) {
		double rand = Math.random();
		double sum = 0.0;
		Set<String> suffs = this.prefixes.get(prefix).getSuffixes();

		for (String suffix : suffs) {
			sum += this.getCount(prefix, suffix) / this.getCount(prefix);
			if (sum > rand) {
				return suffix;
			}
		}

		return "<UNK>";
	}

	/**
	 * Retrieves the most likely suffix for the given prefix.
	 * 
	 * @param prefix
	 *          Prefix (first word) of the bigram
	 * @return Most probable suffix
	 */
	public String getMostLikelySuffix(String prefix) {
		String suffix = this.prefixes.get(prefix).getSortedSuffixes().firstKey();

		if (suffix != null) {
			return suffix;
		}

		return "<UNK>";
	}

	/**
	 * Wrapper class in order to hold words and there associated counts
	 * 
	 * @author Christoph Bruecke <christoph.bruecke@campus.tu-berlin.de>
	 * 
	 */
	private static class Gram {
		private double count;
		private Map<String, Double> suffix;

		/**
		 * Initialize the gram object with count set to 0 and an empty suffix map
		 */
		public Gram() {
			count = 0;
			suffix = new HashMap<String, Double>();
		}

		/**
		 * Get the count of the prefix
		 * 
		 * @return The occurrences of the word as prefix
		 */
		public double getCount() {
			return this.count;
		}

		/**
		 * Get the count of the whole bigram
		 * 
		 * @param suffix
		 * @return
		 */
		public double getCount(String suffix) {
			Double c = this.suffix.get(suffix);

			if (c == null) {
				return 0;
			}
			return c;
		}

		/**
		 * Increments the count of the prefix by 1.0
		 */
		public void incrementCount() {
			this.count += 1.0;
		}

		/**
		 * Increments the count of the whole bigram
		 * 
		 * @param suffix
		 */
		public void incrementCount(String suffix) {
			Double c = this.suffix.get(suffix);

			if (c == null) {
				c = 1.0;
			} else {
				c += 1.0;
			}

			this.count += 1.0;
			this.suffix.put(suffix, c);
		}

		public Set<String> getSuffixes() {
			return this.suffix.keySet();
		}

		public SortedMap<String, Double> getSortedSuffixes() {
			return this.getSortedSuffixes(true);
		}

		public SortedMap<String, Double> getSortedSuffixes(boolean desc) {
			SortedMap<String, Double> result = new TreeMap<String, Double>(new DoubleValueComparator(this.suffix, desc));
			result.putAll(this.suffix);

			return result;
		}

		@Override
		public String toString() {
			return "Gram [count=" + count + ", suffix=" + suffix + "]";
		}
	}

	/**
	 * Custom Comparator in order to sort a tree based on its values.
	 * 
	 * @author Christoph Bruecke <christoph.bruecke@campus.tu-berlin.de>
	 * 
	 */
	static class DoubleValueComparator implements Comparator<String> {

		private Map<String, Double> origin = null;
		private int orderFactor;

		public DoubleValueComparator(Map<String, Double> origin, boolean desc) {
			super();
			this.orderFactor = desc == true ? -1 : 1;
			this.origin = origin;
		}

		@Override
		public int compare(String o1, String o2) {
			Double value1 = origin.get(o1);
			Double value2 = origin.get(o2);

			return this.orderFactor * value1.compareTo(value2);
		}

	}

}
