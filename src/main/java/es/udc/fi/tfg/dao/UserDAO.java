package es.udc.fi.tfg.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.User;

@Component
public class UserDAO {
	 public void addUser(User bean){
	        Session session = SessionUtil.getSession();        
	        Transaction tx = session.beginTransaction();
	        addUser(session,bean);        
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
	    
	    public List<User> getUsers(){
	        Session session = SessionUtil.getSession();    
	        Query query = session.createQuery("from User");
	        List<User> users =  query.list();
	        session.close();
	        return users;
	    }
	    
	    public List<User> getUsersKeywords(String keywords){
	        Session session = SessionUtil.getSession();    
	        Query query = session.createQuery("from User where lower(name) LIKE lower(:keywords)");
	        query.setString("keywords", "%"+keywords+"%");
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
		
	    public int deleteUser(int id) {
	        Session session = SessionUtil.getSession();
	        Transaction tx = session.beginTransaction();
	        String hql = "delete from User where id = :id";
	        Query query = session.createQuery(hql);
	        query.setInteger("id",id);
	        int rowCount = query.executeUpdate();
	        System.out.println("Rows affected: " + rowCount);
	        tx.commit();
	        session.close();
	        return rowCount;
	    }
	    
	    //TODO: actualizar password?
	    public int updateUser(int id, User user){
	         if(id <=0)  
	               return 0;  
	         Session session = SessionUtil.getSession();
	            Transaction tx = session.beginTransaction();
	            String hql = "update User set name =:name, email=:email, type =:type where id = :id";
	            Query query = session.createQuery(hql);
	            query.setInteger("id",id);
	            query.setString("name",user.getName());
	            query.setString("email",user.getEmail());
	            query.setInteger("type", user.getType());
	            int rowCount = query.executeUpdate();
	            System.out.println("Rows affected: " + rowCount);
	            tx.commit();
	            session.close();
	            return rowCount;
	    }
}
