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
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@Component
public class UserDAO {
 
	@Transactional
	public void addUser(User bean) throws EntityNotCreatableException{
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        try {
        	addUser(session,bean);        
        }
        catch(Exception e)
        {
        	tx.rollback();
        	throw new EntityNotCreatableException("No se pudo crear el usuario.");
        }   
        tx.commit();
        session.close();
        
    }
    
    private void addUser(Session session, User bean){
        User user = new User();
        
        user.setName(bean.getName());
        user.setEmail(bean.getEmail());
        user.setPassword(bean.getPassword());
        user.setType(bean.getType());
        
        session.save(user);
    }
    
    public List<User> getUsers(int first, int max){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from User order by id");
        query.setFirstResult(first);
        if (max != -1){
            query.setMaxResults(max);
        }
        List<User> users =  query.list();
        session.close();
        return users;
    }
    
    public List<User> getUsersKeywords(String keywords, int first, int max){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from User where lower(name) LIKE lower(:keywords) order by id");
        query.setString("keywords", "%"+keywords+"%");
        query.setFirstResult(first);
        if (max != -1){
            query.setMaxResults(max);
        }
        List<User> users =  query.list();
        session.close();
        return users;
    }
    
    public User getUserEmail(String email){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from User where email = :email");
        query.setString("email", email);
        User user =  (User) query.uniqueResult();
        session.close();
        return user;
    }
    
    public User getUser(int id){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from User where id = :id");
        query.setInteger("id",id);
        User user = (User) query.uniqueResult();
        session.close();
        return user;
    }
	
    @Transactional
    public int deleteUser(int id) throws EntityNotRemovableException {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from User where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = 0;
        try{
        	rowCount = query.executeUpdate();
        }
        catch(ConstraintViolationException e)
        {
        	tx.rollback();
        	throw new EntityNotRemovableException("Elimine primero las entidades que dependen del Usuario.");
        }   
        tx.commit();
        System.out.println("Rows affected: " + rowCount);
        return rowCount;
    }
    
    public int updateUser(int id, User user) throws EntityNotUpdatableException{
         if(id <=0)  
               return 0;  
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update User set name =:name, email=:email, type =:type, password =:password where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setString("name",user.getName());
            query.setString("email",user.getEmail());
            query.setString("password",user.getPassword());
            query.setInteger("type", user.getType());
            int rowCount;
            try {
            	rowCount = query.executeUpdate();
            }
            catch(Exception e)
            {
            	tx.rollback();
            	throw new EntityNotUpdatableException("No se pudo actualizar el usuario.");
            }  
            
            System.out.println("Rows affected: " + rowCount);
            tx.commit();
            session.close();
            return rowCount;
    }
}
