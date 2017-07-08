package es.udc.fi.tfg.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Tag;
import es.udc.fi.tfg.util.EntityNotRemovableException;

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
    
    public List<Tag> getTags(int first, int max){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Tag order by tag_id");
        query.setFirstResult(first);
        if (max != -1){
            query.setMaxResults(max);
        }
        List<Tag> tags =  query.list();
        session.close();
        return tags;
    }
    
    public List<Tag> getTagsKeywords(String keywords, int first, int max){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Tag where lower(name) LIKE lower(:keywords) order by tag_id");
        query.setString("keywords", "%"+keywords+"%");
        query.setFirstResult(first);
        if (max != -1){
            query.setMaxResults(max);
        }
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
    
    public List<Integer> getTagsFromArtist(int artistId) {
    	Session session = SessionUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery("select tag_id from tag_artist where artist_id = ?");
		sqlQuery.setParameter(0, artistId);
		//session.close();
		return sqlQuery.list();
    }
	
    @Transactional
    public int deleteTag(int id) throws EntityNotRemovableException {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Tag where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = 0;
        try{
            rowCount = query.executeUpdate();
        }
        catch(ConstraintViolationException e)
        {
        	tx.rollback();
        	throw new EntityNotRemovableException("Elimine primero las entidades que dependen del Tag.");
        }    
        tx.commit();
        System.out.println("Rows affected: " + rowCount);

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
