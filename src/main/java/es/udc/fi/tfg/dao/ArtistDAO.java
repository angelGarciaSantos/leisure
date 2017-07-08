package es.udc.fi.tfg.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Component;

import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Employee;
import es.udc.fi.tfg.util.EntityNotRemovableException;

@Component
public class ArtistDAO {
    public void addArtist(Artist bean){
        Session session = SessionUtil.getSession();        
        Transaction tx = session.beginTransaction();
        addArtist(session,bean);        
        tx.commit();
        session.close();
        
    }
    
    private void addArtist(Session session, Artist bean){
        Artist artist;
    	if (bean.getImage()==null){
        	artist = new Artist(bean.getName(), bean.getDescription());
        }
        else{
        	artist = new Artist(bean.getName(), bean.getDescription(), bean.getImage());
        }    
        session.save(artist);
    }
    
    public List<Artist> getArtists(int first, int max){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Artist order by artist_id");
        query.setFirstResult(first);
        if (max != -1){
            query.setMaxResults(max);
        }
        List<Artist> artists =  query.list();
        session.close();
        return artists;
    }
    
    public List<Artist> getArtistsKeywords(String keywords, int first, int max){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Artist where lower(name) LIKE lower(:keywords) order by artist_id");
        query.setString("keywords", "%"+keywords+"%");
        query.setFirstResult(first);
        if (max != -1){
            query.setMaxResults(max);
        }
        List<Artist> artists =  query.list();
        session.close();
        return artists;
    }
    
    public Artist getArtist(int id){
        Session session = SessionUtil.getSession();    
        Query query = session.createQuery("from Artist where id = :id");
        query.setInteger("id",id);
        Artist artist = (Artist) query.uniqueResult();
        session.close();
        return artist;
    }
    
    public List<Integer> getArtistsFromEvent(int eventId, int first, int max) {
    	Session session = SessionUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery("select artist_id from event_artist where event_id = ? order by artist_id");
		sqlQuery.setParameter(0, eventId);
		sqlQuery.setFirstResult(first);
        if (max != -1){
        	sqlQuery.setMaxResults(max);
        }
		//session.close();
		return sqlQuery.list();
    }
    
    public List<Integer> getArtistsFromUser(int userId) {
    	Session session = SessionUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery("select artist_id from user_artist where user_id = ?");
		sqlQuery.setParameter(0, userId);
		//session.close();
		return sqlQuery.list();
    }
	
    public List<Integer> getArtistsFromTag(int tagId) {
    	Session session = SessionUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery("select artist_id from tag_artist where tag_id = ?");
		sqlQuery.setParameter(0, tagId);
		//session.close();
		return sqlQuery.list();
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
    
    public int updateArtist(int id, Artist art){
         if(id <=0)  
               return 0;  
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update Artist set name =:name, description=:description, image =:image where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setString("name",art.getName());
            query.setString("description",art.getDescription());
            query.setString("image", art.getImage());
            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            tx.commit();
            //session.close();
            return rowCount;
    }
    
    public int followArtist (int artistId, int userId) {
		Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        SQLQuery insertQuery = session.createSQLQuery("" +
        "INSERT INTO user_artist(user_id, artist_id) VALUES (?,?)");
        insertQuery.setParameter(0, userId);
        insertQuery.setParameter(1, artistId);
        int rows = insertQuery.executeUpdate();
        session.getTransaction().commit();  
        //session.close();
        return rows;    		
    }
	
    public int unfollowArtist (int artistId, int userId) {
		Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        SQLQuery insertQuery = session.createSQLQuery("" +
        "delete from User_Artist where (user_id, artist_id) = (?,?)");
        insertQuery.setParameter(0, userId);
        insertQuery.setParameter(1, artistId);
        int rows = insertQuery.executeUpdate();
        session.getTransaction().commit();
        //session.close();
        return rows;
    }
    
    public int isFollowingArtist(int artistId, int userId) {
    	Session session = SessionUtil.getSession();
		SQLQuery sqlQuery = session.createSQLQuery("select count(*) from user_artist where user_id = ? and artist_id = ?");
		sqlQuery.setParameter(0, userId);
		sqlQuery.setParameter(1, artistId);
		//session.close();
		Integer count = ((BigInteger) sqlQuery.uniqueResult()).intValue();
		
		return count;
    }
	
	
	
	
	
//Lo de debajo es el antiguo DAO de prueba	
	
//	// Dummy database. Initialize with some dummy values.
//	private static List<Artist> artists;
//	{
//		artists = new ArrayList<Artist>();
//		artists.add(new Artist(101,"Metallica","Grupo de trash metal",0.0));
//		artists.add(new Artist(201,"Red Hot Chili Peppers","Grupo de grunge/funk",0.0));
//		artists.add(new Artist(301,"Ernesto Sevilla","C�mico",0.0));
//	}
//
//	/**
//	 * Returns list of artists from dummy database.
//	 * 
//	 * @return list of artists
//	 */
//	public List<Artist> list() {
//		return artists;
//	}
//
//	/**
//	 * Return customer object for given id from dummy database. If customer is
//	 * not found for id, returns null.
//	 * 
//	 * @param id
//	 *            customer id
//	 * @return customer object for given id
//	 */
//	public Artist get(Long id) {
//
//		for (Artist a : artists) {
//			if (a.getId().equals(id)) {
//				return a;
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * Create new artist in dummy database. Updates the id and insert new
//	 * customer in list.
//	 * 
//	 * @param customer
//	 *            Customer object
//	 * @return customer object with updated id
//	 */
//	public Artist create(Artist artist) {
//		artist.setId(System.currentTimeMillis());
//		artists.add(artist);
//		return artist;
//	}
//
//	/**
//	 * Delete the artist object from dummy database. If artist not found for
//	 * given id, returns null.
//	 * 
//	 * @param id
//	 *            the customer id
//	 * @return id of deleted customer object
//	 */
//	public Long delete(Long id) {
//
//		for (Artist c : artists) {
//			if (c.getId().equals(id)) {
//				artists.remove(c);
//				return id;
//			}
//		}
//
//		return null;
//	}
//
//	/**
//	 * Update the customer object for given id in dummy database. If customer
//	 * not exists, returns null
//	 * 
//	 * @param id
//	 * @param customer
//	 * @return customer object with id
//	 */
//	public Artist update(Long id, Artist artist) {
//
//		for (Artist a : artists) {
//			if (a.getId().equals(id)) {
//				artist.setId(a.getId());
//				artists.remove(a);
//				artists.add(artist);
//				return artist;
//			}
//		}
//
//		return null;
//	}
}
