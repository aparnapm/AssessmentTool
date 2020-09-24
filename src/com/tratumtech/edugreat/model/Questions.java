package com.tratumtech.edugreat.model;

import java.util.Set;

import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.annotations.BatchSize;

/* QUESTIONS class model: Contains all information about Questions. 
 * Connects to 'questions' table in database.
 * Mapped by Questions.hbm.xml.
 */
// JsonIgnore doesn't print assessments set in the Questions model
@JsonIgnoreProperties(value= {"assessments"})
public class Questions {
	
	private int id; // Questions ID, primary key, not null, unique
	/*TODO: Make not null*/
	private String category; // Category, String
	private String ques; // Question text, String, not null
	private String opta; // Option A, String, not null
	private String optb; // Option B, String, not null
	private String optc; // Option C, String
	private String optd; // Option D, String
	private char answer; // Answer, contains a, b, c, or d, Char, not null
	/*TODO: Make not null*/
	private String difficulty; // Difficulty, String
	/*TODO: Make not null and map to Admin id column*/
	private int createdby; // ID of Admin that created Assessment, int
	
	/* Assessment Set containing all assessments containing this Question
	 * Mapped many to many with "questions" Set<Questions> in Assessment.java.
	 * Ordered by when it was created Descending.
	 */
	@ManyToMany(mappedBy = "questions")
	@OrderBy("created DESC")
	@BatchSize(size = 1000)
	private Set<Assessment> assessments;
	
	// blank default constructor
	public Questions() {
	}

	// constructor with all not null components
	public Questions(int id, String ques, String opta, String optb, char answer) {
		this.id = id;
		this.ques = ques;
		this.opta = opta;
		this.optb = optb;
		this.answer = answer;
	}

	// constructor with all components
	public Questions(int id, String category, String ques, String opta, String optb, String optc, String optd,
			char answer, String difficulty, int createdby, Set<Assessment> assessments) {
		this.id = id;
		this.category = category;
		this.ques = ques;
		this.opta = opta;
		this.optb = optb;
		this.optc = optc;
		this.optd = optd;
		this.answer = answer;
		this.difficulty = difficulty;
		this.createdby = createdby;
		this.assessments = assessments;
	}
	
	// constructor from JSONObject
		/* Passes JSONObject joQuestions to the constructor. This object is parsed for values it should contain
		 * and the Questions values are set. Surrounded with try/catch statements to avoid JSONExceptions.
		 */
	public Questions(JSONObject joQuestions){
		try {
			if(joQuestions.has("id"))
				this.id = joQuestions.getInt("id");
			
			if(joQuestions.has("category"))
				this.category = joQuestions.getString("category");
			
			if(joQuestions.has("ques"))
				this.ques = joQuestions.getString("ques");
			
			/*TODO: Find some way to check if answer is an option given in JSON.*/
			if(joQuestions.has("opta"))
				this.opta = joQuestions.getString("opta");
			
			if(joQuestions.has("optb"))
				this.optb = joQuestions.getString("optb");
			
			if(joQuestions.has("optc"))
				this.optc = joQuestions.getString("optc");
			
			if(joQuestions.has("optd"))
				this.optd = joQuestions.getString("optd");
			
			if(joQuestions.has("answer"))
				this.answer = joQuestions.getString("answer").charAt(0);
			
			if(joQuestions.has("difficulty"))
				this.difficulty = joQuestions.getString("difficulty");
			
			if(joQuestions.has("createdby"))
				this.createdby = joQuestions.getInt("createdby");
			
		} catch (JSONException e) {
			// if error is thrown, stack trace is printed.
			e.printStackTrace();
		}
	}
	
	// Getters and Setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getQues() {
		return ques;
	}

	public void setQues(String ques) {
		this.ques = ques;
	}

	public String getOpta() {
		return opta;
	}

	public void setOpta(String opta) {
		this.opta = opta;
	}

	public String getOptb() {
		return optb;
	}

	public void setOptb(String optb) {
		this.optb = optb;
	}

	public String getOptc() {
		return optc;
	}

	public void setOptc(String optc) {
		this.optc = optc;
	}

	public String getOptd() {
		return optd;
	}

	public void setOptd(String optd) {
		this.optd = optd;
	}

	public char getAnswer() {
		return answer;
	}

	public void setAnswer(char answer) {
		this.answer = answer;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public int getCreatedby() {
		return createdby;
	}

	public void setCreatedby(int createdby) {
		this.createdby = createdby;
	}

	public Set<Assessment> getAssessments() {
		return assessments;
	}

	public void setAssessments(Set<Assessment> assessments) {
		this.assessments = assessments;
	}

	// toString method
	
	@Override
	public String toString() {
		return "Questions [id=" + id + ", category=" + category + ", ques=" + ques + ", opta=" + opta + ", optb=" + optb
				+ ", optc=" + optc + ", optd=" + optd + ", answer=" + answer + ", difficulty=" + difficulty
				+ ", createdby=" + createdby + "]";
	}

}
