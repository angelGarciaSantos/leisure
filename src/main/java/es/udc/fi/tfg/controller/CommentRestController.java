package es.udc.fi.tfg.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.tfg.dao.ArtistDAO;
import es.udc.fi.tfg.dao.CommentDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.Rating;
import es.udc.fi.tfg.service.CommentService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.UserService;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@CrossOrigin
@RestController
public class CommentRestController {	
	private Logger logger = Logger.getLogger(CommentRestController.class);
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/comments")
	public ResponseEntity<List<Comment>> getComments() {
		logger.info("Obteniendo todos los comentarios.");
		return new ResponseEntity<List<Comment>> (commentService.getComments(), HttpStatus.OK);
	}	
		
	@GetMapping("/comments/{id}")
	public ResponseEntity<Comment> getComment(@PathVariable int id) {
		Comment comment = commentService.getComment(id);
		if (comment == null){
			logger.error("No ha podido obtenerse el comentario: "+id);
			return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo el comentario: "+id);
			return new ResponseEntity<Comment>(comment, HttpStatus.OK);
		}		
	}	
	
	@GetMapping("/comments/event/{eventId}")
	public ResponseEntity<List<Comment>> getCommentsFromEvent(@PathVariable int eventId) {	
		if (eventService.getEvent(eventId) == null){
			logger.error("No han podido obtenerse los comentarios del evento: "+eventId);
			return new ResponseEntity<List<Comment>>(HttpStatus.NOT_FOUND);
		}
		else {	
			logger.info("Obteniendo los comentarios del evento: "+eventId);
			return new ResponseEntity<List<Comment>> (commentService.getCommentsFromEvent(eventId), HttpStatus.OK);
		}
	}	
	
	//TODO: ver como comprobar que se crea correctamente desde el DAO
	@PostMapping(value = "/private/comments/{eventId}/{userId}")
	public ResponseEntity<Comment> createComment(@RequestBody Comment comment, @PathVariable int eventId, @PathVariable int userId) throws EntityNotCreatableException {
		if (eventService.getEvent(eventId) == null || userService.getUser(userId) == null){
			logger.error("No ha podido crearse el comentario. El evento: "+eventId+" o el usuario: "+userId+" no existen.");
			return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
		}	
		else {
			logger.error("Creado comentario en el evento: "+eventId+" por el usuario: "+userId);
			commentService.createComment(comment, eventId, userId);
			return new ResponseEntity<Comment>(HttpStatus.CREATED);		
		}
	}
	
	@DeleteMapping("/private/comments/{id}")
	public ResponseEntity<String> deleteComment(@PathVariable int id) throws EntityNotRemovableException {
		if (commentService.getComment(id)==null) {
			logger.error("No ha podido eliminarse el comentario: "+id);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		else {
			int rows = commentService.deleteComment(id);
			if (rows < 1) {
				logger.error("No ha podido eliminarse el comentario: "+id);
				return new ResponseEntity<String>("No ha podido eliminarse el Comentario"+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Obteniendo el comentario: "+id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}	
		}
	}

	@PutMapping("/private/comments/{id}")
	public ResponseEntity<String> updateComment(@PathVariable int id, @RequestBody Comment comment) throws EntityNotUpdatableException {
		if (comment.getId()!=id) {
			logger.error("No ha podido actualizarse el comentario: "+id+" los ids no coinciden.");
			return new ResponseEntity<String>("Los ids no coinciden"+id, HttpStatus.BAD_REQUEST);
		}
		else if (commentService.getComment(id)==null) {
			logger.error("No ha podido actualizarse el comentario: "+id+" no se ha encontrado.");
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		else{
			int rows = commentService.updateComment(id, comment);
			if (rows < 1) {
				logger.error("No ha podido actualizarse el comentario: "+id+".");
				return new ResponseEntity<String>("No ha podido actualizarse el Comentario"+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Actualizando el comentario: "+id+".");
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
}
