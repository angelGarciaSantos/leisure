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

import es.udc.fi.tfg.dao.CommentDAO;
import es.udc.fi.tfg.dao.RatingDAO;
import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.Rating;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.CommentService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.LocalService;
import es.udc.fi.tfg.service.RatingService;
import es.udc.fi.tfg.service.UserService;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@CrossOrigin
@RestController
public class RatingRestController {
	private Logger logger = Logger.getLogger(RatingRestController.class);
	@Autowired
	private RatingService ratingService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private ArtistService artistService;
	
	@Autowired
	private LocalService localService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/ratings")
	public ResponseEntity<List<Rating>> getRatings() {
		logger.info("Obteniendo todas las valoraciones.");
		return new ResponseEntity<List<Rating>> (ratingService.getRatings(), HttpStatus.OK);
	}	
	
	@GetMapping("/ratings/{id}")
	public ResponseEntity<Rating> getRating(@PathVariable int id) {
		Rating rating = ratingService.getRating(id);
		if (rating == null){
			logger.error("No se ha encontrado la valoracion: "+id);
			return new ResponseEntity<Rating>(HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo valoracion: "+id);
			return new ResponseEntity<Rating>(rating, HttpStatus.OK);
		}	
	}	
	
	@GetMapping("/ratings/event/{eventId}")
	public ResponseEntity<List<Rating>> getRatingsFromEvent(@PathVariable int eventId) {
		if (eventService.getEvent(eventId) == null){
			logger.error("No se ha encontrado el evento: "+eventId);
			return new ResponseEntity<List<Rating>>(HttpStatus.NOT_FOUND);
		}
		else {	
			logger.info("Obteniendo las valoraciones del evento: "+eventId);
			return new ResponseEntity<List<Rating>> (ratingService.getRatingsFromEvent(eventId), HttpStatus.OK);
		}
	}	
	
	@GetMapping("/rating/event/{eventId}")
	public ResponseEntity<List<Double>> getRatingFromEvent(@PathVariable int eventId) {
		if (eventService.getEvent(eventId) == null){
			logger.error("No se ha encontrado el evento: "+eventId);
			return new ResponseEntity<List<Double>>(HttpStatus.NOT_FOUND);
		}
		else {	
			logger.info("Obteniendo la valoracion del evento: "+eventId);
			return new ResponseEntity<List<Double>> (ratingService.getRatingFromEvent(eventId), HttpStatus.OK);
		}
	}	
	
	@GetMapping("/rating/artist/{artistId}")
	public ResponseEntity<List<Double>> getRatingFromArtist(@PathVariable int artistId) {
		if (artistService.getArtist(artistId) == null){
			logger.error("No se ha encontrado el artista: "+artistId);
			return new ResponseEntity<List<Double>>(HttpStatus.NOT_FOUND);
		}
		else {	
			logger.info("Obteniendo la valoracion del artista: "+artistId);
			return new ResponseEntity<List<Double>> (ratingService.getRatingFromArtist(artistId), HttpStatus.OK);
		}
	}	
	
	@GetMapping("/rating/local/{localId}")
	public ResponseEntity<List<Double>> getRatingFromLocal(@PathVariable int localId) {
		if (localService.getLocal(localId) == null){
			logger.error("No se ha encontrado el local: "+localId);
			return new ResponseEntity<List<Double>>(HttpStatus.NOT_FOUND);
		}
		else {	
			logger.info("Obteniendo la valoracion del local: "+localId);
			return new ResponseEntity<List<Double>> (ratingService.getRatingFromLocal(localId), HttpStatus.OK);
		}
	}		
	
	//TODO: ver como comprobar que se crea correctamente desde el DAO
	@PostMapping(value = "/private/ratings/{eventId}/{userId}")
	public ResponseEntity<Rating> createRating(@RequestBody Rating rating, @PathVariable int eventId, @PathVariable int userId) throws EntityNotCreatableException, EntityNotUpdatableException {
		if (eventService.getEvent(eventId)==null || userService.getUser(userId)==null){
			logger.error("No se han encontrado el evento: "+eventId+" o el usuario: "+userId);
			return new ResponseEntity<Rating>(HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Creando valoracion para el evento: "+eventId+" con el usuario: "+userId);
			ratingService.createRating(rating, eventId, userId);
			return new ResponseEntity<Rating>(HttpStatus.CREATED);			
		}
	}
		
	@DeleteMapping("/private/ratings/{id}")
	public ResponseEntity<String> deleteRating(@PathVariable int id) throws EntityNotRemovableException {
		if (ratingService.getRating(id)==null){
			logger.error("No se ha encontrado la valoracion: "+id);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		else {
			int rows = ratingService.deleteRating(id);
			if (rows < 1) {
				logger.error("No ha podido eliminarse la valoracion: "+id);
				return new ResponseEntity<String>("No ha podido eliminarse la valoración "+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Eliminando la valoracion: "+id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}			
		}
	}
}
