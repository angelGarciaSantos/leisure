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
import es.udc.fi.tfg.dao.CommentDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.Rating;
import es.udc.fi.tfg.service.CommentService;
import es.udc.fi.tfg.service.EventService;

@CrossOrigin
@RestController
public class CommentRestController {	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private EventService eventService;
	
	@GetMapping("/comments")
	public ResponseEntity<List<Comment>> getComments() {
		return new ResponseEntity<List<Comment>> (commentService.getComments(), HttpStatus.OK);
	}	
		
	@GetMapping("/comments/{id}")
	public ResponseEntity<Comment> getComment(@PathVariable int id) {
		Comment comment = commentService.getComment(id);
		if (comment == null){
			return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<Comment>(comment, HttpStatus.OK);
		}		
	}	
	
	//TODO: devuelve 1 de cada 2 veces el not_found sin tener sentido, por eso está así
	@GetMapping("/comments/event/{eventId}")
	public ResponseEntity<List<Comment>> getCommentsFromEvent(@PathVariable int eventId) {	
//		if (eventService.getEvent(eventId) == null){
//			return new ResponseEntity<List<Comment>>(HttpStatus.NOT_FOUND);
//		}
//		else {	
			return new ResponseEntity<List<Comment>> (commentService.getCommentsFromEvent(eventId), HttpStatus.OK);
//		}
	}	
	
	//TODO: ver como comprobar que se crea correctamente desde el DAO
	@PostMapping(value = "/comments/{eventId}/{userId}")
	public ResponseEntity<Comment> createComment(@RequestBody Comment comment, @PathVariable int eventId, @PathVariable int userId) {
		
		commentService.createComment(comment, eventId, userId);
		return new ResponseEntity<Comment>(HttpStatus.CREATED);
	}
	
	@DeleteMapping("/comments/{id}")
	public ResponseEntity<String> deleteComment(@PathVariable int id) {
		int rows = commentService.deleteComment(id);
		if (rows < 1) {
			return new ResponseEntity<String>("No ha podido eliminarse el Comentario"+id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else{
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	@PutMapping("/comments/{id}")
	public ResponseEntity<String> updateComment(@PathVariable int id, @RequestBody Comment comment) {
		if (comment.getId()!=id) {
			return new ResponseEntity<String>("Los ids no coinciden"+id, HttpStatus.BAD_REQUEST);
		}
		else{
			int rows = commentService.updateComment(id, comment);
			if (rows < 1) {
				return new ResponseEntity<String>("No ha podido actualizarse el Comentario"+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
}
