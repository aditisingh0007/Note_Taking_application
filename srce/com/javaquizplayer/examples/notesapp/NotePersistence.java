package com.javaquizplayer.examples.notesapp;

import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.PersistenceException;
import java.util.List;


public class NotePersistence {


	private EntityManagerFactory factory;
	private EntityManager em;


	public void getEntityManager() {
	
		factory = Persistence.createEntityManagerFactory("NOTES_APP_PU" );     
		em = factory.createEntityManager();
	}
	
	
	public List<Note> getAll()
			throws AppPersistenceException {
	
		getEntityManager();
		
		try {
			TypedQuery<Note> query =
					em.createQuery("SELECT n FROM Note n", Note.class);
			List<Note> notes = query.getResultList();
			return notes;
		}
		catch (PersistenceException ex) {
		
			throw new AppPersistenceException();
		}
		finally {
			em.close();
			factory.close();
		}
	}

	public Note create(Note n)
			throws AppPersistenceException {
	
		getEntityManager();
		em.getTransaction().begin();
		
		try {			
			em.persist(n);
			em.getTransaction().commit();
		}
		catch (PersistenceException ex) {

			// NOTE: this code can be used to get specific exception 
			// Throwable t = null;
			// for(t = ex.getCause(); t != null; t = t.getCause())
			//		System.out.println("Exception: " + t.getClass());		
			throw new AppPersistenceException();
		}
		finally {
			em.close();
			factory.close();
		}
		
		return n;
	}
	
	public void update(Note updatedN)
			throws AppPersistenceException {
	
		getEntityManager();		
		em.getTransaction().begin();
		
		try {
			em.find(Note.class, Integer.valueOf(updatedN.getId()));			
			em.merge(updatedN);			
			em.getTransaction().commit();
		}
		catch (PersistenceException ex) {
		
			throw new AppPersistenceException();
		}
		finally {
			em.close();
			factory.close();
		}
	}
	
	public void delete(int key)
			throws AppPersistenceException {

		getEntityManager();
		em.getTransaction().begin();
		
		try {			
			Note n = em.find(Note.class, Integer.valueOf(key));
			em.remove(n);			
			em.getTransaction().commit();
		}
		catch (PersistenceException ex) {
		
			throw new AppPersistenceException();
		}
		finally {
			em.close();
			factory.close();
		}
	}
}