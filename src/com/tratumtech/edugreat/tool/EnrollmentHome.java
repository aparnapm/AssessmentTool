package com.tratumtech.edugreat.tool;

import java.util.HashSet;
import java.util.List;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.tratumtech.edugreat.tool.Enrollment;
import com.tratumtech.edugreat.tool.HibernateUtil;

public class EnrollmentHome {
	
	public HashSet<Enrollment> getAllEnrollment() {

		Transaction tx = null;
		Session objSession = null;
		try{
			
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			Query query = objSession.createQuery("from Enrollment"); 

		
			@SuppressWarnings("unchecked")
			List<Enrollment> empList = query.list(); 
			HashSet<Enrollment> enrollmentList = new HashSet<Enrollment>(empList);
	
			tx.commit();
			
			return enrollmentList;
		}
		catch (RuntimeException re) {
			objSession.close();
			tx.rollback();
			throw re;
		}
	}
	
	public Enrollment GetEnrollmentById(int enrollmentId){
		Enrollment objEnrollment = null;
		
		try {
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            objEnrollment =  (Enrollment) objSession.get(Enrollment.class, enrollmentId);
            objSession.close();
            return objEnrollment;
        } catch (Exception e) {
        	throw e;
        }
	}
	
	public int addEnrollment(String enrollmentData){		
		JSONObject joEnrollment = null;
		Transaction tx = null ;
		int enrollmentId = 0;
		
		try
		{
			joEnrollment = new JSONObject(enrollmentData);
			Enrollment objEnrollment = new Enrollment(joEnrollment);			
						
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			objSession.save(objEnrollment);
			
			tx.commit();
			objSession.close();
			enrollmentId = objEnrollment.getId();
			
			return enrollmentId ;
		}
		catch (RuntimeException | JSONException re) {
			tx.rollback();
		}
		return enrollmentId ;
	}
	
	
	public JSONObject updateEnrollment(String enrollmentData){
		Transaction tx = null ;
		JSONObject joEnrollment = null;
		
		int enrollmentId = 0;
		JSONObject joResult = new JSONObject();
		try
		{
			joResult.put("message", "ERROR");
			joResult.put("code", Response.Status.NOT_FOUND.getStatusCode());
			
			joEnrollment = new JSONObject(enrollmentData);
			Enrollment objEnrollment = new Enrollment(joEnrollment);
				
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			objSession.saveOrUpdate(objEnrollment);
			
			tx.commit();
			objSession.close();
			enrollmentId = objEnrollment.getId();
				
			joResult.put("message", "SUCCESSFULLY UPDATED");
			joResult.put("enrollmentId", enrollmentId);
			joResult.put("code", Response.Status.OK.getStatusCode());
			return joResult;
		}
		catch (RuntimeException | JSONException re) {
			tx.rollback();
		}
		return joResult ;
	}
	
	public JSONObject deleteEnrollment(int enrollment_id){
		Transaction tx = null ;
		
		JSONObject joResult = new JSONObject();
		  Enrollment objEnrollment = new Enrollment();
		
		try
		{
			joResult.put("message", "ERROR");  
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			objEnrollment =  (Enrollment) objSession.get(Enrollment.class, enrollment_id);
			tx = objSession.beginTransaction();
			
			objSession.delete(objEnrollment);
				
			joResult.put("message", "SUCCESSFULLY DELETED");
			joResult.put("enrollmentId", enrollment_id);
			tx.commit();
			objSession.close();
			return joResult;
		}
		catch (RuntimeException | JSONException re) {
			
			tx.rollback();
		}
		
		return joResult ;
	}
	public HashSet<Enrollment> getEnrollmentByStudent(int student_id) {
		Transaction tx = null;
		Session objSession = null;
		try{
			
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			Criteria criteria = objSession.createCriteria(Enrollment.class);
		    criteria.add(Restrictions.eq("studentId", student_id));

			@SuppressWarnings("unchecked")
			List<Enrollment> empList = criteria.list(); 
			HashSet<Enrollment> enrollmentList = new HashSet<Enrollment>(empList);
			
			tx.commit();
			objSession.close();
			
			return enrollmentList;
		}
		catch (RuntimeException re) {
			tx.rollback();
			throw re;
		}
	}
		
		public HashSet<Enrollment> getEnrollmentByAssessment(int assessment_id) {
			Transaction tx = null;
			Session objSession = null;
			try{
				
				objSession = HibernateUtil.getSessionFactory().openSession();
				tx = objSession.beginTransaction();
				
				Criteria criteria = objSession.createCriteria(Enrollment.class);
			    criteria.add(Restrictions.eq("assessmentId", assessment_id));

				@SuppressWarnings("unchecked")
				List<Enrollment> empList = criteria.list(); 
				HashSet<Enrollment> enrollmentList = new HashSet<Enrollment>(empList);
				
				tx.commit();
				objSession.close();
				
				return enrollmentList;
			}
			catch (RuntimeException re) {
				tx.rollback();
				throw re;
			}
	}

		public Enrollment getSpecificEnrollment(int a_id, int s_id) {
			// TODO Auto-generated method stub
			Transaction tx = null;
			Session objSession = null;
			Enrollment obj=new Enrollment();
			try
			{
			
				objSession = HibernateUtil.getSessionFactory().openSession();
				tx = objSession.beginTransaction();
				
				Criteria criteria= objSession.createCriteria(Enrollment.class);
				criteria.add(Restrictions.eq("assessmentId",a_id));
				criteria.add(Restrictions.eq("student_id",s_id));
				
				@SuppressWarnings("unchecked")
				List<Enrollment> enrollmentlist=criteria.list();
				obj=enrollmentlist.get(0);
				tx.commit();
				objSession.close();
				return obj;
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return null;
		}
}
