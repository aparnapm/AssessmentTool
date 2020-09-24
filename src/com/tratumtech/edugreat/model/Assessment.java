package com.tratumtech.edugreat.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.annotations.BatchSize;

/* ASSESSMENT class model: Contains all information about Assessments. 
 * Connects to 'assessment' table in database.
 * Mapped by Assessment.hbm.xml.
 */
// JsonIgnore doesn't print questions or enrollments set in the Assessment model
@JsonIgnoreProperties(value= {"questions", "enrollments"})
public class Assessment {
	
	private int id; // Assessment ID, primary key, not null, unique
	private String assessmentName; // Assessment Name, String, not null
	private String description; // Description, String
	private String category; // Category, String
	private int qcount; // Question count, int
	private int attempts; // Number of attempts allowed, int
	private int timeLimit; // Time for taking assessment
	private int createdby; // ID of Admin that created Assessment, int
	private String difficulty; // Difficulty, String
	private Date createdDate; // Date Assessment was created, Date, not null
	private Date modifiedDate; // Date Assessment was most recently modified, Date
	
	/* Question Set containing all questions in Assessment
	 * Mapped many to many with "assessments" Set<Assessment> in Questions.java.
	 * Ordered by when it was created Descending.
	 */
	@ManyToMany(mappedBy = "assessments")
	@OrderBy("created DESC")
	@BatchSize(size = 1000)
	private Set<Questions> questions;
	
	/* Enrollment Set containing all enrollments for this Assessment
	 * Mapped one to many with "assessment" Assessment in Enrollment.java.
	 * TODO: Supposed to Cascade delete Enrollments if Assessment is deleted but error is occuring.
	 * Ordered by when it was created Descending.
	 */
	@OrderBy("created DESC")
	@BatchSize(size = 1000)
	@OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL)
	private Set<Enrollment> enrollments;

	// blank default constructor
	public Assessment() {
	}

	// constructor with all not null components
	public Assessment(int id, String assessmentName, String category, int attempts, int timeLimit, int createdby, 
			String difficulty, Date createdDate) {
		this.id = id;
		this.assessmentName = assessmentName;
		this.category = category;
		this.timeLimit = timeLimit;
		this.createdby = createdby;
		this.difficulty = difficulty;
		this.createdDate = createdDate;
	}

	// constructor with all components (except sets)
	public Assessment(int id, String assessmentName, String description, String category, Integer qcount,
			Integer attempts, int timeLimit, int createdby, String difficulty, Date createdDate, Date modifiedDate) {
		this.id = id;
		this.assessmentName = assessmentName;
		this.description = description;
		this.category = category;
		this.qcount = qcount;
		this.attempts = attempts;
		this.timeLimit = timeLimit;
		this.createdby = createdby;
		this.difficulty = difficulty;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}
	
	// constructor from JSONObject
	/* Passes JSONObject joAssessment to the constructor. This object is parsed for values it should contain
	 * and the Assessment values are set. Date is converted from string using DateFormatter. Surrounded with
	 * try/catch statements to avoid JSONExceptions.
	 */
	public Assessment(JSONObject joAssessment) {
		try {
			if(joAssessment.has("id"))
				this.id = joAssessment.getInt("id");
			
			if(joAssessment.has("assessmentName"))
				this.assessmentName = joAssessment.getString("assessmentName");
			
			if(joAssessment.has("description"))
				this.description = joAssessment.getString("description");
			
			if(joAssessment.has("category"))
				this.category = joAssessment.getString("category");
			
			if(joAssessment.has("qcount"))
				this.qcount = joAssessment.getInt("qcount");
			
			if(joAssessment.has("attempts"))
				this.attempts = joAssessment.getInt("attempts");
			
			if(joAssessment.has("timeLimit"))
				this.timeLimit = joAssessment.getInt("timeLimit");
			
			if(joAssessment.has("createdby"))
				this.createdby = joAssessment.getInt("createdby");
			
			if(joAssessment.has("difficulty"))
				this.difficulty = joAssessment.getString("difficulty");
			// sdf defines Date string as yyyy-MM-dd
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if(joAssessment.has("createdDate")) {
				String dateStr = joAssessment.getString("createdDate");
				try {
					// Date createdDate is set by using sdf to parse dateStr
					this.createdDate = sdf.parse(dateStr);
				} catch (ParseException e) {
					// if error is thrown, stack trace is printed.
					e.printStackTrace();
				}
			}
			
			if(joAssessment.has("modifiedDate")) {
				String dateStr = joAssessment.getString("modifiedDate");
				try {
					// Date modifiedDate is set by using sdf to parse dateStr
					this.modifiedDate = sdf.parse(dateStr);
				} catch (ParseException e) {
					// if error is thrown, stack trace is printed.
					e.printStackTrace();
				}
			}
			
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

	public String getAssessmentName() {
		return assessmentName;
	}

	public void setAssessmentName(String assessmentName) {
		this.assessmentName = assessmentName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getQcount() {
		return qcount;
	}

	public void setQcount(int qcount) {
		this.qcount = qcount;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int getCreatedby() {
		return createdby;
	}

	public void setCreatedby(int createdby) {
		this.createdby = createdby;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Set<Questions> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Questions> questions) {
		this.questions = questions;
	}

	public Set<Enrollment> getEnrollments() {
		return enrollments;
	}

	public void setEnrollments(Set<Enrollment> enrollments) {
		this.enrollments = enrollments;
	}

	// toString method
	
	@Override
	public String toString() {
		return "Assessment [id=" + id + ", assessmentName=" + assessmentName + ", description=" + description
				+ ", category=" + category + ", qcount=" + qcount + ", attempts=" + attempts + ", timeLimit="
				+ timeLimit + ", createdby=" + createdby + ", difficulty=" + difficulty + ", createdDate=" 
				+ createdDate + ", modifiedDate=" + modifiedDate + "]";
	}
	

}
