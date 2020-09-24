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

public class EnrollmentHome {
	
	public JSONObject getAllEnrollment() {
		Transaction tx = null;
		HashSet<Enrollment> allEnrollment = null;
		JSONArray joAllEnrollment = new JSONArray();
		JSONObject jo = new JSONObject();
		Session objSession = null;
		
		try{	
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized");
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Query error.");
			Query query = objSession.createQuery("from Enrollment"); 

			@SuppressWarnings("unchecked")
			List<Enrollment> enrollmentList = query.list(); 
			allEnrollment = new HashSet<Enrollment>(enrollmentList);
			
			tx.commit();
			objSession.close();
			
			jo.put("message", "Could not convert object to JSON.");
			if(allEnrollment != null && !allEnrollment.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAllEnrollment = new JSONArray(mapper.writeValueAsString(allEnrollment));
				jo.put("message", "GET success.");
				jo.put("object", joAllEnrollment);
				jo.put("status", "VALID");
			}
		}
		catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject getEnrollmentById(int enrollmentId){
		Enrollment objEnrollment = null;
		JSONObject jo = new JSONObject();
		JSONObject joEnrollment = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            
            jo.put("message", "Object not found.");
            objEnrollment =  (Enrollment) objSession.get(Enrollment.class, enrollmentId);
            
            /*TODO: Change when SQL jobs can be added.*/
            Date today = new Date();
            if (objEnrollment.getValidUntil().before(today)) {
            	if (objEnrollment.getAttempts() == objEnrollment.getAssessment().getAttempts()) {
            		objEnrollment.setStat("incomplete");
            	} else {
            		objEnrollment.setStat("complete");
            	}
            }
            objSession.saveOrUpdate(objEnrollment);
            objSession.close();
            
            jo.put("message", "Could not convert object to JSON.");
            if(objEnrollment != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joEnrollment = new JSONObject(mapper.writeValueAsString(objEnrollment));
				jo.put("message", "GET success.");
	            jo.put("object", joEnrollment);
				jo.put("status", "VALID");
			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return jo;
		
	}
	
	public JSONObject getEnrollmentByStudent(int studentId) {
		HashSet<Enrollment> stuEnrollment = null;
		JSONObject jo = new JSONObject();
		JSONObject joStudent = null;
		JSONArray joStuEnrollment = new JSONArray();
		Transaction tx = null;
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Student objStudent = (Student) objSession.get(Student.class, studentId);
			
			jo.put("message", "Error in Hibernate initialize.");
			Hibernate.initialize(objStudent.getEnrollments());
			
			jo.put("message", "Error in generating questions.");
			stuEnrollment = new HashSet<Enrollment>(objStudent.getEnrollments());
			tx.commit();
            objSession.close();
			
            jo.put("message", "Could not convert object to JSON.");
			if(objStudent != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joStudent = new JSONObject(mapper.writeValueAsString(objStudent));
				if(stuEnrollment != null && !stuEnrollment.isEmpty()){	
					mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
					joStuEnrollment = new JSONArray(mapper.writeValueAsString(stuEnrollment));
					jo.put("message", "GET success.");
					joStudent.put("enrollments", joStuEnrollment);
					jo.put("object", joStudent);
					jo.put("status", "VALID");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	
	public JSONObject getEnrollmentByAssessment (int assessmentId) {
		HashSet<Enrollment> assessEnrollment = null;
		JSONObject jo = new JSONObject();
		JSONObject joAssessment = null;
		JSONArray joAssesEnrollment = new JSONArray();
		Transaction tx = null;
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Assessment objAssessment = (Assessment) objSession.get(Assessment.class, assessmentId);
			
			jo.put("message", "Error in Hibernate initialize.");
			Hibernate.initialize(objAssessment.getEnrollments());
			
			jo.put("message", "Error in generating questions.");
			assessEnrollment = new HashSet<Enrollment>(objAssessment.getEnrollments());
			tx.commit();
            objSession.close();
			
            jo.put("message", "Could not convert object to JSON.");
			if(objAssessment != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAssessment = new JSONObject(mapper.writeValueAsString(objAssessment));
				if(assessEnrollment != null && !assessEnrollment.isEmpty()){	
					mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
					joAssesEnrollment = new JSONArray(mapper.writeValueAsString(assessEnrollment));
					jo.put("message", "DELETE success.");
					joAssessment.put("enrollments", joAssesEnrollment);
					jo.put("object", joAssessment);
					jo.put("status", "VALID");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject getAvailableEnrollmentOfStudent(int studentId) {
		JSONObject jo = new JSONObject();
		HashSet<Enrollment> enrollments = null;
		HashSet<Enrollment> avaEnrollments = new HashSet<Enrollment>();
		JSONArray joAvaEnrollments = null;
		Transaction tx = null;
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Student objStudent = (Student) objSession.get(Student.class, studentId);
			
			jo.put("message", "Error in Hibernate initialize.");
			Hibernate.initialize(objStudent.getEnrollments());
			enrollments = new HashSet<Enrollment>(objStudent.getEnrollments());
			
			for (Enrollment enrollment : enrollments) {
				if (enrollment.getStat().equals("approved") || enrollment.getStat().equals("requested")) {
					avaEnrollments.add(enrollment);
				}
			}
			
			tx.commit();
			objSession.close();
			jo.put("message", "Could not convert object to JSON.");
			if(avaEnrollments != null && !avaEnrollments.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAvaEnrollments = new JSONArray(mapper.writeValueAsString(avaEnrollments));
				jo.put("message", "GET success.");
				jo.put("object", joAvaEnrollments);
				jo.put("status", "VALID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject getUnavailableEnrollmentOfStudent(int studentId) {
		JSONObject jo = new JSONObject();
		HashSet<Enrollment> enrollments = null;
		HashSet<Enrollment> unavaEnrollments = new HashSet<Enrollment>();
		JSONArray joUnavaEnrollments = null;
		Transaction tx = null;
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Student objStudent = (Student) objSession.get(Student.class, studentId);
			
			jo.put("message", "Error in Hibernate initialize.");
			Hibernate.initialize(objStudent.getEnrollments());
			enrollments = new HashSet<Enrollment>(objStudent.getEnrollments());
			
			for (Enrollment enrollment : enrollments) {
				if (enrollment.getStat().equals("incomplete") || enrollment.getStat().equals("complete")) {
					unavaEnrollments.add(enrollment);
				}
			}
			
			tx.commit();
			objSession.close();
			
			jo.put("message", "Could not convert object to JSON.");
			if(unavaEnrollments != null && !unavaEnrollments.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joUnavaEnrollments = new JSONArray(mapper.writeValueAsString(unavaEnrollments));
				jo.put("message", "GET success.");
				jo.put("object", joUnavaEnrollments);
				jo.put("status", "VALID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	public JSONObject addEnrollment(String enrollmentData){		
		JSONObject joEnrollment = null;
		JSONObject jo = new JSONObject();
		Transaction tx = null ;
		Enrollment objEnrollment = null;

		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			JSONObject joData = new JSONObject(enrollmentData);
			objEnrollment = new Enrollment(joData);
						
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Assessment objAssessment = (Assessment) objSession.get(Assessment.class, joData.getInt("assessmentId"));
			Student objStudent = (Student) objSession.get(Student.class, joData.getInt("studentId"));
			objEnrollment.setAssessment(objAssessment);
			objEnrollment.setStudent(objStudent);
			objEnrollment.setAttempts(objAssessment.getAttempts());

			objSession.save(objEnrollment);
			jo.put("message", "Saved.");
			
			if(objEnrollment != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joEnrollment = new JSONObject(mapper.writeValueAsString(objEnrollment));
	            jo.put("object", joEnrollment);
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
	
	
	public JSONObject updateEnrollment(String enrollmentData){
		Transaction tx = null;
		JSONObject jo = new JSONObject();
		JSONObject joEnrollment = new JSONObject();
		
		try {
			JSONObject joData = new JSONObject(enrollmentData);
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized."); 
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Enrollment objEnrollment = (Enrollment) objSession.get(Enrollment.class, joData.getInt("id"));
			
			if (joData.has("validFrom")) {
				objEnrollment.setValidFrom(formatter.parse(joData.getString("validFrom")));
			}
			if (joData.has("validUntil")) {
				objEnrollment.setValidUntil(formatter.parse(joData.getString("validUntil")));
			}
			if (joData.has("stat")) {
				objEnrollment.setStat(joData.getString("stat"));
			}
			if (joData.has("attempts")) {
				objEnrollment.setAttempts(joData.getInt("attempts"));
			}
			if (joData.has("score")) {
				objEnrollment.setScore(joData.getInt("score"));
			}
			
			objSession.saveOrUpdate(objEnrollment);
			
			jo.put("message", "Could not convert object to JSON.");
			if(objEnrollment != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joEnrollment = new JSONObject(mapper.writeValueAsString(objEnrollment));
				jo.put("object", joEnrollment);
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
	
	public JSONObject deleteEnrollment(String enrollmentData) {
		Transaction tx = null;
		Enrollment objEnrollment = null;
		JSONObject jo = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			JSONObject joData = new JSONObject(enrollmentData);
			
			jo.put("message", "Session Factory uninitialized.");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			objEnrollment = (Enrollment) objSession.get(Enrollment.class, joData.getInt("id"));
			objSession.delete(objEnrollment);

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
