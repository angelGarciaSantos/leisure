package es.udc.fi.tfg.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.tfg.dao.ArtistDAO;
import es.udc.fi.tfg.dao.UserDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@Service
public class UserService {
	@Autowired
	private UserDAO userDAO;
	
	public List<User> getUsers(int first, int max) {
		return userDAO.getUsers(first, max);
	}
	
	public List<User> getUsersKeywords(String keywords, int first, int max) {
		return userDAO.getUsersKeywords(keywords, first, max);
	}
	
	public User getUserEmail(String email) {
		return userDAO.getUserEmail(email);
	}
	
	public User getUser(int id){
		return userDAO.getUser(id);
	}
	
	public void createUser(User user) throws EntityNotCreatableException{
		userDAO.addUser(user);
	}
	
	public int deleteUser(int id) throws EntityNotRemovableException{
		return userDAO.deleteUser(id);
	}
	
	public int updateUser(int id, User user) throws EntityNotUpdatableException{
		return userDAO.updateUser(id, user);
	}
	
	public boolean existsUser (User user){
		List<User> users = new ArrayList<User>();
		users = userDAO.getUsers(0,-1);
		boolean exists = false;
        for(User a : users) {
        	if (user.getEmail().equals(a.getEmail())){
        		exists = true;
        	}
        }
		return exists;
	}
}
