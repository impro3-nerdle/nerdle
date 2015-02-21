package edu.tuberlin.dima.nerdle.graph;

import java.util.List;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;

import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.model.NerdlePredicate;
import edu.tuberlin.dima.nerdle.model.NerdleSubject;
import edu.tuberlin.dima.nerdle.stringmetric.FuzzyStringMatcher;

public class GraphMatcher {

	public static List<Vertex> getVertexesForSubject(
			NerdleFact questionExtraction, TinkerGraph graph) {
		final List<NerdleArg> questionsArguments = questionExtraction
				.getArguments();
		final NerdlePredicate questionPredicate = questionExtraction
				.getPredicate();

		GremlinPipeline<Iterable<Vertex>, Vertex> pipeline = new GremlinPipeline<Iterable<Vertex>, Vertex>();
		pipeline.filter(new PipeFunction<Vertex, Boolean>() {

			@Override
			public Boolean compute(Vertex vertex) {

				NerdleFact transform = NerdleGraphTransformer.transform(vertex);

				for (NerdleArg arg : transform.getArguments()) {

					boolean match = FuzzyStringMatcher.fuzzyArgumentMatcher(
							questionsArguments, arg.getText());

					if (match)
						return true;

				}

				return false;

			}

		});

		pipeline.start(graph.getVertices(NerdleGraphTransformer.PROPERTY_LEMMA,
				questionPredicate.getLemma()));
		List<Vertex> list = pipeline.toList();
		return list;
	}

	public static List<Vertex> getVertexesForArgument(
			NerdleFact questionExtraction, TinkerGraph graph) {
		final NerdlePredicate predicate = questionExtraction.getPredicate();
		final NerdleSubject subject = questionExtraction.getSubject();

		GremlinPipeline<Iterable<Vertex>, Vertex> pipeline = new GremlinPipeline<Iterable<Vertex>, Vertex>();
		pipeline.filter(new PipeFunction<Vertex, Boolean>() {

			@Override
			public Boolean compute(Vertex vertex) {
				boolean subjectMatch = false;
				boolean hasArgument = false;

				for (Vertex connectedVertex : vertex.getVertices(Direction.OUT)) {

					if (connectedVertex
							.getProperty(
									NerdleGraphTransformer.PROPERTY_CLAUSE_TYPE)
							.equals(NerdleGraphTransformer.VALUE_CLAUSE_TYPE_SUBJECT)) {
						subjectMatch = FuzzyStringMatcher.fuzzySubjectMatcher(
								subject.getText(),
								connectedVertex.getProperty(
										NerdleGraphTransformer.PROPERTY_TEXT)
										.toString());
					}

					if (connectedVertex
							.getProperty(
									NerdleGraphTransformer.PROPERTY_CLAUSE_TYPE)
							.equals(NerdleGraphTransformer.VALUE_CLAUSE_TYPE_ARGUMENT)) {
						hasArgument = true;

					}

				}

				if (subjectMatch && hasArgument) {
					return true;
				}

				return false;

			}

		});

		pipeline.start(graph.getVertices(NerdleGraphTransformer.PROPERTY_LEMMA,
				predicate.getLemma()));
		List<Vertex> list = pipeline.toList();

		// System.out.println(list);

		return list;
	}

}
