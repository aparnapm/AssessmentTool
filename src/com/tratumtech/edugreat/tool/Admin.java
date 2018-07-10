package com.tratumtech.edugreat.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Admin {
		
		private int id;
		private String firstName;
		private String lastName;
		private String email;
		private String phone;
		private String passw;
		private String location;
		private Date dob;
		
		public Admin() {
		}

		public Admin(int id, String firstName, String lastName, String email, String phone, String passw, String location,
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
					
					if(joAdmin.has("passw"))
						this.passw = joAdmin.getString("passw");
					
					
					if(joAdmin.has("location"))
						this.location = joAdmin.getString("location");
					
					if(joAdmin.has("dob")) {
						String dateStr = joAdmin.getString("dob");
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

		public String getPassw() {
			return passw;
		}

		public void setPassw(String passw) {
			this.passw = passw;
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

		@Override
		public String toString() {
			return "Admin [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
					+ ", phone=" + phone + ", passw=" + passw + ", location=" + location + ", dob=" + dob + "]";
		}
		
		

}



