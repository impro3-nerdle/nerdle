package edu.tuberlin.dima;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExplorerResponse {

	public List<ExplorerAnswer> answers;
	public int answersSize;
	
	public ExplorerResponse() {
	}

	public ExplorerResponse(List<ExplorerAnswer> answers, int answersSize) {
		this.answers = answers;
		this.answersSize = answersSize;
	}

}
