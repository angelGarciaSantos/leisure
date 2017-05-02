package es.udc.fi.tfg.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Event;

@Component
public class EventDAO {
	public void addEvent(Event bean){
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        addEvent(session,bean);        
        tx.commit();
        session.close();
        
    }
    
    private void addEvent(Session session, Event bean){
        Event event = new Event();
        
        event.setName(bean.getName());
        event.setDescription(bean.getDescription());
        event.setBeginDate(bean.getBeginDate());
        event.setEndDate(bean.getEndDate());
        
        session.save(event);
    }
    
    public List<Event> getEvents(){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Event");
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
         if(id <=0)  
               return 0;  
         
//         DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//
//      // Get the date today using Calendar object.
//      Date today = Calendar.getInstance().getTime();        
//      // Using DateFormat format method we can create a string 
//      // representation of a date with the defined format.
//      String date1 = df.format(event.getBeginDate());
//      String date2 = df.format(event.getEndDate());
//
//      Date d1 = new Date();
//      Date d2 = new Date();
      
//      	try {
//			d1 = df.parse(date1);
//			d2 = df.parse(date2);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
      	
         
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update Event set name =:name, description=:description, begin_date =:beginDate, end_date =:endDate where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setString("name",event.getName());
            query.setString("description",event.getDescription());
            query.setDate("beginDate", event.getBeginDate());
            query.setDate("endDate", event.getEndDate());
            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            tx.commit();
            session.close();
            return rowCount;
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
        return rows;
    }
    
    public List<Integer> getEventsFromArtist(int artistId) {
		Session session = SessionUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery("select event_id from event_artist where artist_id = ?");
		sqlQuery.setParameter(0, artistId);

		return sqlQuery.list();
    }
	
}
