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

@CrossOrigin
@RestController
public class CommentRestController {
	@Autowired
	private CommentDAO commentDAO;

	
	@GetMapping("/comments")
	public List getComments() {
		return commentDAO.getComments();
	}	
	
	@GetMapping("/comments/{id}")
	public Comment getComment(@PathVariable int id) {
		return commentDAO.getComment(id);
	}	
	
	@GetMapping("/comments/event/{id}")
	public List getCommentsFromEvent(@PathVariable int id) {
		return commentDAO.getCommentsFromEvent(id);
	}	
	
	@PostMapping(value = "/comments/{eventId}/{userId}")
	public ResponseEntity createComment(@RequestBody Comment comment, @PathVariable int eventId, @PathVariable int userId) {

		commentDAO.addComment(comment, eventId, userId);

		return new ResponseEntity(comment, HttpStatus.OK);
	}
	
	@DeleteMapping("/comments/{id}")
	public ResponseEntity deleteComment(@PathVariable int id) {

		if (0 == commentDAO.deleteComment(id)) {
			return new ResponseEntity("No Comment found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(id, HttpStatus.OK);

	}

	@PutMapping("/comments/{id}")
	public ResponseEntity updateComment(@PathVariable int id, @RequestBody Comment comment) {

		int rows = commentDAO.updateComment(id, comment);
		if (0 == rows) {
			return new ResponseEntity("No Comment found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(rows, HttpStatus.OK);
	}
}
