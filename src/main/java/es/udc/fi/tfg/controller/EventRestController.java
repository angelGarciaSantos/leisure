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

@CrossOrigin
@RestController
public class EventRestController {
	@Autowired
	private EventDAO eventDAO;

	
	@GetMapping("/events")
	public List getEvents() {
		return eventDAO.getEvents();
	}	
	
	@GetMapping("/events/{id}")
	public Event getEvent(@PathVariable int id) {
		return eventDAO.getEvent(id);
	}	
	
	@PostMapping(value = "/events")
	public ResponseEntity createEvent(@RequestBody Event event) {

		eventDAO.addEvent(event);

		return new ResponseEntity(event, HttpStatus.OK);
	}
	
	@DeleteMapping("/events/{id}")
	public ResponseEntity deleteEvent(@PathVariable int id) {

		if (0 == eventDAO.deleteEvent(id)) {
			return new ResponseEntity("No Event found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(id, HttpStatus.OK);

	}

	@PutMapping("/events/{id}")
	public ResponseEntity updateEvent(@PathVariable int id, @RequestBody Event event) {

		int rows = eventDAO.updateEvent(id, event);
		if (0 == rows) {
			return new ResponseEntity("No Event found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(rows, HttpStatus.OK);
	}
	
	@PostMapping(value = "/events/artists/{eventId}/{artistId}")
	public ResponseEntity addArtistToEvent(@PathVariable int eventId, @PathVariable int artistId) {

		int rows = eventDAO.addArtistToEvent(eventId, artistId);

		return new ResponseEntity(rows, HttpStatus.OK);
	}
	
	@DeleteMapping("/events/artists/{eventId}/{artistId}")
	public ResponseEntity deleteArtistFromEvent(@PathVariable int eventId, @PathVariable int artistId) {

		int rows = eventDAO.deleteArtistFromEvent(eventId, artistId);

		return new ResponseEntity(rows, HttpStatus.OK);
	}
	
	@GetMapping("/events/artists/{eventId}")
	public List getArtistsFromEvent(@PathVariable int eventId) {
		return eventDAO.getArtistsFromEvent(eventId);
	}	
}
