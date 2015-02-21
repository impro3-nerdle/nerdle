package edu.tuberlin.dima.nerdle.pig;

import java.io.IOException;
import java.util.List;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DefaultDataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.tools.counters.PigCounterHelper;

import com.google.gson.Gson;

import edu.tuberlin.dima.nerdle.NerdleFactExtractor;
import edu.tuberlin.dima.nerdle.model.NerdleFact;

public class OpenIEFunction extends EvalFunc<DataBag> {
	
	TupleFactory tupleFactory = TupleFactory.getInstance();
	
    private final PigCounterHelper pigCounterHelper = new PigCounterHelper();
	
    enum Counters
	{
			FACTS,
			ARTICLES,
			EMPTY_ARTICLES,
			ERRORS
	}
    
	@Override
	public DataBag exec(Tuple input) throws IOException {
		
		if (input == null) {
			pigCounterHelper.incrCounter(Counters.ERRORS, 1);
            return null;
        }

        if (input.size() != 6) {
        	pigCounterHelper.incrCounter(Counters.ERRORS, 1);
            throw new IllegalArgumentException("Tuple needs to contain only six arguments");
        }
        
        String source = (String) input.get(0);
        String articleText = (String) input.get(1);
        String articleTextsCoref = (String) input.get(2);
        boolean doCoreference = (Boolean) input.get(3);
        boolean doLemma = (Boolean) input.get(4);
        boolean doSynonyms = (Boolean) input.get(5);
        
        DataBag dataBag = new DefaultDataBag();
        
        try {
			
            if (source != null && !source.isEmpty() && articleText != null && !articleText.isEmpty()) {
                
            	pigCounterHelper.incrCounter(Counters.ARTICLES, 1);
            	
            	NerdleFactExtractor nerdleFactExtractor = new NerdleFactExtractor();
                
                List<NerdleFact> nerdleFacts = nerdleFactExtractor.process(articleText, source, doCoreference, doLemma, doSynonyms);
                
                if (articleTextsCoref != null && !articleTextsCoref.isEmpty()) {
                    nerdleFacts.addAll(nerdleFactExtractor.process(articleTextsCoref, source, doCoreference, doLemma, doSynonyms));
                }
                
            	Gson gson = new Gson();
            	
            	for (NerdleFact nerdleFact : nerdleFacts) {
            		nerdleFactExtractor.processSynonyms(nerdleFact);
        			String json = gson.toJson(nerdleFact);
        			Tuple tuple = tupleFactory.newTuple(json);
        			dataBag.add(tuple);
        			pigCounterHelper.incrCounter(Counters.FACTS, 1);
        		}

            } else {
            	pigCounterHelper.incrCounter(Counters.EMPTY_ARTICLES, 1);
            }

        	
		} catch (Exception e) {
			System.err.println(e.getMessage());
			pigCounterHelper.incrCounter(Counters.ERRORS, 1);
		}
        
		return dataBag;
	}

}
