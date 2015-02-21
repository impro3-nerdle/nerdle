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

import java.awt.Dimension;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory;
import com.tinkerpop.blueprints.oupls.jung.GraphJung;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class JungGraphVisualizer {

	public void visualize(TinkerGraph graph) {
		GraphJung<TinkerGraph> graphJung = new GraphJung<TinkerGraph>(graph);
		Layout<Vertex, Edge> layout = new CircleLayout<Vertex, Edge>(graphJung);
		layout.setSize(new Dimension(400, 400));

		BasicVisualizationServer<Vertex, Edge> viz = new BasicVisualizationServer<Vertex, Edge>(
				layout);
		viz.setPreferredSize(new Dimension(550, 550));

		Transformer<Vertex, String> vertexLabelTransformer = new Transformer<Vertex, String>() {
			public String transform(Vertex vertex) {

				String vertexLabel = "";

				if (vertex
						.getProperty(NerdleGraphTransformer.PROPERTY_CLAUSE_TYPE) == NerdleGraphTransformer.VALUE_CLAUSE_TYPE_PREDICATE) {
					vertexLabel = ((String) vertex
							.getProperty(NerdleGraphTransformer.PROPERTY_VERBTEXT)
							+ " { " + (String) vertex
								.getProperty(NerdleGraphTransformer.PROPERTY_CLAUSE_TYPE))
							+ " } "
							+ vertex.getProperty(NerdleGraphTransformer.PROPERTY_SENTENCE);
				} else {
					vertexLabel = ((String) vertex
							.getProperty(NerdleGraphTransformer.PROPERTY_TEXT)
							+ " { " + (String) vertex
								.getProperty(NerdleGraphTransformer.PROPERTY_NER))
							+ ", "
							+ vertex.getProperty(NerdleGraphTransformer.PROPERTY_CLAUSE_TYPE)
							+ " } ";
				}

				return vertexLabel;
			}
		};

		Transformer<Edge, String> edgeLabelTransformer = new Transformer<Edge, String>() {
			public String transform(Edge edge) {
				return edge.getLabel();
			}
		};

		viz.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
		viz.getRenderContext()
				.setVertexLabelTransformer(vertexLabelTransformer);

		JFrame frame = new JFrame("Nerdle Graph Visualizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(viz);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		JungGraphVisualizer visualizer = new JungGraphVisualizer();
		visualizer.visualize(TinkerGraphFactory.createTinkerGraph());
	}
}
