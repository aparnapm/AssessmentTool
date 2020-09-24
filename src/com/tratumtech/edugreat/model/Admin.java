package com.tratumtech.edugreat.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/* ADMIN class model: Contains all information about Admin users. 
 * Connects to 'admin' table in database.
 * Mapped by Admin.hbm.xml.
 */
public class Admin {
	
	private int id; // Admin ID, primary key, not null, unique
	private String firstName; // First name, String, not null
	private String lastName; // Last name, String, not null
	private String email; // Email ID, String, not null, unique
	private String phone; // Phone Number, String, not null
	private String location; // Location (admin's address), String
	private Date dob; // Date of birth, Date
	private Date registeredDate;
	
	// blank default constructor
	public Admin() {
	}

	// constructor with all not null components
	public Admin(int id, String firstName, String lastName, String email, Date regDate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.registeredDate = regDate;
	}

	// constructor with all components
	public Admin(int id, String firstName, String lastName, String email, String phone, String location,
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
	/* Passes JSONObject joAdmin to the constructor. This object is parsed for values it should contain and
	 * the Admin values are set. Date is converted from string using DateFormatter. Surrounded with try/catch
	 * statements to avoid JSONExceptions.
	 */
	public Admin(JSONObject joAdmin){
		try {
			if(joAdmin.has("id"))
				this.id = joAdmin.getInt("id");
			
			if(joAdmin.has("firstName"))
				this.firstName = joAdmin.getString("firstName");
			
			if(joAdmin.has("lastName"))
				this.lastName = joAdmin.getString("lastName");
			
			if(joAdmin.has("email"))
				this.email = joAdmin.getString("email");
			
			if(joAdmin.has("phone"))
				this.phone = joAdmin.getString("phone");
			
			if(joAdmin.has("location"))
				this.location = joAdmin.getString("location");
			
			// sdf defines Date string as yyyy-MM-dd
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if(joAdmin.has("dob")) {
				String dateStr = joAdmin.getString("dob");
				try {
					// Date dob is set by using sdf to parse dateStr
					this.dob = sdf.parse(dateStr);
				} catch (ParseException e) {
					// if error is thrown, stack trace is printed.
					e.printStackTrace();
				}
			}
			
			if(joAdmin.has("registeredDate")) {
				String dateStr = joAdmin.getString("registeredDate");
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

	// Getters and setters
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getDob() {
		return dob;
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
	
	// toString method 

	@Override
	public String toString() {
		return "Admin [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", phone=" + phone + ", location=" + location + ", dob=" + dob + ", registeredDate=" + registeredDate
				+ "]";
	}
	
}
