package edu.tuberlin.dima.nerdle.qa;

import java.util.Comparator;
import java.util.Map;

import edu.tuberlin.dima.nerdle.model.NerdleFact;

public class ScoreComparator implements Comparator<NerdleFact> {

	Map<NerdleFact, Double> base;

	public ScoreComparator(Map<NerdleFact, Double> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with
	// equals.
	public int compare(NerdleFact a, NerdleFact b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}
