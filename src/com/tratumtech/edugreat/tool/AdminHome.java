package com.tratumtech.edugreat.tool;

import java.util.HashSet;
import java.util.List;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.tratumtech.edugreat.tool.Admin;
import com.tratumtech.edugreat.tool.HibernateUtil;

public class AdminHome {
	
	public HashSet<Admin> getAllAdmin() {

		Transaction tx = null;
		Session objSession = null;
		try{
			
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			Query query = objSession.createQuery("from Admin"); 

		
			@SuppressWarnings("unchecked")
			List<Admin> empList = query.list(); 
			HashSet<Admin> adminList = new HashSet<Admin>(empList);
	
			tx.commit();
			
			return adminList;
		}
		catch (RuntimeException re) {
			objSession.close();
			tx.rollback();
			throw re;
		}
	}
	
	public Admin GetAdminById(int adminId){
		Admin objAdmin = null;
		
		try {
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            objAdmin =  (Admin) objSession.get(Admin.class, adminId);
            objSession.close();
            return objAdmin;
        } catch (Exception e) {
        	throw e;
        }
	}
	
	public int addAdmin(String adminData){		
		JSONObject joAdmin = null;
		Transaction tx = null ;
		int adminId = 0;
		
		try
		{
			joAdmin = new JSONObject(adminData);
			Admin objAdmin = new Admin(joAdmin);			
						
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			objSession.save(objAdmin);
			
			tx.commit();
			objSession.close();
			adminId = objAdmin.getId();
			
			return adminId ;
		}
		catch (RuntimeException | JSONException re) {
			tx.rollback();
		}
		return adminId ;
	}
	
	
	public JSONObject updateAdmin(String adminData){
		Transaction tx = null ;
		JSONObject joAdmin = null;
		
		int adminId = 0;
		JSONObject joResult = new JSONObject();
		try
		{
			joResult.put("message", "ERROR");
			joResult.put("code", Response.Status.NOT_FOUND.getStatusCode());
			
			joAdmin = new JSONObject(adminData);
			Admin objAdmin = new Admin(joAdmin);
				
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			objSession.saveOrUpdate(objAdmin);
			
			tx.commit();
			objSession.close();
			adminId = objAdmin.getId();
				
			joResult.put("message", "SUCCESSFULLY UPDATED");
			joResult.put("adminId", adminId);
			joResult.put("code", Response.Status.OK.getStatusCode());
			return joResult;
		}
		catch (RuntimeException | JSONException re) {
			tx.rollback();
		}
		return joResult ;
	}
	
	public JSONObject deleteAdmin(String adminData){
		Transaction tx = null ;
		JSONObject joAdmin = null;
		
		int adminId = 0;
		JSONObject joResult = new JSONObject();

		
		try
		{
			joResult.put("message", "ERROR");
			joAdmin = new JSONObject(adminData);
		    Admin objAdmin = new Admin(joAdmin);
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			adminId = objAdmin.getId();
			objSession.delete(objAdmin);
				
			joResult.put("message", "SUCCESSFULLY DELETED");
			joResult.put("adminId", adminId);
			tx.commit();
			objSession.close();
			return joResult;
		}
		catch (RuntimeException | JSONException re) {
			
			tx.rollback();
		}
		
		return joResult ;
	}

}
