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

	import com.tratumtech.edugreat.tool.Questions;
	import com.tratumtech.edugreat.tool.QuestionsHome;

	@Path("question/")
	public class QuestionsService {
		
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
		public JSONObject testQuestions(String data) {
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
		public Response getAllQuestions() {
			JSONArray joAllQuestions = new JSONArray();
			HashSet<Questions> emptySet = null;
			
			try {
				QuestionsHome objPH = new QuestionsHome();
				emptySet = objPH.getAllQuestions();
				
				if(emptySet != null && !emptySet.isEmpty()){
					ObjectMapper mapper = new ObjectMapper();	
					mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
					joAllQuestions = new JSONArray(mapper.writeValueAsString(emptySet));
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
			
			return Response.ok(joAllQuestions).header("Access-Control-Allow-Origin","*").build();	
		}
		
		@GET
		@Path("{id}")
		@Produces(MediaType.APPLICATION_JSON)	
		public Response GetQuestionsById(@PathParam("id") int id){
			Questions objQuestions = null;
			JSONObject jo = new JSONObject();		
			try{
				QuestionsHome objPH = new QuestionsHome();
				objQuestions = objPH.GetQuestionsById(id);
					
				if(objQuestions != null){
					ObjectMapper mapper = new ObjectMapper();	
					mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
					jo = new JSONObject(mapper.writeValueAsString(objQuestions));
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
		public JSONObject addQuestions(String data){
			
			JSONObject jo = new JSONObject();
			
			try {
				
				QuestionsHome PH = new QuestionsHome();
				jo = PH.addQuestions(data);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jo;
		}

		@PUT
		@Path("update")
		@Produces(MediaType.APPLICATION_JSON)	
		public JSONObject updateQuestions(String data){
			JSONObject jo = null;
			try{
				QuestionsHome objPH = new QuestionsHome();
				jo = objPH.updateQuestions(data);
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
		public JSONObject deleteQuestions(String data) {
			JSONObject jo = null;
			
			try {
				QuestionsHome objPH = new QuestionsHome();
				jo = objPH.deleteQuestions(data);

				return jo;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jo;
		}

	}
