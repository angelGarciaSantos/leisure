package es.udc.fi.tfg.controller;

import java.util.ArrayList;
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
import es.udc.fi.tfg.dao.EventDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.LocalService;
import es.udc.fi.tfg.service.TagService;
import es.udc.fi.tfg.service.UserService;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@CrossOrigin
@RestController
public class EventRestController {
	private Logger logger = Logger.getLogger(EventRestController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private ArtistService artistService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private LocalService localService;
	
	@GetMapping("/events/{first}/{max}")
	public ResponseEntity<List<Event>> getEvents(@PathVariable int first, @PathVariable int max) {
		logger.info("Obteniendo todos los eventos.");
		return new ResponseEntity<List<Event>>(eventService.getEvents(first, max), HttpStatus.OK);
	}	
	
	@GetMapping("/events/keywords/{keywords}/{first}/{max}")
	public ResponseEntity<List<Event>> getEventsKeywords(@PathVariable String keywords, @PathVariable int first, @PathVariable int max) {
		logger.info("Obteniendo todos los eventos con las palabras clave: "+keywords);
		return new ResponseEntity<List<Event>>(eventService.getEventsKeywords(keywords, first, max), HttpStatus.OK);
	}	
	
	@GetMapping("/events/{id}")
	public ResponseEntity<Event> getEvent(@PathVariable int id) {

		Event event = eventService.getEvent(id);
		if (event == null){
			logger.error("No se ha encontrado el evento: "+id);
			return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo el evento."+id);
			return new ResponseEntity<Event>(event, HttpStatus.OK);
		}
	}	
	
	@PostMapping(value = "/admin/events/{localId}")
	public ResponseEntity<Event> createEvent(@RequestBody Event event, @PathVariable int localId) throws EntityNotCreatableException {
		logger.info("Creando el evento."+ event.getName());
		eventService.createEvent(event, localId);
		return new ResponseEntity<Event>(HttpStatus.CREATED);
	}
	
	@DeleteMapping("/admin/events/{id}")
	public ResponseEntity<String> deleteEvent(@PathVariable int id) {
		int rows;
		if(eventService.getEvent(id)==null) {
			logger.error("No se ha encontrado el evento: "+id);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		else {
			try{
				rows = eventService.deleteEvent(id);
			}
			catch(EntityNotRemovableException e){
				logger.error("No ha podido eliminarse el evento: "+id);
				return new ResponseEntity<String>(HttpStatus.LOCKED);
			}
			catch(Exception e){
				logger.error("No ha podido eliminarse el evento: "+id);
				return new ResponseEntity<String>("No ha podido eliminarse el Evento: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if (rows < 1) {
				logger.error("No ha podido eliminarse el evento: "+id);
				return new ResponseEntity<String>("No ha podido eliminarse el Evento"+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Eliminando el evento: "+id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}	
		}		
	}

	@PutMapping("/admin/events/{id}")
	public ResponseEntity<String> updateEvent(@PathVariable int id, @RequestBody Event event) throws EntityNotUpdatableException {
		if (event.getId()!=id) {
			logger.error("No ha podido actualizarse el evento: "+id+" los ids no coinciden.");
			return new ResponseEntity<String>("Los ids no coinciden"+id, HttpStatus.BAD_REQUEST);
		}
		else if(eventService.getEvent(id)==null) {
			logger.error("No ha podido actualizarse el evento: "+id+" no se ha encontrado el evento.");
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		else {
			int rows = eventService.updateEvent(id, event);
			if (rows < 1) {
				logger.error("No ha podido actualizarse el evento: "+id);
				return new ResponseEntity<String>("No ha podido actualizarse el Event"+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Actualizando el evento: "+id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@PostMapping(value = "/admin/events/artist/{eventId}/{artistId}")
	public ResponseEntity<String> addArtistToEvent(@PathVariable int eventId, @PathVariable int artistId) throws EntityNotCreatableException {
		if ((eventService.getEvent(eventId) == null ) || artistService.getArtist(artistId) == null) {
			logger.error("No ha podido añadirse el artista: "+artistId+" al evento: "+eventId+" no se han encontrado.");
			return new ResponseEntity<String>("El evento o artista indicados no existen. Evento: "+
				eventId + " Artista: " + artistId, HttpStatus.NOT_FOUND);
		}
		else {
			int rows = eventService.addArtistToEvent(eventId, artistId);
			if (rows < 1) {
				logger.error("No ha podido añadirse el artista: "+artistId+" al evento: "+eventId);
				return new ResponseEntity<String>("No ha podido añadirse el artista " + artistId
					+ " al evento " + eventId, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Añadiendo el artista: "+artistId+" al evento: "+eventId);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@DeleteMapping("/admin/events/artist/{eventId}/{artistId}")
	public ResponseEntity<String> deleteArtistFromEvent(@PathVariable int eventId, @PathVariable int artistId) throws EntityNotRemovableException {
		if ((eventService.getEvent(eventId) == null ) || artistService.getArtist(artistId) == null) {
			logger.error("No ha podido eliminarse el artista: "+artistId+" del evento: "+eventId+" no se han encontrado.");
			return new ResponseEntity<String>("El evento o artista indicados no existen. Evento: "+
				eventId + " Artista: " + artistId, HttpStatus.NOT_FOUND);
		}
		else {
			int rows = eventService.deleteArtistFromEvent(eventId, artistId);
			if (rows < 1) {
				logger.error("No ha podido eliminarse el artista: "+artistId+" del evento: "+eventId);
				return new ResponseEntity<String>("No ha podido eliminarse el artista " + artistId
					+ " del evento " + eventId, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Eliminando el artista: "+artistId+" del evento: "+eventId);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@PutMapping("/admin/events/local/{eventId}/{localId}")
	public ResponseEntity<String> modifyLocalFromEvent(@PathVariable int eventId, @PathVariable int localId) throws EntityNotUpdatableException {
		if ((eventService.getEvent(eventId) == null ) || localService.getLocal(localId) == null) {
			logger.error("No ha podido modificarse el local: "+localId+" del evento: "+eventId);
			return new ResponseEntity<String>("El evento o local indicados no existen. Evento: "+
				eventId + " Local: " + localId, HttpStatus.NOT_FOUND);
		}
		else {
			int rows = eventService.modifyLocalFromEvent(eventId, localId);
			if (rows < 1) {
				logger.error("No ha podido modificarse el local: "+localId+" del evento: "+eventId);
				return new ResponseEntity<String>("No ha podido cambiarse el local " + localId
					+ " del evento " + eventId, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Modificando el local: "+localId+" del evento: "+eventId);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@GetMapping("/event/artist/{artistId}")
	public ResponseEntity getEventsFromArtist(@PathVariable int artistId) {
		if (artistService.getArtist(artistId) == null ) {
			logger.error("No han podido obtenerse los eventos del artista: "+artistId+". No se ha encontrado");
			return new ResponseEntity<String>("El artista " + artistId + " no existe.", HttpStatus.NOT_FOUND);
		}
		else {	
			logger.info("Obteniendo los eventos del artista: "+artistId);
			return new ResponseEntity<List<Integer>>(eventService.getEventsFromArtist(artistId),HttpStatus.OK);		
		}
	}	
	
	@GetMapping("/events/artist/{artistId}")
	public ResponseEntity getNextEventsFromArtist(@PathVariable int artistId) {
		if (artistService.getArtist(artistId) == null ) {
			logger.error("No han podido obtenerse los proximos eventos del artista: "+artistId+". No se ha encontrado");
			return new ResponseEntity<String>("El artista " + artistId + " no existe.", HttpStatus.NOT_FOUND);
		}
		else {	
			logger.info("Obteniendo los proximos eventos del artista: "+artistId);
			return new ResponseEntity<List<Event>>(eventService.getNextEventsFromArtist(artistId),HttpStatus.OK);		
		}
	}	
	
	@GetMapping("/events/local/{localId}")
	public ResponseEntity getNextEventsFromLocal(@PathVariable int localId) {
		if (localService.getLocal(localId) == null ) {
			logger.error("No han podido obtenerse los proximos eventos del local: "+localId+". No se ha encontrado");
			return new ResponseEntity<String>("El local " + localId + " no existe.", HttpStatus.NOT_FOUND);
		}
		else {	
			logger.info("Obteniendo los proximos eventos del local: "+localId);
			return new ResponseEntity<List<Event>>(eventService.getNextEventsFromLocal(localId),HttpStatus.OK);		
		}
	}	
	
	@GetMapping("/events/tag/{tagId}")
	public ResponseEntity getNextEventsFromTag(@PathVariable int tagId) {
		if (tagService.getTag(tagId) == null ) {
			logger.error("No han podido obtenerse los proximos eventos del tag: "+tagId+". No se ha encontrado");
			return new ResponseEntity<String>("El tag " + tagId + " no existe.", HttpStatus.NOT_FOUND);
		}
		else {	
			logger.info("Obteniendo los proximos eventos del tag: "+tagId);
			return new ResponseEntity<List<Event>>(eventService.getNextEventsFromTag(tagId),HttpStatus.OK);		
		}
	}	
	
	@GetMapping("/events/user/{userId}/{first}/{max}")
	public ResponseEntity getEventsFromUser(@PathVariable int userId, @PathVariable int first, @PathVariable int max) {
		if (userService.getUser(userId) == null ) {
			logger.error("No han podido obtenerse los eventos del usuario: "+userId+". No se ha encontrado");
			return new ResponseEntity<String>("El usuario " + userId + " no existe.", HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo los proximos eventos del usuario: "+userId);
			return new ResponseEntity<List<Event>>(eventService.getEventsFromUser(userId, first, max),HttpStatus.OK);		
		}
	}
	
	@GetMapping(value = "/private/events/user/{userId}")
	public ResponseEntity getRecommendedEvents(@PathVariable int userId) {
		if (userService.getUser(userId) == null) {
			logger.error("No han podido obtenerse los eventos recomendados del usuario: "+userId+". No se ha encontrado");
			return new ResponseEntity<String>("El usuario indicado no existe. Usuario: " + userId, HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo los eventos recomendados del usuario: "+userId);
			List<Event> list = new ArrayList<Event>();
			list = eventService.getRecommendedEvents(userId);
			return new ResponseEntity<List<Event>>( list,HttpStatus.OK);
		}
	}
	
	@PostMapping(value = "/private/event/user/{eventId}/{userId}")
	public ResponseEntity<String> followEvent(@PathVariable int eventId, @PathVariable int userId) throws EntityNotCreatableException {
		if ((eventService.getEvent(eventId) == null ) || userService.getUser(userId) == null) {
			logger.error("No ha podido seguirse el evento "+ eventId+" con el usuario: "+userId+". No se ha encontrado");
			return new ResponseEntity<String>("El evento o usuario indicados no existen. Evento: "+
				eventId + " Usuario: " + userId, HttpStatus.NOT_FOUND);
		}
		else {
			int rows = eventService.followEvent(eventId, userId);
			if (rows < 1) {
				logger.error("No ha podido seguirse el evento "+ eventId+" con el usuario: "+userId);
				return new ResponseEntity<String>("No ha podido seguirse el evento " + eventId
					+ " con el usuario " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Siguiendo el evento "+ eventId+" con el usuario: "+userId);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@DeleteMapping(value = "/private/event/user/{eventId}/{userId}")
	public ResponseEntity<String> unfollowEvent(@PathVariable int eventId, @PathVariable int userId) throws EntityNotRemovableException {
		if ((eventService.getEvent(eventId) == null ) || userService.getUser(userId) == null) {
			logger.error("No ha podido dejar de seguirse el evento "+ eventId+" con el usuario: "+userId+". No se ha encontrado");
			return new ResponseEntity<String>("El evento o usuario indicados no existen. Evento: "+
				eventId + " Usuario: " + userId, HttpStatus.NOT_FOUND);
		}
		else {
			int rows = eventService.unfollowEvent(eventId, userId);
			if (rows < 1) {
				logger.error("No ha podido dejar de seguirse el evento "+ eventId+" con el usuario: "+userId);
				return new ResponseEntity<String>("No ha podido dejar de seguirse el evento: " + eventId
					+ " con el usuario: " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Dejando de seguir el evento "+ eventId+" con el usuario: "+userId);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@GetMapping(value = "/private/event/user/{eventId}/{userId}")
	public ResponseEntity isFollowingEvent(@PathVariable int eventId, @PathVariable int userId) {
		if ((eventService.getEvent(eventId) == null ) || userService.getUser(userId) == null) {
			logger.error("El evento "+ eventId+" o el usuario: "+userId+" no existen.");
			return new ResponseEntity<String>("El evento o usuario indicados no existen. Evento: "+
				eventId + " Usuario: " + userId, HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo informacion de seguimiendo del evento "+ eventId+" con el usuario: "+userId);
			List<Boolean> list = new ArrayList<Boolean>();
			list.add(eventService.isFollowingEvent(eventId, userId));		
			return new ResponseEntity<List<Boolean>>( list,HttpStatus.OK);
		}
	}
	
}
