package es.udc.fi.tfg.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.model.Interest;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.Rating;
import es.udc.fi.tfg.model.Tag;
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.TagService;
import es.udc.fi.tfg.service.UserService;

@Component
public class InterestDAO {
	@Autowired
	private TagService tagService;
	
	@Autowired
	private UserService userService;
	
	public void addInterest(Interest bean, int tagId, int userId){
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        addInterest(session,bean, tagId, userId);        
        tx.commit();
        session.close();
    }
    
    private void addInterest(Session session, Interest bean, int tagId, int userId ){
        Interest interest = new Interest();        
        Tag tag = (Tag) session.load(Tag.class, tagId);
        User user = (User) session.load(User.class, userId);        
        interest.setUser(user);
        interest.setTag(tag);       
        interest.setPoints(bean.getPoints());        
        session.save(interest);
    }
    
    public List<Interest> getInterests(){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Interest");
        List<Interest> interests =  query.list();
        session.close();
        return interests;
    }
	
    public Interest getInterest(int id){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Interest where id = :id");
        query.setInteger("id",id);
        Interest interest = (Interest) query.uniqueResult();
        session.close();
        return interest;
    }
    
    public List<Interest> getInterestsFromUser(int userId){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Interest where user_id = :userId");
        query.setInteger("userId",userId);
        List<Interest> interests =  query.list();
        session.close();
        return interests;
    }
    
    public int existsInterest(int tagId, int userId){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Interest where tag_id = :tagId AND user_id = :userId");
        query.setInteger("tagId", tagId);
        query.setInteger("userId", userId);
        List<Interest> interests =  query.list();
        session.close();
        
        if(interests.size() > 0) {
        	return interests.get(0).getId();
        }
        else {
        	return -1;
        }
    }
    
    public int deleteInterest(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Interest where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }
    
    public int updateInterest(int id, Interest interest){
         if(id <=0)  
               return 0;  
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update Interest set points =:points where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setInteger("points",interest.getPoints());
            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            tx.commit();
            session.close();
            return rowCount;
    }
}
