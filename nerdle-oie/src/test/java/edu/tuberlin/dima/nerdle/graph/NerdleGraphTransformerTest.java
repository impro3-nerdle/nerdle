package edu.tuberlin.dima.nerdle.graph;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.tinkerpop.blueprints.Index;
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
		fact = new NerdleFact(
				"Einstein was born on 24th May in Ulm",
				"http://www.einstein.de/info", new NerdlePredicate("was born",
						"bear", synonyms), new NerdleSubject("Einstein"), args,
				1.0D);

		graph = new TinkerGraph();
		graph.createIndex("verb-idx", Vertex.class);
		
		NerdleGraphTransformer.transform(fact, graph);
		
		Index<Vertex> index = graph.getIndex("verb-idx", Vertex.class);
		Vertex vertex = index.get("verbIndex", "bear").iterator().next();
		transFact = NerdleGraphTransformer.transform(vertex);

		// JungGraphVisualizer visualizer = new JungGraphVisualizer();
		// visualizer.visualize(graph);
		// Thread.sleep(10000);

	}
	
	@Test
	public void testTransformer() throws Exception {
		Assert.assertEquals(fact, transFact);
	}
}
