package com.tratumtech.edugreat.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class QuestionsHome {
	
	public JSONObject getAllQuestions() {
		HashSet<Questions> allQuestions = null;
		Transaction tx = null;
		JSONArray joAllQuestions = new JSONArray();
		JSONObject jo = new JSONObject();
		Session objSession = null;
		try{
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized");
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Query error.");
			Query query = objSession.createQuery("from Questions"); 

			@SuppressWarnings("unchecked")
			List<Questions> questionList = query.list(); 
			allQuestions = new HashSet<Questions>(questionList);
			
			tx.commit();
			objSession.close();
			
			jo.put("message", "Could not convert object to JSON.");
			if(allQuestions != null && !allQuestions.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAllQuestions = new JSONArray(mapper.writeValueAsString(allQuestions));
				jo.put("message", "GET success.");
				jo.put("object", joAllQuestions);
				jo.put("status", "VALID");
			}
		}
		catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject getQuestionsById(int questionsId){
		Questions objQuestions = null;
		JSONObject jo = new JSONObject();
		JSONObject joQuestions = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            
            jo.put("message", "Object not found.");
            objQuestions =  (Questions) objSession.get(Questions.class, questionsId);
            objSession.close();
            
            jo.put("message", "Could not convert object to JSON.");
            if(objQuestions != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joQuestions = new JSONObject(mapper.writeValueAsString(objQuestions));
				jo.put("message", "GET success.");
	            jo.put("object", joQuestions);
				jo.put("status", "VALID");
			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return jo;
	}
	
	public JSONObject addQuestions(String questionsData){		
		JSONObject joQuestions = null;
		JSONObject jo = new JSONObject();
		Questions objQuestions = null;
		Transaction tx = null ;

		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			joQuestions = new JSONObject(questionsData);
			objQuestions = new Questions(joQuestions);
						
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			objSession.save(objQuestions);
			joQuestions.put("id", objQuestions.getId());
			jo.put("message", "Saved.");
			
			tx.commit();
			objSession.close();
			
			jo.put("object", joQuestions);
			jo.put("status", "VALID");
		}
		catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	
	public JSONObject updateQuestions(String questionsData){
		Transaction tx = null;
		JSONObject jo = new JSONObject();
		JSONObject joQuestions = new JSONObject();
		
		try {
			JSONObject joData = new JSONObject(questionsData);
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized."); 
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Questions objQuestions = (Questions) objSession.get(Questions.class, joData.getInt("id"));
			if (joData.has("ques")) {
				objQuestions.setQues(joData.getString("ques"));
			}
			if (joData.has("opta")) {
				objQuestions.setOpta(joData.getString("opta"));
			}
			if (joData.has("optb")) {
				objQuestions.setOptb(joData.getString("optb"));
			}
			if (joData.has("optc")) {
				objQuestions.setOptc(joData.getString("optc"));
			}
			if (joData.has("optd")) {
				objQuestions.setOptd(joData.getString("optd"));
			}
			if (joData.has("answer")) {
				objQuestions.setAnswer(joData.getString("answer").charAt(0));
			}
			if (joData.has("category")) {
				objQuestions.setCategory(joData.getString("category"));
			}
			if (joData.has("difficulty")) {
				objQuestions.setDifficulty(joData.getString("difficulty"));
			}
			
			objSession.saveOrUpdate(objQuestions);
			jo.put("status", "Saved.");
			
			jo.put("message", "Could not convert object to JSON.");
			if(objQuestions != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joQuestions = new JSONObject(mapper.writeValueAsString(objQuestions));
				jo.put("object", joQuestions);
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
	
	public JSONObject deleteQuestions(String questionsData) {
		Transaction tx = null;
		Questions objQuestions = null;
		JSONObject jo = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			JSONObject joData = new JSONObject(questionsData);
			
			jo.put("message", "Session Factory uninitialized.");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			objQuestions = (Questions) objSession.get(Questions.class, joData.getInt("id"));
			
			objSession.delete(objQuestions);
			
			jo.put("message", "Deleted.");
			tx.commit();
			objSession.close();
			
			jo.put("status", "VALID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}	
	
	public Set<Questions> selectQuestions(String data) {
		Transaction tx = null;
		JSONObject filters = null;
		Set<Questions> questions = null;
		
		try {
			filters = new JSONObject(data);
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			Criteria criteria = objSession.createCriteria(Questions.class);
			if (filters.has("category")) {
				String categoryFilter = filters.getString("category");
				if(!categoryFilter.equals("mix")) {
					criteria.add(Restrictions.eq("category", categoryFilter));
				}
			}
			if (filters.has("difficulty")) {
				String difficultyFilter = filters.getString("difficulty");
				if(!difficultyFilter.equals("mix")) {
					criteria.add(Restrictions.eq("difficulty", difficultyFilter));
				}
			}
			if (filters.has("qcount")) {
				criteria.setMaxResults(filters.getInt("qcount"));
			} else {
				criteria.setMaxResults(10);
			}
			criteria.add(Restrictions.sqlRestriction("1=1 order by rand()"));
			
			@SuppressWarnings("unchecked")
			List<Questions> questionsList = criteria.list();
			questions = new HashSet<Questions>(questionsList);
			
			tx.commit();
			objSession.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return questions;
	}
	

}
