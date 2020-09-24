package com.tratumtech.edugreat.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AssessmentHome {
	
	public JSONObject getAllAssessment() {
		Transaction tx = null;
		HashSet<Assessment> allAssessment = null;
		JSONArray joAllAssessment = new JSONArray();
		JSONObject jo = new JSONObject();
		Session objSession = null;
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized");
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Query error.");
			Query query = objSession.createQuery("from Assessment"); 

			@SuppressWarnings("unchecked")
			List<Assessment> assessmentList = query.list(); 
			allAssessment = new HashSet<Assessment>(assessmentList);
			
			tx.commit();
			objSession.close();
			
			jo.put("message", "Could not convert object to JSON.");
			if(allAssessment != null && !allAssessment.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAllAssessment = new JSONArray(mapper.writeValueAsString(allAssessment));
				jo.put("message", "GET success.");
				jo.put("object", joAllAssessment);
				jo.put("status", "VALID");
			}
		}
		catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject getAssessmentById(int assessmentId){
		Assessment objAssessment = null;
		JSONObject jo = new JSONObject();
		JSONObject joAssessment = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            
            jo.put("message", "Object not found.");
            objAssessment =  (Assessment) objSession.get(Assessment.class, assessmentId);
            objSession.close();
            
            jo.put("message", "Could not convert object to JSON.");
            if(objAssessment != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAssessment = new JSONObject(mapper.writeValueAsString(objAssessment));
				jo.put("message", "GET success.");
	            jo.put("object", joAssessment);
				jo.put("status", "VALID");
			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return jo;		
	}
	
	public JSONObject addAssessment(String assessmentData){		
		JSONObject joAssessment = null;
		JSONObject jo = new JSONObject();
		Transaction tx = null ;
		Assessment objAssessment = null;
		QuestionsHome QH = new QuestionsHome();

		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			JSONObject joData = new JSONObject(assessmentData);
		    Set<Questions> questionList = QH.selectQuestions(assessmentData);
		    
		    jo.put("message", "Incorrect data.");
			objAssessment = new Assessment(joData);
			objAssessment.setQcount(questionList.size());
			objAssessment.setQuestions(questionList);
			Date date = new Date();
			objAssessment.setCreatedDate(date);
			
			objSession.saveOrUpdate(objAssessment);
			
			jo.put("message", "Could not convert object to JSON.");
			if(objAssessment != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAssessment = new JSONObject(mapper.writeValueAsString(objAssessment));
	            jo.put("object", joAssessment);
	            jo.put("message", "POST success.");
				jo.put("status", "VALID");
			}
			
			tx.commit();
			objSession.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	
	public JSONObject updateAssessment(String assessmentData){
		Transaction tx = null;
		JSONObject jo = new JSONObject();
		JSONObject joAssessment = new JSONObject();
		
		try {
			JSONObject joData = new JSONObject(assessmentData);
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized."); 
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Assessment objAssessment = (Assessment) objSession.get(Assessment.class, joData.getInt("id"));
			if (joData.has("assessmentName")) {
				objAssessment.setAssessmentName(joData.getString("assessmentName"));
			}
			if (joData.has("description")) {
				objAssessment.setDescription(joData.getString("description"));
			}
			if (joData.has("category")) {
				objAssessment.setCategory(joData.getString("category"));
			}
			if (joData.has("attempts")) {
				objAssessment.setAttempts(joData.getInt("attempts"));
			}
			if (joData.has("qcount")) {
				objAssessment.setQcount(joData.getInt("qcount"));
			}
			if (joData.has("difficulty")) {
				objAssessment.setDifficulty(joData.getString("difficulty"));
			}
			if (joData.has("timeLimit")) {
				objAssessment.setTimeLimit(joData.getInt("timeLimit"));
			}
			Date date = new Date();
			objAssessment.setModifiedDate(date);
			
			objSession.saveOrUpdate(objAssessment);
			
			jo.put("message", "Could not convert object to JSON.");
			if(objAssessment != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAssessment = new JSONObject(mapper.writeValueAsString(objAssessment));
				jo.put("object", joAssessment);
				jo.put("message", "PUT success.");
				jo.put("status", "VALID");
			}
			
			tx.commit();	
			objSession.close();
		}
		catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject deleteAssessment(String assessmentData) {
		Transaction tx = null;
		Assessment objAssessment = null;
		JSONObject jo = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			JSONObject joData = new JSONObject(assessmentData);
			
			jo.put("message", "Session Factory uninitialized.");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			objAssessment = (Assessment) objSession.get(Assessment.class, joData.getInt("id"));
			objSession.delete(objAssessment);

			jo.put("message", "Deleted.");
			tx.commit();
			objSession.close();
			
			jo.put("status", "VALID");
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject getAllQuestionsById (int assessmentId) {
		Assessment objAssessment = null;
		JSONObject jo = new JSONObject();
		JSONArray joQuestionSet = null;
		JSONObject joAssessment = null;
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            
            jo.put("message", "Object not found.");
            objAssessment = (Assessment) objSession.get(Assessment.class, assessmentId);
            Hibernate.initialize(objAssessment.getQuestions());
            
            jo.put("message", "Error in generating questions.");
            Set<Questions> questionList = objAssessment.getQuestions();
            objSession.close();
            
			jo.put("message", "Could not convert object to JSON.");
			if(objAssessment != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAssessment = new JSONObject(mapper.writeValueAsString(objAssessment));
				jo.put("message", "Question list empty.");
				if(questionList != null && !questionList.isEmpty()){	
					mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
					joQuestionSet = new JSONArray(mapper.writeValueAsString(questionList));
					joAssessment.put("questions", joQuestionSet);
					jo.put("message", "GET success.");
				}
				jo.put("object", joAssessment);
				jo.put("status", "VALID");
			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return jo;
	}
	
	public JSONObject deleteQuestionFromAssessment (String data) {
		JSONObject jo = new JSONObject();
		JSONObject joAssessment = null;
		JSONArray joQuestionSet = null;
		Assessment objAssessment = null;
		Questions objQuestion = null;
		Transaction tx = null;
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");

			JSONObject joData = new JSONObject(data);
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			objAssessment = (Assessment) objSession.get(Assessment.class, joData.getInt("assessmentId"));
			objQuestion = (Questions) objSession.get(Questions.class, joData.getInt("questionId"));
			
			jo.put("message", "Error in Hibernate initialize.");
			Hibernate.initialize(objAssessment.getQuestions());
			
			objAssessment.getQuestions().remove(objQuestion);
			objAssessment.setQcount(objAssessment.getQcount()-1);
			Date date = new Date();
			objAssessment.setModifiedDate(date);
			objSession.saveOrUpdate(objAssessment);
            
            jo.put("message", "Error in generating questions.");
            Set<Questions> questionList = objAssessment.getQuestions();
            tx.commit();
            objSession.close();
            
			jo.put("message", "Could not convert object to JSON.");
			if(objAssessment != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAssessment = new JSONObject(mapper.writeValueAsString(objAssessment));
				if(questionList != null && !questionList.isEmpty()){	
					mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
					joQuestionSet = new JSONArray(mapper.writeValueAsString(questionList));
					jo.put("message", "DELETE success.");
					joAssessment.put("questions", joQuestionSet);
					jo.put("object", joAssessment);
					jo.put("status", "VALID");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject addQuestionToAssessment (String data) {
		JSONObject jo = new JSONObject();
		JSONObject joAssessment = null;
		JSONArray joQuestionSet = null;
		Assessment objAssessment = null;
		Questions objQuestion = null;
		Transaction tx = null;
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");

			JSONObject joData = new JSONObject(data);
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			objAssessment = (Assessment) objSession.get(Assessment.class, joData.getInt("assessmentId"));
			objQuestion = (Questions) objSession.get(Questions.class, joData.getInt("questionId"));
			
			jo.put("message", "Error in Hibernate initialize.");
			Hibernate.initialize(objAssessment.getQuestions());
			
			objAssessment.getQuestions().add(objQuestion);
			objAssessment.setQcount(objAssessment.getQcount()+1);
			Date date = new Date();
			objAssessment.setModifiedDate(date);
			objSession.saveOrUpdate(objAssessment);
            
            jo.put("message", "Error in generating questions.");
            Set<Questions> questionList = objAssessment.getQuestions();
            tx.commit();
            objSession.close();
            
			jo.put("message", "Could not convert object to JSON.");
			if(objAssessment != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAssessment = new JSONObject(mapper.writeValueAsString(objAssessment));
				if(questionList != null && !questionList.isEmpty()){	
					mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
					joQuestionSet = new JSONArray(mapper.writeValueAsString(questionList));
					jo.put("message", "DELETE success.");
					joAssessment.put("questions", joQuestionSet);
					jo.put("object", joAssessment);
					jo.put("status", "VALID");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return jo;
	}

}
