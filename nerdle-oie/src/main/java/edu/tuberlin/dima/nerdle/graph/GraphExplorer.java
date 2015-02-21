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
