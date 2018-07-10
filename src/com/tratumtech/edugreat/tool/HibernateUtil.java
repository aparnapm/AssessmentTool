package com.tratumtech.edugreat.tool;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory = null;
	
	
	public static SessionFactory getSessionFactory(){
	try{
			if(sessionFactory == null)
				sessionFactory = new Configuration().configure().buildSessionFactory();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return sessionFactory;
	}
}
