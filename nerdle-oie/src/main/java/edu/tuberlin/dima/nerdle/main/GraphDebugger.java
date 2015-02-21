package edu.tuberlin.dima.nerdle.main;

import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;

import com.google.gson.Gson;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.tuberlin.dima.nerdle.graph.NerdleGraphTransformer;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.qa.SingletonGraph;

public class GraphDebugger {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Please enter a graph name.");
			System.err.println("graphs: memory-beta, simpsons, wookieepedia");
			System.err
					.println("Graph path should be set in the graphs.poperties file.");
			System.exit(1);
		}

		Gson gson = new Gson();
		TinkerGraph graph;
		try {
			graph = SingletonGraph.getInstance().getGraphs().get(args[0]);

			Iterable<Vertex> it = graph.getVertices();

			Iterator<Vertex> iterator = it.iterator();

			while (iterator.hasNext()) {
				Vertex answerVerbVertex = (Vertex) iterator.next();
				NerdleFact transform = NerdleGraphTransformer
						.transform(answerVerbVertex);

				if (transform.getSubject().getText().equals("Kang")) {
					System.out.println(gson.toJson(transform));
				}
			}

		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
