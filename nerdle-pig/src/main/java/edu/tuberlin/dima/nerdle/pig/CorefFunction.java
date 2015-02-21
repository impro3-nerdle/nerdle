package edu.tuberlin.dima.nerdle.pig;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.tools.counters.PigCounterHelper;

import edu.tuberlin.dima.nerdle.CoreferencesHelper;

public class CorefFunction extends EvalFunc<Tuple> {
	
	private int limitCharSize = 5000;
	
	public CorefFunction(String limitCharSize) {
		try {
			this.limitCharSize = Integer.parseInt(limitCharSize);
		} catch (Exception e) {
		}
		
		System.out.println("limitCharSize: " + limitCharSize);
	}

	TupleFactory tupleFactory = TupleFactory.getInstance();

	private final PigCounterHelper pigCounterHelper = new PigCounterHelper();

	enum Counters {
		ARTICLES, EMPTY_ARTICLES, FILTERED_ARTICLES, ERRORS
	}

	@Override
	public Tuple exec(Tuple input) throws IOException {

		if (input == null) {
			pigCounterHelper.incrCounter(Counters.ERRORS, 1);
			return null;
		}

		if (input.size() != 2) {
			pigCounterHelper.incrCounter(Counters.ERRORS, 1);
			throw new IllegalArgumentException(
					"Tuple needs to contain only two arguments");
		}

		String source = (String) input.get(0);
		String articleText = (String) input.get(1);
		
		if (source != null && !source.isEmpty() && articleText != null
				&& !articleText.isEmpty()) {
			
			if (articleText.length() > limitCharSize) {
				pigCounterHelper.incrCounter(Counters.FILTERED_ARTICLES, 1);
				Tuple tuple = tupleFactory.newTuple("");
				return tuple;
			}
			
			pigCounterHelper.incrCounter(Counters.ARTICLES, 1);
			
			CoreferencesHelper coreferencesHelper = CoreferencesHelper.INSTANCE;
			
			String corefArticleText = coreferencesHelper.resolveCoreferences(articleText);
			
			Tuple tuple = tupleFactory.newTuple(corefArticleText);
			
			return tuple;
			
		} else {
			pigCounterHelper.incrCounter(Counters.EMPTY_ARTICLES, 1);
		}
		
		return null;
	}

}
