package edu.tuberlin.dima.qa;

import java.util.ArrayList;
import java.util.Arrays;
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
import edu.tuberlin.dima.nerdle.qa.QuestionAnswer;
import edu.tuberlin.dima.nerdle.qa.QuestionAnswerResponse;
import edu.tuberlin.dima.nerdle.qa.ScoreCalculator;
import edu.tuberlin.dima.nerdle.qa.ScoreComparator;
import edu.tuberlin.dima.nerdle.qa.Grouper;

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
//	@Test
//	public void testGrouping() throws ConfigurationException, InterruptedException {
//		String sentence = "Who was born in Ulm";
//		
//		QuestionAnswer questionParser = new QuestionAnswer();
//			QuestionAnswerResponse questionAnswerResponse = questionParser
//					.answerToQuestion(sentence, graph);
		
//
//	}
	
	@Test
	public void testGroupingOf() throws Exception{
		QuestionAnswer answerQuestion = new QuestionAnswer();

		// TreeMap<String, Double> jsonObject = answerQuestion.answerToWho(
		// "Who was born in Ulm on 4th May.", graph);

		QuestionAnswerResponse questionAnswerResponse = answerQuestion
				.answerToWho("Who was born in Ulm on 4th May.",
						graph);
		HashMap<String, List<NerdleFact>> subjectMap = Grouper
				.group(questionAnswerResponse, Grouper.TIME);

		System.out.println(subjectMap);
		
	}
	
	@Test
	public void testSortingOfGroup() throws ConfigurationException {
		
		QuestionAnswer answerQuestion = new QuestionAnswer();

		QuestionAnswerResponse questionAnswerResponse = answerQuestion
				.answerToWho("Who was born in Ulm on 4th May.",
						graph);
		HashMap<String, List<NerdleFact>> subjectMap = Grouper
				.group(questionAnswerResponse, Grouper.SUBJECT);
		
//		HashMap<String, ArrayList<Answer>> answers = new HashMap<String, ArrayList<Answer>>();
//
//		List<GroupedAnswer> groupedAnswers = new ArrayList<GroupedAnswer>();
		
		HashMap <String, List<NerdleFact>> sortedGroupAndElements = new HashMap<String, List<NerdleFact>>();
		HashMap <String, Double> groupToScore = new HashMap<String, Double>();
		GroupScoreComparator groupScoreComparator = new GroupScoreComparator(groupToScore);
		TreeMap<String, Double> sortedGroupMap = new TreeMap<String, Double>(groupScoreComparator);

		for (Entry<String, List<NerdleFact>> entry : subjectMap.entrySet()) {

//			ArrayList<Answer> aList = new ArrayList<Answer>();

			HashMap<NerdleFact, Double> map = new HashMap<NerdleFact, Double>();
			ScoreComparator bvc = new ScoreComparator(map);
			TreeMap<NerdleFact, Double> sortedMap = new TreeMap<NerdleFact, Double>(
					bvc);

			ArrayList<Double> scoreList = new ArrayList<Double>();
			for (NerdleFact extraction : entry.getValue()) {

				NerdleFact questionExtraction = questionAnswerResponse
						.getAnswerToQuestion().get(extraction);

				double score = ScoreCalculator.calculate(extraction,
						questionExtraction);
				scoreList.add(score);
				double roundedScore = Math.round(score * 100) / 100.0;
				map.put(extraction, roundedScore);

			}
			
			groupToScore.put(entry.getKey(), calculateGroupScore(scoreList));
			
			
			
			
			
			
			
			sortedMap.putAll(map);
			ArrayList<NerdleFact> facts = new ArrayList<NerdleFact>();
			
			facts.addAll(sortedMap.keySet());
			
			sortedGroupAndElements.put(entry.getKey(), facts);
			
			System.out.println("sortedMap:  "+sortedMap);
			System.out.println("AllSortedMap:  "+sortedGroupAndElements);

			//sorting elements within group
			for (Entry<NerdleFact, Double> element : sortedMap.entrySet()) {

				NerdleFact questionExtraction = questionAnswerResponse
						.getAnswerToQuestion().get(element.getKey());
				System.out.println(element.getKey());

//				try {
//
//					aList.add(new Answer(element.getKey().getPredicate()
//							.getText(), D3GraphGenerator
//							.generateD3GraphJson(element.getKey(), false)
//							.toString(), "", D3GraphGenerator
//							.generateD3GraphJson(questionExtraction, true)
//							.toString(), element.getValue()));
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
			}
//			groupedAnswers.add(new GroupedAnswer(entry.getKey(), aList));
//
//			answers.put(entry.getKey(), aList);

		}
		System.out.println(groupToScore);
		sortedGroupMap.putAll(groupToScore);
		
		System.out.println(sortedGroupMap);
		
		
		
		//TODO How is the score calculated? assigned nerdlefacts to sortedgroup
		
	}
	
	private double calculateGroupScore(List<Double> scoreList){
		double scoreSum=0.0;
		for(Double score: scoreList){
			scoreSum+=score;
		}
		
		double unroundedGroupScore = scoreSum/scoreList.size();
		
		double roundedGroupScore = Math.round(unroundedGroupScore * 100) / 100.0;
		
		return  roundedGroupScore;
	}

}
