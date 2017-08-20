package es.udc.fi.tfg.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@Component
public class CommentDAO {
	
	@Autowired
	private EventDAO eventDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Transactional
	public void addComment(Comment bean, int eventId, int userId) throws EntityNotCreatableException{
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        try {
        	addComment(session,bean, eventId, userId);  
            tx.commit();
        }
        catch(Exception e)
        {
        	tx.rollback();
        	throw new EntityNotCreatableException("No se pudo crear el comentario.");
        }
        finally {
        	session.close();
        }
    }
    
    private void addComment(Session session, Comment bean, int eventId, int userId ){
        Comment comment = new Comment();            
        Event event = (Event) session.load(Event.class, eventId);
        User user = (User) session.load(User.class, userId);       
        comment.setUser(user);
        comment.setEvent(event);       
        comment.setText(bean.getText());     
        Date date = new Date();
        comment.setDate(date);
        session.save(comment);
    }
    
    public List<Comment> getComments(){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Comment");
        List<Comment> comments =  query.list();
        //session.close();
        return comments;
    }
    
    public Comment getComment(int id){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Comment where id = :id");
        query.setInteger("id",id);
        Comment comment = (Comment) query.uniqueResult();
        //session.close();
        return comment;
    }
    
    public List<Comment> getCommentsFromEvent(int eventId, int first, int max){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Comment where event_id = :eventId order by date desc, id desc");
        query.setInteger("eventId",eventId);
        query.setFirstResult(first);
        if (max != -1){
        	query.setMaxResults(max);
        }
        List<Comment> comments =  query.list();
        //session.close();
        return comments;
    }
	
    @Transactional
    public int deleteComment(int id) throws EntityNotRemovableException {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Comment where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount;
        try {
        	rowCount = query.executeUpdate();
            tx.commit();
        }
        catch(Exception e)
        {
        	tx.rollback();
        	throw new EntityNotRemovableException("No se pudo eliminar el comentario.");
        }   
        finally {
            session.close();
        }
        System.out.println("Rows affected: " + rowCount);
        
        return rowCount;
    }
    
    @Transactional
    public int updateComment(int id, Comment comment) throws EntityNotUpdatableException{
         if(id <=0)  
               return 0;  
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update Comment set text =:text where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setString("text",comment.getText());
            int rowCount;
            try {
            	rowCount = query.executeUpdate();
                tx.commit();
            }
            catch(Exception e)
            {
            	tx.rollback();
            	throw new EntityNotUpdatableException("No se pudo actualizar el comentario");
            }   
            finally {
            	session.close();
            }
            System.out.println("Rows affected: " + rowCount);
           
            return rowCount;
    }
}
