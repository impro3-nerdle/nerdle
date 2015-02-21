/**
 * Copyright 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
