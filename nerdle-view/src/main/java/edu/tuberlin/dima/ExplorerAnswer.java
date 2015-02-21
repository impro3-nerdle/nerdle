package edu.tuberlin.dima;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExplorerAnswer {

	public String sentence;
	public String d3GraphJson;
	public String questionD3GraphJson;
	public String graphJSON;
	public Integer confidence;

	public ExplorerAnswer() {
		super();
	}

	public ExplorerAnswer(String sentence, Integer confidence) {
		super();
		this.sentence = sentence;
		this.confidence = confidence;
	}
	
	public ExplorerAnswer(String sentence, String d3GrpahJson, String graphJSON, String questionD3GraphJson, Integer confidence) {
		super();
		this.sentence = sentence;
		this.d3GraphJson = d3GrpahJson;
		this.questionD3GraphJson = questionD3GraphJson;
		this.graphJSON = graphJSON;
		this.confidence = confidence;
	}
}
