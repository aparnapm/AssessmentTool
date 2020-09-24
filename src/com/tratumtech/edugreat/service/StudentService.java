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

import com.tratumtech.edugreat.model.StudentHome;

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
	public Response getAllStudent() {
		JSONObject jo = new JSONObject();
		try {
			StudentHome objSH = new StudentHome();
			jo = objSH.getAllStudent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();			
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getStudentById(@PathParam("id") int id){
		JSONObject jo = new JSONObject();		
		try{
			StudentHome objSH = new StudentHome();
			jo = objSH.getStudentById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
	@Path("get/email")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getStudentByEmail(String data){
		JSONObject jo = new JSONObject();		
		try{
			StudentHome objSH = new StudentHome();
			jo = objSH.getStudentByEmail(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	@Path("pid/{p_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetStudentByParent(@PathParam("p_id") int p_id) {
		JSONObject jo = new JSONObject();
		try {
			StudentHome objSH = new StudentHome();
			jo = objSH.getStudentByParent(p_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();	
	}
	
	@POST
	@Path("add")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response addStudent(String data){
		JSONObject jo = new JSONObject();
		try {
			StudentHome SH = new StudentHome();
			jo = SH.addStudent(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	@PUT
	@Path("update")	
	public Response updateStudent(String data){
		JSONObject jo = new JSONObject();		
		try{
			StudentHome objSH = new StudentHome();
			jo = objSH.updateStudent(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

	@DELETE
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteStudent(String data) {
		JSONObject jo = null;
		try {
			StudentHome objSH = new StudentHome();
			jo = objSH.deleteStudent(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	@POST
	@Path("change/password")
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeStudentPassword(String data) {
		JSONObject jo = null;
		try {
			StudentHome objSH = new StudentHome();
			jo = objSH.changeStudentPassword(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

}
