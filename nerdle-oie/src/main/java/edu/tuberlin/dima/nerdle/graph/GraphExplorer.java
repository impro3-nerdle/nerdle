package edu.tuberlin.dima.nerdle.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.stringmetric.FuzzyStringMatcher;

public class GraphExplorer {

	public static List<NerdleFact> getMatchedFacts(
			NerdleFact questionExtraction, TinkerGraph graph) {

		List<NerdleFact> result = new ArrayList<NerdleFact>();

		String questionSubject = questionExtraction.getSubject().getText();

		Iterable<Vertex> it = graph.getVertices(
				NerdleGraphTransformer.PROPERTY_LEMMA, questionExtraction
						.getPredicate().getLemma());

		Iterator<Vertex> iterator = it.iterator();

		while (iterator.hasNext()) {
			Vertex answerVerbVertex = (Vertex) iterator.next();

			NerdleFact answerFact = NerdleGraphTransformer
					.transform(answerVerbVertex);

			if (questionSubject != null && !questionSubject.isEmpty()) {
				if (!FuzzyStringMatcher.fuzzySubjectMatcher(questionSubject,
						answerFact.getSubject().getText())) {
					continue;
				}
			}

			for (NerdleArg questionExtractionArg : questionExtraction
					.getArguments()) {

				if (!FuzzyStringMatcher.fuzzyArgumentMatcher(
						answerFact.getArguments(),
						questionExtractionArg.getText())) {
					continue;
				}

			}

			result.add(answerFact);

		}

		return result;
	}
}
