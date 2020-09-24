package com.tratumtech.edugreat.service;

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

import org.codehaus.jettison.json.JSONObject;

import com.tratumtech.edugreat.model.AdminHome;

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
	
	// gets a list of all Admin users
	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAdmin() {
		JSONObject jo = new JSONObject();
		try {
			AdminHome objAH = new AdminHome();
			jo = objAH.getAllAdmin();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();		
	}

	// gets Admin user using path parameter "id"
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response GetAdminById(@PathParam("id") int id){
		JSONObject jo = new JSONObject();		
		try{
			AdminHome objAH = new AdminHome();
			jo = objAH.getAdminById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	// Gets Admin user by "email" param in POST data
	@POST
	@Path("get/email")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response GetAdminByEmail(String data){
		JSONObject jo = new JSONObject();		
		try{
			AdminHome objAH = new AdminHome();
			jo = objAH.getAdminByEmail(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}

	// adds Admin user to database (calls Sign Up function in SignIn.java)
	@POST
	@Path("add")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response addAdmin(String data){
		JSONObject jo = new JSONObject();
		try {
			AdminHome AH = new AdminHome();
			jo = AH.addAdmin(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	// Updates Admin data by passing "id" data and updated fields
	@PUT
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response updateAdmin(String data){
		JSONObject jo = new JSONObject();
		try{
			AdminHome objAH = new AdminHome();
			jo = objAH.updateAdmin(data);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	// Deletes Admin user by passing id in POST param. Calls Delete function in SignIn.java
	@DELETE
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAdmin(String data) {
		JSONObject jo = null;
		
		try {
			AdminHome objAH = new AdminHome();
			jo = objAH.deleteAdmin(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	@POST
	@Path("change/password")
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeAdminPassword(String data) {
		JSONObject jo = null;
		try {
			AdminHome objAH = new AdminHome();
			jo = objAH.changeAdminPassword(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
}
