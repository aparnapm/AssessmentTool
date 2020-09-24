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

import com.tratumtech.edugreat.model.EnrollmentHome;

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

	@GET
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEnrollment() {
		JSONObject jo = new JSONObject();
		try {
			EnrollmentHome objEH = new EnrollmentHome();
			jo = objEH.getAllEnrollment();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();		
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getEnrollmentById(@PathParam("id") int id){
		JSONObject jo = new JSONObject();		
		try{
			EnrollmentHome objEH = new EnrollmentHome();
			jo = objEH.getEnrollmentById(id);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	@Path("student/{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getEnrollmentByStudent(@PathParam("id") int id){
		JSONObject jo = new JSONObject();
		try {
			EnrollmentHome objEH = new EnrollmentHome();
			jo = objEH.getEnrollmentByStudent(id);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}

	@GET
	@Path("assessment/{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getEnrollmentByAssessment(@PathParam("id") int id){
		JSONObject jo = new JSONObject();
		
		try {
			EnrollmentHome objEH = new EnrollmentHome();
			jo = objEH.getEnrollmentByAssessment(id);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	@Path("student/available/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAvailableEnrollmentOfStudent(@PathParam("id") int id) {
		JSONObject jo = new JSONObject();
		try {
			EnrollmentHome objEH = new EnrollmentHome();
			jo = objEH.getAvailableEnrollmentOfStudent(id);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	@Path("student/unavailable/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInavailableEnrollmentOfStudent(@PathParam("id") int id) {
		JSONObject jo = new JSONObject();
		try {
			EnrollmentHome objEH = new EnrollmentHome();
			jo = objEH.getUnavailableEnrollmentOfStudent(id);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
	@Path("add")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response addEnrollment(String data){
		JSONObject jo = new JSONObject();
		try {
			EnrollmentHome EH = new EnrollmentHome();
			jo = EH.addEnrollment(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	@PUT
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response updateEnrollment(String data){
		JSONObject jo = new JSONObject();
		try{
			EnrollmentHome objEH = new EnrollmentHome();
			jo = objEH.updateEnrollment(data);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	@DELETE
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteEnrollment(String data) {
		JSONObject jo = new JSONObject();
		try {
			EnrollmentHome objEH = new EnrollmentHome();
			jo = objEH.deleteEnrollment(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

}
