package edu.tuberlin.dima.nerdle.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.model.NerdlePredicate;
import edu.tuberlin.dima.nerdle.model.NerdleSubject;

public class NerdleGraphTransformer {

	public final static String PROPERTY_TEXT = "text";
	public final static String PROPERTY_VERBTEXT = "verbText";
	public final static String PROPERTY_SOURCE = "source";
	public final static String PROPERTY_NER = "ner";
	public final static String PROPERTY_SENTENCE = "sentence";
	public final static String PROPERTY_CONFIDENCE = "confidence";
	public final static String PROPERTY_CLAUSE_TYPE = "clauseType";
	public final static String PROPERTY_LEMMA = "lemma";
	public final static String PROPERTY_ISSYNONYM = "isSynonym";

	public final static String VALUE_CLAUSE_TYPE_PREDICATE = "P";
	public final static String VALUE_CLAUSE_TYPE_ARGUMENT = "A";
	public final static String VALUE_CLAUSE_TYPE_SUBJECT = "S";

	public static void transform(NerdleFact fact, TinkerGraph graph) {
//		System.out.println(fact.getSentence());
		Vertex predicate = createPredicateVertex(fact.getPredicate(),
				fact.getSentence(), fact.getSource(), fact.getConfidence(),
				graph);
		Vertex subject = createSubjectVertex(fact.getSubject(), graph);
		graph.addEdge(null, predicate, subject, "hasSubject");

		for (NerdleArg nerdleArg : fact.getArguments()) {
			Vertex argument = createArgumentVertex(nerdleArg, graph);
			graph.addEdge(null, predicate, argument, "hasArgument");
		}
	}

	public static NerdleFact transform(Vertex verbVertex) {

		Vertex predicateVertex = null;
		if(((Boolean)verbVertex.getProperty(PROPERTY_ISSYNONYM)) == false){
			predicateVertex = verbVertex;
		} else {
			predicateVertex = verbVertex.getVertices(Direction.IN,"hasSynonym").iterator().next();
		}
		
		List<String> synonyms = new ArrayList<String>();
		Iterator<Vertex> synIt = predicateVertex.getVertices(Direction.OUT,
				"hasSynonym").iterator();
		while (synIt.hasNext()) {
			Vertex vertex = (Vertex) synIt.next();
			synonyms.add(vertex.getProperty(PROPERTY_LEMMA).toString());
		}

		NerdlePredicate predicate = new NerdlePredicate(predicateVertex.getProperty(
				PROPERTY_VERBTEXT).toString(), predicateVertex.getProperty(
				PROPERTY_LEMMA).toString(), synonyms);

		NerdleSubject subject = new NerdleSubject(predicateVertex
				.getVertices(Direction.OUT, "hasSubject").iterator().next()
				.getProperty(PROPERTY_TEXT).toString());

		List<NerdleArg> args = new ArrayList<NerdleArg>();
		Iterator<Vertex> argIt = predicateVertex.getVertices(Direction.OUT,
				"hasArgument").iterator();
		while (argIt.hasNext()) {
			Vertex vertex = (Vertex) argIt.next();
			args.add(new NerdleArg(
					vertex.getProperty(PROPERTY_TEXT).toString(), vertex
							.getProperty(PROPERTY_NER).toString()));
		}
		

		return new NerdleFact(predicateVertex.getProperty(PROPERTY_SENTENCE)
				.toString(),
				predicateVertex.getProperty(PROPERTY_SOURCE).toString(), predicate,
				subject, args, Double.parseDouble(predicateVertex.getProperty(
						PROPERTY_CONFIDENCE).toString()));
	}

	private static Vertex createArgumentVertex(NerdleArg nerdleArg,
			TinkerGraph graph) {

		Vertex vertex = graph.addVertex(null);
		vertex.setProperty(PROPERTY_TEXT, nerdleArg.getText());
		vertex.setProperty(PROPERTY_NER, nerdleArg.getNer());
		vertex.setProperty(PROPERTY_CLAUSE_TYPE, VALUE_CLAUSE_TYPE_ARGUMENT);
		return vertex;
	}

	private static Vertex createSubjectVertex(NerdleSubject nerdleSubject,
			TinkerGraph graph) {

		Vertex vertex = graph.addVertex(null);
		vertex.setProperty(PROPERTY_TEXT, nerdleSubject.getText());
		vertex.setProperty(PROPERTY_CLAUSE_TYPE, VALUE_CLAUSE_TYPE_SUBJECT);
		return vertex;
	}

	private static Vertex createPredicateVertex(
			NerdlePredicate nerdlePredicate, String sentence, String source,
			double confidence, TinkerGraph graph) {

		Vertex vertex = graph.addVertex(null);
		vertex.setProperty(PROPERTY_VERBTEXT, nerdlePredicate.getText());
		String lemma = nerdlePredicate.getLemma();
		if(lemma == null || lemma.isEmpty()){
			vertex.setProperty(PROPERTY_LEMMA, nerdlePredicate.getText());
		} else {
			vertex.setProperty(PROPERTY_LEMMA, lemma);
		}
		vertex.setProperty(PROPERTY_ISSYNONYM, false);
		vertex.setProperty(PROPERTY_CLAUSE_TYPE, VALUE_CLAUSE_TYPE_PREDICATE);
		vertex.setProperty(PROPERTY_SENTENCE, sentence);
		vertex.setProperty(PROPERTY_SOURCE, source);
		vertex.setProperty(PROPERTY_CONFIDENCE, confidence);
		for (String synonym : nerdlePredicate.getSynonyms()) {
			Vertex synonymVertex = graph.addVertex(null);
			synonymVertex.setProperty(PROPERTY_LEMMA, synonym);
			synonymVertex.setProperty(PROPERTY_CLAUSE_TYPE,
					VALUE_CLAUSE_TYPE_PREDICATE);
			synonymVertex.setProperty(PROPERTY_ISSYNONYM, true);
			graph.addEdge(null, vertex, synonymVertex, "hasSynonym");
		}
		return vertex;
	}
	
}
