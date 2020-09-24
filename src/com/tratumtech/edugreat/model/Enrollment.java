package com.tratumtech.edugreat.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.annotations.BatchSize;

/* ENROLLMENT class model: Contains all information about Enrollments. 
 * Connects to 'enrollment' table in database.
 * Mapped by Enrollment.hbm.xml.
 */
@JsonIgnoreProperties(value= {"results"})
public class Enrollment {
	
	private int id; // Enrollment ID, primary key, unique, not null
	private Date validFrom; // From when the Enrollment is valid, Date, not null
	private Date validUntil; // Until when the Enrollment is valid, Date, not null
	private String stat; // Status for Enrollment (requested, available, rejected, complete, incomplete), String, not null
	/* TODO: Must implement -
	 * requested = Enrollment has been requested by not approved by an Admin
	 * approved = Enrollment has been approved by Admin user
	 * rejected = Enrollment has been rejected by Admin user
	 * incomplete = validUntil is passed, but score=null
	 * complete = validUntil is passed and score!=null OR attempts=0
	 */
	private int attempts; /* Attempts for the enrollment. Equal to Assessment attempts to start. Dec by 1 every time
	assessment is taken. When attempts equals 0, stat=completed */
	private int score; // Highest score for Assessment. If exam isn't taken it is null.
	
	/* Assessment related to this Enrollment
	 * Mapped one to many with "enrollments" Set<Enrollment> in Assessment.java 
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private Assessment assessment;
	
	/* Student related to this Enrollment
	 * Mapped one to many with "enrollments" Set<Enrollment> in Student.java 
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private Student student;
	
	/* Result Set containing all results for this Enrollment
	 * Mapped one to many with "enrollment" Enrollment in Result.java.
	 * Ordered by when it was created Descending.
	 */
	@OrderBy("created DESC")
	@BatchSize(size = 1000)
	@OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL)
	private Set<Result> results;

	// default blank constructor
	public Enrollment() {
	}

	// constructor with all not null components
	public Enrollment(int id, Assessment assessment, Student student, String stat, int attempts) {
		this.id = id;
		this.assessment = assessment;
		this.student = student;
		this.stat = stat;
		this.attempts = attempts;
	}

	// constructor with all components
	public Enrollment(int id, Assessment assessment, Student student, Date validFrom, Date validUntil, String stat,
			int attempts, Integer score) {
		this.id = id;
		this.assessment = assessment;
		this.student = student;
		this.validFrom = validFrom;
		this.validUntil = validUntil;
		this.stat = stat;
		this.attempts = attempts;
		this.score = score;
	}
	
	// constructor from JSONObject
	/* Passes JSONObject joEnrollment to the constructor. This object is parsed for values it should contain
	 * and the Enrollment values are set. Date is converted from string using DateFormatter. Surrounded with
	 * try/catch statements to avoid JSONExceptions.
	 */
	public Enrollment(JSONObject joEnrollment) {
		try {
			
			if (joEnrollment.has("id"))
				this.id = joEnrollment.getInt("id");
			// sdf defines Date string as yyyy-MM-dd
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if(joEnrollment.has("validFrom")) {
				String dateStr = joEnrollment.getString("validFrom");
				try {
					// Date validFrom is set by using sdf to parse dateStr
					this.validFrom = sdf.parse(dateStr);
				} catch (ParseException e) {
					// if error is thrown, stack trace is printed.
					e.printStackTrace();
				}
			}
			
			if(joEnrollment.has("validUntil")) {
				String dateStr = joEnrollment.getString("validUntil");
				try {
					// Date validUntil is set by using sdf to parse dateStr
					this.validUntil = sdf.parse(dateStr);
				} catch (ParseException e) {
					// if error is thrown, stack trace is printed.
					e.printStackTrace();
				}
			}
			
			if (joEnrollment.has("stat"))
				this.stat = joEnrollment.getString("stat");
			
			if (joEnrollment.has("attempts"))
				this.attempts = joEnrollment.getInt("attempts");
			
			if (joEnrollment.has("score"))
				this.score = joEnrollment.getInt("score");
			
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

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Assessment getAssessment() {
		return assessment;
	}

	public void setAssessment(Assessment assessment) {
		this.assessment = assessment;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Set<Result> getResults() {
		return results;
	}

	public void setResults(Set<Result> results) {
		this.results = results;
	}
	
	// toString method

	@Override
	public String toString() {
		return "Enrollment [id=" + id + ", validFrom=" + validFrom + ", validUntil=" + validUntil + ", stat=" + stat
				+ ", attempts=" + attempts + ", score=" + score + ", assessment=" + assessment + ", student=" + student
				+ "]";
	}

}
