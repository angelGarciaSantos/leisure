package es.udc.fi.tfg.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Comment;

@Component
public class CommentDAO {
	public void addComment(Comment bean){
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        addComment(session,bean);        
        tx.commit();
        session.close();
        
    }
    
    private void addComment(Session session, Comment bean){
        Comment comment = new Comment();
        
        comment.setText(bean.getText());        
        session.save(comment);
    }
    
    public List<Comment> getComments(){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Comment");
        List<Comment> comments =  query.list();
        session.close();
        return comments;
    }
	
    public int deleteComment(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Comment where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }
    
    public int updateComment(int id, Comment comment){
         if(id <=0)  
               return 0;  
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update Comment set text =:text where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setString("text",comment.getText());
            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            tx.commit();
            session.close();
            return rowCount;
    }
}
