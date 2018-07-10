package com.tratumtech.edugreat.tool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AssessmentHome {
	public HashSet<Assessment> getAllAssessment() {

		Transaction tx = null;
		Session objSession = null;
		try{
			
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			Query query = objSession.createQuery("from Assessment"); 

			@SuppressWarnings("unchecked")
			List<Assessment> empList = query.list(); 
			HashSet<Assessment> assessmentList = new HashSet<Assessment>(empList);
			
			tx.commit();
			objSession.close();
			
			return assessmentList;
		}
		catch (RuntimeException re) {
			objSession.close();
			tx.rollback();
			throw re;
		}
	}
	
	public Assessment GetAssessmentById(int assessmentId){
		Assessment objAssessment = null;
		
		try {
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            objAssessment =  (Assessment) objSession.get(Assessment.class, assessmentId);
            objSession.close();
            return objAssessment;
        } catch (Exception e) {
        	throw e;
        }
		
	}
	
	public JSONObject addAssessment(String assessmentData ){		
		JSONObject joAssessment = null;
		Integer assessment_id=-1;
		Transaction tx = null ;
		JSONObject result = new JSONObject();
        QuestionsHome QH=new QuestionsHome();
		try
		{
			joAssessment = new JSONObject(assessmentData);
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
			Date date= new Date();
			joAssessment.put("createdDate",formatter.format(date).toString());
			
			Set<Questions> questionList= QH.selectQuestions(assessmentData);
			Assessment objAssessment = new Assessment(joAssessment);
			result.put("id", "-1");
			result.put("message", "Error. Insert failed.");
			
			if(!joAssessment.has("qcount"))
				objAssessment.setQcount(questionList.size());
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			objSession.save(objAssessment);
			assessment_id=objAssessment.getId();
			result.put("id", assessment_id);
			result.put("message", "Inserted , not populated.");
		
			objAssessment.setQuestionset(questionList);
			objSession.saveOrUpdate(objAssessment);
			result.put("id", assessment_id);
			result.put("message", "Inserted and populated.");
			 
			tx.commit();
				}
		catch (RuntimeException | JSONException re) {
			tx.rollback();
		}
		return result;
	}
	
	
	public JSONObject updateAssessment(String empData){
		Transaction tx = null ;
		JSONObject joAssessment = null;

		JSONObject joResult = new JSONObject();
		try
		{
			joResult.put("id", -1);
			joResult.put("message", "Error. Update failed.");
			
			joAssessment = new JSONObject(empData);
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
			Date date= new Date();
			joAssessment.put("modifiedDate",formatter.format(date).toString());
			Assessment objAssessment = new Assessment(joAssessment);
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			objSession.saveOrUpdate(objAssessment);
			joResult.put("id", 0);
			joResult.put("message", "Updated.");
			
			tx.commit();
		
			joResult.put("id", joAssessment.get("id"));
			objSession.close();
			return joResult;
		}
		catch (RuntimeException | JSONException re) {
			tx.rollback();
		}
		return joResult ;
	}
	
	public JSONObject deleteAssessment(String assessmentData) {
		Transaction tx = null;
		JSONObject result = new JSONObject();
		
		try {
			result.put("id", -1);
			result.put("message", "Error. Delete failed.");
			JSONObject joAssessment = new JSONObject(assessmentData);
			Assessment objAssessment = new Assessment(joAssessment);
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			objSession.delete(objAssessment);
			result.put("id", "0");
			result.put("message", "Deleted.");
			
			tx.commit();
			objSession.close();
			
			result.put("id", joAssessment.get("id"));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	
	}

	
	public JSONObject populateAssessment(String data, int assessmentId) {
        JSONObject result = new JSONObject();
        QuestionsHome QH = new QuestionsHome();
        Transaction tx = null;
        
        try {
            result.put("message", "Could not populate assessment.");
            
            Set<Questions> questionList = QH.selectQuestions(data);
           
            Session objSession = HibernateUtil.getSessionFactory().openSession();
           
            tx = objSession.beginTransaction();
            Assessment assessment =  (Assessment) objSession.get(Assessment.class, assessmentId);
            
            assessment.setQcount(assessment.getQcount() + questionList.size());
            assessment.setQuestionset(questionList);
            objSession.saveOrUpdate(assessment);
            result.put("message", "Questions added.");
            
            tx.commit();
            
            objSession.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

		public Set<Questions> allQuestions(int assessment_id) {

			Transaction tx = null;
			Session objSession = null;
			Set<Questions> questionList=null;
			try{
				
				objSession = HibernateUtil.getSessionFactory().openSession();
				tx = objSession.beginTransaction();
	
				Assessment assessment =  (Assessment) objSession.get(Assessment.class, assessment_id);
				Hibernate.initialize(assessment.getQuestionset());
				questionList=assessment.getQuestionset();
				tx.commit();
				objSession.close();
				
				return questionList;
			}
			catch (RuntimeException re) {
				objSession.close();
				tx.rollback();
				throw re;
			}
		}
		
		public JSONObject deleteQuestionFromAssessment(String data)
		{
			Transaction tx = null;
			Session objSession = null;
			JSONObject result = new JSONObject();
			JSONObject jo = null;
			try
			{
				result.put("message","Question not deleted");
				jo=new JSONObject(data);
				objSession = HibernateUtil.getSessionFactory().openSession();
				tx = objSession.beginTransaction();
				Assessment assessment =(Assessment)objSession.get(Assessment.class, jo.getInt("assessment_id"));
				Questions question=(Questions)objSession.get(Questions.class, jo.getInt("question_id"));
				Hibernate.initialize(assessment.getQuestionset());
				assessment.getQuestionset().remove(question);
				assessment.setQcount(assessment.getQcount()-1);
				objSession.update(assessment);
				result.put("message","Question deleted");
				tx.commit();
				objSession.close();
				return result;
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		return result;
	}
		
		public JSONObject addQuestionToAssessment(String data)
		{
			Transaction tx = null;
			Session objSession = null;
			JSONObject result = new JSONObject();
			JSONObject jo = null;
			try
			{
				result.put("message","Question not added");
				jo=new JSONObject(data);
				objSession = HibernateUtil.getSessionFactory().openSession();
				tx = objSession.beginTransaction();
				Assessment assessment =(Assessment)objSession.get(Assessment.class, jo.getInt("assessment_id"));
				Questions question=(Questions)objSession.get(Questions.class, jo.getInt("question_id"));
				Hibernate.initialize(assessment.getQuestionset());
				assessment.getQuestionset().add(question);
				assessment.setQcount(assessment.getQcount()+1);
				objSession.update(assessment);
				result.put("message","Question added");
				tx.commit();
				objSession.close();
				return result;
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		return result;
	}
}