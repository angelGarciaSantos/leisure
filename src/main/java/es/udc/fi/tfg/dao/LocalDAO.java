package es.udc.fi.tfg.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.util.EntityNotRemovableException;

@Component
public class LocalDAO {
    public void addLocal(Local bean){
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        addLocal(session,bean);        
        tx.commit();
        session.close();
        
    }
    
    private void addLocal(Session session, Local bean){
    	
    	Local local;
     	if (bean.getImage()==null){
     		local = new Local(bean.getName(), bean.getDescription(), bean.getCapacity(), bean.getLat(), bean.getLng());
        }
        else{
     		local = new Local(bean.getName(), bean.getDescription(), bean.getCapacity(), bean.getLat(), bean.getLng(), bean.getImage());
        }    
    	
        session.save(local);
    }
    
    public List<Local> getLocals(int first, int max){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Local order by id");
        query.setFirstResult(first);
        if (max != -1){
            query.setMaxResults(max);
        }
        List<Local> locals =  query.list();
        session.close();
        return locals;
    }
    
    public List<Local> getLocalsKeywords(String keywords, int first, int max){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Local where lower(name) LIKE lower(:keywords) order by id");
        query.setString("keywords", "%"+keywords+"%");
        query.setFirstResult(first);
        if (max != -1){
            query.setMaxResults(max);
        }
        List<Local> locals =  query.list();
        session.close();
        return locals;
    }
    
    public Local getLocal(int id){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Local where id = :id");
        query.setInteger("id",id);
        Local local = (Local) query.uniqueResult();
        session.close();
        return local;
    }
	
    @Transactional
    public int deleteArtist(int id) throws Exception {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Artist where artist_id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = 0;
        try{
            rowCount = query.executeUpdate();
        }
        catch(ConstraintViolationException e)
        {
        	tx.rollback();
        	throw new EntityNotRemovableException("Elimine primero las entidades que dependen del Artista.");
        }       
        tx.commit(); 
        System.out.println("Rows affected: " + rowCount);

        return rowCount;
    }
    
    @Transactional
    public int deleteLocal(int id) throws EntityNotRemovableException {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Local where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = 0;
        try{
        	rowCount = query.executeUpdate();
        }
        catch(ConstraintViolationException e)
        {
        	tx.rollback();
        	throw new EntityNotRemovableException("Elimine primero las entidades que dependen del Local.");
        } 
        tx.commit();
        System.out.println("Rows affected: " + rowCount);
        return rowCount;
    }
    
    public int updateLocal(int id, Local local){
         if(id <=0)  
               return 0;  
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update Local set name =:name, description=:description, capacity =:capacity, lat =:lat, lng =:lng, image =:image where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setString("name",local.getName());
            query.setString("description",local.getDescription());
            query.setInteger("capacity", local.getCapacity());
            query.setDouble("lat", local.getLat());
            query.setDouble("lng", local.getLng());
            query.setString("image",local.getImage());


            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            tx.commit();
            session.close();
            return rowCount;
    }
}
