package com.training.ivan.data;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @deprecated
 * @author ivaniv
 *
 */
public class HibernateListener implements ServletContextListener {
	
	public void contextInitialized(ServletContextEvent event){
		HibernateUtil.configureSessionFactory();
	}
	
	public void contextDestroyed(ServletContextEvent event){
		if(HibernateUtil.getSessionFactory() != null)
		HibernateUtil.getSessionFactory().close(); // Free all resources  
	}

}
