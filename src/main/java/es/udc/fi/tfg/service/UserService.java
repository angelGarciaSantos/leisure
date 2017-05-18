package es.udc.fi.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.tfg.dao.ArtistDAO;
import es.udc.fi.tfg.dao.UserDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.User;

@Service
public class UserService {
	@Autowired
	private UserDAO userDAO;
	
	public List<User> getUsers() {
		return userDAO.getUsers();
	}
	
	public List<User> getUsersKeywords(String keywords) {
		return userDAO.getUsersKeywords(keywords);
	}
	
	public User getUser(int id){
		return userDAO.getUser(id);
	}
	
	public void createUser(User user){
		userDAO.addUser(user);
	}
	
	public int deleteUser(int id){
		return userDAO.deleteUser(id);
	}
	
	public int updateUser(int id, User user){
		return userDAO.updateUser(id, user);
	}
	
}
