package edu.tuberlin.dima.nerdle.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.tuberlin.dima.nerdle.graph.NerdleGraphTransformer;
import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;

public class App {

	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.err
					.println("Please specify following: input_dir graphs_dir graph_name.");
			System.err.println("input_dir: directory of the input text files.");
			System.err.println("graphs_dir: directory of the the graphs.");
			System.err.println("graph_name: name of the generated grpah.");
			return;
		}

		File inputDir = new File(args[0]);
		if (!inputDir.isDirectory()) {
			System.err.println(inputDir);
			System.err.println("Directory does not exist.");
			return;
		}

		File graphsDir = new File(args[1]);

		File graphDir = new File(graphsDir.getPath() + File.separator + args[2]
				+ File.separator);
		if (graphDir.exists()) {
			System.err.println(inputDir);
			System.err.println("Graph already exists.");
			return;
		}

		File[] files = inputDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().startsWith("part-");
			}
		});

		TinkerGraph graph = new TinkerGraph(graphDir.getPath(),
				TinkerGraph.FileType.GRAPHSON);

		// graph.createIndex("verb-idx", Vertex.class);

		Gson gson = new Gson();

		BufferedReader reader;
		for (File file : files) {
			// System.out.println(file.getName());
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				boolean argSub = true;
				NerdleFact fact = gson.fromJson(line, NerdleFact.class);
				String filter = "^[a-zA-Z0-9äöüÄÖÜ.,!?:;\\-' ]*$";
				String subject = fact.getSubject().getText();
				String verb = fact.getPredicate().getText();
				String lemma = fact.getPredicate().getLemma();
				List<String> arguments = new ArrayList<String>();
				for (NerdleArg arg : fact.getArguments()) {
					if (!arg.getText().matches(filter)
							|| arg.getText().length() > 20) {
						arguments.add(arg.getText());
					}
				}

				if (fact.getArguments().size() == 1
						&& fact.getArguments().get(0).getText().equals(subject)) {
					argSub = false;
				}

				if (!subject.matches("(He|he|She|she|It|it)")
						&& subject.matches(filter) && verb.matches(filter)
						&& arguments.isEmpty() && !lemma.isEmpty() && argSub) {
					NerdleGraphTransformer.transform(fact, graph);
				}
			}
		}

		graph.createKeyIndex(NerdleGraphTransformer.PROPERTY_LEMMA,
				Vertex.class);
		graph.shutdown();

	}
}