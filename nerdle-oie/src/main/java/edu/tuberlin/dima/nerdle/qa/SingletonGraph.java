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

package edu.tuberlin.dima.nerdle.qa;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.tuberlin.dima.nerdle.graph.NerdleGraphTransformer;
import edu.tuberlin.dima.nerdle.util.ResourceManager;

/**
 * creates the graph once and returns it
 * 
 * @author arif
 * 
 */
public class SingletonGraph {

	private static SingletonGraph instance;

	private HashMap<String, TinkerGraph> graphs;
	private PropertiesConfiguration propertiesConfiguration;

	private SingletonGraph() {
		try {
			propertiesConfiguration = new PropertiesConfiguration(ResourceManager.getResourcePath(File.separator+"graphs.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		String[] graphStrings = propertiesConfiguration.getStringArray("graphs");

		String graphsBasePath = propertiesConfiguration.getString("basePath");
		
		graphs = new HashMap<String, TinkerGraph>();
		
		for(String graph: graphStrings){
			TinkerGraph g = new TinkerGraph(graphsBasePath+File.separator+graph, TinkerGraph.FileType.GRAPHSON);
			g.createKeyIndex(NerdleGraphTransformer.PROPERTY_LEMMA, Vertex.class);
			graphs.put(graph, g);
		}
	}

	public static SingletonGraph getInstance() throws ConfigurationException {
		if (SingletonGraph.instance == null) {
			
			SingletonGraph.instance = new SingletonGraph();
			
		}
		return SingletonGraph.instance;
	}

	public HashMap<String, TinkerGraph> getGraphs() {
		return graphs;
	}

}
