package es.udc.fi.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import es.udc.fi.tfg.dao.EventDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Event;

@Service
public class EventService {

	@Autowired
	private EventDAO eventDAO;
	
	public List<Event> getEvents() {
		return eventDAO.getEvents();
	}
	
	public Event getEvent(int id){
		return eventDAO.getEvent(id);
	}
	
	public void createEvent(Event event){
		eventDAO.addEvent(event);
	}
	
	public int deleteEvent(int id){
		return eventDAO.deleteEvent(id);
	}
	
	public int updateEvent(int id, Event event){
		return eventDAO.updateEvent(id, event);
	}
	
	public int addArtistToEvent(int eventId, int artistId){
		return eventDAO.addArtistToEvent(eventId, artistId);
	}
	
	public int deleteArtistFromEvent (int eventId, int artistId){
		return eventDAO.deleteArtistFromEvent(eventId, artistId);
	}
	
	public List<Integer> getEventsFromArtist(int artistId) {
		return eventDAO.getEventsFromArtist(artistId);
	}
}
