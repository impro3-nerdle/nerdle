package edu.tuberlin.dima.nerdle.qa;

import java.util.Comparator;
import java.util.Map;

public class GroupScoreComparator implements Comparator<String>{
	
	Map<String, Double> groupToScore;

	public GroupScoreComparator(Map<String, Double> groupToScore) {
		super();
		this.groupToScore = groupToScore;
	}

	public Map<String, Double> getGroupToScore() {
		return groupToScore;
	}

	public void setGroupToScore(Map<String, Double> groupToScore) {
		this.groupToScore = groupToScore;
	}

	@Override
	public int compare(String a, String b) {
		if (groupToScore.get(a) >= groupToScore.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}

}
