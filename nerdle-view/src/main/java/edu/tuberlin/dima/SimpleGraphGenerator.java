package edu.tuberlin.dima;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import edu.tuberlin.dima.nerdle.graph.NerdleGraphTransformer;
import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;

public class SimpleGraphGenerator {
	
	public static JSONObject generateSimpleGraphJson(Vertex vertex)
			throws JSONException {
		JSONObject graphJson = new JSONObject();
		JSONArray nodesArray = new JSONArray();
		JSONArray edgesArray = new JSONArray();

		String predicateText = vertex
				.getProperty(NerdleGraphTransformer.PROPERTY_TEXT);

		nodesArray.put(predicateText);

		Iterator<Edge> edges = vertex.getEdges(Direction.OUT).iterator();

		while (edges.hasNext()) {
			Edge edge = (Edge) edges.next();
			Vertex v = edge.getVertex(Direction.IN);
			String text = v.getProperty(NerdleGraphTransformer.PROPERTY_TEXT);
			nodesArray.put(text);
			JSONArray link = new JSONArray();
			link.put(predicateText);
			link.put(text);
			edgesArray.put(link);
		}

		graphJson.put("nodes", nodesArray);
		graphJson.put("edges", edgesArray);

		return graphJson;
	}

	public static JSONObject generateSimpleGraphJson(NerdleFact NerdleFact)
			throws JSONException {
		JSONObject graphJson = new JSONObject();
		JSONArray nodesArray = new JSONArray();
		JSONArray edgesArray = new JSONArray();

		String predicateText = NerdleFact.getPredicate().getText();
		nodesArray.put(predicateText);

		nodesArray.put(NerdleFact.getSubject().getText());
		JSONArray link = new JSONArray();
		link.put(predicateText);
		link.put(NerdleFact.getSubject().getText());
		edgesArray.put(link);

		for (NerdleArg NerdleArg : NerdleFact.getArguments()) {
			nodesArray.put(NerdleArg.getText());
			link = new JSONArray();
			link.put(predicateText);
			link.put(NerdleArg.getText());
			edgesArray.put(link);
		}

		graphJson.put("nodes", nodesArray);
		graphJson.put("edges", edgesArray);

		return graphJson;
	}

}
