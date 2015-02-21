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

import org.apache.commons.configuration.ConfigurationException;

import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

import edu.tuberlin.dima.nerdle.graph.NerdleGraphTransformer;
import edu.tuberlin.dima.nerdle.qa.SingletonGraph;

public class GraphFactsCounter {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Please enter a graph name.");
			System.err.println("graphs: memory-beta, simpsons, wookieepedia");
			System.err
					.println("Graph path should be set in the graphs.poperties file.");
			System.exit(1);
		}
		

		TinkerGraph graph;
		try {
			graph = SingletonGraph.getInstance().getGraphs().get(args[0]);
			
			Index<Vertex> index = graph.getIndex("verb-idx", Vertex.class);
			GremlinPipeline<Iterable<Vertex>, Vertex> pipeline = new GremlinPipeline<Iterable<Vertex>, Vertex>();

			pipeline.start(index.get("verbIndex", null)).hasNot(NerdleGraphTransformer.PROPERTY_ISSYNONYM, true);
//			System.out.println(pipeline.toList().size());
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

}
