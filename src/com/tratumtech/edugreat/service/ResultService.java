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

import com.tratumtech.edugreat.model.ResultHome;

@Path("result/")
public class ResultService {
	
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
	public Response getAllResult() {
		JSONObject jo = new JSONObject();
		try {
			ResultHome objRH = new ResultHome();
			jo = objRH.getAllResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getResultById(@PathParam("id") int id) {
		JSONObject jo = new JSONObject();		
		try{
			ResultHome objRH = new ResultHome();
			jo = objRH.getResultById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	@Path("enrollment/{id}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response getResultByEnrollment(@PathParam("id") int id){
		JSONObject jo = new JSONObject();		
		try{
			ResultHome objRH = new ResultHome();
			jo = objRH.getResultsByEnrollment(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
	@Path("add")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response addResult(String data){
		JSONObject jo = new JSONObject();		
		try{
			ResultHome objRH = new ResultHome();
			jo = objRH.addResult(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	@PUT
	@Path("update")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response updateResult(String data){
		JSONObject jo = new JSONObject();		
		try{
			ResultHome objRH = new ResultHome();
			jo = objRH.updateResult(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	@DELETE
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteResult(String data) {
		JSONObject jo = new JSONObject();		
		try{
			ResultHome objRH = new ResultHome();
			jo = objRH.deleteResult(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

}
