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

public class StudentHome {
	
	// FUNCTION: Gets all Student user info
	public JSONObject getAllStudent() {
		HashSet<Student> allStudent = null;
		JSONArray joAllStudent = new JSONArray();
		JSONObject jo = new JSONObject();
		Transaction tx = null;
		Session objSession = null;
		try{
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized");
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Query error.");
			Query query = objSession.createQuery("from Student"); 

			@SuppressWarnings("unchecked")
			List<Student> studentList = query.list(); 
			allStudent = new HashSet<Student>(studentList);
			
			tx.commit();
			objSession.close();
			
			jo.put("message", "Could not convert object to JSON.");
			if(allStudent != null && !allStudent.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAllStudent = new JSONArray(mapper.writeValueAsString(allStudent));
				jo.put("message", "GET success.");
				jo.put("object", joAllStudent);
				jo.put("status", "VALID");
			}
		}
		catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	// FUNCTION: Gets an Student user by ID
	public JSONObject getStudentById(int studentId){
		Student objStudent = null;
		JSONObject jo = new JSONObject();
		JSONObject joStudent = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            
            jo.put("message", "Object not found.");
            objStudent =  (Student) objSession.get(Student.class, studentId);
            objSession.close();
            
            jo.put("message", "Could not convert object to JSON.");
            if(objStudent != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joStudent = new JSONObject(mapper.writeValueAsString(objStudent));
				jo.put("message", "GET success.");
	            jo.put("object", joStudent);
				jo.put("status", "VALID");
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return jo;
	}
	
	// FUNCTION: Gets an Student user by Email
	public JSONObject getStudentByEmail(String studentData){
		Student objStudent = null;
		JSONObject jo = new JSONObject();
		JSONObject joStudent = new JSONObject();
		
		try {
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            
            JSONObject joData = new JSONObject(studentData);
            jo.put("message", "Criteria error.");
            Criteria criteria = objSession.createCriteria(Student.class)
            		.add(Restrictions.eq("email", joData.get("email")));
            @SuppressWarnings("unchecked")
			List<Student> list = criteria.list();
            objStudent = list.get(0);
            objSession.close();
            
            jo.put("message", "Could not convert object to JSON.");
            if(objStudent != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joStudent = new JSONObject(mapper.writeValueAsString(objStudent));
				jo.put("message", "GET success.");
	            jo.put("object", joStudent);
				jo.put("status", "VALID");
			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return jo;
	}
	
	// FUNCTION: Get Students by Parent ID
	public JSONObject getStudentByParent(int p_id) {
		HashSet<Student> students = null;
		JSONArray joStudents = new JSONArray();
		JSONObject jo = new JSONObject();
		Transaction tx = null;
		Session objSession = null;
		
		try{
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized");
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Parent object not found.");
            Parent objParent =  (Parent) objSession.get(Parent.class, p_id);

			students = new HashSet<Student>(objParent.getStudents());
			
			tx.commit();
			objSession.close();
			
			jo.put("message", "Could not convert object to JSON.");
			if(students != null && !students.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joStudents = new JSONArray(mapper.writeValueAsString(students));
				jo.put("message", "GET success.");
				jo.put("object", joStudents);
				jo.put("status", "VALID");
			}
		}
		catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	// FUNCTION: Adds new Student user
	public JSONObject addStudent(String studentData){		
		JSONObject joStudent = null;
		JSONObject jo = new JSONObject();
		Student objStudent = null;
		Transaction tx = null ;

		try{
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
 
			joStudent = new JSONObject(studentData);
		    objStudent = new Student(joStudent);
		    Date date = new Date();
		    objStudent.setRegisteredDate(date);

			SignInHome SH = new SignInHome();
			if (joStudent.getBoolean("generate")) {
				JSONObject pass = SH.altSignUp(joStudent);
				if (pass.getString("status").equals("VALID")) {
					jo.put("auth", pass.getString("auth"));
					jo.put("passw", pass.getString("passw"));
				} else {
					jo.put("message", "JSONObject has insufficient information.");
				}
			} else {
				JSONObject auth = SH.signUp(joStudent);
				if (auth.getString("status").equals("VALID")) {
					jo.put("auth", auth.getString("auth"));
				}
				else {
					jo.put("message", "JSONObject has insufficient information.");
					throw new RuntimeException();
				}
			}
			
			jo.put("message", "Parent object not found.");
			Parent parent = (Parent) objSession.get(Parent.class, joStudent.getInt("p_id"));
			objStudent.setParent(parent);
			
			objSession.save(objStudent);
			joStudent.put("id", objStudent.getId());
			jo.put("message", "Saved.");
			
			tx.commit();
			objSession.close();
			
			jo.put("object", joStudent);
			jo.put("status", "VALID");
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return jo;
	}
	
	// FUNCTION: Updates Student user
	public JSONObject updateStudent(String studentData){
		Transaction tx = null;
		JSONObject jo = new JSONObject();
		JSONObject joStudent = new JSONObject();
		
		try {	
			JSONObject joData = new JSONObject(studentData);
			jo.put("status", "INVALID");
			jo.put("message", "Session Factory uninitialized.");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			Student objStudent = (Student) objSession.get(Student.class, joData.getInt("id"));
			if (joData.has("firstName")) {
				objStudent.setFirstName(joData.getString("firstName"));
			}
			if (joData.has("lastName")) {
				objStudent.setLastName(joData.getString("lastName"));
			}
			if (joData.has("p_id")) {
				Parent newParent = (Parent) objSession.get(Parent.class, joData.getInt("id"));
				objStudent.setParent(newParent);
			}
			if (joData.has("standard")) {
				objStudent.setStandard(joData.getInt("standard"));
			}
			if (joData.has("email")) {
				objStudent.setEmail(joData.getString("email"));
			}
			if (joData.has("phone")) {
				objStudent.setPhone(joData.getString("phone"));
			}
			if (joData.has("institution")) {
				objStudent.setInstitution(joData.getString("institution"));
			}
			if (joData.has("location")) {
				objStudent.setLocation(joData.getString("location"));
			}
			if (joData.has("dob")) {
				objStudent.setDob(formatter.parse(joData.getString("dob")));
			}
			
			objSession.saveOrUpdate(objStudent);
			jo.put("message", "Saved.");
			
			tx.commit();
			objSession.close();
			
			jo.put("message", "Could not convert object to JSON.");
			if(objStudent != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joStudent = new JSONObject(mapper.writeValueAsString(objStudent));
				jo.put("object", joStudent);
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
	
	// FUNCTION: Deletes Student user
	public JSONObject deleteStudent(String studentData) {
		Transaction tx = null;
		Student objStudent = null;
		JSONObject jo = new JSONObject();
		SignInHome SH = new SignInHome();
		
		try {
			jo.put("status", "INVALID");
			JSONObject joData = new JSONObject(studentData);
			
			jo.put("message", "Session Factory uninitialized.");
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			jo.put("message", "Object not found.");
			objStudent = (Student) objSession.get(Student.class, joData.getInt("id"));
			objSession.delete(objStudent);
			
			if (SH.deleteAccount(studentData)) {
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
	
	// FUNCTION: Changes Student Password
	public JSONObject changeStudentPassword(String studentData) {
		SignInHome SH = new SignInHome();
		JSONObject jo = new JSONObject();
		try {
			jo.put("status", "INVALID");
			boolean check = SH.changePassword(studentData);
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
