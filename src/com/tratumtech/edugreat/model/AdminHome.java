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

import com.tratumtech.edugreat.model.Admin;
import com.tratumtech.edugreat.model.HibernateUtil;

public class AdminHome {
	
	// FUNCTION: Gets all Admin user info
	public JSONObject getAllAdmin() {
		Transaction tx = null;
		HashSet<Admin> allAdmin = null;
		JSONArray joAllAdmin = new JSONArray();
		JSONObject jo = new JSONObject();
		Session objSession = null;
		
		try{
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized");
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Query error.");
			Query query = objSession.createQuery("from Admin"); 
			
			@SuppressWarnings("unchecked")
			List<Admin> adminList = query.list(); 
			allAdmin = new HashSet<Admin>(adminList);
			
			tx.commit();
			objSession.close();
			
			jo.put("message", "Could not convert object to JSON.");
			if(allAdmin != null && !allAdmin.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAllAdmin = new JSONArray(mapper.writeValueAsString(allAdmin));
				jo.put("message", "GET success.");
				jo.put("object", joAllAdmin);
				jo.put("status", "VALID");
			}
		}
		catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	// FUNCTION: Gets an Admin user by ID
	public JSONObject getAdminById(int adminId){
		Admin objAdmin = null;
		JSONObject jo = new JSONObject();
		JSONObject joAdmin = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            
            jo.put("message", "Object not found.");
            objAdmin =  (Admin) objSession.get(Admin.class, adminId);            
            objSession.close();
            
            jo.put("message", "Could not convert object to JSON.");
            if(objAdmin != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAdmin = new JSONObject(mapper.writeValueAsString(objAdmin));
				jo.put("message", "GET success.");
	            jo.put("object", joAdmin);
				jo.put("status", "VALID");
			}            
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return jo;	
	}
	
	// FUNCTION: Gets an Admin user by Email
	public JSONObject getAdminByEmail(String adminData) {
		Admin objAdmin = null;
		JSONObject jo = new JSONObject();
		JSONObject joAdmin = new JSONObject();
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            
            JSONObject joData = new JSONObject(adminData);
            jo.put("message", "Criteria error.");
            Criteria criteria = objSession.createCriteria(Admin.class)
            		.add(Restrictions.eq("email", joData.get("email")));
            @SuppressWarnings("unchecked")
			List<Admin> list = criteria.list();
            objAdmin = list.get(0);
            objSession.close();
            
            jo.put("message", "Could not convert object to JSON.");
            if(objAdmin != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAdmin = new JSONObject(mapper.writeValueAsString(objAdmin));
				jo.put("message", "GET success.");
	            jo.put("object", joAdmin);
				jo.put("status", "VALID");
			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return jo;
	}
	
	// FUNCTION: Adds new Admin user
	public JSONObject addAdmin(String adminData){		
		JSONObject joAdmin = null;
		JSONObject jo = new JSONObject();
		Admin objAdmin = null;
		Transaction tx = null;

		try{
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			joAdmin = new JSONObject(adminData);
			objAdmin = new Admin(joAdmin);
			Date date = new Date();
		    objAdmin.setRegisteredDate(date);
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();

			SignInHome SH = new SignInHome();
			if (joAdmin.getBoolean("generate")) {
				JSONObject pass = SH.altSignUp(joAdmin);
				if (pass.getString("status").equals("VALID")) {
					jo.put("auth", pass.getString("auth"));
					jo.put("passw", pass.getString("passw"));
				} else {
					jo.put("message", "JSONObject has insufficient information.");
				}
			} else {
				JSONObject auth = SH.signUp(joAdmin);
				if (auth.getString("status").equals("VALID")) {
					jo.put("auth", auth.getString("auth"));
				}
				else {
					jo.put("message", "JSONObject has insufficient information.");
					throw new RuntimeException();
				}
			}
			
			objSession.save(objAdmin);
			joAdmin.put("id", objAdmin.getId());
			jo.put("message", "Saved.");
			
			tx.commit();
			objSession.close();
			
			jo.put("object", joAdmin);
			jo.put("status", "VALID");
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	// FUNCTION: Updates Admin user
	public JSONObject updateAdmin(String adminData){
		Transaction tx = null;
		JSONObject jo = new JSONObject();
		JSONObject joAdmin = new JSONObject();
		
		try {	
			JSONObject joData = new JSONObject(adminData);
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Admin objAdmin = (Admin) objSession.get(Admin.class, joData.getInt("id"));
			
			if (joData.has("firstName")) {
				objAdmin.setFirstName(joData.getString("firstName"));
			}
			if (joData.has("lastName")) {
				objAdmin.setLastName(joData.getString("lastName"));
			}
			if (joData.has("email")) {
				objAdmin.setEmail(joData.getString("email"));
			}
			if (joData.has("phone")) {
				objAdmin.setPhone(joData.getString("phone"));
			}
			if (joData.has("location")) {
				objAdmin.setLocation(joData.getString("location"));
			}
			if (joData.has("dob")) {
				objAdmin.setDob(formatter.parse(joData.getString("dob")));
			}
			
			objSession.saveOrUpdate(objAdmin);
			jo.put("status", "Saved.");
			
			jo.put("message", "Could not convert object to JSON.");
			if(objAdmin != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAdmin = new JSONObject(mapper.writeValueAsString(objAdmin));
				jo.put("object", joAdmin);
				jo.put("message", "PUT success.");
				jo.put("status", "VALID");
			}
			
			tx.commit();	
			objSession.close();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	// FUNCTION: Deletes Admin user
	public JSONObject deleteAdmin(String adminData) {
		Transaction tx = null;
		Admin objAdmin = null;
		JSONObject jo = new JSONObject();
		SignInHome SH = new SignInHome();
		
		try {
			jo.put("status", "INVALID");
			JSONObject joData = new JSONObject(adminData);
			
			jo.put("message", "Session Factory uninitialized.");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			objAdmin = (Admin) objSession.get(Admin.class, joData.getInt("id"));
			
			objSession.delete(objAdmin);
			
			if (SH.deleteAccount(adminData)) {
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
	
	// FUNCTION: Changes Admin Password
	public JSONObject changeAdminPassword(String adminData) {
		SignInHome SH = new SignInHome();
		JSONObject jo = new JSONObject();
		try {
			jo.put("status", "INVALID");
			boolean check = SH.changePassword(adminData);
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
