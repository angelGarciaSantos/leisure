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
import es.udc.fi.tfg.model.Interest;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.Rating;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.CommentService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.InterestService;
import es.udc.fi.tfg.service.RatingService;

@CrossOrigin
@RestController
public class InterestRestController {

	@Autowired
	private InterestService interestService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private ArtistService artistService;
	
	@GetMapping("/interests")
	public ResponseEntity<List<Interest>> getInterests() {
		return new ResponseEntity<List<Interest>> (interestService.getInterests(), HttpStatus.OK);
	}	
	
	@GetMapping("/interests/{id}")
	public ResponseEntity<Interest> getInterest(@PathVariable int id) {
		Interest interest = interestService.getInterest(id);
		if (interest == null){
			return new ResponseEntity<Interest>(HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<Interest>(interest, HttpStatus.OK);
		}	
	}	
	
	//TODO: devuelve 1 de cada 2 veces el not_found sin tener sentido, por eso está así
	@GetMapping("/interests/user/{userId}")
	public ResponseEntity<List<Interest>> getInterestsFromUser(@PathVariable int userId) {
//		if (eventService.getEvent(eventId) == null){
//			return new ResponseEntity<List<Rating>>(HttpStatus.NOT_FOUND);
//		}
//		else {	
			return new ResponseEntity<List<Interest>> (interestService.getInterestsFromUser(userId), HttpStatus.OK);
//		}
	}	
	
	
	//TODO: ver como comprobar que se crea correctamente desde el DAO
	@PostMapping(value = "/interests/{tagId}/{userId}")
	public ResponseEntity<Rating> createInterest(@RequestBody Interest interest, @PathVariable int tagId, @PathVariable int userId) {
		
		interestService.createInterest(interest, tagId, userId);
		return new ResponseEntity<Rating>(HttpStatus.CREATED);
	}
		
	@DeleteMapping("/interests/{id}")
	public ResponseEntity<String> deleteInterest(@PathVariable int id) {
		int rows = interestService.deleteInterest(id);
		if (rows < 1) {
			return new ResponseEntity<String>("No ha podido eliminarse el interés "+ id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else{
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	@PutMapping("/interests/{id}")
	public ResponseEntity<String> updateInterest(@PathVariable int id, @RequestBody Interest interest) {
		if (interest.getId()!=id) {
			return new ResponseEntity<String>("Los ids no coinciden "+id, HttpStatus.BAD_REQUEST);
		}
		else{
			int rows = interestService.updateInterest(id, interest);
			if (rows < 1) {
				return new ResponseEntity<String>("No ha podido actualizarse el interés " + id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
}
