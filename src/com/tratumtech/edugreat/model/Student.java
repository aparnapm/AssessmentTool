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

/* STUDENT class model: Contains all information about Student users. 
 * Connects to 'student' table in database.
 * Mapped by Student.hbm.xml.
 */
@JsonIgnoreProperties(value= {"enrollments"})
public class Student {
	
	private int id; // Student ID, primary key, not null, unique
	/*TODO: Map p_id to id in parent table*/
	private String firstName; // First name, String, not null
	private String lastName; // Last name, String, not null
	private String email; // Email ID, String, not null, unique
	private String phone; // Phone Number, String, not null
	private String institution; // Institution (college or university), String
	/*TODO: Update over time using dob (or maybe add registered date)*/
	private Integer standard; // Standard (grade), String
	private String location; // Location (parent's address), String
	private Date dob; // Date of birth, Date
	private Date registeredDate;
	
	/* Assessment related to this Enrollment
	 * Mapped one to many with "enrollments" Set<Enrollment> in Assessment.java 
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	private Parent parent;
	
	/* Enrollment Set containing all enrollments for this Student
	 * Mapped one to many with "student" Student in Enrollment.java.
	 * TODO: Supposed to Cascade delete Enrollments if Student is deleted but error is occuring.
	 * Ordered by when it was created Descending.
	 */
	@OrderBy("created DESC")
	@BatchSize(size = 1000)
	@OneToMany(mappedBy = "student")
	private Set<Enrollment> enrollments;
	
	// blank default constructor
	public Student() {
	}

	// constructor with all not null components
	public Student(int id, String firstName, String lastName, String email, Date regDate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.registeredDate = regDate;
	}

	// constructor with all components
	public Student(int id, Parent parent, String firstName, String lastName, String email, String phone, 
			String institution, Integer standard, String location, Date dob, Date regDate) {
		this.id = id;
		this.parent = parent;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.institution = institution;
		this.standard = standard;
		this.location = location;
		this.dob = dob;
		this.registeredDate = regDate;
	}
	
	// constructor from JSONObject
		/* Passes JSONObject joStudent to the constructor. This object is parsed for values it should contain and
		 * the Student values are set. Date is converted from string using DateFormatter. Surrounded with try/catch
		 * statements to avoid JSONExceptions.
		 */
	public Student(JSONObject joStudent){
		try {
			if(joStudent.has("id"))
				this.id = joStudent.getInt("id");
			
			if(joStudent.has("firstName"))
				this.firstName = joStudent.getString("firstName");
			
			if(joStudent.has("lastName"))
				this.lastName = joStudent.getString("lastName");
			
			if(joStudent.has("email"))
				this.email = joStudent.getString("email");
			
			if(joStudent.has("phone"))
				this.phone = joStudent.getString("phone");
			
			if(joStudent.has("institution"))
				this.institution = joStudent.getString("institution");
			
			if(joStudent.has("standard"))
				this.standard = joStudent.getInt("standard");
			
			if(joStudent.has("location"))
				this.location = joStudent.getString("location");
			
			// sdf defines Date string as yyyy-MM-dd
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if(joStudent.has("dob")) {
				String dateStr = joStudent.getString("dob");
				try {
					// Date dob is set by using sdf to parse dateStr
					this.dob = sdf.parse(dateStr);
				} catch (ParseException e) {
					// if error is thrown, stack trace is printed.
					e.printStackTrace();
				}
			}
			
			if(joStudent.has("registeredDate")) {
				String dateStr = joStudent.getString("registeredDate");
				try {
					// Date dob is set by using sdf to parse dateStr
					this.registeredDate = sdf.parse(dateStr);
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
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getInstitution() {
		return this.institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public Integer getStandard() {
		return this.standard;
	}

	public void setStandard(Integer standard) {
		this.standard = standard;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	public Set<Enrollment> getEnrollments() {
		return enrollments;
	}

	public void setEnrollments(Set<Enrollment> enrollments) {
		this.enrollments = enrollments;
	}
	
	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
	}
	
	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	// toString method
	
	@Override
	public String toString() {
		return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", phone=" + phone + ", institution=" + institution + ", standard=" + standard + ", location="
				+ location + ", dob=" + dob + ", registeredDate=" + registeredDate + ", parent=" + parent
				+ "]";
	}

}
