package com.tratumtech.edugreat.tool;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class QuestionsHome {
	public HashSet<Questions> getAllQuestions() {

		Transaction tx = null;
		Session objSession = null;
		try{
			
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			Query query = objSession.createQuery("from Questions"); 

			@SuppressWarnings("unchecked")
			List<Questions> empList = query.list(); 
			HashSet<Questions> quesList = new HashSet<Questions>(empList);
			
			tx.commit();
			objSession.close();
			
			return quesList;
		}
		catch (RuntimeException re) {
			objSession.close();
			tx.rollback();
			throw re;
		}
	}
	
	public Questions GetQuestionsById(int questionId){
		Questions objQuestions = null;
		
		try {
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            objQuestions =  (Questions) objSession.get(Questions.class, questionId);
            objSession.close();
            return objQuestions;
        } catch (Exception e) {
        	throw e;
        }
		
	}
	
	public JSONObject addQuestions(String questionData){		
		JSONObject joQuestions = null;
		Transaction tx = null ;
		int questionId = -1;
		JSONObject result = new JSONObject();

		try
		{
			joQuestions = new JSONObject(questionData);
			Questions objQuestions = new Questions(joQuestions);
			result.put("id", "-1");
			result.put("message", "Error. Insert failed.");
						
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			objSession.save(objQuestions);
			questionId=objQuestions.getId();
			result.put("id", questionId);
			result.put("message", "Inserted.");
			
			
			tx.commit();
		
			objSession.close();
		
			return result;
		}
		catch (RuntimeException | JSONException re) {
			tx.rollback();
		}
		return result;
	}
	
	
	public JSONObject updateQuestions(String empData){
		Transaction tx = null ;
		JSONObject joQuestions = null;

		JSONObject joResult = new JSONObject();
		try
		{
			joResult.put("id", -1);
			joResult.put("message", "Error. Update failed.");
			
			joQuestions = new JSONObject(empData);
			Questions objQuestions = new Questions(joQuestions);
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			objSession.saveOrUpdate(objQuestions);
			joResult.put("id", 0);
			joResult.put("message", "Updated.");
			
			tx.commit();
		
			joResult.put("id", joQuestions.get("id"));
			objSession.close();
			return joResult;
		}
		catch (RuntimeException | JSONException re) {
			tx.rollback();
		}
		return joResult ;
	}
	
	public JSONObject deleteQuestions(String questionData) {
		Transaction tx = null;
		JSONObject result = new JSONObject();
		
		try {
			result.put("id", -1);
			result.put("message", "Error. Delete failed.");
			JSONObject joQuestions = new JSONObject(questionData);
			Questions objQuestions = new Questions(joQuestions);
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			objSession.delete(objQuestions);
			result.put("id", "0");
			result.put("message", "Deleted.");
			
			tx.commit();
			objSession.close();
			
			result.put("id", joQuestions.get("id"));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	   @SuppressWarnings("unchecked")
	public Set<Questions> selectQuestions(String data) {
	        Transaction tx = null;
	        List<Questions> questionsList = null;
	        JSONObject filters = null;
	        Set<Questions> questions = null;
	        
	        try {
	            filters = new JSONObject(data);
	            Session objSession = HibernateUtil.getSessionFactory().openSession();
	            tx = objSession.beginTransaction();
	            Criteria criteria = objSession.createCriteria(Questions.class);
	            if (filters.has("category")) {
	                criteria.add(Restrictions.eq("category", filters.getString("category")));
	            }
	            if (filters.has("difficulty")) {
	                criteria.add(Restrictions.eq("difficulty", filters.getString("difficulty")));
	            }
	            criteria.add(Restrictions.sqlRestriction("1=1 order by rand()"));
	            if (filters.has("qcount")) {
	                criteria.setMaxResults(filters.getInt("qcount"));
	            }
	            else {
	                criteria.setMaxResults(10);
	            }
	            
	            questionsList = criteria.list();
	            questions = new HashSet<Questions>(questionsList);
	            
	            tx.commit();
	            objSession.close();
	            
	            return questions;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return questions;
	    }

	
}
