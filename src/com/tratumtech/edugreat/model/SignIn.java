package com.tratumtech.edugreat.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/* SIGNIN class model: Contains all logging in/signing up logic. 
 * Connects to 'signin' table in database.
 * Mapped by SignIn.hbm.xml.
 */
public class SignIn {
	
	private String email; // Email ID, String, primary key, not null, unique
	private String userType; // Type of user for this email, String, not null
	private String salt; // Salt added to password, String
	private String hashedPass; // SHA15 Hashed Password, String
	private String auth; // Authentication ID during log in, String, null when not logged in
	private Date authDate; // Date auth was created, Date, null when not logged in
	private boolean active; // Whether account is active or not.
	
	// blank default constructor
	public SignIn() {
	}
	
	// constructor with all not null components
	public SignIn(String email, String userType){
		this.email = email;
		this.userType = userType;
	}
	
	// constructor with all components
	public SignIn(String email, String userType, String salt, String hashedPass, String auth, Date authDate, boolean active) {
		this.email = email;
		this.userType = userType;
		this.salt = salt;
		this.hashedPass = hashedPass;
		this.auth = auth;
		this.authDate = authDate;
		this.active = active;
	}
	
	// constructor from JSONObject
	/* Passes JSONObject joSignIn to the constructor. This object is parsed for values it should contain
	 * and the SignIn values are set. Date is converted from string using DateFormatter. Surrounded with
	 * try/catch statements to avoid JSONExceptions.
	 */
	public SignIn(JSONObject joSignIn) {
		try {
			if(joSignIn.has("email"))
				this.email = joSignIn.getString("email");
			
			if(joSignIn.has("userType"))
				this.userType = joSignIn.getString("userType");
			
			if(joSignIn.has("salt"))
				this.salt = joSignIn.getString("salt");
			
			if(joSignIn.has("hashedPass"))
				this.hashedPass = joSignIn.getString("hashedPass");
			
			if(joSignIn.has("auth"))
				this.auth = joSignIn.getString("auth");
			
			if(joSignIn.has("authDate")) {
				String dateStr = joSignIn.getString("authDate");
				// sdf defines Date string as yyyy-MM-dd HH:mm:ss
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					// Date authDate is set by using sdf to parse dateStr
					this.authDate = sdf.parse(dateStr);
				} catch (ParseException e) {
					// if error is thrown, stack trace is printed.
					e.printStackTrace();
				}
			}
			
			if (joSignIn.has("active"))
				this.active = joSignIn.getBoolean("active");
			
		} catch (JSONException e) {
			// if error is thrown, stack trace is printed.
			e.printStackTrace();
		}
	}

	// Getters and Setters
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getHashedPass() {
		return hashedPass;
	}

	public void setHashedPass(String hashedPass) {
		this.hashedPass = hashedPass;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public Date getAuthDate() {
		return authDate;
	}

	public void setAuthDate(Date authDate) {
		this.authDate = authDate;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
