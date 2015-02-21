package edu.tuberlin.dima.nerdle.graph;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.model.NerdlePredicate;
import edu.tuberlin.dima.nerdle.model.NerdleSubject;

public class GraphMatcherTest {

	private static TinkerGraph graph;

	@BeforeClass
	public static void setup() {

		graph = new TinkerGraph();
		graph.createIndex("verb-idx", Vertex.class);
		/* Add extractions to graph */

		// exact answer
		NerdleFact extraction = new NerdleFact(
				"Einstein was born in Ulm on 4th May.", "", 0.5D);

		extraction.setPredicate(new NerdlePredicate("was born", ""));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("in Ulm", "L"));
		extraction.addArgument(new NerdleArg("on 4th May", "T"));

		NerdleGraphTransformer.transform(extraction, graph);

		// one argument missing
		extraction = new NerdleFact("Einstein was born in Ulm.", "", 0.5D);

		extraction.setPredicate(new NerdlePredicate("was born", ""));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("in Ulm", "L"));

		NerdleGraphTransformer.transform(extraction, graph);

		// one argument missing
		extraction = new NerdleFact("Einstein was born on 4th May.", "", 0.5D);

		extraction.setPredicate(new NerdlePredicate("was born", ""));
		extraction.setSubject(new NerdleSubject("Einstein"));
		extraction.addArgument(new NerdleArg("on 4th May", "T"));

		NerdleGraphTransformer.transform(extraction, graph);
		
		// different
		extraction = new NerdleFact("Groblen was captain of Troika.", "", 0.5D);

		extraction.setPredicate(new NerdlePredicate("was captain", ""));
		extraction.setSubject(new NerdleSubject("Groblen"));
		extraction.addArgument(new NerdleArg("of Troika","T"));

		NerdleGraphTransformer.transform(extraction, graph);
		
		// different
		extraction = new NerdleFact("Groblen attacked star strek caption of Andria.", "", 0.5D);

		extraction.setPredicate(new NerdlePredicate("attacked", ""));
		extraction.setSubject(new NerdleSubject("Groblen"));
		extraction.addArgument(new NerdleArg("star strek caption of Andria", "T"));

		NerdleGraphTransformer.transform(extraction, graph);

		
	}

	@Test
	@Ignore
	public void testMatchedVertexes() {

		NerdleFact questionExtraction = new NerdleFact(
				"Peter was born in Ulm on 4th May.", "", 0.63D);
		NerdlePredicate predicate = new NerdlePredicate("was born", "");
		NerdleSubject subject = new NerdleSubject("Peter");
		NerdleArg argument1 = new NerdleArg("in Ulm","L");
		NerdleArg argument2 = new NerdleArg("on 4th May","T");

		questionExtraction.setPredicate(predicate);
		questionExtraction.setSubject(subject);
		questionExtraction.addArgument(argument1);
		questionExtraction.addArgument(argument2);

		List<Vertex> matchedVertexes = GraphMatcher.getVertexesForSubject(
				questionExtraction, graph);
		System.out.println(matchedVertexes);
		for(Vertex vertex: matchedVertexes){
			System.out.println(NerdleGraphTransformer.transform(vertex));
		}
		assertEquals(3, matchedVertexes.size());

	}

	@Test
	@Ignore
	public void testFuzzyVerb() {
		NerdleFact questionExtraction = new NerdleFact(
				"Peter born in Ulm on 4th May.", "", 0.63D);
		NerdlePredicate predicate = new NerdlePredicate("wam born", ""); // wam
																			// born
																			// <->
																			// was
																			// born
		NerdleSubject subject = new NerdleSubject("Peter");
		NerdleArg argument1 = new NerdleArg("in Ulm", "L");
		NerdleArg argument2 = new NerdleArg("on the 4th May", "T");

		questionExtraction.setPredicate(predicate);
		questionExtraction.setSubject(subject);
		questionExtraction.addArgument(argument1);
		questionExtraction.addArgument(argument2);

		List<Vertex> matchedVertexes = GraphMatcher.getVertexesForSubject(
				questionExtraction, graph);

		assertEquals(3, matchedVertexes.size());

	}

	@Test
	@Ignore
	public void testPartialVerb() {

		NerdleFact questionExtraction = new NerdleFact(
				"Peter born in Ulm on 4th May.", "", 0.63D);
		NerdlePredicate predicate = new NerdlePredicate("born", ""); // was
																		// <->
																		// was
																		// born
		NerdleSubject subject = new NerdleSubject("Peter");
		NerdleArg argument1 = new NerdleArg("in Ulm","L");
		NerdleArg argument2 = new NerdleArg("on the 4th May", "T");

		questionExtraction.setPredicate(predicate);
		questionExtraction.setSubject(subject);
		questionExtraction.addArgument(argument1);
		questionExtraction.addArgument(argument2);

		List<Vertex> matchedVertexes = GraphMatcher.getVertexesForSubject(
				questionExtraction, graph);

		assertEquals(3, matchedVertexes.size());
	}

	@Test
	@Ignore
	public void testFuzzyArgument() {

		NerdleFact questionExtraction = new NerdleFact(
				"Peter born in Ulm on 4th May.", "", 0.63D);
		NerdlePredicate predicate = new NerdlePredicate("was born", "");
		NerdleSubject subject = new NerdleSubject("Peter");
		NerdleArg argument1 = new NerdleArg("at Ulm", "L"); // at
																			// Ulm
																			// <->
																			// in
																			// Ulm
		NerdleArg argument2 = new NerdleArg("on the 4th May", "T");

		questionExtraction.setPredicate(predicate);
		questionExtraction.setSubject(subject);
		questionExtraction.addArgument(argument1);
		questionExtraction.addArgument(argument2);

		List<Vertex> matchedVertexes = GraphMatcher.getVertexesForSubject(
				questionExtraction, graph);

		assertEquals(3, matchedVertexes.size());
	}

	@Test
	@Ignore
	public void testPartialArgument() {

		NerdleFact questionExtraction = new NerdleFact(
				"Peter born in Ulm on 4th May.", "", 0.63D);
		NerdlePredicate predicate = new NerdlePredicate("was born", "");
		NerdleSubject subject = new NerdleSubject("Peter");
		NerdleArg argument1 = new NerdleArg("Ulm", "L"); // Ulm
																		// <->
																		// in
																		// Ulm
		NerdleArg argument2 = new NerdleArg("on the 4th May", "T");

		questionExtraction.setPredicate(predicate);
		questionExtraction.setSubject(subject);
		questionExtraction.addArgument(argument1);
		questionExtraction.addArgument(argument2);

		List<Vertex> matchedVertexes = GraphMatcher.getVertexesForSubject(
				questionExtraction, graph);

		assertEquals(3, matchedVertexes.size());
	}

}
