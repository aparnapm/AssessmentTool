package com.tratumtech.edugreat.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;

import org.codehaus.jettison.json.JSONObject;

/* RESULT class model: Contains all information about Results. 
 * Connects to 'result' table in database.
 * Mapped by Result.hbm.xml.
 */
public class Result {
	private int id; // primary key
	private Date dateTaken; // date assessment was taken
	private int timeTaken; // time taken to finish assessment
	private int score; // score for the assessment
	
	/* Enrollment associated with this result
	 * Mapped one to many with "results" Set<Result> in Enrollment.java 
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private Enrollment enrollment;
	
	public Result() {
	}
	
	public Result(int id, int timeTaken, int score) {
		this.id = id;
		this.timeTaken = timeTaken;
		this.score = score;
	}
	
	public Result(int id, Date dateTaken, int timeTaken, int score) {
		this.id = id;
		this.dateTaken = dateTaken;
		this.timeTaken = timeTaken;
		this.score = score;
	}
	
	public Result(JSONObject joResult) {
		try {
			if (joResult.has("id"))
				this.id = joResult.getInt("id");
						
			if(joResult.has("dateTaken")) {
				String dateStr = joResult.getString("dateTaken");
				try {
					// sdf defines Date string as yyyy-MM-dd
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					// Date validFrom is set by using sdf to parse dateStr
					this.dateTaken = sdf.parse(dateStr);
				} catch (ParseException e) {
					// if error is thrown, stack trace is printed.
					e.printStackTrace();
				}
			}
			
			if (joResult.has("timeTaken"))
				this.timeTaken = joResult.getInt("timeTaken");
			
			if (joResult.has("score"))
				this.score = joResult.getInt("score");
			
		} catch (Exception e) {
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

	public Date getDateTaken() {
		return dateTaken;
	}

	public void setDateTaken(Date dateTaken) {
		this.dateTaken = dateTaken;
	}

	public int getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(int timeTaken) {
		this.timeTaken = timeTaken;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Enrollment getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(Enrollment enrollment) {
		this.enrollment = enrollment;
	}

	// toString method
	
	@Override
	public String toString() {
		return "Result [id=" + id + ", dateTaken=" + dateTaken + ", timeTaken=" + timeTaken + ", score=" + score
				+ ", enrollment=" + enrollment + "]";
	}

}
