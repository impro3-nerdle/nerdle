package edu.tuberlin.dima.nerdle.qa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;

public class Grouper {

	public static final int SUBJECT = 0;
	public static final int LOCATION = 1;
	public static final int TIME = 2;

	public static HashMap<String, List<NerdleFact>> group(
			QuestionAnswerResponse questionAnswerResponse, int groupBy) {

		HashMap<String, List<NerdleFact>> subjectMap = new HashMap<String, List<NerdleFact>>();
		HashMap<NerdleFact, Double> answerToScore = questionAnswerResponse
				.getAnswerToScore();

		for (Entry<NerdleFact, Double> entry : answerToScore.entrySet()) {
			String groupedBy = "";
			Iterator<NerdleArg> arguments = entry.getKey().getArguments()
					.iterator();
			switch (groupBy) {
			case SUBJECT:
				groupedBy = entry.getKey().getSubject().getText();
				if (!subjectMap.containsKey(groupedBy)) {
					subjectMap.put(groupedBy, new ArrayList<NerdleFact>());
				}
				subjectMap.get(groupedBy).add(entry.getKey());
				break;
			case LOCATION:
				String location = "";
				while (arguments.hasNext()) {
					NerdleArg NerdleArg = (NerdleArg) arguments.next();
					String argumentText = NerdleArg.getText();
					String argumentNer = NerdleArg.getNer();
					if (argumentNer.equals("L")) {
						location += argumentText;
					}
				}
				if (!subjectMap.containsKey(location) && !location.equals("")) {
					subjectMap.put(location, new ArrayList<NerdleFact>());
				}

				if (!location.equals("")) {
					subjectMap.get(location).add(entry.getKey());
				}

				break;
			case TIME:
				String time = "";
				while (arguments.hasNext()) {
					NerdleArg NerdleArg = (NerdleArg) arguments.next();
					String argumentText = NerdleArg.getText();
					String argumentNer = NerdleArg.getNer();
					if (argumentNer.equals("T")) {
						time += argumentText;
					}
				}
				if (!subjectMap.containsKey(time) && !time.equals("")) {
					subjectMap.put(time, new ArrayList<NerdleFact>());
				}

				if (!time.equals("")) {
					subjectMap.get(time).add(entry.getKey());
				}

				break;
			}

		}

		return subjectMap;

	}

}
