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

import es.udc.fi.tfg.dao.CommentDAO;
import es.udc.fi.tfg.dao.RatingDAO;
import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.Rating;

@CrossOrigin
@RestController
public class RatingRestController {
	@Autowired
	private RatingDAO ratingDAO;

	
	@GetMapping("/ratings")
	public List getRatings() {
		return ratingDAO.getRatings();
	}	
	
	@GetMapping("/ratings/{id}")
	public Rating getRating(@PathVariable int id) {
		return ratingDAO.getRating(id);
	}	
	
	@GetMapping("/ratings/event/{id}")
	public List getRatingsFromEvent(@PathVariable int id) {
		return ratingDAO.getRatingsFromEvent(id);
	}	
	
	@PostMapping(value = "/ratings")
	public ResponseEntity createRating(@RequestBody Rating rating) {

		ratingDAO.addRating(rating);

		return new ResponseEntity(rating, HttpStatus.OK);
	}
	
	@DeleteMapping("/ratings/{id}")
	public ResponseEntity deleteRating(@PathVariable int id) {

		if (0 == ratingDAO.deleteRating(id)) {
			return new ResponseEntity("No Rating found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(id, HttpStatus.OK);

	}

	@PutMapping("/ratings/{id}")
	public ResponseEntity updateRating(@PathVariable int id, @RequestBody Rating rating) {

		int rows = ratingDAO.updateRating(id, rating);
		if (0 == rows) {
			return new ResponseEntity("No Rating found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(rows, HttpStatus.OK);
	}
}
