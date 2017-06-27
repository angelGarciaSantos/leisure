package es.udc.fi.tfg.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.Rating;
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.LocalService;

@Component
public class EventDAO {
	@Autowired
	private LocalService localService;
	
	public void addEvent(Event bean, int localId){
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        addEvent(session,bean, localId);
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
    
	public List<Event> getEvents(){
        Session session = SessionUtil.getSession();    
        //session.flush();
        Query query = session.createQuery("from Event");
        //session.setCacheMode(CacheMode.IGNORE);
        List<Event> events =  query.list();
        
        session.close();
        return events;
    }
    
    public List<Event> getEventsKeywords(String keywords){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Event where lower(name) LIKE lower(:keywords)");
        query.setString("keywords", "%"+keywords+"%");
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
	
    public int deleteEvent(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Event where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }
    
	public int updateEvent(int id, Event event){
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
				int rowCount = query.executeUpdate();
				System.out.println("Rows affected: " + rowCount);
				tx.commit();
				session.close();
				return rowCount;
         }
    }
    
    public int addArtistToEvent(int eventId, int artistId){
		Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        SQLQuery insertQuery = session.createSQLQuery("" +
        "INSERT INTO event_artist(event_id, artist_id) VALUES (?,?)");
        insertQuery.setParameter(0, eventId);
        insertQuery.setParameter(1, artistId);
        int rows = insertQuery.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return rows;
    }
    
    public int deleteArtistFromEvent(int eventId, int artistId) {
		Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        SQLQuery insertQuery = session.createSQLQuery("" +
        "delete from Event_Artist where (event_id, artist_id) = (?,?)");
        insertQuery.setParameter(0, eventId);
        insertQuery.setParameter(1, artistId);
        int rows = insertQuery.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return rows;
    }
    
    public int modifyLocalFromEvent(int eventId, int localId){
        Session session = SessionUtil.getSession();
           Transaction tx = session.beginTransaction();
           String hql = "update Event set local_id =:localId where id = :eventId";
           Query query = session.createQuery(hql);
           query.setInteger("eventId",eventId);
           query.setInteger("localId",localId);
           int rowCount = query.executeUpdate();
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
	
}
