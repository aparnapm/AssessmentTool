package com.tratumtech.edugreat.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

public class SignInHome {
	
	private static String getSHA512(String input){
		String toReturn = null;
		try {
		    MessageDigest digest = MessageDigest.getInstance("SHA-512");
		    digest.reset();
		    digest.update(input.getBytes("utf8"));
		    toReturn = String.format("%040x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		return toReturn;
	}
	
	private static String generateSalt() { 
        SecureRandom random = new SecureRandom(); 
        byte[] byteSalt = new byte[16]; 
        random.nextBytes(byteSalt); 
        
        String salt = new String(byteSalt);
 
        return salt; 
    }
	
	private static String generateDefaultPass() {
		String ucLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lcLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*_=+-/.?<>)";

        String values = ucLetters + lcLetters + numbers + symbols;

        Random randomizer = new Random();
 
        String password = "";
 
        for (int i = 0; i < 12; i++) {
        	password += values.charAt(randomizer.nextInt(values.length()));
        }
        return password;
	}
	
	public JSONObject initialize() {
		JSONObject initObj = new JSONObject();
		Transaction tx = null;
		try {
			initObj.put("init", "false");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			tx.commit();
			objSession.close();
			initObj.put("init", "true");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return initObj;
	}
	
	public JSONObject getEmails() {
		HashSet<String> allEmail = null;
		JSONObject jo = new JSONObject();
		JSONArray joAllEmail = null;
		Transaction tx = null;
		Session objSession = null;
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized");
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Criteria error.");
			Criteria criteria = objSession.createCriteria(SignIn.class)
					.setProjection(Projections.projectionList()
							.add(Projections.property("email"), "email"));
			
			@SuppressWarnings("unchecked")
			List<String> emailList = criteria.list();
			allEmail = new HashSet<String>(emailList);
			
			tx.commit();
			objSession.close();
			
			jo.put("message", "Could not convert object to JSON.");
			if(allEmail != null && !allEmail.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAllEmail = new JSONArray(mapper.writeValueAsString(allEmail));
				jo.put("message", "GET success.");
				jo.put("object", joAllEmail);
				jo.put("status", "VALID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}

	public JSONObject login(String data) {
		JSONObject joSignIn = null;
		Transaction tx = null;
		JSONObject jo = new JSONObject();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			joSignIn = new JSONObject(data);
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			SignIn objSignIn = (SignIn) objSession.get(SignIn.class, joSignIn.getString("email"));
			
			jo.put("message", "User is not active.");
			if (objSignIn.getActive()) {
				if (!joSignIn.getString("userType").equals(objSignIn.getUserType())) {
					jo.put("message", "User type is wrong.");
					throw new RuntimeException();
				}
				
				if(joSignIn.has("auth")) {
					if (objSignIn.getAuth().equals(joSignIn.get("auth"))) {
						jo.put("status", "VALID");
						jo.put("message", "User is already logged in.");
					}
				} else {
					String passStr = joSignIn.getString("passw");
					String hashedPass = objSignIn.getHashedPass();
					String salt = objSignIn.getSalt();
					
					String salted = passStr+salt;
					String hashCheck = getSHA512(salted);
					
					jo.put("message", "Password is incorrect.");
					if (hashCheck.equals(hashedPass)) {
						jo.put("status", "VALID");
						jo.put("message", "User is now logged in.");
						
						boolean unique=false;
						while (!unique) {
							String auth = UUID.randomUUID().toString();
							try {
								objSignIn.setAuth(auth);
								unique = true;
								break;
							} catch (Exception e) {
								// loops again
							}
						}
						
					    Date date = new Date();
						objSignIn.setAuthDate(date);
						
						jo.put("email", objSignIn.getEmail());
						jo.put("auth", objSignIn.getAuth());
						jo.put("authDate", formatter.format(objSignIn.getAuthDate()).toString());
						jo.put("userType", objSignIn.getUserType());
						
						objSession.saveOrUpdate(objSignIn);
					}
				}
			}

			tx.commit();
			objSession.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject signUp(JSONObject joSignIn) {
		JSONObject jo = new JSONObject();
		Transaction tx = null;
		
		try {
			jo.put("status", "INVALID");
			
			SignIn objSignIn = new SignIn(joSignIn);
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			String salt = generateSalt();
			String saltedPass = joSignIn.getString("passw")+salt;
			String hashedPass = getSHA512(saltedPass);
			String auth = UUID.randomUUID().toString();
			
			objSignIn.setEmail(joSignIn.getString("email"));
			objSignIn.setSalt(salt);
			objSignIn.setUserType(joSignIn.getString("userType"));
			objSignIn.setHashedPass(hashedPass);
			objSignIn.setAuth(auth);
			objSignIn.setActive(false);
			
			objSession.save(objSignIn);
			jo.put("auth", objSignIn.getAuth());
			
			tx.commit();
			objSession.close();
			jo.put("status", "VALID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject altSignUp(JSONObject joSignIn) {
		Transaction tx = null;
		JSONObject jo = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			SignIn objSignIn = new SignIn(joSignIn);
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			String password = generateDefaultPass();
			String salt = generateSalt();
			String saltedPass = password+salt;
			String hashedPass = getSHA512(saltedPass);
			String auth = UUID.randomUUID().toString();
			
			objSignIn.setEmail(joSignIn.getString("email"));
			objSignIn.setSalt(salt);
			objSignIn.setUserType(joSignIn.getString("userType"));
			objSignIn.setHashedPass(hashedPass);
			objSignIn.setAuth(auth);
			objSignIn.setActive(false);
			
			objSession.save(objSignIn);
			
			tx.commit();
			objSession.close();
			
			jo.put("status", "VALID");
			jo.put("passw", password);
			jo.put("auth", auth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject confirmSignIn(String emailID, String authID) {
		JSONObject jo = new JSONObject();
		Transaction tx;
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			SignIn objSignIn = (SignIn) objSession.get(SignIn.class, emailID);
			if (objSignIn.getAuth().equals(authID)) {
				jo.put("status", "VALID");
				jo.put("message", "Email confirmed.");
				objSignIn.setActive(true);
				objSignIn.setAuth(null);
			}
			
			tx.commit();
			objSession.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public boolean changePassword(String data) {
		boolean changed = false;
		JSONObject joSignIn = null;
		Transaction tx = null;
		
		try {
			joSignIn = new JSONObject(data);
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			SignIn objSignIn = (SignIn) objSession.get(SignIn.class, joSignIn.getString("email"));
			
			if (objSignIn.getActive()) {
				String oldPassword = joSignIn.getString("oldPassw");
				String newPassword = joSignIn.getString("passw");
				
				String oldSalt = objSignIn.getSalt();
				String oldHash = objSignIn.getHashedPass();
				String oldHashCheck = getSHA512(oldPassword+oldSalt);
				
				if (oldHashCheck.equals(oldHash)) {
					String newSalt = generateSalt();
					String newHash = getSHA512(newPassword+newSalt);
					objSignIn.setHashedPass(newHash);
					objSignIn.setSalt(newSalt);
					objSession.saveOrUpdate(objSignIn);
					changed = true;
				}
			}
			tx.commit();
			objSession.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return changed;
	}
	
	public JSONObject forceChangePassword(String data) {
		JSONObject joSignIn = null;
		JSONObject jo = new JSONObject();
		Transaction tx = null;
		
		try {
			jo.put("status", "VALID");
			jo.put("message", "Session Factory uninitialized.");
			
			joSignIn = new JSONObject(data);
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object does not exist.");
			SignIn objSignIn = (SignIn) objSession.get(SignIn.class, joSignIn.getString("email"));
			
			if (objSignIn.getActive()) {
				jo.put("message", "No user type entered.");
				String userType = joSignIn.getString("userType");
				jo.put("message", "No password entered.");
				String newPassword = joSignIn.getString("passw");
				
				jo.put("message", "User Type is not correct.");
				if (objSignIn.getUserType().equals(userType)) {
					String newSalt = generateSalt();
					String newHash = getSHA512(newPassword+newSalt);
					objSignIn.setHashedPass(newHash);
					objSignIn.setSalt(newSalt);
					objSession.saveOrUpdate(objSignIn);
					jo.put("status", "VALID");
					jo.put("message", "Password changed.");
				}
			}

			tx.commit();
			objSession.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject deactivateAccount(String data) {
		JSONObject joSignIn = null;
		JSONObject jo = new JSONObject();
		Transaction tx = null;
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			joSignIn = new JSONObject(data);
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object does not exist.");
			SignIn objSignIn = (SignIn) objSession.get(SignIn.class, joSignIn.getString("email"));
			
			objSignIn.setActive(false);
			objSession.save(objSignIn);
			
			tx.commit();
			objSession.close();
			jo.put("message", "Account deactivated.");
			jo.put("status", "VALID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public boolean deleteAccount(String data) {
		boolean deleted = false;
		JSONObject joSignIn = null;
		Transaction tx = null;
		
		try {
			joSignIn = new JSONObject(data);
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			SignIn objSignIn = (SignIn) objSession.get(SignIn.class, joSignIn.getString("email"));
			objSession.delete(objSignIn);
			
			tx.commit();
			objSession.close();
			deleted = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleted;
	}
	
	public JSONObject logout(String data) {
		JSONObject jo = new JSONObject();
		Transaction tx = null;
		JSONObject joToken = null;
		
		try {
			joToken = new JSONObject(data);
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized");
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			SignIn objSignIn = (SignIn) objSession.get(SignIn.class, joToken.getString("email"));
			
			jo.put("message", "Auth does not match.");
			if (objSignIn.getAuth().equals(joToken.getString("auth"))) {
				jo.put("status", "VALID");
				objSignIn.setAuth(null);
				objSignIn.setAuthDate(null);
				objSession.saveOrUpdate(objSignIn);
				jo.put("message", "Logged out.");
			}
			
			tx.commit();
			objSession.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject checkToken (String data) {
		Transaction tx = null;
		JSONObject joToken = null;
		JSONObject jo = new JSONObject();
		
		try {
			joToken = new JSONObject(data);
			
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			SignIn objSignIn = (SignIn) objSession.get(SignIn.class, joToken.getString("email"));
			
			jo.put("message", "Auth does not match.");
			if (objSignIn.getAuth().equals(joToken.getString("auth"))) {
				jo.put("message", "User Type does not match.");
				if (objSignIn.getUserType().equals(joToken.getString("userType"))) {
					jo.put("status", "VALID");
					jo.put("message", "Correct token.");
					objSession.saveOrUpdate(objSignIn);
				}
			}
			tx.commit();
			objSession.close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		}
		return jo;
	}
	
}
