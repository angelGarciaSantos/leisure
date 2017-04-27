package es.udc.fi.tfg.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.Rating;
import es.udc.fi.tfg.model.User;

@Component
public class RatingDAO {
	@Autowired
	private EventDAO eventDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	public void addRating(Rating bean, int eventId, int userId){
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        addRating(session,bean, eventId, userId);        
        tx.commit();
        session.close();
        
    }
    
    private void addRating(Session session, Rating bean, int eventId, int userId ){
        Rating rating = new Rating();        
        Event event = eventDAO.getEvent(eventId);
        User user = userDAO.getUser(userId);        
        rating.setUser(user);
        rating.setEvent(event);       
        rating.setRating(bean.getRating());        
        session.save(rating);
    }
    
    public List<Rating> getRatings(){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Rating");
        List<Rating> ratings =  query.list();
        session.close();
        return ratings;
    }
	
    public Rating getRating(int id){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Rating where id = :id");
        query.setInteger("id",id);
        Rating rating = (Rating) query.uniqueResult();
        session.close();
        return rating;
    }
    
    public List<Rating> getRatingsFromEvent(int eventId){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Rating where event_id = :eventId");
        query.setInteger("eventId",eventId);
        List<Rating> ratings =  query.list();
        session.close();
        return ratings;
    }
    
    public int deleteRating(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Rating where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }
    
    public int updateRating(int id, Rating rating){
         if(id <=0)  
               return 0;  
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update Rating set rating =:rating where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setDouble("rating",rating.getRating());
            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            tx.commit();
            session.close();
            return rowCount;
    }
}