package com.tratumtech.edugreat.tool;


import java.util.HashSet;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class StudentHome {
	
	public HashSet<Student> getAllStudent() {

		Transaction tx = null;
		Session objSession = null;
		try{
			
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			Query query = objSession.createQuery("from Student"); 

			@SuppressWarnings("unchecked")
			List<Student> empList = query.list(); 
			HashSet<Student> studentList = new HashSet<Student>(empList);
			
			tx.commit();
			objSession.close();
			
			return studentList;
		}
		catch (RuntimeException re) {
			objSession.close();
			tx.rollback();
			throw re;
		}
	}
	
	public Student GetStudentById(int studentId){
		Student objStudent = null;
		
		try {
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            objStudent =  (Student) objSession.get(Student.class, studentId);
            objSession.close();
            return objStudent;
        } catch (Exception e) {
        	throw e;
        }
		
	}
	
	public HashSet<Student> getStudentByParent(int p_id) {
		Transaction tx = null;
		Session objSession = null;
		try{
			
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			Criteria criteria = objSession.createCriteria(Student.class)
				    .add(Restrictions.eq("PId", p_id));
			criteria.addOrder(Order.asc("standard"));

			@SuppressWarnings("unchecked")
			List<Student> empList = criteria.list(); 
			HashSet<Student> studentList = new HashSet<Student>(empList);
			
			tx.commit();
			objSession.close();
			
			return studentList;
		}
		catch (RuntimeException re) {
			tx.rollback();
			throw re;
		}
	}
	
	public JSONObject addStudent(String studentData){		
		JSONObject joStudent = null;
		Transaction tx = null ;
		int studentId = -1;
		JSONObject result = new JSONObject();

		try
		{
			joStudent = new JSONObject(studentData);
			Student objStudent = new Student(joStudent);
			result.put("id", "-1");
			result.put("message", "Error. Insert failed.");
						
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			objSession.save(objStudent);
			studentId=objStudent.getId();
			result.put("id", studentId);
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
	
	public JSONObject updateStudent(String empData){
		Transaction tx = null ;
		JSONObject joStudent = null;

		JSONObject joResult = new JSONObject();
		try
		{
			joResult.put("id", -1);
			joResult.put("message", "Error. Update failed.");
			
			joStudent = new JSONObject(empData);
			Student objStudent = new Student(joStudent);
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			objSession.saveOrUpdate(objStudent);
			joResult.put("id", 0);
			joResult.put("message", "Updated.");
			
			tx.commit();
		
			joResult.put("id", joStudent.get("id"));
			objSession.close();
			return joResult;
		}
		catch (RuntimeException | JSONException re) {
			tx.rollback();
		}
		return joResult ;
	}
	
	public JSONObject deleteStudent(int student_id) {
		Transaction tx = null;
		Student objStudent = new Student();
		JSONObject result = new JSONObject();
		try {
			result.put("id", -1);
			result.put("message", "Error. Delete failed.");
			
		
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			objStudent =  (Student) objSession.get(Student.class, student_id);
			objSession.delete(objStudent);
			result.put("id", "0");
			result.put("message", "Deleted.");
			
			tx.commit();
			objSession.close();
			
			//result.put("id", joStudent.get("id"));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}