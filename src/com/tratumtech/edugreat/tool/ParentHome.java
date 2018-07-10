package com.tratumtech.edugreat.tool;

import java.util.HashSet;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ParentHome {
	
	public HashSet<Parent> getAllParent() {

		Transaction tx = null;
		Session objSession = null;
		try{
			
			objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			Query query = objSession.createQuery("from Parent"); 

			@SuppressWarnings("unchecked")
			List<Parent> empList = query.list(); 
			HashSet<Parent> parentList = new HashSet<Parent>(empList);
			
			tx.commit();
			objSession.close();
			
			return parentList;
		}
		catch (RuntimeException re) {
			objSession.close();
			tx.rollback();
			throw re;
		}
	}
	
	public Parent GetParentById(int parentId){
		Parent objParent = null;
		
		try {
            Session objSession = HibernateUtil.getSessionFactory().openSession();
            objParent =  (Parent) objSession.get(Parent.class, parentId);
            objSession.close();
            return objParent;
        } catch (Exception e) {
        	throw e;
        }
		
	}
	
	public JSONObject addParent(String parentData){		
		JSONObject joParent = null;
		Transaction tx = null ;
		int parentId = -1;
		JSONObject result = new JSONObject();

		try
		{
			joParent = new JSONObject(parentData);
			Parent objParent = new Parent(joParent);
			result.put("id", "-1");
			result.put("message", "Error. Insert failed.");
						
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			objSession.save(objParent);
			
			parentId=objParent.getId();
			result.put("id", parentId);
			
			result.put("message", "Inserted.");
			tx.commit();
			objSession.close();
		
			return result;
		}
		catch (RuntimeException | JSONException re) {
			tx.rollback();
		}
		return result;
	}
	
	
	public JSONObject updateParent(String empData){
		Transaction tx = null ;
		JSONObject joParent = null;

		JSONObject joResult = new JSONObject();
		try
		{
			joResult.put("id", -1);
			joResult.put("message", "Error. Update failed.");
			
			joParent = new JSONObject(empData);
			Parent objParent = new Parent(joParent);
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			
			objSession.saveOrUpdate(objParent);
			joResult.put("id", 0);
			joResult.put("message", "Updated.");
			
			tx.commit();
		
			joResult.put("id", joParent.get("id"));
			objSession.close();
			return joResult;
		}
		catch (RuntimeException | JSONException re) {
			tx.rollback();
		}
		return joResult ;
	}
	
	public JSONObject deleteParent(String empData) {
		Transaction tx = null;
		JSONObject result = new JSONObject();
		
		try {
			result.put("id", -1);
			result.put("message", "Error. Delete failed.");
			JSONObject joParent = new JSONObject(empData);
			Parent objParent = new Parent(joParent);
			
			Session objSession = HibernateUtil.getSessionFactory().openSession();
			tx = objSession.beginTransaction();
			objSession.delete(objParent);
			result.put("id", "0");
			result.put("message", "Deleted.");
			
			tx.commit();
			objSession.close();
			
			result.put("id", joParent.get("id"));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}