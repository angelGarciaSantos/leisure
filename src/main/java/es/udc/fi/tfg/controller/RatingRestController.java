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
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.CommentService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.RatingService;

@CrossOrigin
@RestController
public class RatingRestController {

	@Autowired
	private RatingService ratingService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private ArtistService artistService;
	
	@GetMapping("/ratings")
	public ResponseEntity<List<Rating>> getRatings() {
		return new ResponseEntity<List<Rating>> (ratingService.getRatings(), HttpStatus.OK);
	}	
	
	@GetMapping("/ratings/{id}")
	public ResponseEntity<Rating> getRating(@PathVariable int id) {
		Rating rating = ratingService.getRating(id);
		if (rating == null){
			return new ResponseEntity<Rating>(HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<Rating>(rating, HttpStatus.OK);
		}	
	}	
	
	@GetMapping("/ratings/event/{eventId}")
	public ResponseEntity<List<Rating>> getRatingsFromEvent(@PathVariable int eventId) {
		if (eventService.getEvent(eventId) == null){
			return new ResponseEntity<List<Rating>>(HttpStatus.NOT_FOUND);
		}
		else {	
			return new ResponseEntity<List<Rating>> (ratingService.getRatingsFromEvent(eventId), HttpStatus.OK);
		}
	}	
	
	@GetMapping("/rating/event/{eventId}")
	public ResponseEntity<List<Double>> getRatingFromEvent(@PathVariable int eventId) {
		if (eventService.getEvent(eventId) == null){
			return new ResponseEntity<List<Double>>(HttpStatus.NOT_FOUND);
		}
		else {	
			return new ResponseEntity<List<Double>> (ratingService.getRatingFromEvent(eventId), HttpStatus.OK);
		}
	}	
	
	@GetMapping("/rating/artist/{artistId}")
	public ResponseEntity<List<Double>> getRatingFromArtist(@PathVariable int artistId) {
		if (artistService.getArtist(artistId) == null){
			return new ResponseEntity<List<Double>>(HttpStatus.NOT_FOUND);
		}
		else {	
			return new ResponseEntity<List<Double>> (ratingService.getRatingFromArtist(artistId), HttpStatus.OK);
		}
	}	
	
	//TODO: ver como comprobar que se crea correctamente desde el DAO
	@PostMapping(value = "/ratings/{eventId}/{userId}")
	public ResponseEntity<Rating> createRating(@RequestBody Rating rating, @PathVariable int eventId, @PathVariable int userId) {
		
		ratingService.createRating(rating, eventId, userId);
		return new ResponseEntity<Rating>(HttpStatus.CREATED);
	}
		
	@DeleteMapping("/ratings/{id}")
	public ResponseEntity<String> deleteRating(@PathVariable int id) {
		int rows = ratingService.deleteRating(id);
		if (rows < 1) {
			return new ResponseEntity<String>("No ha podido eliminarse la valoración "+id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else{
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	@PutMapping("/ratings/{id}")
	public ResponseEntity<String> updateRating(@PathVariable int id, @RequestBody Rating rating) {
		if (rating.getId()!=id) {
			return new ResponseEntity<String>("Los ids no coinciden "+id, HttpStatus.BAD_REQUEST);
		}
		else{
			int rows = ratingService.updateRating(id, rating);
			if (rows < 1) {
				return new ResponseEntity<String>("No ha podido actualizarse la valoración "+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
}
