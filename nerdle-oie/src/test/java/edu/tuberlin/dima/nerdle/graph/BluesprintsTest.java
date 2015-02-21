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

import org.junit.Test;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

public class BluesprintsTest {
	
	@Test
	public void testAPI() throws Exception {
		TinkerGraph graph = new TinkerGraph();
		Vertex a = graph.addVertex(null);
		Vertex b = graph.addVertex(null);
		a.setProperty("name","marko");
		b.setProperty("name","peter");
		Edge e = graph.addEdge(null, a, b, "knows");
		e.setProperty("since", 2006);
	}
	
	@Test
	public void testEisteinClause() throws Exception {
		TinkerGraph graph = new TinkerGraph();
		graph.createIndex("verb-idx", Vertex.class);
		
		Vertex a = graph.addVertex(null);
		Vertex b = graph.addVertex(null);
		Vertex c = graph.addVertex(null);
		Vertex d = graph.addVertex(null);
		
		a.setProperty(NerdleGraphTransformer.PROPERTY_TEXT,"Einstein");
		a.setProperty("ner", "PERSON");
		a.setProperty("clause_type", NerdleGraphTransformer.VALUE_CLAUSE_TYPE_SUBJECT);
		
		b.setProperty(NerdleGraphTransformer.PROPERTY_TEXT,"in Ulm");
		b.setProperty("ner", "PERSON");
		b.setProperty("clause_type", NerdleGraphTransformer.VALUE_CLAUSE_TYPE_ARGUMENT);
		
		c.setProperty(NerdleGraphTransformer.PROPERTY_TEXT,"on 4th May");
		c.setProperty("ner", "PERSON");
		c.setProperty("clause_type", NerdleGraphTransformer.VALUE_CLAUSE_TYPE_ARGUMENT);
		
		d.setProperty(NerdleGraphTransformer.PROPERTY_TEXT,"was born");
		d.setProperty("clause_type", NerdleGraphTransformer.VALUE_CLAUSE_TYPE_PREDICATE);
	}
	
	@Test
	public void testMultipleClauses() throws Exception {
		TinkerGraph graph = new TinkerGraph();
		graph.createIndex("verb-idx", Vertex.class);
		
		Vertex a = graph.addVertex(null);
		Vertex b = graph.addVertex(null);
		Vertex c = graph.addVertex(null);
		Vertex d = graph.addVertex(null);
		
		a.setProperty(NerdleGraphTransformer.PROPERTY_TEXT,"Einstein");
		a.setProperty("ner", "PERSON");
		a.setProperty("clause_type", NerdleGraphTransformer.VALUE_CLAUSE_TYPE_SUBJECT);
		
		b.setProperty(NerdleGraphTransformer.PROPERTY_TEXT,"in Ulm");
		b.setProperty("ner", "PERSON");
		b.setProperty("clause_type", NerdleGraphTransformer.VALUE_CLAUSE_TYPE_ARGUMENT);
		
		c.setProperty(NerdleGraphTransformer.PROPERTY_TEXT,"on 4th May");
		c.setProperty("ner", "PERSON");
		c.setProperty("clause_type", NerdleGraphTransformer.VALUE_CLAUSE_TYPE_ARGUMENT);
		
		d.setProperty(NerdleGraphTransformer.PROPERTY_TEXT,"was born");
		d.setProperty("clause_type", NerdleGraphTransformer.VALUE_CLAUSE_TYPE_PREDICATE);
		
		graph.addEdge(null, d, a, "");
		graph.addEdge(null, d, b, "");
		graph.addEdge(null, d, c, "");
		
		Vertex aX = graph.addVertex(null);
		Vertex bX = graph.addVertex(null);
		Vertex cX = graph.addVertex(null);
		Vertex dX = graph.addVertex(null);
		
		aX.setProperty(NerdleGraphTransformer.PROPERTY_TEXT,"Einstein X");
		aX.setProperty("ner", "PERSON");
		aX.setProperty("clause_type", NerdleGraphTransformer.VALUE_CLAUSE_TYPE_SUBJECT);
		
		bX.setProperty(NerdleGraphTransformer.PROPERTY_TEXT,"in Ulm X");
		bX.setProperty("ner", "PERSON");
		bX.setProperty("clause_type", NerdleGraphTransformer.VALUE_CLAUSE_TYPE_ARGUMENT);
		
		cX.setProperty(NerdleGraphTransformer.PROPERTY_TEXT,"on 4th May");
		cX.setProperty("ner", "PERSON X");
		cX.setProperty("clause_type", NerdleGraphTransformer.VALUE_CLAUSE_TYPE_ARGUMENT);
		
		dX.setProperty(NerdleGraphTransformer.PROPERTY_TEXT,"was born X");
		dX.setProperty("clause_type", NerdleGraphTransformer.VALUE_CLAUSE_TYPE_PREDICATE);
		
		graph.addEdge(null, dX, aX, "");
		graph.addEdge(null, dX, bX, "");
		graph.addEdge(null, dX, cX, "");
	}	
	
}
