package com.tratumtech.edugreat.service;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.tratumtech.edugreat.tool.Student;
import com.tratumtech.edugreat.tool.StudentHome;

@Path("student/")
public class StudentService {
	
	@Context
	private HttpServletRequest request;
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray getAllStudent() {
		JSONArray joAllStudent = new JSONArray();
		HashSet<Student> emptySet = null;
		
		try {
			StudentHome objSH = new StudentHome();
			emptySet = objSH.getAllStudent();
			
			if(emptySet != null && !emptySet.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAllStudent = new JSONArray(mapper.writeValueAsString(emptySet));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return joAllStudent;		
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public JSONObject GetStudentById(@PathParam("id") int id){
		Student objStudent = null;
		JSONObject jo = new JSONObject();		
		try{
			StudentHome objSH = new StudentHome();
			objStudent = objSH.GetStudentById(id);
				
			if(objStudent != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				jo = new JSONObject(mapper.writeValueAsString(objStudent));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}
	
	@GET
	@Path("pid/{p_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray GetStudentByParent(@PathParam("p_id") int p_id) {
		JSONArray joStudentsOfParent = new JSONArray();
		HashSet<Student> emptySet = null;
		
		try {
			StudentHome objSH = new StudentHome();
			emptySet = objSH.getStudentByParent(p_id);
			
			if(emptySet != null && !emptySet.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joStudentsOfParent = new JSONArray(mapper.writeValueAsString(emptySet));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return joStudentsOfParent;		
	}
	
	@POST
	@Path("add")
	@Produces(MediaType.APPLICATION_JSON)	
	public JSONObject addStudent(String data){
		
		JSONObject jo = new JSONObject();
		
		try {
			
			StudentHome SH = new StudentHome();
			jo = SH.addStudent(data);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}
	
	@PUT
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)	
	public JSONObject updateStudent(String data){
		JSONObject jo = null;
		try{
			StudentHome objSH = new StudentHome();
			jo = objSH.updateStudent(data);
			return jo;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}

	@DELETE
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject deleteStudent(String data) {
		JSONObject jo = null;
		
		try {
			StudentHome objSH = new StudentHome();
			jo = objSH.deleteStudent(data);

			return jo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo;
	}

}