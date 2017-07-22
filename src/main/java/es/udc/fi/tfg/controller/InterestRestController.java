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
import es.udc.fi.tfg.model.Interest;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.Rating;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.CommentService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.InterestService;
import es.udc.fi.tfg.service.RatingService;
import es.udc.fi.tfg.service.TagService;
import es.udc.fi.tfg.service.UserService;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@CrossOrigin
@RestController
public class InterestRestController {
	private Logger logger = Logger.getLogger(InterestRestController.class);

	@Autowired
	private InterestService interestService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private ArtistService artistService;
	
	@GetMapping("/private/interests")
	public ResponseEntity<List<Interest>> getInterests() {
		logger.info("Obteniendo todos los intereses.");
		return new ResponseEntity<List<Interest>> (interestService.getInterests(), HttpStatus.OK);
	}	
	
	@GetMapping("/private/interests/{id}")
	public ResponseEntity<Interest> getInterest(@PathVariable int id) {
		Interest interest = interestService.getInterest(id);
		if (interest == null){
			logger.error("No ha podido encontrarse el interes: "+id);
			return new ResponseEntity<Interest>(HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo el interes: "+id);
			return new ResponseEntity<Interest>(interest, HttpStatus.OK);
		}	
	}	
	
	@GetMapping("/private/interests/tag/user/{tagId}/{userId}")
	public ResponseEntity existsInterest(@PathVariable int tagId, @PathVariable int userId) {
		if ((tagService.getTag(tagId) == null ) || userService.getUser(userId) == null) {
			
			logger.error("El Tag o Usuario indicados no existen. Tag: "+
					tagId + " Usuario: " + userId);
			return new ResponseEntity<String>("El Tag o Usuario indicados no existen. Tag: "+
				tagId + " Usuario: " + userId, HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo informacion sobre el interes con el tag: "+tagId+" y el usuario: "+userId);
			return new ResponseEntity<Integer>(interestService.existsInterest(tagId, userId), HttpStatus.OK);
		}	
	}	
	
	//TODO: devuelve 1 de cada 2 veces el not_found sin tener sentido, por eso está así
	@GetMapping("/private/interests/user/{userId}")
	public ResponseEntity<List<Interest>> getInterestsFromUser(@PathVariable int userId) {
		if (userService.getUser(userId) == null){
			logger.error("No ha podido encontrarse el usuario: "+userId);
			return new ResponseEntity<List<Interest>>(HttpStatus.NOT_FOUND);
		}
		else {	
			logger.info("Obteniendo los intereses del usuario: "+userId);
			return new ResponseEntity<List<Interest>> (interestService.getInterestsFromUser(userId), HttpStatus.OK);
		}
	}	
	
	//TODO: ver como comprobar que se crea correctamente desde el DAO
		@PostMapping(value = "/private/interests/event/{eventId}/{userId}")
		public ResponseEntity<Rating> createInterestByEvent(@RequestBody Interest interest, @PathVariable int eventId, @PathVariable int userId) throws EntityNotUpdatableException, EntityNotCreatableException {
			if (eventService.getEvent(eventId) == null || userService.getUser(userId) == null){
				logger.error("El Evento o Usuario indicados no existen. Evento: "+
						eventId + " Usuario: " + userId);
				return new ResponseEntity<Rating>(HttpStatus.NOT_FOUND);
			}
			else {
				logger.info("Creando interes para el evento: "+ eventId +" y el usuario "+userId);
				interestService.createInterestByEvent(interest, eventId, userId);
				return new ResponseEntity<Rating>(HttpStatus.CREATED);				
			}
		}
	
	//TODO: ver como comprobar que se crea correctamente desde el DAO
	@PostMapping(value = "/private/interests/{tagId}/{userId}")
	public ResponseEntity<Rating> createInterest(@RequestBody Interest interest, @PathVariable int tagId, @PathVariable int userId) throws EntityNotCreatableException {
		if (tagService.getTag(tagId) == null || userService.getUser(userId) == null){
			logger.error("El Tag o Usuario indicados no existen. Tag: "+
					tagId + " Usuario: " + userId);
			return new ResponseEntity<Rating>(HttpStatus.NOT_FOUND);
		}	
		else {
			logger.info("Creando interes con el tag: "+tagId+" y el usuario: "+userId);
			interestService.createInterest(interest, tagId, userId);
			return new ResponseEntity<Rating>(HttpStatus.CREATED);			
		}
	}
		
	@DeleteMapping("/private/interests/{id}")
	public ResponseEntity<String> deleteInterest(@PathVariable int id) throws EntityNotRemovableException {
		if (interestService.getInterest(id) == null){
			logger.error("El interes indicado no existe: "+id);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		else {
			int rows = interestService.deleteInterest(id);
			if (rows < 1) {
				logger.error("No ha podido eliminarse el interes: "+id);
				return new ResponseEntity<String>("No ha podido eliminarse el interés "+ id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.error("Eliminando el interes: "+id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}			
		}
	}

	@PutMapping("/private/interests/{id}")
	public ResponseEntity<String> updateInterest(@PathVariable int id, @RequestBody Interest interest) throws EntityNotUpdatableException {
		if (interest.getId()!=id) {
			logger.error("Los ids no coinciden: "+id);
			return new ResponseEntity<String>("Los ids no coinciden "+id, HttpStatus.BAD_REQUEST);
		}
		else if (interestService.getInterest(id) == null){
			logger.error("El interes indicado no existe: "+id);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		else{
			int rows = interestService.updateInterest(id, interest);
			if (rows < 1) {
				logger.error("No ha podido actualizarse el interes: "+id);
				return new ResponseEntity<String>("No ha podido actualizarse el interés " + id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Actualizando el interes: "+id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
}
