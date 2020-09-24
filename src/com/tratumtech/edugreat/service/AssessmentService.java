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

import com.tratumtech.edugreat.model.AssessmentHome;

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
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAssessment() {
		JSONObject jo = new JSONObject();
		try {
			AssessmentHome objAH = new AssessmentHome();
			jo = objAH.getAllAssessment();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();		
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getAssessmentById(@PathParam("id") int id){
		JSONObject jo = new JSONObject();		
		try{
			AssessmentHome objAH = new AssessmentHome();
			jo = objAH.getAssessmentById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();	
	}

	@POST
	@Path("add")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response addAssessment(String data){
		JSONObject jo = new JSONObject();
		try {
			AssessmentHome AH = new AssessmentHome();
			jo = AH.addAssessment(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

	@PUT
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response updateAssessment(String data){
		JSONObject jo = null;
		try{
			AssessmentHome objAH = new AssessmentHome();
			jo = objAH.updateAssessment(data);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

	@DELETE
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAssessment(String data) {
		JSONObject jo = null;
		try {
			AssessmentHome objAH = new AssessmentHome();
			jo = objAH.deleteAssessment(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	@GET
	@Path("questions/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllQuestionsById(@PathParam("id") int id) {
		JSONObject jo = new JSONObject();
		try {
			AssessmentHome objAH = new AssessmentHome();
			jo = objAH.getAllQuestionsById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();	
	}
	
	@DELETE
	@Path("delete/question")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteQuestionFromAssessment(String data) {
		JSONObject jo = null;
		try {
			AssessmentHome objAH = new AssessmentHome();
			jo = objAH.deleteQuestionFromAssessment(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	@PUT
	@Path("add/question")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addQuestion(String data) {
		JSONObject jo = null;
		try {
			AssessmentHome objAH = new AssessmentHome();
			jo = objAH.addQuestionToAssessment(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

}
