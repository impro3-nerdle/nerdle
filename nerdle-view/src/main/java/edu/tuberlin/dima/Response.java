package edu.tuberlin.dima;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Response {

	public List<GroupedAnswer> groupedAnswers;
	
	public Response() {
		super();
	}

	public List<GroupedAnswer> getGroupedAnswers() {
		return groupedAnswers;
	}

	public Response(List<GroupedAnswer> groupedAnswers) {
		super();
		this.groupedAnswers = groupedAnswers;
	}

	public void setGroupedAnswers(List<GroupedAnswer> groupedAnswers) {
		this.groupedAnswers = groupedAnswers;
	}


}
