package com.training.ivan.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.training.ivan.Ticket;
import com.training.ivan.TicketSystemConfig;

public class JpaUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(JpaUtil.class);

	// @PersistenceUnit( name = "com.training.ivan")
	// can not be used due to tomcat limitations
	private static EntityManagerFactory emf = Persistence
			.createEntityManagerFactory("Ticket_Reservation_System1");

	/**
	 * Initializes the database in an initial state. This method is used after
	 * first database creation or after a clear() method. In production this
	 * method will be used only for an initial deployment of the web app.
	 */
	public static void initDB() {
		EntityManager em = null;
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			for (int i = 0; i < TicketSystemConfig.NUMBER_OF_TICKETS; i++)
				em.persist(new Ticket(i, null));
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		} finally {
			em.close();
		}
	}

	/**
	 * Clears the tables in the database - deletes all data in the database.
	 * This method is only applicable for exercise and testing purposes
	 */
	public static void clear() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Query q = em.createQuery("delete from Ticket");
			q.executeUpdate();
			q = em.createQuery("delete from User");
			q.executeUpdate();
			tx.commit();
			logger.info("|# Jpa datatables cleared #|");
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		} finally {
			em.close();
			// closes the entity. Entity factory must be closed only on
			// application shutdown
		}
	}

	public static EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}

}
