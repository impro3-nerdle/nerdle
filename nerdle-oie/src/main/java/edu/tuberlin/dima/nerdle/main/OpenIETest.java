package edu.tuberlin.dima.nerdle.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import scala.collection.Seq;
import edu.knowitall.tool.coref.StanfordCoreferenceResolver;
import edu.knowitall.tool.coref.Substitution;
import edu.tuberlin.dima.nerdle.OpenIEHelper;
import edu.tuberlin.dima.nerdle.model.NerdleArg;
import edu.tuberlin.dima.nerdle.model.NerdleFact;

public class OpenIETest {

	private static OpenIEHelper pipeline = OpenIEHelper.INSTANCE;;
	
	public static List<NerdleFact> extractions = new ArrayList<NerdleFact>();

	public static String extract(String text) {
		String extract = "";
		List<NerdleFact> extracts;
		extracts = pipeline.extractFactsFromArticleText(text, "", true);
		extractions.addAll(extracts);
		for (NerdleFact ex : extracts) {
			extract += "e: " + ex.getSubject().getText() + ";";
			extract += ex.getPredicate().getText() + ";";
			for (NerdleArg arg : ex.getArguments()) {
				extract += arg.getText() + ";";
			}
			extract += "\r";
		}
		return extract;
	}

	public static String coref(String text) {
		StanfordCoreferenceResolver coref = new StanfordCoreferenceResolver();
		Seq<Substitution> subs = coref.substitutions(text);
		System.err.println(subs.mkString());
		return StanfordCoreferenceResolver.substitute(text, subs);
	}

	public static List<String> sentencer(String text) {
		return pipeline.getSentences(text);
	}

	public static void main(String[] args) {
		OpenIETest ieTest = new OpenIETest();
		Scanner scanner = new Scanner(System.in);
		String input = "";
		String text = "";

		while (!input.equals("exit")) {
			extractions.clear();
			input = scanner.nextLine();
			List<NerdleFact> extractions = new ArrayList<NerdleFact>();
			
			// Coreference test
			System.err.println("Coreference:");

			text = OpenIETest.coref(input);
//			System.out.println(text);

			// Sentencer test
			System.err.println("Sentences:");

			List<String> sens = OpenIETest.sentencer(text);
			for (String sen : sens) {
//				System.out.println("s: " + sen);
			}

			// Extractor test
			System.err.println("Extractor:");

			for (String sen : sens) {
//				System.out.println(OpenIETest.extract(sen));
			}

			// Linking test
			System.err.println("Linking:");
						
		}
	}
}
