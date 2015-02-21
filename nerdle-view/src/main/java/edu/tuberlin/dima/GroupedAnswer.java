package edu.tuberlin.dima;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GroupedAnswer {
	
	public String subject;
	public List<Answer> answers;
	public GroupedAnswer(String subject, List<Answer> answers) {
		super();
		this.subject = subject;
		this.answers = answers;
	}
	public GroupedAnswer() {
		super();
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	public String toString(){
		return "Subjekt:  "+subject+"  Answers:   "+answers; 
	}

}
