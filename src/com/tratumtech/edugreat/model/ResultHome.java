package com.tratumtech.edugreat.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ResultHome {
	
	public JSONObject getAllResult() {
		Transaction tx = null;
		HashSet<Result> allResult = null;
		JSONArray joAllResult = new JSONArray();
		JSONObject jo = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Query error.");
			Query query = objSession.createQuery("from Result");
			
			@SuppressWarnings("unchecked")
			List<Result> resultList = query.list();
			allResult = new HashSet<Result>(resultList);
			
			tx.commit();
			objSession.close();
			
			jo.put("message", "Could not convert object to JSON.");
			if(allResult != null && !allResult.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAllResult = new JSONArray(mapper.writeValueAsString(allResult));
				jo.put("message", "GET success.");
				jo.put("object", joAllResult);
				jo.put("status", "VALID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject getResultById(int resultId) {
		Result objResult = null;
		JSONObject jo = new JSONObject();
		JSONObject joResult = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            
            jo.put("message", "Object not found.");
            objResult =  (Result) objSession.get(Result.class, resultId);
			
            jo.put("message", "Could not convert object to JSON.");
            if(objResult != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joResult = new JSONObject(mapper.writeValueAsString(objResult));
				jo.put("message", "GET success.");
	            jo.put("object", joResult);
				jo.put("status", "VALID");
            }
            
            objSession.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject getResultsByEnrollment(int enrollmentId) {
		HashSet<Result> enrResult = null;
		JSONObject jo = new JSONObject();
		JSONObject joEnrollment = null;
		JSONArray joEnrResult = new JSONArray();
		Transaction tx = null;
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Enrollment objEnrollment = (Enrollment) objSession.get(Enrollment.class, enrollmentId);
			
			jo.put("message", "Error in Hibernate initialize.");
			Hibernate.initialize(objEnrollment.getResults());
			
			jo.put("message", "Error in generating questions.");
			enrResult = new HashSet<Result>(objEnrollment.getResults());
			tx.commit();
            objSession.close();
            
            jo.put("message", "Could not convert object to JSON.");
			if(objEnrollment != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joEnrollment = new JSONObject(mapper.writeValueAsString(objEnrollment));
				if(enrResult != null && !enrResult.isEmpty()){	
					mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
					joEnrResult = new JSONArray(mapper.writeValueAsString(enrResult));
					jo.put("message", "GET success.");
					joEnrollment.put("results", joEnrResult);
					jo.put("object", joEnrollment);
					jo.put("status", "VALID");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject addResult(String resultData) {
		JSONObject joResult = null;
		JSONObject jo = new JSONObject();
		Transaction tx = null ;
		Result objResult = null;
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			JSONObject joData = new JSONObject(resultData);
			objResult = new Result(joData);
						
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Date today = new Date();
			objResult.setDateTaken(today);
			Enrollment objEnrollment = (Enrollment) objSession.get(Enrollment.class, joData.getInt("enrollmentId"));
			objResult.setEnrollment(objEnrollment);
			
			objSession.save(objResult);
			jo.put("message", "Saved.");
			
			if(objResult != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joResult = new JSONObject(mapper.writeValueAsString(objResult));
	            jo.put("object", joResult);
				jo.put("status", "VALID");
			}
			
			tx.commit();
			objSession.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject updateResult(String resultData) {
		Transaction tx = null;
		JSONObject jo = new JSONObject();
		JSONObject joResult = new JSONObject();
		
		try {
			JSONObject joData = new JSONObject(resultData);
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized."); 
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Result objResult = (Result) objSession.get(Result.class, joData.getInt("id"));
			if (joData.has("score")) {
				objResult.setScore(joData.getInt("score"));
			}
			if (joData.has("timeTaken")) {
				objResult.setTimeTaken(joData.getInt("timeTaken"));
			}
			if (joData.has("dateTaken")) {
				objResult.setDateTaken(formatter.parse(joData.getString("dateTaken")));
			}
			
			objSession.saveOrUpdate(objResult);
			
			jo.put("message", "Could not convert object to JSON.");
			if(objResult != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joResult = new JSONObject(mapper.writeValueAsString(objResult));
				jo.put("object", joResult);
				jo.put("message", "PUT success.");
				jo.put("status", "VALID");
			}
			
			tx.commit();	
			objSession.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject deleteResult(String resultData) {
		Transaction tx = null;
		Result objResult = null;
		JSONObject jo = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			JSONObject joData = new JSONObject(resultData);
			
			jo.put("message", "Session Factory uninitialized.");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			objResult = (Result) objSession.get(Result.class, joData.getInt("id"));
			objSession.delete(objResult);

			jo.put("message", "Deleted.");
			tx.commit();
			objSession.close();
			
			jo.put("status", "VALID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}

}
