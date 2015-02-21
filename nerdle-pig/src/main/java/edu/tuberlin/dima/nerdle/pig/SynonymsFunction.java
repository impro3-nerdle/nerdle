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
