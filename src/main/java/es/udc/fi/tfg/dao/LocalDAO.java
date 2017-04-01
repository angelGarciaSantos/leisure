package es.udc.fi.tfg.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Local;

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
    	Local local = new Local();
        
    	local.setName(bean.getName());
    	local.setDescription(bean.getDescription());
    	local.setCapacity(bean.getCapacity());
    	local.setRating(bean.getRating());
        
        session.save(local);
    }
    
    public List<Local> getLocals(){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Local");
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
	
    public int deleteLocal(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Local where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }
    
    public int updateLocal(int id, Local local){
         if(id <=0)  
               return 0;  
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update Local set name =:name, description=:description, capacity =:capacity, rating =:rating where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setString("name",local.getName());
            query.setString("description",local.getDescription());
            query.setInteger("capacity", local.getCapacity());
            query.setDouble("rating", local.getRating());
            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            tx.commit();
            session.close();
            return rowCount;
    }
}
