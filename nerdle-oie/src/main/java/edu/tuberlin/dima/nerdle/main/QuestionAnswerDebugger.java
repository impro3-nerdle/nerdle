package edu.tuberlin.dima.nerdle.main;

import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.qa.QuestionAnswer;
import edu.tuberlin.dima.nerdle.qa.QuestionAnswerResponse;
import edu.tuberlin.dima.nerdle.qa.SingletonGraph;

public class QuestionAnswerDebugger {

	public static void main(String[] args) throws Exception {

		SingletonGraph.getInstance();

		TinkerGraph graph = SingletonGraph.getInstance().getGraphs()
				.get("simpsons");

		// Index<Vertex> index = graph.getIndex("verb-idx", Vertex.class);
		// Iterator<Vertex> it = index.get("verbIndex", "sleeps").iterator();
		// while (it.hasNext()) {
		// Vertex vertex = (Vertex) it.next();
		// System.out.println(NerdleGraphTransformer.transform(vertex));
		// }

		// while (it.hasNext()) {
		// Vertex v = (Vertex) it.next();
		// System.out.println("found");
		// }

		QuestionAnswer answerQuestion = new QuestionAnswer();

		QuestionAnswerResponse questionAnswerResponse = answerQuestion
				.answerToWhen("Which 118 years old runs for Governor?", graph);

		// System.out.println(questionAnswerResponse.getAnswerToScore());

		for (NerdleFact fact : questionAnswerResponse.getAnswerToScore()
				.keySet()) {
			System.out.println(fact);

		}
	}
}
