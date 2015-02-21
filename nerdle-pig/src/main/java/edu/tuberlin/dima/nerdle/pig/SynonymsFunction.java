package edu.tuberlin.dima.nerdle.pig;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import com.google.gson.Gson;

import edu.tuberlin.dima.nerdle.NerdleFactExtractor;
import edu.tuberlin.dima.nerdle.model.NerdleFact;

public class SynonymsFunction extends EvalFunc<Tuple> {

	TupleFactory tupleFactory = TupleFactory.getInstance();

	@Override
	public Tuple exec(Tuple input) throws IOException {

		if (input == null) {
			return null;
		}

		if (input.size() != 1) {
			throw new IllegalArgumentException(
					"Tuple needs to contain only one argument");
		}

		String fact = (String) input.get(0);
		
		if (fact.startsWith("(") && fact.endsWith(")")) {
			fact = fact.substring(1, fact.length() - 1);
		}

		NerdleFactExtractor nerdleFactExtractor = new NerdleFactExtractor();

		Gson gson = new Gson();

		NerdleFact nerdleFact = gson.fromJson(fact, NerdleFact.class);

		nerdleFactExtractor.processSynonyms(nerdleFact);

		String json = gson.toJson(nerdleFact);
		Tuple tuple = tupleFactory.newTuple(json);

		return tuple;
	}

}
