package es.udc.fi.tfg.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.model.Rating;

@Component
public class RatingDAO {
	public void addRating(Rating bean){
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        addRating(session,bean);        
        tx.commit();
        session.close();
        
    }
    
    private void addRating(Session session, Rating bean){
        Rating rating = new Rating();
        
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
