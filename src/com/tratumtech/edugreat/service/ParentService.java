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

import com.tratumtech.edugreat.model.ParentHome;

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
		JSONObject jo = new JSONObject();
		try {
			ParentHome objPH = new ParentHome();
			jo = objPH.getAllParent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();		
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getParentById(@PathParam("id") int id){
		JSONObject jo = new JSONObject();		
		try{
			ParentHome objPH = new ParentHome();
			jo = objPH.getParentById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
	@Path("get/email")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getParentByEmail(String data){
		JSONObject jo = new JSONObject();		
		try{
			ParentHome objPH = new ParentHome();
			jo = objPH.getParentByEmail(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	@Path("{id}/student/enrollments")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEnrollmentsOfStudents(@PathParam("id") int id) {
		JSONObject jo = new JSONObject();
		try {
			ParentHome PH = new ParentHome();
			jo = PH.getEnrollmentsOfStudents(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
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
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

	@PUT
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response updateParent(String data){
		JSONObject jo = new JSONObject();	
		try{
			ParentHome objPH = new ParentHome();
			jo = objPH.updateParent(data);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

	@DELETE
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteParent(String data) {
		JSONObject jo = null;
		try {
			ParentHome objPH = new ParentHome();
			jo = objPH.deleteParent(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	@POST
	@Path("change/password")
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeParentPassword(String data) {
		JSONObject jo = null;
		try {
			ParentHome objPH = new ParentHome();
			jo = objPH.changeParentPassword(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

}
