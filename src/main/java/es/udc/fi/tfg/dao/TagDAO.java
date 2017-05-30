package es.udc.fi.tfg.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Tag;

@Component
public class TagDAO {
	public void addTag(Tag bean){
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        addTag(session,bean);        
        tx.commit();
        session.close();
        
    }
    
    private void addTag(Session session, Tag bean){
    	Tag tag = new Tag();
        
    	tag.setName(bean.getName());
        
        session.save(tag);
    }
    
    public List<Tag> getTags(){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Tag");
        List<Tag> tags =  query.list();
        session.close();
        return tags;
    }
    
    public List<Tag> getTagsKeywords(String keywords){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Tag where lower(name) LIKE lower(:keywords)");
        query.setString("keywords", "%"+keywords+"%");
        List<Tag> tags =  query.list();
        session.close();
        return tags;
    }
    
    public Tag getTag(int id){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Tag where id = :id");
        query.setInteger("id",id);
        Tag tag = (Tag) query.uniqueResult();
        session.close();
        return tag;
    }
	
    public int deleteTag(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Tag where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }
    
    public int updateTag(int id, Tag tag){
         if(id <=0)  
               return 0;  
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update Tag set name =:name where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setString("name",tag.getName());
            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            tx.commit();
            session.close();
            return rowCount;
    }
}
