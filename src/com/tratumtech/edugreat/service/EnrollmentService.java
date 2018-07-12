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
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.tratumtech.edugreat.tool.Enrollment;
import com.tratumtech.edugreat.tool.EnrollmentHome;


@Path("enrollment/")
public class EnrollmentService {

	@Context
	private HttpServletRequest request;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	// working

	@GET
	@Path("test")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject testEnrollment() {
		JSONObject item = new JSONObject();
		try {
			item.put("information", "test");
			item.put("id", 2);
			item.put("name", "ce1");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return item;
	}

	// working

	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEnrollment() {
		JSONArray joAllEnrollment = new JSONArray();
		HashSet<Enrollment> emptySet = null;

		try {
			EnrollmentHome objAH = new EnrollmentHome();
			emptySet = objAH.getAllEnrollment();

			if(emptySet != null && !emptySet.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAllEnrollment = new JSONArray(mapper.writeValueAsString(emptySet));
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
		return Response.ok(joAllEnrollment).header("Access-Control-Allow-Origin","*").build();		
	}

	// working

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public JSONObject GetEnrollmentById(@PathParam("id") int id){
		Enrollment objEnrollment = null;
		JSONObject jo = new JSONObject();		
		try{
			EnrollmentHome objAH = new EnrollmentHome();
			objEnrollment = objAH.GetEnrollmentById(id);

			if(objEnrollment != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				jo = new JSONObject(mapper.writeValueAsString(objEnrollment));
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

	@POST
	@Path("add")
	@Produces(MediaType.APPLICATION_JSON)	
	public JSONObject addEnrollment(String data){

		JSONObject jo = null;
		int adminId = 0;
		try {

			EnrollmentHome AH = new EnrollmentHome();
			adminId = AH.addEnrollment(data);
			jo = new JSONObject();
			jo.put("id", adminId);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}




	@PUT
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)	
	public JSONObject updateEnrollment(String data){
		JSONObject jo = null;
		try{
			EnrollmentHome objAH = new EnrollmentHome();
			jo = objAH.updateEnrollment(data);
		}catch (Exception e)
		{e.printStackTrace();
		}return jo;
	}

	@DELETE
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)	
	public JSONObject deleteEnrollment(String data){
		JSONObject jo = null;
		try{
			EnrollmentHome objAH = new EnrollmentHome();
			jo = objAH.deleteEnrollment(data);
		}catch (Exception e)
		{e.printStackTrace();
		}return jo;
	}
@GET
@Path("assessment_id/{assessment_id}")
@Produces(MediaType.APPLICATION_JSON)
public JSONArray GetEnrollmentByAssessment(@PathParam("assessment_id") int a_id) {
	JSONArray joEnrollmentOfAssessment = new JSONArray();
	HashSet<Enrollment> emptySet = null;
	
	try {
		EnrollmentHome objSH = new EnrollmentHome();
		emptySet = objSH.getEnrollmentByAssessment(a_id);
		
		if(emptySet != null && !emptySet.isEmpty()){
			ObjectMapper mapper = new ObjectMapper();	
			mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
			joEnrollmentOfAssessment = new JSONArray(mapper.writeValueAsString(emptySet));
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
	
	return joEnrollmentOfAssessment;		
}

@GET
@Path("student_id/{student_id}")
@Produces(MediaType.APPLICATION_JSON)
public JSONArray GetEnrollmentByStudent(@PathParam("student_id") int s_id) {
	JSONArray joEnrollmentOfStudent = new JSONArray();
	HashSet<Enrollment> emptySet = null;
	
	try {
		EnrollmentHome objSH = new EnrollmentHome();
		emptySet = objSH.getEnrollmentByStudent(s_id);
		
		if(emptySet != null && !emptySet.isEmpty()){
			ObjectMapper mapper = new ObjectMapper();	
			mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
			joEnrollmentOfStudent = new JSONArray(mapper.writeValueAsString(emptySet));
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
	
	return joEnrollmentOfStudent;
}

@GET
@Path("{a_id}/{s_id}")
@Produces(MediaType.APPLICATION_JSON)
public JSONObject GetSpecificEnrollment(@PathParam("a_id") int a_id ,@PathParam("s_id") int s_id) {
JSONObject jo=null;
Enrollment obj= null;
	try {
		EnrollmentHome objSH = new EnrollmentHome();
		obj = objSH.getSpecificEnrollment(a_id,s_id);
		
		if(obj!= null){
			ObjectMapper mapper = new ObjectMapper();	
			mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
			jo = new JSONObject(mapper.writeValueAsString(obj));
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
}