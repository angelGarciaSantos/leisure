package es.udc.fi.tfg.dao;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.Rating;
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.LocalService;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@Component
public class EventDAO {
	@Autowired
	private LocalService localService;
	
	@Transactional
	public void addEvent(Event bean, int localId) throws EntityNotCreatableException{
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        try {
        	addEvent(session,bean, localId);
        }
        catch(Exception e)
        {
        	tx.rollback();
        	throw new EntityNotCreatableException("No se pudo crear el evento.");
        }   
        tx.commit();
        session.close();  
    }
    
    private void addEvent(Session session, Event bean, int localId){    	
    	Event event = new Event();        
        Local local = (Local) session.load(Local.class, localId);
        event.setLocal(local);
        event.setName(bean.getName());
        event.setDescription(bean.getDescription());
        event.setBeginDate(bean.getBeginDate());
        event.setEndDate(bean.getEndDate());        
        session.save(event);
    }
    
	public List<Event> getEvents(int first, int max){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Event order by id");
        query.setFirstResult(first);
        if (max != -1){
            query.setMaxResults(max);
        }
        List<Event> events =  query.list();
        
        session.close();
        return events;
    }
    
    public List<Event> getEventsKeywords(String keywords, int first, int max){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Event where lower(name) LIKE lower(:keywords) order by id");
        query.setString("keywords", "%"+keywords+"%");
        query.setFirstResult(first);
        if (max != -1){
            query.setMaxResults(max);
        }
        List<Event> events =  query.list();
        session.close();
        return events;
    }
    
    public Event getEvent(int id){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Event where id = :id");
        query.setInteger("id",id);
        Event event = (Event) query.uniqueResult();
        session.close();
        return event;
    }
    
    @Transactional
    public int deleteEvent(int id) throws EntityNotRemovableException {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Event where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = 0;
        try{
        	rowCount = query.executeUpdate();
        }
        catch(ConstraintViolationException e)
        {
        	tx.rollback();
        	throw new EntityNotRemovableException("Elimine primero las entidades que dependen del Artista.");
        }  
        tx.commit();
        System.out.println("Rows affected: " + rowCount);

        return rowCount;
    }
    
    @Transactional
	public int updateEvent(int id, Event event) throws EntityNotUpdatableException{
         if(id <=0)  {
               return 0; 
         }
         else {
        	 java.text.SimpleDateFormat sdf = 
        			 new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	         
        	 Session session = SessionUtil.getSession();
				Transaction tx = session.beginTransaction();
				String hql = "update Event set name =:name, description=:description, begin_date =:beginDate, end_date =:endDate where id = :id";
				Query query = session.createQuery(hql);
				query.setInteger("id",id);
				query.setString("name",event.getName());
				query.setString("description",event.getDescription());
				query.setString("beginDate", sdf.format(event.getBeginDate()));
				query.setString("endDate", sdf.format(event.getEndDate()));
				int rowCount;
				try {
					rowCount = query.executeUpdate();
				}
				catch(Exception e)
		        {
		        	tx.rollback();
		        	throw new EntityNotUpdatableException("No pudo actualizarse el evento.");
		        }
				System.out.println("Rows affected: " + rowCount);
				tx.commit();
				session.close();
				return rowCount;
         }
    }
    
    @Transactional
    public int addArtistToEvent(int eventId, int artistId) throws EntityNotCreatableException{
		Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        SQLQuery insertQuery = session.createSQLQuery("" +
        "INSERT INTO event_artist(event_id, artist_id) VALUES (?,?)");
        insertQuery.setParameter(0, eventId);
        insertQuery.setParameter(1, artistId);
        int rows;
        try {
        	rows = insertQuery.executeUpdate();
        }
        catch(Exception e)
        {
        	tx.rollback();
        	throw new EntityNotCreatableException("No se pudo añadir el artista al evento.");
        }   
        //session.getTransaction().commit();
        tx.commit();
        session.close();
        return rows;
    }
    
    @Transactional
    public int deleteArtistFromEvent(int eventId, int artistId) throws EntityNotRemovableException {
		Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        SQLQuery insertQuery = session.createSQLQuery("" +
        "delete from Event_Artist where (event_id, artist_id) = (?,?)");
        insertQuery.setParameter(0, eventId);
        insertQuery.setParameter(1, artistId);
        int rows;
        try {
            rows = insertQuery.executeUpdate();
        }
        catch(Exception e)
        {
        	tx.rollback();
        	throw new EntityNotRemovableException("No se pudo eliminar el artista del evento.");
        }   
        //session.getTransaction().commit();
        tx.commit();
        session.close();
        return rows;
    }
    
    @Transactional
    public int modifyLocalFromEvent(int eventId, int localId) throws EntityNotUpdatableException{
        Session session = SessionUtil.getSession();
           Transaction tx = session.beginTransaction();
           String hql = "update Event set local_id =:localId where id = :eventId";
           Query query = session.createQuery(hql);
           query.setInteger("eventId",eventId);
           query.setInteger("localId",localId);
           int rowCount;
           try {
        	   rowCount = query.executeUpdate();
           }
           catch(Exception e)
           {
           	tx.rollback();
           	throw new EntityNotUpdatableException("No se pudo modificar el local del evento.");
           }   
           System.out.println("Rows affected: " + rowCount);
           tx.commit();
           session.close();
           return rowCount;
   }
    
    public List<Integer> getEventsFromArtist(int artistId) {
		Session session = SessionUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery("select event_id from event_artist where artist_id = ?");
		sqlQuery.setParameter(0, artistId);

		return sqlQuery.list();
    }
    
    public List<Event> getEventsFromLocal(int localId){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Event where local_id = :localId");
        query.setInteger("localId", localId);
        //session.setCacheMode(CacheMode.IGNORE);
        List<Event> events =  query.list();
        
        session.close();
        return events;
    }
    
    public List<Integer> getEventsFromUser(int userId) {
    	Session session = SessionUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery("select event_id from user_event where user_id = ?");
		sqlQuery.setParameter(0, userId);
		//session.close();
		return sqlQuery.list();
    }
    
    @Transactional
    public int followEvent (int eventId, int userId) throws EntityNotCreatableException {
		Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        SQLQuery insertQuery = session.createSQLQuery("" +
        "INSERT INTO user_event(user_id, event_id) VALUES (?,?)");
        insertQuery.setParameter(0, userId);
        insertQuery.setParameter(1, eventId);
        int rows;
        try {
        	rows = insertQuery.executeUpdate();
        }
        catch(Exception e)
        {
        	tx.rollback();
        	throw new EntityNotCreatableException("No se pudo seguir el evento.");
        }   
        tx.commit();
        //session.getTransaction().commit();  
        //session.close();
        return rows;    		
    }
	
    @Transactional
    public int unfollowEvent (int eventId, int userId) throws EntityNotRemovableException {
		Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        SQLQuery insertQuery = session.createSQLQuery("" +
        "delete from User_Event where (user_id, event_id) = (?,?)");
        insertQuery.setParameter(0, userId);
        insertQuery.setParameter(1, eventId);
        int rows;
        try {
        	rows = insertQuery.executeUpdate();
        }
        catch(Exception e)
        {
        	tx.rollback();
        	throw new EntityNotRemovableException("No se pudo dejar de seguir el evento.");
        }   
        
        session.getTransaction().commit();
        //session.close();
        return rows;
    }
    
    public int isFollowingEvent(int eventId, int userId) {
    	Session session = SessionUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery("select count(*) from user_event where user_id = ? and event_id = ?");
		sqlQuery.setParameter(0, userId);
		sqlQuery.setParameter(1, eventId);
		//session.close();
		Integer count = ((BigInteger) sqlQuery.uniqueResult()).intValue();
		
		return count;
    }
	
}
