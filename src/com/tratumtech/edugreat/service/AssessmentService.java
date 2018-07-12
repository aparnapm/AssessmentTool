package com.tratumtech.edugreat.service;

	import java.io.IOException;
	import java.util.HashSet;
import java.util.Set;

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

	import com.tratumtech.edugreat.tool.Assessment;
	import com.tratumtech.edugreat.tool.AssessmentHome;
	import com.tratumtech.edugreat.tool.Questions;

	@Path("assessment/")
	public class AssessmentService {
		
		@Context
		private HttpServletRequest request;
		
		public HttpServletRequest getRequest() {
			return request;
		}

		public void setRequest(HttpServletRequest request) {
			this.request = request;
		}
		
		@GET
		@Path("test")
		@Produces(MediaType.APPLICATION_JSON)
		public JSONObject testAssessment(String data) {
			JSONObject item = new JSONObject();
			try {
				item = new JSONObject(data);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return item;
		}
		
		@GET
		@Path("all")
		@Produces(MediaType.APPLICATION_JSON)
		public Response getAllAssessment() {
			JSONArray joAllAssessment = new JSONArray();
			HashSet<Assessment> emptySet = null;
			
			try {
				AssessmentHome objPH = new AssessmentHome();
				emptySet = objPH.getAllAssessment();
				
				if(emptySet != null && !emptySet.isEmpty()){
					ObjectMapper mapper = new ObjectMapper();	
					mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
					joAllAssessment = new JSONArray(mapper.writeValueAsString(emptySet));
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
			
			return Response.ok(joAllAssessment).header("Access-Control-Allow-Origin","*").build();	
		}
		
		@GET
		@Path("{id}")
		@Produces(MediaType.APPLICATION_JSON)	
		public Response GetAssessmentById(@PathParam("id") int id){
			Assessment objAssessment = null;
			JSONObject jo = new JSONObject();		
			try{
				AssessmentHome objPH = new AssessmentHome();
				objAssessment = objPH.GetAssessmentById(id);
					
				if(objAssessment != null){
					ObjectMapper mapper = new ObjectMapper();	
					mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
					jo = new JSONObject(mapper.writeValueAsString(objAssessment));
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
			return Response.ok(jo).header("Access-Control-Allow-Origin","*").build();	
		}

		@POST
		@Path("add")
		@Produces(MediaType.APPLICATION_JSON)	
		public Response addAssessment(String data){
			
			JSONObject jo = new JSONObject();
			
			try {
				
				AssessmentHome PH = new AssessmentHome();
				jo = PH.addAssessment(data);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Response.ok(jo).header("Access-Control-Allow-Origin","*").build();	
		}

		@PUT
		@Path("update")
		@Produces(MediaType.APPLICATION_JSON)	
		public Response updateAssessment(String data){
			JSONObject jo = null;
			try{
				AssessmentHome objPH = new AssessmentHome();
				jo = objPH.updateAssessment(data);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Response.ok(jo).header("Access-Control-Allow-Origin","*").build();	
		}

		@DELETE
		@Path("delete")
		@Produces(MediaType.APPLICATION_JSON)
		public JSONObject deleteAssessment(String data) {
			JSONObject jo = null;
			
			try {
				AssessmentHome objPH = new AssessmentHome();
				jo = objPH.deleteAssessment(data);

				return jo;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jo;
		}
		
		
		@POST
		@Path("populate/{id}")
		@Produces(MediaType.APPLICATION_JSON)
		public JSONObject populateAssessment(String data, @PathParam("id")int id) {
		JSONObject jo=null;
		try
		{
			AssessmentHome obj= new AssessmentHome();
			jo=obj.populateAssessment(data,id);
			return jo;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return jo;
		}
		
		
		@GET
		@Path("{id}/questions")
		@Produces(MediaType.APPLICATION_JSON)
	
		public JSONArray allQuestions(@PathParam("id") int id) {
			JSONArray jo = new JSONArray();
			Set<Questions> emptySet = null;
			
			try {
				AssessmentHome objPH = new AssessmentHome();
				emptySet = objPH.allQuestions(id);
				
				if(emptySet != null && !emptySet.isEmpty()){
					ObjectMapper mapper = new ObjectMapper();	
					mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
					jo= new JSONArray(mapper.writeValueAsString(emptySet));
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
		
		@DELETE
		@Path("delete/question")
		@Produces(MediaType.APPLICATION_JSON)
		public JSONObject deleteQuestionFromAssessment(String data) {
		JSONObject jo=null;
		try
		{
			AssessmentHome obj= new AssessmentHome();
			jo=obj.deleteQuestionFromAssessment(data);
			return jo;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return jo;
		}
		
		
		@PUT
		@Path("add/question")
		@Produces(MediaType.APPLICATION_JSON)
		public JSONObject addQuestionToAssessment(String data) {
		JSONObject jo=null;
		try
		{
			AssessmentHome obj= new AssessmentHome();
			jo=obj.addQuestionToAssessment(data);
			return jo;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return jo;
		}
	}
