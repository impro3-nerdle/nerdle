package edu.tuberlin.dima.nerdle;

import org.junit.Test;

import com.google.gson.Gson;

import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;
import edu.tuberlin.dima.nerdle.model.NerdlePredicate;
import edu.tuberlin.dima.nerdle.model.NerdleSubject;

public class GsonAPITest {

	@Test
	public void test() {
		NerdleArg argument = new NerdleArg("in Ulm", "L");
		NerdleFact fact = new NerdleFact("Einstein was born in Ulm",
				"https://blabla.de", 0.9);
		NerdlePredicate predicate = new NerdlePredicate("born", "was born");
		NerdleSubject subject = new NerdleSubject("Einstein");
		fact.setPredicate(predicate);
		fact.setSubject(subject);
		fact.addArgument(argument);

		Gson gson = new Gson();
		String json = gson.toJson(fact);
		
		System.out.println(json);
		
		NerdleFact fromJson = gson.fromJson(json, NerdleFact.class);
		
		System.out.println(fromJson);
	}

}
