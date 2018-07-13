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

	import com.tratumtech.edugreat.tool.Parent;
	import com.tratumtech.edugreat.tool.ParentHome;

	@Path("parent/")
	public class ParentService {
		
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
		public Response getAllParent() {
			JSONArray joAllParent = new JSONArray();
			HashSet<Parent> emptySet = null;
			
			try {
				ParentHome objPH = new ParentHome();
				emptySet = objPH.getAllParent();
				
				if(emptySet != null && !emptySet.isEmpty()){
					ObjectMapper mapper = new ObjectMapper();	
					mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
					joAllParent = new JSONArray(mapper.writeValueAsString(emptySet));
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
			
			return Response.ok(joAllParent).header("Access-Control-Allow-Origin","*").build();		
		}
		
		@GET
		@Path("{id}")
		@Produces(MediaType.APPLICATION_JSON)	
		public Response GetParentById(@PathParam("id") int id){
			Parent objParent = null;
			JSONObject jo = new JSONObject();		
			try{
				ParentHome objPH = new ParentHome();
				objParent = objPH.GetParentById(id);
					
				if(objParent != null){
					ObjectMapper mapper = new ObjectMapper();	
					mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
					jo = new JSONObject(mapper.writeValueAsString(objParent));
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
		public Response addParent(String data){
			
			JSONObject jo = new JSONObject();
			
			try {
				
				ParentHome PH = new ParentHome();
				jo = PH.addParent(data);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Response.ok(jo).header("Access-Control-Allow-Origin","*").build();	
		}

		@PUT
		@Path("update")
		@Produces(MediaType.APPLICATION_JSON)	
		public JSONObject updateParent(String data){
			JSONObject jo = null;
			try{
				ParentHome objPH = new ParentHome();
				jo = objPH.updateParent(data);
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
		public JSONObject deleteParent(String data) {
			JSONObject jo = null;
			
			try {
				ParentHome objPH = new ParentHome();
				jo = objPH.deleteParent(data);

				return jo;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jo;
		}

	}
