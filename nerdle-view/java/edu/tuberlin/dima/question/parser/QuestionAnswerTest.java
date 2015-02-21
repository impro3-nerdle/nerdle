package edu.tuberlin.dima.question.parser;

import static org.junit.Assert.assertEquals;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.tuberlin.dima.nerdle.graph.NerdleGraphTransformer;
import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.model.NerdlePredicate;
import edu.tuberlin.dima.nerdle.model.NerdleSubject;
import edu.tuberlin.dima.nerdle.qa.QuestionAnswer;
import edu.tuberlin.dima.nerdle.qa.QuestionAnswerResponse;

public class QuestionAnswerTest {

	private static TinkerGraph graph;

	@BeforeClass
	public static void setup() {

		graph = new TinkerGraph();
		graph.createIndex("verb-idx", Vertex.class);
		/* Add extractions to graph */

		// exact answer
		NerdleFact extraction = new NerdleFact(
				"Einstein was born in Ulm on 4th May.", "", 0.5D);

		extraction.setPredicate(new NerdlePredicate("was born", "bear"));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("in Ulm", "L"));
		extraction.addArgument(new NerdleArg("on 4th May", "T"));

		NerdleGraphTransformer.transform(extraction, graph);

		// exact answer
		extraction = new NerdleFact("Einstein was a professor.", "", 0.5D);

		extraction.setPredicate(new NerdlePredicate("was", "be"));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("a professor", "T"));

		NerdleGraphTransformer.transform(extraction, graph);

		// false answer
		extraction = new NerdleFact("Leibnitz was a professor.", "", 0.5D);

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
		extraction = new NerdleFact("Gauss was a professor.", "", 0.5D);

		extraction.setPredicate(new NerdlePredicate("was", "be"));
		extraction.setSubject(new NerdleSubject("Gauss"));
		extraction.addArgument(new NerdleArg("a professor", "T"));

		NerdleGraphTransformer.transform(extraction, graph);

		// one argument missing
		extraction = new NerdleFact("Einstein was born in Ulm.", "", 0.5D);

		extraction.setPredicate(new NerdlePredicate("was born", "bear"));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("in Ulm", "L"));

		NerdleGraphTransformer.transform(extraction, graph);

		// one argument missing
		extraction = new NerdleFact("Einstein was born on 4th May.", "", 0.5D);

		extraction.setPredicate(new NerdlePredicate("was born", "bear"));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("on 4th May", "T"));

		NerdleGraphTransformer.transform(extraction, graph);

	}

	@Test
	public void answerToWhich() throws InterruptedException,
			ConfigurationException {
		QuestionAnswer answerQuestion = new QuestionAnswer();

		// TreeMap<String, Double> jsonObject = answerQuestion.answerToWho(
		// "Who was born in Ulm on 4th May.", graph);

		QuestionAnswerResponse questionAnswerResponse = answerQuestion
				.answerToWhich("Which professor was born on 4th May in Ulm.",
						graph);
		System.out.println(questionAnswerResponse.getAnswerToScore());
		assertEquals(6, questionAnswerResponse.getAnswerToScore().size());
	}

	@Test
	public void answerToWho() throws InterruptedException,
			ConfigurationException {
		QuestionAnswer answerQuestion = new QuestionAnswer();

		// TreeMap<String, Double> jsonObject = answerQuestion.answerToWho(
		// "Who was born in Ulm on 4th May.", graph);

		QuestionAnswerResponse questionAnswerResponse = answerQuestion
				.answerToWho("Who was born in Ulm.", graph);
		assertEquals(2, questionAnswerResponse.getAnswerToScore().size());
	}

	@Test
	public void answerToWhere() throws InterruptedException,
			ConfigurationException {
		QuestionAnswer answerQuestion = new QuestionAnswer();

		QuestionAnswerResponse map = answerQuestion.answerToWhere(
				"Where was Einstein born?", graph);
		System.out.println(map.getAnswerToQuestion());
		System.out.println(map.getAnswerToScore());

		QuestionAnswerResponse map2 = answerQuestion.answerToWhere(
				"Where was Einstein born on 4th May?", graph);
		System.out.println(map2.getAnswerToQuestion());
		System.out.println(map2.getAnswerToScore());
	}

	@Test
	public void answerToWhen() throws InterruptedException,
			ConfigurationException {
		QuestionAnswer answerQuestion = new QuestionAnswer();

		QuestionAnswerResponse map = answerQuestion.answerToWhen(
				"When was Einstein born?", graph);
		System.out.println(map.getAnswerToQuestion());
		System.out.println(map.getAnswerToScore());

		QuestionAnswerResponse map2 = answerQuestion.answerToWhen(
				"When was Einstein born in Ulm?", graph);
		System.out.println(map2.getAnswerToQuestion());
		System.out.println(map2.getAnswerToScore());
	}

}
