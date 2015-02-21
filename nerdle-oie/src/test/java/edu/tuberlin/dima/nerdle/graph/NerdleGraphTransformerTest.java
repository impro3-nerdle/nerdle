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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.model.NerdlePredicate;
import edu.tuberlin.dima.nerdle.model.NerdleSubject;

public class NerdleGraphTransformerTest {

	TinkerGraph graph;
	NerdleFact fact;
	NerdleFact transFact;

	@Before
	public void prepareTransformer() throws Exception {
		List<String> synonyms = new ArrayList<String>();
		synonyms.add("deliver");
		synonyms.add("give birth");
		synonyms.add("have");
		List<NerdleArg> args = new ArrayList<NerdleArg>();
		args.add(new NerdleArg("on 24th May", "T"));
		args.add(new NerdleArg("in Ulm", "L"));
		fact = new NerdleFact("Einstein was born on 24th May in Ulm",
				"http://www.einstein.de/info", new NerdlePredicate("was born",
						"bear", synonyms), new NerdleSubject("Einstein"), args,
				1.0D);

		graph = new TinkerGraph();

		NerdleGraphTransformer.transform(fact, graph);

		Iterator<Vertex> iterator = graph.getVertices().iterator();

		while (iterator.hasNext()) {
			Vertex vertex = (Vertex) iterator.next();

			if (vertex.getProperty(NerdleGraphTransformer.PROPERTY_CLAUSE_TYPE) == NerdleGraphTransformer.VALUE_CLAUSE_TYPE_PREDICATE
					&& (Boolean) vertex
							.getProperty(NerdleGraphTransformer.PROPERTY_ISSYNONYM) == false) {
				transFact = NerdleGraphTransformer.transform(vertex);
			}
		}

		// JungGraphVisualizer visualizer = new JungGraphVisualizer();
		// visualizer.visualize(graph);
		// Thread.sleep(10000);

	}

	@Test
	public void testTransformer() throws Exception {
		Assert.assertEquals(fact, transFact);
	}
}
