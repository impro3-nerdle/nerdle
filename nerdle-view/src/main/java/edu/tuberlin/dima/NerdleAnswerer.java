package edu.tuberlin.dima;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.configuration.ConfigurationException;
import org.glassfish.jersey.server.JSONP;
import org.json.JSONException;

import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.qa.GroupScoreComparator;
import edu.tuberlin.dima.nerdle.qa.Grouper;
import edu.tuberlin.dima.nerdle.qa.QuestionAnswer;
import edu.tuberlin.dima.nerdle.qa.QuestionAnswerResponse;
import edu.tuberlin.dima.nerdle.qa.ScoreComparator;
import edu.tuberlin.dima.nerdle.qa.SingletonGraph;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("nerdleAnswerer")
public class NerdleAnswerer {

	@GET
	@JSONP(callback = "eval", queryParam = "jsonpCallback")
	@Produces({ "application/json", "application/javascript" })
	@Path("answer")
	public Response getAnswer(@QueryParam("sentence") String sentence,
			@QueryParam("wikia") String wikia) throws InterruptedException,
			ConfigurationException {

		long beginLoad = System.currentTimeMillis();

		SingletonGraph.getInstance();
		TinkerGraph graph = SingletonGraph.getInstance().getGraphs().get(wikia);

		System.out.println("SCAN LOAD : "
				+ (System.currentTimeMillis() - beginLoad));

		QuestionAnswer questionParser = new QuestionAnswer();

		long beginAnswer = System.currentTimeMillis();

		QuestionAnswerResponse questionAnswerResponse = questionParser
				.answerToQuestion(sentence, graph);

		System.out.println("SCAN ANSWER: "
				+ (System.currentTimeMillis() - beginAnswer));

		HashMap<String, List<NerdleFact>> groupMap = null;

		long beginGrouping = System.currentTimeMillis();

		switch (questionAnswerResponse.getQuestionType()) {
		case WHO:
			groupMap = Grouper.group(questionAnswerResponse, Grouper.SUBJECT);
			break;
		case WHICH:
			groupMap = Grouper.group(questionAnswerResponse, Grouper.SUBJECT);
			break;
		case WHERE:
			groupMap = Grouper.group(questionAnswerResponse, Grouper.LOCATION);
			break;
		case WHEN:
			groupMap = Grouper.group(questionAnswerResponse, Grouper.TIME);
			break;
		}

		System.out.println("SCAN GROUPING : "
				+ (System.currentTimeMillis() - beginGrouping));

		long beginSorting = System.currentTimeMillis();
		HashMap<String, List<NerdleFact>> groupAndSortedElements = new HashMap<String, List<NerdleFact>>();
		HashMap<String, Double> groupToScore = new HashMap<String, Double>();
		GroupScoreComparator groupScoreComparator = new GroupScoreComparator(
				groupToScore);
		TreeMap<String, Double> sortedGroupMap = new TreeMap<String, Double>(
				groupScoreComparator);

		for (Entry<String, List<NerdleFact>> entry : groupMap.entrySet()) {

			HashMap<NerdleFact, Double> map = new HashMap<NerdleFact, Double>();
			ScoreComparator bvc = new ScoreComparator(map);
			TreeMap<NerdleFact, Double> sortedMap = new TreeMap<NerdleFact, Double>(
					bvc);

			ArrayList<Double> scoreList = new ArrayList<Double>();

			for (NerdleFact extraction : entry.getValue()) {

				Double score = questionAnswerResponse.getAnswerToScore().get(
						extraction);

				scoreList.add(score);

				map.put(extraction, score);

			}

			groupToScore.put(entry.getKey(), calculateGroupScore(scoreList));

			sortedMap.putAll(map);

			ArrayList<NerdleFact> facts = new ArrayList<NerdleFact>();
			facts.addAll(sortedMap.keySet());

			groupAndSortedElements.put(entry.getKey(), facts);

		}

		sortedGroupMap.putAll(groupToScore);

		System.out.println("SCAN SORTING: "
				+ (System.currentTimeMillis() - beginSorting));

		long beginGenerating = System.currentTimeMillis();
		// Generate sorted and grouped answers
		HashMap<String, ArrayList<Answer>> answers = new HashMap<String, ArrayList<Answer>>();
		List<GroupedAnswer> groupedAnswers = new ArrayList<GroupedAnswer>();

		for (Entry<String, Double> sortedGroup : sortedGroupMap.entrySet()) {
			ArrayList<Answer> aList = new ArrayList<Answer>();
			for (NerdleFact sortedElement : groupAndSortedElements
					.get(sortedGroup.getKey())) {

				NerdleFact questionExtraction = questionAnswerResponse
						.getAnswerToQuestion().get(sortedElement);
				try {

					double roundedScore = Math.round(questionAnswerResponse
							.getAnswerToScore().get(sortedElement) * 100) / 100.0;

					aList.add(new Answer(sortedElement.getSentence(),
							D3GraphGenerator.generateD3GraphJson(sortedElement,
									false).toString(), "", D3GraphGenerator
									.generateD3GraphJson(questionExtraction,
											true).toString(), roundedScore,
							sortedElement.getSource()));

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			groupedAnswers.add(new GroupedAnswer(sortedGroup.getKey(), aList));
			answers.put(sortedGroup.getKey(), aList);
		}

		System.out.println("SCAN GENERATING: "
				+ (System.currentTimeMillis() - beginGenerating));

		return new Response(groupedAnswers.subList(0,
				Math.min(10, groupedAnswers.size())));
	}

	private double calculateGroupScore(List<Double> scoreList) {

		double max = Double.MIN_VALUE;
		for (Double score : scoreList) {
			if (max < score) {
				max = score;
			}
		}

		return max;

		// double scoreSum = 0.0;
		// for (Double score : scoreList) {
		// scoreSum += score;
		// }
		//
		// double unroundedGroupScore = scoreSum / scoreList.size();
		//
		// double roundedGroupScore = Math.round(unroundedGroupScore * 100) /
		// 100.0;
		//
		// return roundedGroupScore;

	}
}
