package edu.tuberlin.dima;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.configuration.ConfigurationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.tuberlin.dima.nerdle.StanfordCoreNLPHelper;
import edu.tuberlin.dima.nerdle.graph.GraphExplorer;
import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.model.NerdlePredicate;
import edu.tuberlin.dima.nerdle.model.NerdleSubject;
import edu.tuberlin.dima.nerdle.qa.ScoreCalculator;
import edu.tuberlin.dima.nerdle.qa.ScoreComparator;
import edu.tuberlin.dima.nerdle.qa.SingletonGraph;

@Path("nerdleExplorer")
public class NerdleExplorer {

	@GET
	@Path("explore")
	@Produces("application/json")
	public ExplorerResponse getExplore(@QueryParam("wikia") String wikia,
			@QueryParam("subject") String subject,
			@QueryParam("verb") String verb,
			@QueryParam("arguments") List<String> arguments,
			@QueryParam("max") Integer max, @QueryParam("offset") Integer offset)
			throws ConfigurationException {

		NerdleFact questionExtraction = new NerdleFact("unknown", "", 0.5D);

		List<String> lemma = null;
		if (!verb.isEmpty()) {
			lemma = StanfordCoreNLPHelper.getInstance().getLemma(verb);
		}

		if (lemma != null) {
			questionExtraction.setPredicate(new NerdlePredicate(verb, lemma
					.get(0)));
		} else {
			questionExtraction.setPredicate(new NerdlePredicate(verb, ""));
		}

		questionExtraction.setSubject(new NerdleSubject(subject));

		for (String argument : arguments) {
			if (!argument.equals("")) {
				questionExtraction.addArgument(new NerdleArg(argument, ""));
			}
		}

		long beginLoad = System.currentTimeMillis();

		SingletonGraph.getInstance();
		TinkerGraph graph = SingletonGraph.getInstance().getGraphs().get(wikia);

		System.out.println("LOAD Time: "
				+ (System.currentTimeMillis() - beginLoad));

		long beginScan = System.currentTimeMillis();

		List<NerdleFact> matchedFacs = GraphExplorer.getMatchedFacts(
				questionExtraction, graph);

		System.out.println("SCAN Time: "
				+ (System.currentTimeMillis() - beginScan));

		HashMap<NerdleFact, Double> map = new HashMap<NerdleFact, Double>();
		ScoreComparator bvc = new ScoreComparator(map);
		TreeMap<NerdleFact, Double> sortedMap = new TreeMap<NerdleFact, Double>(
				bvc);

		long beginSort = System.currentTimeMillis();

		for (NerdleFact nerdleFact : matchedFacs) {
			double score = ScoreCalculator.calculate(questionExtraction,
					nerdleFact);
			map.put(nerdleFact, score);
		}

		sortedMap.putAll(map);

		System.out.println("SCAN TIME SORT: "
				+ (System.currentTimeMillis() - beginSort));

		ArrayList<ExplorerAnswer> answers = new ArrayList<ExplorerAnswer>();

		long beginAnswer = System.currentTimeMillis();

		try {

			for (Entry<NerdleFact, Double> entry : sortedMap.entrySet()) {
				JSONArray graphArray = D3GraphGenerator.generateD3GraphJson(
						entry.getKey(), false);

				JSONObject graphJson = SimpleGraphGenerator
						.generateSimpleGraphJson(entry.getKey());

				answers.add(new ExplorerAnswer(entry.getKey().getSentence(),
						graphArray.toString(), graphJson.toString(), "", entry
								.getValue().intValue()));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		System.out.println("SCAN TIME AMSWER: "
				+ (System.currentTimeMillis() - beginAnswer));

		int fromIndex = Math.min(offset, answers.size());
		int toIndex = Math.min(offset + max, answers.size());

		return new ExplorerResponse(answers.subList(fromIndex, toIndex),
				answers.size());
	}

}
