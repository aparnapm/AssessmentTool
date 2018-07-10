package com.tratumtech.edugreat.tool;


import java.util.Set;

import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.annotations.BatchSize;

@JsonIgnoreProperties(value= {"assessmentset"})
public class Questions {


	private int id;
	private String category;
	private String question;
	private String opta;
	private String optb;
	private String optc;
	private String optd;
	private char answer;
	private String difficulty;
	private int createdby;
	
	@ManyToMany(mappedBy="questionset")
	@OrderBy("created DESC")
	@BatchSize(size=1000)
	private Set<Assessment> assessmentset;

	public Questions() {
	}

	public Questions(int id, String question, String opta, String optb, char answer, int createdby) {
		this.id = id;
		this.question = question;
		this.opta = opta;
		this.optb = optb;
		this.answer = answer;
		this.createdby = createdby;
	}

	public Questions(int id, String category, String question, String opta, String optb, String optc, String optd,
			char answer, String difficulty, int createdby) {
		this.id = id;
		this.category = category;
		this.question = question;
		this.opta = opta;
		this.optb = optb;
		this.optc = optc;
		this.optd = optd;
		this.answer = answer;
		this.difficulty = difficulty;
		this.createdby = createdby;
		
		  
	}
	public Questions(JSONObject joQuestions){
		try {
				if(joQuestions.has("id"))
					this.id = joQuestions.getInt("id");
				
				if(joQuestions.has("category"))
					this.category = joQuestions.getString("category");
				
				if(joQuestions.has("question"))
					this.question = joQuestions.getString("question");
				
				if(joQuestions.has("opta"))
					this.opta = joQuestions.getString("opta");
				
				if(joQuestions.has("optb"))
					this.optb = joQuestions.getString("optb");
				
				if(joQuestions.has("optc"))
					this.optc = joQuestions.getString("optc");
				
				if(joQuestions.has("optd"))
					this.optc = joQuestions.getString("optd");
				
				if(joQuestions.has("answer"))
					this.answer = joQuestions.getString("answer").charAt(0);
				
				if(joQuestions.has("difficulty"))
					this.difficulty = joQuestions.getString("difficulty");
				
				
				if(joQuestions.has("createdby"))
					this.createdby = joQuestions.getInt("createdby");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getOpta() {
		return this.opta;
	}

	public void setOpta(String opta) {
		this.opta = opta;
	}

	public String getOptb() {
		return this.optb;
	}

	public void setOptb(String optb) {
		this.optb = optb;
	}

	public String getOptc() {
		return this.optc;
	}

	public void setOptc(String optc) {
		this.optc = optc;
	}

	public String getOptd() {
		return this.optd;
	}

	public void setOptd(String optd) {
		this.optd = optd;
	}

	public char getAnswer() {
		return this.answer;
	}

	public void setAnswer(char answer) {
		this.answer = answer;
	}

	public String getDifficulty() {
		return this.difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public int getCreatedby() {
		return this.createdby;
	}

	public void setCreatedby(int createdby) {
		this.createdby = createdby;
	}
	public Set<Assessment> getAssessmentset() {
		return assessmentset;
	}

	public void setAssessmentset(Set<Assessment> assessmentset) {
		this.assessmentset = assessmentset;
	}
}