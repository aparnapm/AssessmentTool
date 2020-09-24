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

import com.tratumtech.edugreat.model.QuestionsHome;

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
	@Path("all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllQuestions() {
		JSONObject jo = new JSONObject();
		try {
			QuestionsHome objQH = new QuestionsHome();
			jo = objQH.getAllQuestions();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();		
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getQuestionsById(@PathParam("id") int id){
		JSONObject jo = new JSONObject();		
		try{
			QuestionsHome objQH = new QuestionsHome();
			jo = objQH.getQuestionsById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}

	@POST
	@Path("add")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response addQuestions(String data){
		JSONObject jo = new JSONObject();
		try {
			QuestionsHome QH = new QuestionsHome();
			jo = QH.addQuestions(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

	@PUT
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response updateQuestions(String data){
		JSONObject jo = null;
		try{
			QuestionsHome objQH = new QuestionsHome();
			jo = objQH.updateQuestions(data);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

	@DELETE
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteQuestions(String data) {
		JSONObject jo = null;
		try {
			QuestionsHome objQH = new QuestionsHome();
			jo = objQH.deleteQuestions(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
}
