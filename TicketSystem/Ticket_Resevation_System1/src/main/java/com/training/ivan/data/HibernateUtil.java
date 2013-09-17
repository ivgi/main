package com.training.ivan.data;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.training.ivan.Ticket;
import com.training.ivan.TicketSystemConfig;

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	protected static SessionFactory configureSessionFactory() {
		
		Configuration configuration = new Configuration();
		configuration.configure();
		serviceRegistry = new ServiceRegistryBuilder().applySettings(
				configuration.getProperties()).buildServiceRegistry();
		 sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		 return sessionFactory;
	}
	
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
	protected static void initDB(){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		for(int i = 0; i < TicketSystemConfig.NUMBER_OF_TICKETS; i++)
		session.save(new Ticket(i,null));
		session.getTransaction().commit();
		session.close();
	}
	
	public static void clear(){
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("delete from t_tickets");
		query.executeUpdate();
		query = session.createQuery("delete from t_user");
		query.executeUpdate();
		session.close();
	}

}
