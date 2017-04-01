package es.udc.fi.tfg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.tfg.dao.ArtistDAO;
import es.udc.fi.tfg.dao.UserDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.User;

@CrossOrigin
@RestController
public class UserRestController {
	@Autowired
	private UserDAO userDAO;

	
	@GetMapping("/users")
	public List getUsers() {
		return userDAO.getUsers();
	}	
	
	@GetMapping("/users/{id}")
	public User getUser(@PathVariable int id) {
		return userDAO.getUser(id);
	}	
	
	@PostMapping(value = "/users")
	public ResponseEntity createUser(@RequestBody User user) {

		userDAO.addUser(user);

		return new ResponseEntity(user, HttpStatus.OK);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity deleteUser(@PathVariable int id) {

		if (0 == userDAO.deleteUser(id)) {
			return new ResponseEntity("No User found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(id, HttpStatus.OK);

	}

	@PutMapping("/users/{id}")
	public ResponseEntity updateUser(@PathVariable int id, @RequestBody User user) {

		int rows = userDAO.updateUser(id, user);
		if (0 == rows) {
			return new ResponseEntity("No User found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(rows, HttpStatus.OK);
	}
}
