package com.tratumtech.edugreat.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class ParentHome {
	
	// FUNCTION: Gets all Parent user info
	public JSONObject getAllParent() {
		HashSet<Parent> allParent = null;
		JSONArray joAllParent = new JSONArray();
		JSONObject jo = new JSONObject();
		Transaction tx = null;
		Session objSession = null;
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized");
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Query error.");
			Query query = objSession.createQuery("from Parent"); 

			@SuppressWarnings("unchecked")
			List<Parent> parentList = query.list(); 
			allParent = new HashSet<Parent>(parentList);
			
			tx.commit();
			objSession.close();
			
			jo.put("message", "Could not convert object to JSON.");
			if(allParent != null && !allParent.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAllParent = new JSONArray(mapper.writeValueAsString(allParent));
				jo.put("message", "GET success.");
				jo.put("object", joAllParent);
				jo.put("status", "VALID");
			}
		}
		catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	// FUNCTION: Gets an Parent user by ID
	public JSONObject getParentById(int parentId){
		Parent objParent = null;
		JSONObject jo = new JSONObject();
		JSONObject joParent = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            
            jo.put("message", "Object not found.");
            objParent =  (Parent) objSession.get(Parent.class, parentId);
            objSession.close();
            
            jo.put("message", "Could not convert object to JSON.");
            if(objParent != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joParent = new JSONObject(mapper.writeValueAsString(objParent));
				jo.put("message", "GET success.");
	            jo.put("object", joParent);
				jo.put("status", "VALID");
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return jo;		
	}
	
	// FUNCTION: Gets an Parent user by Email
	public JSONObject getParentByEmail(String parentData) {
		Parent objParent = null;
		JSONObject jo = new JSONObject();
		JSONObject joParent = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            
            JSONObject joData = new JSONObject(parentData);
            jo.put("message", "Criteria error.");
            Criteria criteria = objSession.createCriteria(Parent.class)
            		.add(Restrictions.eq("email", joData.get("email")));
            @SuppressWarnings("unchecked")
			List<Parent> list = criteria.list();
            objParent = list.get(0);
            objSession.close();
            
            jo.put("message", "Could not convert object to JSON.");
            if(objParent != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joParent = new JSONObject(mapper.writeValueAsString(objParent));
				jo.put("message", "GET success.");
	            jo.put("object", joParent);
				jo.put("status", "VALID");
			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return jo;
	}
	
	// FUNCTION: Get Student Enrollments
	public JSONObject getEnrollmentsOfStudents(int id) {
		HashSet<Enrollment> enrollments = new HashSet<Enrollment>();
		JSONArray joEnrollments = new JSONArray();
		JSONObject jo = new JSONObject();
		Transaction tx = null;
		Session objSession = null;
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized");
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Parent objParent = (Parent) objSession.get(Parent.class, id);
			HashSet<Student> myStudents = new HashSet<Student>(objParent.getStudents());
			
			for (Student student : myStudents) {
				HashSet<Enrollment> stuEnrollments = new HashSet<Enrollment>(student.getEnrollments());
				for (Enrollment e : stuEnrollments) {
					enrollments.add(e);
				}
			}
			
			tx.commit();
			objSession.close();
			
			jo.put("message", "Could not convert object to JSON.");
			if(enrollments != null && !enrollments.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joEnrollments = new JSONArray(mapper.writeValueAsString(enrollments));
				jo.put("message", "GET success.");
				jo.put("object", joEnrollments);
				jo.put("status", "VALID");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jo;
	}
	
	// FUNCTION: Adds new Parent user
	public JSONObject addParent(String parentData){	
		JSONObject jo = new JSONObject();
		JSONObject joParent = null;
		Transaction tx = null ;
		Parent objParent = null;

		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			joParent = new JSONObject(parentData);
			objParent = new Parent(joParent);
			Date date = new Date();
		    objParent.setRegisteredDate(date);
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();

			SignInHome SH = new SignInHome();
			if (joParent.getBoolean("generate")) {
				JSONObject pass = SH.altSignUp(joParent);
				if (pass.getString("status").equals("VALID")) {
					jo.put("auth", pass.getString("auth"));
					jo.put("passw", pass.getString("passw"));
				} else {
					jo.put("message", "JSONObject has insufficient information.");
				}
			} else {
				JSONObject auth = SH.signUp(joParent);
				if (auth.getString("status").equals("VALID")) {
					jo.put("auth", auth.getString("auth"));
				}
				else {
					jo.put("message", "JSONObject has insufficient information.");
					throw new RuntimeException();
				}
			}
			
			objSession.save(objParent);
			joParent.put("id", objParent.getId());
			jo.put("message", "Saved.");
			
			tx.commit();
			objSession.close();
			
			jo.put("object", joParent);
			jo.put("status", "VALID");
		}
		catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	// FUNCTION: Updates Parent user
	public JSONObject updateParent(String parentData){
		Transaction tx = null ;
		JSONObject joParent = null;
		JSONObject jo = new JSONObject();
		
		try {	
			JSONObject joData = new JSONObject(parentData);
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Parent objParent = (Parent) objSession.get(Parent.class, joData.getInt("id"));
			if (joData.has("firstName")) {
				objParent.setFirstName(joData.getString("firstName"));
			}
			if (joData.has("lastName")) {
				objParent.setLastName(joData.getString("lastName"));
			}
			if (joData.has("email")) {
				objParent.setEmail(joData.getString("email"));
			}
			if (joData.has("phone")) {
				objParent.setPhone(joData.getString("phone"));
			}
			if (joData.has("location")) {
				objParent.setLocation(joData.getString("location"));
			}
			if (joData.has("dob")) {
				objParent.setDob(formatter.parse(joData.getString("dob")));
			}
			
			objSession.saveOrUpdate(objParent);
			jo.put("message", "Saved.");
			
			tx.commit();
			objSession.close();
			
			jo.put("message", "Could not convert object to JSON.");
			if(objParent != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joParent = new JSONObject(mapper.writeValueAsString(objParent));
				jo.put("object", joParent);
				jo.put("message", "PUT success.");
				jo.put("status", "VALID");
			}
		}
		catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject deleteParent(String parentData) {
		Transaction tx = null;
		Parent objParent = null;
		JSONObject jo = new JSONObject();
		SignInHome SH = new SignInHome();
		
		try {
			jo.put("status", "INVALID");
			JSONObject joData = new JSONObject(parentData);
			
			jo.put("message", "Session Factory uninitialized.");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			objParent = (Parent) objSession.get(Parent.class, joData.getInt("id"));
			tx = objSession.beginTransaction();
			objSession.delete(objParent);
			
			if (SH.deleteAccount(parentData)) {
				jo.put("auth", "Authentication deleted.");
			}
			else {
				jo.put("message", "JSONObject has insufficient information.");
				throw new Exception();
			}
			jo.put("message", "Deleted.");
			tx.commit();
			objSession.close();
			
			jo.put("status", "VALID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	// FUNCTION: Changes Parent Password
	public JSONObject changeParentPassword(String parentData) {
		SignInHome SH = new SignInHome();
		JSONObject jo = new JSONObject();
		try {
			jo.put("status", "INVALID");
			boolean check = SH.changePassword(parentData);
			if (check) {
				jo.put("message", "Password changed!");
				jo.put("status", "VALID");
			}
			else {
				jo.put("message", "Something went wrong.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}

}
