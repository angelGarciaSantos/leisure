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
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@Component
public class TagDAO {
	
	@Transactional
	public void addTag(Tag bean) throws EntityNotCreatableException{
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        try {
        	addTag(session,bean); 
            tx.commit();      
        }
        catch(Exception e)
        {
        	tx.rollback();
        	throw new EntityNotCreatableException("No se pudo crear el tag.");
        }   
        finally {
            session.close();
        }
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
        //session.close();
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
        //session.close();
        return tags;
    }
    
    public Tag getTag(int id){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Tag where id = :id");
        query.setInteger("id",id);
        Tag tag = (Tag) query.uniqueResult();
        //session.close();
        return tag;
    }
    
    public List<Integer> getTagsFromArtist(int artistId, int first, int max) {
    	Session session = SessionUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery("select tag_id from tag_artist where artist_id = ? order by tag_id");
		sqlQuery.setParameter(0, artistId);
		sqlQuery.setFirstResult(first);
        if (max != -1){
        	sqlQuery.setMaxResults(max);
        }
		List<Integer> result = sqlQuery.list();
		//session.close();
		return result;
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
            tx.commit();
        }
        catch(ConstraintViolationException e)
        {
        	tx.rollback();
        	throw new EntityNotRemovableException("Elimine primero las entidades que dependen del Tag.");
        }    
        finally {
        	session.close();
        }
        System.out.println("Rows affected: " + rowCount);

        return rowCount;
    }
    
    @Transactional
    public int updateTag(int id, Tag tag) throws EntityNotUpdatableException{
         if(id <=0)  
               return 0;  
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update Tag set name =:name where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setString("name",tag.getName());
            int rowCount;
            try {
            	rowCount = query.executeUpdate();
                tx.commit();
            }
            catch(Exception e)
            {
            	tx.rollback();
            	throw new EntityNotUpdatableException("No se pudo actualizar el tag.");
            }   
            finally{
            	session.close();
            }
            
            System.out.println("Rows affected: " + rowCount);
            return rowCount;
    }
    
    @Transactional
    public int addTagToArtist(int tagId, int artistId) throws EntityNotCreatableException{
		Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        SQLQuery insertQuery = session.createSQLQuery("" +
        "INSERT INTO tag_artist(tag_id, artist_id) VALUES (?,?)");
        insertQuery.setParameter(0, tagId);
        insertQuery.setParameter(1, artistId);
        int rows;
        try {
        	rows = insertQuery.executeUpdate();
            tx.commit();
        }
        catch(Exception e)
        {
        	tx.rollback();
        	throw new EntityNotCreatableException("No se pudo añadir el tag al artista.");
        }   
        finally{
        	session.close();
        }
        //session.getTransaction().commit();

        return rows;
    }
    
    @Transactional
    public int deleteTagFromArtist(int tagId, int artistId) throws EntityNotRemovableException {
		Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        SQLQuery insertQuery = session.createSQLQuery("" +
        "delete from Tag_Artist where (tag_id, artist_id) = (?,?)");
        insertQuery.setParameter(0, tagId);
        insertQuery.setParameter(1, artistId);
        int rows;
        try {
            rows = insertQuery.executeUpdate();
            tx.commit();
        }
        catch(Exception e)
        {
        	tx.rollback();
        	throw new EntityNotRemovableException("No se pudo eliminar el tag del artista.");
        }   
        finally {
            session.close();
        }
        //session.getTransaction().commit();
        return rows;
    }
}
