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
import es.udc.fi.tfg.dao.EventDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.LocalService;

@CrossOrigin
@RestController
public class EventRestController {

	@Autowired
	private EventService eventService;
	
	@Autowired
	private ArtistService artistService;
	
	@Autowired
	private LocalService localService;
	
	@GetMapping("/events")
	public ResponseEntity<List<Event>> getEvents() {
		return new ResponseEntity<List<Event>>(eventService.getEvents(), HttpStatus.OK);
	}	
	
	@GetMapping("/events/{id}")
	public ResponseEntity<Event> getEvent(@PathVariable int id) {

		Event event = eventService.getEvent(id);
		if (event == null){
			return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<Event>(event, HttpStatus.OK);
		}
	}	
	
	@PostMapping(value = "/events/{localId}")
	public ResponseEntity<Event> createEvent(@RequestBody Event event, @PathVariable int localId) {
		eventService.createEvent(event, localId);
		return new ResponseEntity<Event>(HttpStatus.CREATED);
	}
	
	@DeleteMapping("/events/{id}")
	public ResponseEntity<String> deleteEvent(@PathVariable int id) {
		int rows = eventService.deleteEvent(id);
		if (rows < 1) {
			return new ResponseEntity<String>("No ha podido eliminarse el Evento"+id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else{
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	@PutMapping("/events/{id}")
	public ResponseEntity<String> updateEvent(@PathVariable int id, @RequestBody Event event) {
		if (event.getId()!=id) {
			return new ResponseEntity<String>("Los ids no coinciden"+id, HttpStatus.BAD_REQUEST);
		}
		else {
			int rows = eventService.updateEvent(id, event);
			if (rows < 1) {
				return new ResponseEntity<String>("No ha podido actualizarse el Event"+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@PostMapping(value = "/events/artist/{eventId}/{artistId}")
	public ResponseEntity<String> addArtistToEvent(@PathVariable int eventId, @PathVariable int artistId) {
		if ((eventService.getEvent(eventId) == null ) || artistService.getArtist(artistId) == null) {
			return new ResponseEntity<String>("El evento o artista indicados no existen. Evento: "+
				eventId + " Artista: " + artistId, HttpStatus.NOT_FOUND);
		}
		else {
			int rows = eventService.addArtistToEvent(eventId, artistId);
			if (rows < 1) {
				return new ResponseEntity<String>("No ha podido añadirse el artista " + artistId
					+ " al evento " + eventId, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@DeleteMapping("/events/artist/{eventId}/{artistId}")
	public ResponseEntity<String> deleteArtistFromEvent(@PathVariable int eventId, @PathVariable int artistId) {
		if ((eventService.getEvent(eventId) == null ) || artistService.getArtist(artistId) == null) {
			return new ResponseEntity<String>("El evento o artista indicados no existen. Evento: "+
				eventId + " Artista: " + artistId, HttpStatus.NOT_FOUND);
		}
		else {
			int rows = eventService.deleteArtistFromEvent(eventId, artistId);
			if (rows < 1) {
				return new ResponseEntity<String>("No ha podido eliminarse el artista " + artistId
					+ " del evento " + eventId, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@PutMapping("/events/local/{eventId}/{localId}")
	public ResponseEntity<String> modifyLocalFromEvent(@PathVariable int eventId, @PathVariable int localId) {
		if ((eventService.getEvent(eventId) == null ) || localService.getLocal(localId) == null) {
			return new ResponseEntity<String>("El evento o local indicados no existen. Evento: "+
				eventId + " Local: " + localId, HttpStatus.NOT_FOUND);
		}
		else {
			int rows = eventService.modifyLocalFromEvent(eventId, localId);
			if (rows < 1) {
				return new ResponseEntity<String>("No ha podido cambiarse el local " + localId
					+ " del evento " + eventId, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@GetMapping("/event/artist/{artistId}")
	public ResponseEntity getEventsFromArtist(@PathVariable int artistId) {
		if (artistService.getArtist(artistId) == null ) {
			return new ResponseEntity<String>("El artista " + artistId + " no existe.", HttpStatus.NOT_FOUND);
		}
		else {	
			return new ResponseEntity<List<Integer>>(eventService.getEventsFromArtist(artistId),HttpStatus.OK);		
		}
	}	
}
