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

package edu.tuberlin.dima.nerdle.main;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.configuration.ConfigurationException;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

import edu.tuberlin.dima.nerdle.graph.NerdleGraphTransformer;
import edu.tuberlin.dima.nerdle.qa.SingletonGraph;

public class PredicateCounter {

//	public final static String PROPERTY_TEXT = NerdleGraphTransformer.PROPERTY_TEXT;
//	public final static String PROPERTY_NER = "ner";
//	public final static String PROPERTY_CLAUSE_TYPE = NerdleGraphTransformer.PROPERTY_CLAUSE_TYPE;
//	public final static String PROPERTY_SENTENCE = "sentence";
//	public final static String PROPERTY_PROBABILITY = "probability";

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Please enter a graph name.");
			System.err.println("graphs: memory-beta, simpsons, wookieepedia");
			System.err
					.println("Graph path should be set in the graphs.poperties file.");
			System.exit(1);
		}

		TinkerGraph graph;
		
		try {
			System.out.println("loading...");
			graph = SingletonGraph.getInstance().getGraphs().get(args[0]);
			System.out.println("loaded");
			
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			CountComparator bvc = new CountComparator(map);
			TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
			
			GremlinPipeline<Iterable<Vertex>, Vertex> pipeline = new GremlinPipeline<Iterable<Vertex>, Vertex>();
			
			pipeline.start(graph.getVertices(NerdleGraphTransformer.PROPERTY_CLAUSE_TYPE, NerdleGraphTransformer.VALUE_CLAUSE_TYPE_PREDICATE));
			
			for (Vertex vertex : pipeline) {
				System.out.println(".");
				String text = vertex.getProperty(NerdleGraphTransformer.PROPERTY_LEMMA);
				
				if (!map.containsKey(text)) {
					map.put(text, 1);
				} else {
					map.put(text, map.get(text)+1);
				}
			
			}
			
			sortedMap.putAll(map);
			
			System.out.println("sort");
			
			int index = 0;
			
			for (Entry<String, Integer> entry : sortedMap.entrySet()) {
				if (index > 20)
					break;
				
				System.out.println(entry.getKey() + ": " + entry.getValue());
				
				index++;
			}
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static class CountComparator implements Comparator<String> {

		Map<String, Integer> base;

		public CountComparator(Map<String, Integer> base) {
			this.base = base;
		}

		// Note: this comparator imposes orderings that are inconsistent with
		// equals.
		public int compare(String a, String b) {
			if (base.get(a) >= base.get(b)) {
				return -1;
			} else {
				return 1;
			} // returning 0 would merge keys
		}
	}

}
