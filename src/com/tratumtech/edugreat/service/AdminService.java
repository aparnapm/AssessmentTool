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

import com.tratumtech.edugreat.tool.Admin;
import com.tratumtech.edugreat.tool.AdminHome;

@Path("admin/")
public class AdminService {

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
	public Response getAllAdmin() {
		JSONArray joAllAdmin = new JSONArray();
		HashSet<Admin> emptySet = null;

		try {
			AdminHome objAH = new AdminHome();
			emptySet = objAH.getAllAdmin();

			if(emptySet != null && !emptySet.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				joAllAdmin = new JSONArray(mapper.writeValueAsString(emptySet));
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

		return Response.ok(joAllAdmin).header("Access-Control-Allow-Origin","*").build();		
	}


	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response GetAdminById(@PathParam("id") int id){
		Admin objAdmin = null;
		JSONObject jo = new JSONObject();		
		try{
			AdminHome objAH = new AdminHome();
			objAdmin = objAH.GetAdminById(id);

			if(objAdmin != null){
				ObjectMapper mapper = new ObjectMapper();	
				mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
				jo = new JSONObject(mapper.writeValueAsString(objAdmin));
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
	public JSONObject addAdmin(String data){

		JSONObject jo = null;
		int adminId = 0;
		try {

			AdminHome AH = new AdminHome();
			adminId = AH.addAdmin(data);
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
	public JSONObject updateAdmin(String data){
		JSONObject jo = null;
		try{
			AdminHome objAH = new AdminHome();
			jo = objAH.updateAdmin(data);
		}catch (Exception e)
		{e.printStackTrace();
		}return jo;
	}

	@DELETE
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)	
	public JSONObject deleteAdmin(String data){
		JSONObject jo = null;
		try{
			AdminHome objAH = new AdminHome();
			jo = objAH.deleteAdmin(data);
		}catch (Exception e)
		{e.printStackTrace();
		}return jo;
	}
}
