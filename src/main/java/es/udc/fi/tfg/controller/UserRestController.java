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
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.UserService;

@CrossOrigin
@RestController
public class UserRestController {
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserService userService;

	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		return new ResponseEntity<List<User>>(userService.getUsers(), HttpStatus.OK);
	}	
	
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUser(@PathVariable int id) {
		User user = userService.getUser(id);
		if (user == null){
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
	}	
	
	//TODO: mensaje error si no se creó correctamente
	@PostMapping(value = "/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		userService.createUser(user);
		return new ResponseEntity<User>(HttpStatus.CREATED);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable int id) {
		int rows = userService.deleteUser(id);
		if (rows < 1) {
			return new ResponseEntity<String>("No ha podido eliminarse el Usuario "+id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else{
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody User user) {
		if (user.getId()!=id) {
			return new ResponseEntity<String>("Los ids no coinciden"+id, HttpStatus.BAD_REQUEST);
		}
		else {
			int rows = userService.updateUser(id, user);
			if (rows < 1) {
				return new ResponseEntity<String>("No ha podido actualizarse el Usuario "+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
}
