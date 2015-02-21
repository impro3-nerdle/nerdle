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
