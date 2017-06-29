package es.udc.fi.tfg.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.tfg.dao.ArtistDAO;
import es.udc.fi.tfg.dao.UserDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.UserService;
import es.udc.fi.tfg.util.EntityNotRemovableException;

@CrossOrigin
@RestController
public class UserRestController {
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity login (HttpSession session, @RequestBody User userReq) {
		Enumeration atr = session.getAttributeNames();
		if (atr.hasMoreElements()) {
			return new ResponseEntity(HttpStatus.LOCKED);
		}
		else {
			User user = userService.getUserEmail(userReq.getEmail());
			if (user == null){
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			else if (!user.getPassword().equals(userReq.getPassword())) {
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			else {
				session.setAttribute("id", user.getId());
				session.setAttribute("name", user.getName());
				session.setAttribute("type", user.getType());	
				
				return new ResponseEntity(HttpStatus.OK);
			}			
		}			
	}
	
	@PostMapping("/register")
	public ResponseEntity register (HttpSession session, @RequestBody User userReq) {
		Enumeration atr = session.getAttributeNames();
		if (atr.hasMoreElements()) {
			return new ResponseEntity(HttpStatus.LOCKED);
		}
		else {
			if (userService.getUserEmail(userReq.getEmail()) != null){
				return new ResponseEntity(HttpStatus.CONFLICT);
			}
			else {
				//meter en BBDD
				userReq.setType(1);
				userService.createUser(userReq);
				User user = userService.getUserEmail(userReq.getEmail());
				
				session.setAttribute("id", user.getId());
				session.setAttribute("name", user.getName());
				session.setAttribute("type", user.getType());	
				
				return new ResponseEntity(HttpStatus.OK);
			}			
		}			
	}
	
	@DeleteMapping("/logout")
	public ResponseEntity logout (HttpSession session) {
		session.invalidate();

		return new ResponseEntity(HttpStatus.OK);
	}
	
	@GetMapping("/logininfo")
	public ResponseEntity<List<String>> loginInfo (HttpSession session) {
		List<String> info = new ArrayList<String>();
		if (session.getAttributeNames().hasMoreElements()) {
			info.add(((Integer) session.getAttribute("id")).toString());
			info.add((String) session.getAttribute("name"));
			info.add(((Integer) session.getAttribute("type")).toString());
		}

		return new ResponseEntity<List<String>>(info, HttpStatus.OK);
	}
	
	@GetMapping("/private/users")
	public ResponseEntity<List<User>> getUsers() {
		return new ResponseEntity<List<User>>(userService.getUsers(), HttpStatus.OK);
	}	
	
	@GetMapping("/private/users/keywords/{keywords}")
	public ResponseEntity<List<User>> getUsersKeywords(@PathVariable String keywords) {
		return new ResponseEntity<List<User>>(userService.getUsersKeywords(keywords), HttpStatus.OK);
	}	
	
	@GetMapping("/private/user/email/{email}")
	public ResponseEntity<User> getUserEmail(@PathVariable String email) {
		return new ResponseEntity<User>(userService.getUserEmail(email), HttpStatus.OK);
	}
	
	@GetMapping("/private/users/{id}")
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
	@PostMapping(value = "/private/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		userService.createUser(user);
		return new ResponseEntity<User>(HttpStatus.CREATED);
	}
	
	@DeleteMapping("/private/users/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable int id) {
		int rows;
		try{
			rows = userService.deleteUser(id);
		}	
		catch(EntityNotRemovableException e){
			return new ResponseEntity<String>(HttpStatus.LOCKED);
		}
		catch(Exception e){
			return new ResponseEntity<String>("No ha podido eliminarse el Usuario: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (rows < 1) {
			return new ResponseEntity<String>("No ha podido eliminarse el Usuario "+id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else{
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	@PutMapping("/private/users/{id}")
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
