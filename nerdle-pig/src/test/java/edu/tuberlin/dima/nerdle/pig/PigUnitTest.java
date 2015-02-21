package edu.tuberlin.dima.nerdle.pig;

import java.io.IOException;
import java.util.Iterator;

import org.apache.pig.data.Tuple;
import org.apache.pig.pigunit.PigTest;
import org.apache.pig.tools.parameters.ParseException;

public class PigUnitTest {

	protected void printAlias(PigTest test, String alias) throws IOException,
			ParseException {
		System.out.println("Printing: " + alias);
		Iterator<Tuple> iterator = test.getAlias(alias);
		while (iterator.hasNext()) {
			Tuple next = iterator.next();
			System.out.println(next);
		}
	}

}
