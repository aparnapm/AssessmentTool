package com.tratumtech.edugreat.service;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.tratumtech.edugreat.model.SignInHome;

@Path("authorization/")
public class SignInService {
	
	@Context
	private HttpServletRequest request;
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	@GET
	@Path("initialize")
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialize() {
		JSONObject jo = new JSONObject();
		try {
			SignInHome SH = new SignInHome();
			jo = SH.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	@Path("getemails")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmails() {
		JSONObject jo = new JSONObject();
		try {
			SignInHome SH = new SignInHome();
			jo = SH.getEmails();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*").build();
	}
	
	@POST
	@Path("log_in")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logInUser(String data) {
		JSONObject jo = new JSONObject();
		try {
			SignInHome SH = new SignInHome();
			jo = SH.login(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	@POST
	@Path("log_out")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logoutUser(String data)  {
		JSONObject jo = new JSONObject();
		try {
			SignInHome SH = new SignInHome();
			jo = SH.logout(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	// returns valid if auth exists in DB else returns invalid
	@POST
	@Path("check_token")
	@Produces (MediaType.APPLICATION_JSON)
	public Response checkToken(String data) {
		JSONObject jo = new JSONObject();
		try {
			SignInHome SH = new SignInHome();
			jo = SH.checkToken(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
		
	}
	
	@POST
	@Path("send")
	@Produces (MediaType.APPLICATION_JSON)
	public Response send(String data) {
		JSONObject jo = new JSONObject();
		JSONObject emailInfo = null;
		
		try {
			jo.put("status", "INVALID");
			final String username = "testedugreat@gmail.com";
			final String password = "tester123#";
	
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
	
			Session session = Session.getInstance(props,
			  new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			  });
			
			emailInfo = new JSONObject(data);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("testededugreat@gmail.com"));
			// TODO: When deployed must be changed to actual user email.
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("bhadra.chembukave@gmail.com"));
			message.setSubject(emailInfo.getString("subject"));
			message.setText(emailInfo.getString("message"));

			Transport.send(message);

			jo.put("status", "VALID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

	// force change password
	@POST
	@Path("force_change/pass")
	@Produces (MediaType.APPLICATION_JSON)
	public Response forceChangePassword(String data) {
		JSONObject jo = new JSONObject();
		try {
			SignInHome SH = new SignInHome();
			jo = SH.forceChangePassword(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	// confirm email
	@GET
	@Path("confirm/email={emailID}/auth={authID}")
	@Produces (MediaType.APPLICATION_JSON)
	public Response deactivateAccount(@PathParam("emailID") String emailID, @PathParam("authID") String authID) {
		JSONObject jo = new JSONObject();
		try {
			SignInHome SH = new SignInHome();
			jo = SH.confirmSignIn(emailID, authID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}
	
	// deactivate account
	@POST
	@Path("deactivate")
	@Produces (MediaType.APPLICATION_JSON)
	public Response deactivateAccount(String data) {
		JSONObject jo = new JSONObject();
		try {
			SignInHome SH = new SignInHome();
			jo = SH.deactivateAccount(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(jo).header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS").build();
	}

}
