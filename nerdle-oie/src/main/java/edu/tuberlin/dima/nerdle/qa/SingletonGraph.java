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
			// TODO Auto-generated catch block
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
			
//			Resources.getResource("graphs.properties");
			
		}
		return SingletonGraph.instance;
	}

	public HashMap<String, TinkerGraph> getGraphs() {
		return graphs;
	}

}
