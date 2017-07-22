package es.udc.fi.tfg.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;
import es.udc.fi.tfg.util.PasswordEncrypter;

@CrossOrigin
@RestController
public class UserRestController {
	private Logger logger = Logger.getLogger(UserRestController.class);
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity login (HttpSession session, @RequestBody User userReq) {
		Enumeration atr = session.getAttributeNames();
		if (atr.hasMoreElements()) {
			logger.error("Ya hay una sesión iniciada");
			return new ResponseEntity(HttpStatus.LOCKED);
		}
		else {
			User user = userService.getUserEmail(userReq.getEmail());
			if (user == null){
				logger.error("No se ha encontrado ningun usuario con el email: "+userReq.getEmail());
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			else if (!PasswordEncrypter.isClearPasswordCorrect(userReq.getPassword(), user.getPassword())) {
				logger.error("La contraseña usada no es correcta: "+userReq.getPassword());
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			else {
				logger.info("Realizando login en la aplicacion con el usuario: "+user.getName());
				session.setAttribute("id", user.getId());
				session.setAttribute("name", user.getName());
				session.setAttribute("type", user.getType());	
				
				return new ResponseEntity(HttpStatus.OK);
			}			
		}			
	}
	
	@GetMapping("/private/login/{id}")
	public ResponseEntity getLogin(HttpServletRequest request, @PathVariable int id) {
		HttpSession session = request.getSession();
		
		if (session.getAttributeNames().hasMoreElements()) {
        	Integer userId = (Integer) session.getAttribute("id");
        	if (userId != id) {
    			logger.error("El usuario solo puede consultar sus propios datos");
        		return new ResponseEntity<String>("El usuario sólo puede consultar sus propios datos."+id, HttpStatus.FORBIDDEN);
        	}
		}
		
		User user = userService.getUser(id);
		if (user == null){
			logger.error("No se ha encontrado el usuario: "+id);
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo datos de login del usuario: "+id);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
	}
	
	@PutMapping("/private/login/{id}")
	public ResponseEntity<String> loginUpdate(HttpServletRequest request, @PathVariable int id, @RequestBody User user) throws EntityNotUpdatableException {
		HttpSession session = request.getSession();
		
		if (session.getAttributeNames().hasMoreElements()) {
        	Integer userId = (Integer) session.getAttribute("id");
        	if (userId != id) {
    			logger.error("El usuario solo puede actualizar sus propios datos");
        		return new ResponseEntity<String>("El usuario sólo puede actualizar sus propios datos."+id, HttpStatus.FORBIDDEN);
        	}
		}
		
		if (user.getId()!=id) {
			logger.error("Los ids no coinciden: "+id);
			return new ResponseEntity<String>("Los ids no coinciden"+id, HttpStatus.BAD_REQUEST);
		}
		else {
			int rows = userService.updateUser(id, user);
			if (rows < 1) {
				logger.error("No han podido actualizarse los datos del usuario: "+id);
				return new ResponseEntity<String>("No ha podido actualizarse el Usuario "+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				
				session.removeAttribute("name");
				session.setAttribute("name", user.getName());
				logger.info("Actualizando los datos del usuario: "+id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity register (HttpSession session, @RequestBody User userReq) throws EntityNotCreatableException {
		Enumeration atr = session.getAttributeNames();
		if (atr.hasMoreElements()) {
			logger.error("Ya hay una sesión iniciada");
			return new ResponseEntity(HttpStatus.LOCKED);
		}
		else {
			if (userService.getUserEmail(userReq.getEmail()) != null){
				logger.error("Ya hay un usuario registrado con ese email: "+ userReq.getEmail());
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
				
				logger.info("Registrando usuario: "+ userReq.getName());
				return new ResponseEntity(HttpStatus.OK);
			}			
		}			
	}
	
	@DeleteMapping("/logout")
	public ResponseEntity logout (HttpSession session) {
		session.invalidate();

		logger.info("Cerrando sesion");
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
		logger.info("Consultando informacion de la sesion");

		return new ResponseEntity<List<String>>(info, HttpStatus.OK);
	}
	
	@GetMapping("/admin/users/{first}/{max}")
	public ResponseEntity<List<User>> getUsers(@PathVariable int first, @PathVariable int max) {
		logger.info("Obteniendo todos los usuarios");
		return new ResponseEntity<List<User>>(userService.getUsers(first, max), HttpStatus.OK);
	}	
	
	@GetMapping("/admin/users/keywords/{keywords}/{first}/{max}")
	public ResponseEntity<List<User>> getUsersKeywords(@PathVariable String keywords, @PathVariable int first, @PathVariable int max) {
		logger.info("Obteniendo todos los usuarios con palabras clave: "+keywords);
		return new ResponseEntity<List<User>>(userService.getUsersKeywords(keywords, first, max), HttpStatus.OK);
	}	
	
	@GetMapping("/admin/user/email/{email}")
	public ResponseEntity<User> getUserEmail(@PathVariable String email) {
		logger.info("Obteniendo usuario con email: "+email);
		return new ResponseEntity<User>(userService.getUserEmail(email), HttpStatus.OK);
	}
	
	@GetMapping("/admin/users/{id}")
	public ResponseEntity<User> getUser(@PathVariable int id) {
		User user = userService.getUser(id);
		if (user == null){
			logger.error("No se ha encontrado el usuario: "+id);
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo usuario: "+id);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
	}	
	
	//TODO: mensaje error si no se creó correctamente
	@PostMapping(value = "/admin/users")
	public ResponseEntity<User> createUser(@RequestBody User user) throws EntityNotCreatableException {
		if (userService.existsUser(user)){
			logger.error("No ha podido crearse el usuario: "+user.getName());
			return new ResponseEntity<User>(HttpStatus.CONFLICT);	
		}
		else {
			logger.info("Creando usuario: "+user.getName());
			userService.createUser(user);
			return new ResponseEntity<User>(HttpStatus.CREATED);			
		}
	}
	
	@DeleteMapping("/admin/users/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable int id) {
		if (userService.getUser(id)==null) {
			logger.error("No se ha encontrado el usuario: "+id);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		else {
			int rows;
			try{
				rows = userService.deleteUser(id);
			}	
			catch(EntityNotRemovableException e){
				logger.error("No ha podido eliminarse el usuario: "+id);
				return new ResponseEntity<String>(HttpStatus.LOCKED);
			}
			catch(Exception e){
				logger.error("No ha podido eliminarse el usuario: "+id);
				return new ResponseEntity<String>("No ha podido eliminarse el Usuario: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if (rows < 1) {
				logger.error("No ha podido eliminarse el usuario: "+id);
				return new ResponseEntity<String>("No ha podido eliminarse el Usuario "+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Eliminando el usuario: "+id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}			
		}
	}

	@PutMapping("/admin/users/{id}")
	public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody User user) throws EntityNotUpdatableException {
		if (user.getId()!=id) {
			logger.error("Los ids no coinciden: "+id);
			return new ResponseEntity<String>("Los ids no coinciden"+id, HttpStatus.BAD_REQUEST);
		}
		else if (userService.getUser(id)==null) {
			logger.error("No se ha encontrado el usuario: "+id);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		else {
			int rows = userService.updateUser(id, user);
			if (rows < 1) {
				logger.error("No ha podido actualizarse el usuario: "+id);
				return new ResponseEntity<String>("No ha podido actualizarse el Usuario "+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Actualizando usuario: "+id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
}
