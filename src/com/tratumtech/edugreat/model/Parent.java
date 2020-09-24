package com.tratumtech.edugreat.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.annotations.BatchSize;

/* PARENT class model: Contains all information about Parent users. 
 * Connects to 'parent' table in database.
 * Mapped by Parent.hbm.xml.
 */
@JsonIgnoreProperties(value= {"students"})
public class Parent {
	
	private int id; // Parent ID, primary key, not null, unique
	private String firstName; // First name, String, not null
	private String lastName; // Last name, String, not null
	private String email; // Email ID, String, not null, unique
	private String phone; // Phone Number, String, not null
	private String location; // Location (parent's address), String
	private Date dob; // Date of birth, Date
	private Date registeredDate;
	
	/* Student Set containing all students of this Parent
	 * Mapped many to one with "parent" Parent in Student.java.
	 * Ordered by when it was created Descending.
	 */
	@OrderBy("created DESC")
	@BatchSize(size = 1000)
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	private Set<Student> students;

	// blank default constructor
	public Parent() {
	}

	// constructor with all not null components
	public Parent(int id, String firstName, String lastName, String email, Date regDate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.registeredDate = regDate;
	}

	// constructor with all components
	public Parent(int id, String firstName, String lastName, String email, String phone, String location,
			Date dob, Date regDate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.location = location;
		this.dob = dob;
		this.registeredDate = regDate;
	}
	
	// constructor from JSONObject
	/* Passes JSONObject joParent to the constructor. This object is parsed for values it should contain and
	 * the Parent values are set. Date is converted from string using DateFormatter. Surrounded with try/catch
	 * statements to avoid JSONExceptions.
	 */
	public Parent(JSONObject joParent){
		try {
			if(joParent.has("id"))
				this.id = joParent.getInt("id");
			
			if(joParent.has("firstName"))
				this.firstName = joParent.getString("firstName");
			
			if(joParent.has("lastName"))
				this.lastName = joParent.getString("lastName");
			
			if(joParent.has("email"))
				this.email = joParent.getString("email");
			
			if(joParent.has("phone"))
				this.phone = joParent.getString("phone");
			
			if(joParent.has("location"))
				this.location = joParent.getString("location");
			
			// sdf defines Date string as yyyy-MM-dd
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if(joParent.has("dob")) {
				String dateStr = joParent.getString("dob");
				try {
					// Date dob is set by using sdf to parse dateStr
					this.dob = sdf.parse(dateStr);
				} catch (ParseException e) {
					// if error is thrown, stack trace is printed.
					e.printStackTrace();
				}
			}
			
			if(joParent.has("registeredDate")) {
				String dateStr = joParent.getString("registeredDate");
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
	
	public Date getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	// toString method
	
	@Override
	public String toString() {
		return "Parent [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", phone=" + phone + ", location=" + location + ", dob=" + dob + ", registeredDate=" + registeredDate
				+ ", students=" + students + "]";
	}

}
