package com.tratumtech.edugreat.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Parent {
	
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String passw;
	private String location;
	private Date dob;

	public Parent() {
	}

	public Parent(int id, String firstName, String lastName, String email, String passw) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.passw = passw;
	}

	public Parent(int id, String firstName, String lastName, String email, String phone, String passw, String location,
			Date dob) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.passw = passw;
		this.location = location;
		this.dob = dob;
	}
	
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
			
			if(joParent.has("passw"))
				this.passw = joParent.getString("passw");
			
			if(joParent.has("location"))
				this.location = joParent.getString("location");
			
			if(joParent.has("dob")) {
				String dateStr = joParent.getString("dob");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					this.dob = sdf.parse(dateStr);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
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

	public String getPassw() {
		return this.passw;
	}

	public void setPassw(String passw) {
		this.passw = passw;
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

}
