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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.tuberlin.dima.nerdle.OpenIEHelper;
import edu.tuberlin.dima.nerdle.graph.GraphMatcher;
import edu.tuberlin.dima.nerdle.graph.NerdleGraphTransformer;
import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.model.NerdlePredicate;

public class QuestionAnswer {

	private static double TRESHOLD_WH0 = 50.0;
	private static double TRESHOLD_WHICH = 50.0;

	public QuestionAnswerResponse answerToQuestion(String question,
			TinkerGraph graph) throws InterruptedException,
			ConfigurationException {

		QuestionAnswerResponse answers = null;

		if (question.toLowerCase().contains("who")) {
			answers = answerToWho(question, graph);
		} else if (question.toLowerCase().contains("which")) {
			answers = answerToWhich(question, graph);
		} else if (question.toLowerCase().contains("where")) {
			answers = answerToWhere(question, graph);
		} else if (question.toLowerCase().contains("when")) {
			answers = answerToWhen(question, graph);
		}

		return answers;
	}

	public QuestionAnswerResponse answerToWho(String question, TinkerGraph graph)
			throws ConfigurationException {

		String cleanedSentence = question.replaceAll("^(Who|who)", "Peter");

		List<NerdleFact> extractions = OpenIEHelper.INSTANCE
				.extractFactsFromArticleText(cleanedSentence, "", true);

		HashMap<NerdleFact, Double> answerToScore = new HashMap<NerdleFact, Double>();
		HashMap<NerdleFact, NerdleFact> answerToQuestion = new HashMap<NerdleFact, NerdleFact>();

		for (NerdleFact nerdleFact : extractions) {
			List<Vertex> vertices = GraphMatcher.getVertexesForSubject(
					nerdleFact, graph);
			for (Vertex vertex : vertices) {

				NerdleFact transform = NerdleGraphTransformer.transform(vertex);


				double score = ScoreCalculator.calculateForSubject(nerdleFact,
						transform);

				if (score > TRESHOLD_WH0) {
					answerToScore.put(transform, score);
					answerToQuestion.put(transform, nerdleFact);
				}

			}

		}

		QuestionAnswerResponse questionAnswerResponse = new QuestionAnswerResponse(
				answerToScore, answerToQuestion, QuestionType.WHO);

		return questionAnswerResponse;
	}

	public QuestionAnswerResponse answerToWhich(String question,
			TinkerGraph graph) throws InterruptedException,
			ConfigurationException {

		String cleanedQuestion = question.substring(5).trim();
		
		System.out.println("cleanedQuestion:" + cleanedQuestion);
		System.out.println();
		
		List<NerdleFact> extractions = OpenIEHelper.INSTANCE
				.extractFactsFromArticleText(cleanedQuestion, "", true);

		NerdleFact bestExtraction = getMaxConfidenceExtraction(extractions);
		
		System.out.println("bestExtraction: ");
		System.out.println(bestExtraction);
		System.out.println();
		
		HashMap<NerdleFact, Double> answerToScore = new HashMap<NerdleFact, Double>();
		HashMap<NerdleFact, Double> answerToScoreTemp = new HashMap<NerdleFact, Double>();
		HashMap<NerdleFact, NerdleFact> answerToQuestion = new HashMap<NerdleFact, NerdleFact>();

		if (bestExtraction != null) {

			// TODO: Lemmas missing
			NerdleFact firstWhoExtraction = new NerdleFact("", "", 0.0);
			firstWhoExtraction.setPredicate(new NerdlePredicate("was", "be"));
			firstWhoExtraction.addArgument(new NerdleArg(bestExtraction
					.getSubject().getText(), ""));
			
			System.out.println("firstWhoExtraction");
			System.out.println(firstWhoExtraction);
			System.out.println();
			
			NerdleFact secondWhoExtraction = new NerdleFact("", "", 0.0);
			secondWhoExtraction.setPredicate(bestExtraction.getPredicate());
			secondWhoExtraction.setArguments(bestExtraction.getArguments());
			
			System.out.println("secondWhoExtraction");
			System.out.println(secondWhoExtraction);
			System.out.println();
			
			List<Vertex> firstVertices = GraphMatcher.getVertexesForSubject(
					firstWhoExtraction, graph);
			List<Vertex> thirdVertices = GraphMatcher.getVertexesForSubject(
					secondWhoExtraction, graph);

			List<NerdleFact> firstAnswers = new ArrayList<NerdleFact>();
			List<NerdleFact> thirdAnswers = new ArrayList<NerdleFact>();

			for (Vertex vertex : firstVertices) {
				NerdleFact transform = NerdleGraphTransformer.transform(vertex);

				double score = ScoreCalculator.calculateForSubject(
						firstWhoExtraction, transform);

				if (score > TRESHOLD_WHICH) {
					answerToScoreTemp.put(transform, score);
					firstAnswers.add(transform);
				}

			}

			for (Vertex vertex : thirdVertices) {
				NerdleFact transform = NerdleGraphTransformer.transform(vertex);

				double score = ScoreCalculator.calculateForSubject(
						secondWhoExtraction, transform);

				if (score > TRESHOLD_WHICH) {
					answerToScoreTemp.put(transform, score);
					thirdAnswers.add(transform);
				}
			}

			for (NerdleFact firstAnswer : firstAnswers) {

				for (NerdleFact thirdAnswer : thirdAnswers) {

					if (firstAnswer.getSubject().getText()
							.equals(thirdAnswer.getSubject().getText())) {

						if (!answerToScore.containsKey(firstAnswer)) {
							answerToScore.put(firstAnswer,
									answerToScoreTemp.get(firstAnswer));
						}

						if (!answerToScore.containsKey(thirdAnswer)) {
							answerToScore.put(thirdAnswer,
									answerToScoreTemp.get(thirdAnswer));
						}

						answerToQuestion.put(firstAnswer, firstWhoExtraction);

						answerToQuestion.put(thirdAnswer, secondWhoExtraction);

					}

				}

			}
		}

		QuestionAnswerResponse questionAnswerResponse = new QuestionAnswerResponse(
				answerToScore, answerToQuestion, QuestionType.WHICH);

		return questionAnswerResponse;
	}

	private NerdleFact getMaxConfidenceExtraction(List<NerdleFact> extractions) {
		double value = 0.0;
		NerdleFact maxNerdleExtraction = null;
		for (NerdleFact nerdleExtraction : extractions) {
			if (nerdleExtraction.getConfidence() >= value) {
				value = nerdleExtraction.getConfidence();
				maxNerdleExtraction = nerdleExtraction;
			}
		}
		return maxNerdleExtraction;
	}

	public QuestionAnswerResponse answerToWhere(String question,
			TinkerGraph graph) throws InterruptedException,
			ConfigurationException {

		String auxiliaryVerbs = "(be|is|are|was|were|"
				+ "do|does|did|have|had|has)";

		String cleanedSentence = question.replaceAll("^(Where|where) "
				+ auxiliaryVerbs + " ([\\w\\s]*)", "$3 in Berlin");

		List<NerdleFact> extractions = OpenIEHelper.INSTANCE
				.extractFactsFromArticleText(cleanedSentence, "", true);

		HashMap<NerdleFact, Double> answerToScore = new HashMap<NerdleFact, Double>();
		HashMap<NerdleFact, NerdleFact> answerToQuestion = new HashMap<NerdleFact, NerdleFact>();

		for (NerdleFact nerdleExtraction : extractions) {

			List<Vertex> vertices = GraphMatcher.getVertexesForArgument(
					nerdleExtraction, graph);

			for (Vertex vertex : vertices) {
				boolean location = false;
				Iterator<Edge> edges = vertex.getEdges(Direction.OUT)
						.iterator();

				while (edges.hasNext()) {
					Edge edge = (Edge) edges.next();
					Vertex v = edge.getVertex(Direction.IN);
					String cType = v
							.getProperty(NerdleGraphTransformer.PROPERTY_CLAUSE_TYPE);
					if (cType
							.equals(NerdleGraphTransformer.VALUE_CLAUSE_TYPE_ARGUMENT)) {
						String argumentNer = v
								.getProperty(NerdleGraphTransformer.PROPERTY_NER);
						if (argumentNer.equals("L")) {
							location = true;
						} else {

						}
					}
				}
				if (location) {
					NerdleFact transform = NerdleGraphTransformer
							.transform(vertex);
					answerToScore.put(transform, ScoreCalculator
							.calculateForArguments(nerdleExtraction, transform,
									"L"));

					answerToQuestion.put(transform, nerdleExtraction);
				}
			}

		}

		QuestionAnswerResponse questionAnswerResponse = new QuestionAnswerResponse(
				answerToScore, answerToQuestion, QuestionType.WHERE);

		return questionAnswerResponse;
	}

	public QuestionAnswerResponse answerToWhen(String question,
			TinkerGraph graph) throws InterruptedException,
			ConfigurationException {

		String auxiliaryVerbs = "(be|is|are|was|were|"
				+ "do|does|did|have|had|has)";

		String cleanedSentence = question.replaceAll("^(When|when) "
				+ auxiliaryVerbs + " ([\\w\\s]*)", "$3 in 1990");

		List<NerdleFact> extractions = OpenIEHelper.INSTANCE
				.extractFactsFromArticleText(cleanedSentence, "", true);

		HashMap<NerdleFact, Double> answerToScore = new HashMap<NerdleFact, Double>();
		HashMap<NerdleFact, NerdleFact> answerToQuestion = new HashMap<NerdleFact, NerdleFact>();

		for (NerdleFact nerdleExtraction : extractions) {

			List<Vertex> vertices = GraphMatcher.getVertexesForArgument(
					nerdleExtraction, graph);

			for (Vertex vertex : vertices) {
				boolean time = false;

				Iterator<Edge> edges = vertex.getEdges(Direction.OUT)
						.iterator();

				while (edges.hasNext()) {
					Edge edge = (Edge) edges.next();
					Vertex v = edge.getVertex(Direction.IN);
					String cType = v
							.getProperty(NerdleGraphTransformer.PROPERTY_CLAUSE_TYPE);
					if (cType
							.equals(NerdleGraphTransformer.VALUE_CLAUSE_TYPE_ARGUMENT)) {
						String argumentNer = v
								.getProperty(NerdleGraphTransformer.PROPERTY_NER);

						if (argumentNer.equals("T")) {
							time = true;
						}
					}
				}
				if (time) {
					NerdleFact transform = NerdleGraphTransformer
							.transform(vertex);
					answerToScore.put(transform, ScoreCalculator
							.calculateForArguments(nerdleExtraction, transform,
									"T"));

					answerToQuestion.put(transform, nerdleExtraction);
				}
			}

		}

		QuestionAnswerResponse questionAnswerResponse = new QuestionAnswerResponse(
				answerToScore, answerToQuestion, QuestionType.WHEN);

		return questionAnswerResponse;
	}

}
