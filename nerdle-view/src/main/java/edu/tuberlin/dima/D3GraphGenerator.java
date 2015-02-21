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

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import edu.tuberlin.dima.nerdle.graph.NerdleGraphTransformer;
import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;

public class D3GraphGenerator {

	public static JSONArray generateD3GraphJson(Vertex vertex)
			throws JSONException {
		JSONObject graphNode = new JSONObject();
		JSONArray graphArray = new JSONArray();

		String predicateText = vertex
				.getProperty(NerdleGraphTransformer.PROPERTY_VERBTEXT);

		graphNode.put("source", predicateText);
		graphNode.put("target", predicateText);
		graphNode.put("argumentType",
				NerdleGraphTransformer.VALUE_CLAUSE_TYPE_PREDICATE);
		graphNode.put("ner", "");

		graphArray.put(graphNode);

		Iterator<Edge> edges = vertex.getEdges(Direction.OUT).iterator();

		while (edges.hasNext()) {
			Edge edge = (Edge) edges.next();
			Vertex v = edge.getVertex(Direction.IN);
			String cType = v
					.getProperty(NerdleGraphTransformer.PROPERTY_CLAUSE_TYPE);

			if (cType.equals(NerdleGraphTransformer.VALUE_CLAUSE_TYPE_SUBJECT)) {
				String subjectText = v
						.getProperty(NerdleGraphTransformer.PROPERTY_TEXT);
				String subjectNer = v
						.getProperty(NerdleGraphTransformer.PROPERTY_NER);
				graphNode = new JSONObject();
				graphNode.put("source", subjectText);
				graphNode.put("target", predicateText);
				graphNode.put("argumentType",
						NerdleGraphTransformer.VALUE_CLAUSE_TYPE_SUBJECT);
				graphNode.put("ner", subjectNer);
				graphArray.put(graphNode);
			}
			if (cType.equals(NerdleGraphTransformer.VALUE_CLAUSE_TYPE_ARGUMENT)) {
				String argumentText = v
						.getProperty(NerdleGraphTransformer.PROPERTY_TEXT);
				String argumentNer = v
						.getProperty(NerdleGraphTransformer.PROPERTY_NER);
				graphNode = new JSONObject();
				graphNode.put("source", predicateText);
				graphNode.put("target", argumentText);
				graphNode.put("argumentType",
						NerdleGraphTransformer.VALUE_CLAUSE_TYPE_ARGUMENT);
				graphNode.put("ner", argumentNer);
				graphArray.put(graphNode);
			}
		}
		return graphArray;
	}

	public static JSONArray generateD3GraphJson(NerdleFact NerdleFact,
			boolean isQuestion) throws JSONException {
		JSONObject graphNode = new JSONObject();
		JSONArray graphArray = new JSONArray();

		String predicateText = NerdleFact.getPredicate().getText();

		graphNode.put("source", predicateText);
		graphNode.put("target", predicateText);
		graphNode.put("argumentType",
				NerdleGraphTransformer.VALUE_CLAUSE_TYPE_PREDICATE);
		graphNode.put("ner", "P");
		graphArray.put(graphNode);

		String subjectText;
		if (isQuestion) {
			subjectText = "Who";
		} else {
			subjectText = NerdleFact.getSubject().getText();
		}

		graphNode = new JSONObject();
		graphNode.put("source", predicateText);
		graphNode.put("target", subjectText);
		graphNode.put("argumentType",
				NerdleGraphTransformer.VALUE_CLAUSE_TYPE_SUBJECT);
		graphArray.put(graphNode);

		Iterator<NerdleArg> arguments = NerdleFact.getArguments().iterator();

		while (arguments.hasNext()) {
			NerdleArg NerdleArg = (NerdleArg) arguments.next();

			String argumentText = NerdleArg.getText();
			String argumentNer = NerdleArg.getNer();
			graphNode = new JSONObject();
			graphNode.put("source", predicateText);
			graphNode.put("target", argumentText + " (argument)");
			graphNode.put("argumentType",
					NerdleGraphTransformer.VALUE_CLAUSE_TYPE_ARGUMENT);
			graphNode.put("ner", argumentNer);
			graphArray.put(graphNode);
		}

		List<String> synonyms = NerdleFact.getPredicate().getSynonyms();
		int count = 0;
		for (String synonym : synonyms) {
			graphNode = new JSONObject();
			graphNode.put("source", predicateText);
			graphNode.put("target", synonym +" (synonym)");
			graphNode.put("argumentType", "Synonym");
			graphArray.put(graphNode);
			count++;
			if (count == 5) {
				break;
			}
		}
		String lemma = NerdleFact.getPredicate().getLemma();

		if (lemma != null && !lemma.equals("")) {

			graphNode = new JSONObject();
			graphNode.put("source", predicateText);
			graphNode.put("target", lemma + " (lemma)");
			graphNode.put("argumentType", "Lemma");
			graphArray.put(graphNode);
		}

		return graphArray;
	}

}
