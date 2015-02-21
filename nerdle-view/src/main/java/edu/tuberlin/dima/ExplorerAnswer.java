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
