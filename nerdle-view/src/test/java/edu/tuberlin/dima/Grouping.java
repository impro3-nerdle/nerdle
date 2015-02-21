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

package edu.tuberlin.dima;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.configuration.ConfigurationException;
import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.tuberlin.dima.nerdle.graph.NerdleGraphTransformer;
import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.model.NerdlePredicate;
import edu.tuberlin.dima.nerdle.model.NerdleSubject;
import edu.tuberlin.dima.nerdle.qa.GroupScoreComparator;
import edu.tuberlin.dima.nerdle.qa.Grouper;
import edu.tuberlin.dima.nerdle.qa.QuestionAnswer;
import edu.tuberlin.dima.nerdle.qa.QuestionAnswerResponse;
import edu.tuberlin.dima.nerdle.qa.ScoreCalculator;
import edu.tuberlin.dima.nerdle.qa.ScoreComparator;

public class Grouping {

	private static TinkerGraph graph;

	@BeforeClass
	public static void setup() {

		graph = new TinkerGraph();
		graph.createIndex("verb-idx", Vertex.class);
		/* Add extractions to graph */

		// exact answer
		NerdleFact extraction = new NerdleFact(
				"Einstein was born in Ulm on 4th May.", "", 0.8D);

		extraction.setPredicate(new NerdlePredicate("was born", "bear"));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("in Ulm", "L"));
		extraction.addArgument(new NerdleArg("on 4th May", "T"));

		NerdleGraphTransformer.transform(extraction, graph);

		// exact answer
		extraction = new NerdleFact("Einstein was a professor.", "", 0.0D);

		extraction.setPredicate(new NerdlePredicate("was", "be"));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("a professor", "T"));

		NerdleGraphTransformer.transform(extraction, graph);

		// false answer
		extraction = new NerdleFact("Leibnitz was a professor.", "", 0.0D);

		extraction.setPredicate(new NerdlePredicate("was", "be"));
		extraction.setSubject(new NerdleSubject("Leibnitz"));
		extraction.addArgument(new NerdleArg("a professor", "T"));

		NerdleGraphTransformer.transform(extraction, graph);

		// false answer
		extraction = new NerdleFact("Gauss was born in Berlin on 4th May.", "",
				0.5D);

		extraction.setPredicate(new NerdlePredicate("was born", "bear"));
		extraction.setSubject(new NerdleSubject("Gauss"));
		extraction.addArgument(new NerdleArg("in Berlin", "L"));
		extraction.addArgument(new NerdleArg("on 4th May", "T"));

		NerdleGraphTransformer.transform(extraction, graph);

		// false answer
		extraction = new NerdleFact("Gauss was a professor.", "", 0.0D);

		extraction.setPredicate(new NerdlePredicate("was", "be"));
		extraction.setSubject(new NerdleSubject("Gauss"));
		extraction.addArgument(new NerdleArg("a professor", "T"));

		NerdleGraphTransformer.transform(extraction, graph);

		// one argument missing
		extraction = new NerdleFact("Einstein was born in Ulm.", "", 0.6D);

		extraction.setPredicate(new NerdlePredicate("was born", "bear"));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("in Ulm", "L"));

		NerdleGraphTransformer.transform(extraction, graph);

		// one argument missing
		extraction = new NerdleFact("Einstein was born on 4th May.", "", 0.4D);

		extraction.setPredicate(new NerdlePredicate("was born", "bear"));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("on 4th May", "T"));

		NerdleGraphTransformer.transform(extraction, graph);

	}

	//
	// @Test
	// public void testGrouping() throws ConfigurationException,
	// InterruptedException {
	// String sentence = "Who was born in Ulm";
	//
	// QuestionAnswer questionParser = new QuestionAnswer();
	// QuestionAnswerResponse questionAnswerResponse = questionParser
	// .answerToQuestion(sentence, graph);

	//
	// }

	@Test
	public void testGroupingOf() throws Exception {
		QuestionAnswer answerQuestion = new QuestionAnswer();

		// TreeMap<String, Double> jsonObject = answerQuestion.answerToWho(
		// "Who was born in Ulm on 4th May.", graph);

		QuestionAnswerResponse questionAnswerResponse = answerQuestion
				.answerToWho("Who was born in Ulm on 4th May.", graph);
		HashMap<String, List<NerdleFact>> subjectMap = Grouper.group(
				questionAnswerResponse, Grouper.SUBJECT);

		// System.out.println(subjectMap);

	}

	@Test
	public void testSortingOfGroup() throws ConfigurationException {

		QuestionAnswer answerQuestion = new QuestionAnswer();

		QuestionAnswerResponse questionAnswerResponse = answerQuestion
				.answerToWho("Who was born on 4th May in Ulm?", graph);
		HashMap<String, List<NerdleFact>> groupMap = Grouper.group(
				questionAnswerResponse, Grouper.SUBJECT);

		HashMap<String, ArrayList<Answer>> answers = new HashMap<String, ArrayList<Answer>>();

		List<GroupedAnswer> groupedAnswers = new ArrayList<GroupedAnswer>();

		HashMap<String, List<NerdleFact>> groupAndSortedElements = new HashMap<String, List<NerdleFact>>();
		HashMap<String, Double> groupToScore = new HashMap<String, Double>();
		GroupScoreComparator groupScoreComparator = new GroupScoreComparator(
				groupToScore);
		TreeMap<String, Double> sortedGroupMap = new TreeMap<String, Double>(
				groupScoreComparator);

		for (Entry<String, List<NerdleFact>> entry : groupMap.entrySet()) {
			// System.out.println(entry.getKey());

			HashMap<NerdleFact, Double> map = new HashMap<NerdleFact, Double>();
			ScoreComparator bvc = new ScoreComparator(map);
			TreeMap<NerdleFact, Double> sortedMap = new TreeMap<NerdleFact, Double>(
					bvc);

			ArrayList<Double> scoreList = new ArrayList<Double>();
			for (NerdleFact extraction : entry.getValue()) {
				// System.out.println(extraction);
				NerdleFact questionExtraction = questionAnswerResponse
						.getAnswerToQuestion().get(extraction);

				double score = ScoreCalculator.calculateForSubject(
						questionExtraction, extraction);
//				System.out.println("ExtractiontoScore: "
//						+ extraction.getSentence() + "    " + score);
				scoreList.add(score);
				double roundedScore = Math.round(score * 100) / 100.0;
				map.put(extraction, roundedScore);

			}
			// System.out.println(scoreList);
			groupToScore.put(entry.getKey(), calculateGroupScore(scoreList));

			sortedMap.putAll(map);

			ArrayList<NerdleFact> facts = new ArrayList<NerdleFact>();
			for (Entry<NerdleFact, Double> sortedElement : sortedMap.entrySet()) {
				sortedElement.getKey().setMatchingScore(
						sortedElement.getValue());
			}

			facts.addAll(sortedMap.keySet());

			groupAndSortedElements.put(entry.getKey(), facts);

		}

		sortedGroupMap.putAll(groupToScore);
//		System.out.println(sortedGroupMap);
//		System.out.println(groupAndSortedElements);

		for (Entry<String, Double> sortedGroup : sortedGroupMap.entrySet()) {
//				System.out.println(sortedGroup.getKey());
				ArrayList<Answer> aList = new ArrayList<Answer>();
				for (NerdleFact sortedElement : groupAndSortedElements.get(sortedGroup.getKey())) {

					NerdleFact questionExtraction = questionAnswerResponse
							.getAnswerToQuestion().get(sortedElement);
					try {

						aList.add(new Answer(sortedElement.getSentence(), D3GraphGenerator
								.generateD3GraphJson(sortedElement, false)
								.toString(), "", D3GraphGenerator
								.generateD3GraphJson(questionExtraction, true)
								.toString(), sortedElement.getMatchingScore(), sortedElement.getSource()));

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				groupedAnswers.add(new GroupedAnswer(sortedGroup.getKey(),
						aList));
				answers.put(sortedGroup.getKey(), aList);
			}
		

//		System.out.println(groupedAnswers);

		// System.out.println("AllSortedMap:  " + sortedGroupAndElements);

	}

	private double calculateGroupScore(List<Double> scoreList) {
		double scoreSum = 0.0;
		for (Double score : scoreList) {
			scoreSum += score;
		}

		double unroundedGroupScore = scoreSum / scoreList.size();

		double roundedGroupScore = Math.round(unroundedGroupScore * 100) / 100.0;

		return roundedGroupScore;
	}

}
