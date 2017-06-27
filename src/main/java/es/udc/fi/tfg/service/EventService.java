package es.udc.fi.tfg.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
	
	@Autowired
	private ArtistService artistService;
	
	public List<Event> getEvents() {
		return eventDAO.getEvents();
	}
	
	public List<Event> getEventsKeywords(String keywords) {
		return eventDAO.getEventsKeywords(keywords);
	}
	
	public Event getEvent(int id){
		return eventDAO.getEvent(id);
	}
	
	public List<Event> getRecommendedEvents(int userId){
		
		Integer[][] artistsPoints;
		artistsPoints = artistService.getArtistsPoints(userId);
		
		for(int i=0;i<artistsPoints.length;i++){
			//Si el artista esta siendo seguido por el usuario, incrementar 10 los puntos de ese artista.
			if(artistService.isFollowingArtist(artistsPoints[i][0], userId)){
				artistsPoints[i][1] += 10;		
			}
		}
		
		Arrays.sort(artistsPoints, new Comparator<Integer[]>() {	    
 		    public int compare(Integer[] s1, Integer[] s2) {
 		        Integer t1 = s1[1];
 		        Integer t2 = s2[1];
 		        return t2.compareTo(t1);
 		    }
	 	});
		
		List<Event> allEvents = new ArrayList<Event>();
		allEvents = this.getEvents();
		Integer[][] eventsPoints;
		eventsPoints = new Integer[allEvents.size()][];
		int sum = 0;
		int j = 0;
		for(Event event : allEvents) {
			//Buscar los artistas del evento en Event_Artist.
			//Para cada uno ver si hay alguno en artistsPoints, y si lo hay sumar esos points a eventsPoints.
			sum = 0;
			List<Artist> artistsFromEvent = artistService.getArtistsFromEvent(event.getId());
			for (Artist artist : artistsFromEvent) {
				for (int i = 0;i<artistsPoints.length;i++){
					if(artistsPoints[i][0] == artist.getId()){
						sum += artistsPoints[i][1];
					}
				}
			}
			eventsPoints[j] = new Integer[2];
			eventsPoints[j][0] = event.getId();
			eventsPoints[j][1] = sum;
			j++;
		}
		
		Arrays.sort(eventsPoints, new Comparator<Integer[]>() {	    
 		    public int compare(Integer[] s1, Integer[] s2) {
 		        Integer t1 = s1[1];
 		        Integer t2 = s2[1];
 		        return t2.compareTo(t1);
 		    }
	 	});
		
		List<Event> recommendedEvents = new ArrayList<Event>();

		for (int i=0;i<eventsPoints.length;i++){
			recommendedEvents.add(this.getEvent(eventsPoints[i][0]));   
		}
		
		//TODO: aqui decidimos la cantidad de eventos recomendados a devolver:
		List<Event> topRecommendedEvents;
		if (recommendedEvents.size() > 4) {
			topRecommendedEvents = recommendedEvents.subList(0, 5);
		}
		else {
			topRecommendedEvents = recommendedEvents;
		}
		
		return topRecommendedEvents;
	}
	
	public void createEvent(Event event, int localId){
		eventDAO.addEvent(event, localId);
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
	
	public int modifyLocalFromEvent (int eventId, int localId){
		return eventDAO.modifyLocalFromEvent(eventId, localId);
	}
	
	public List<Integer> getEventsFromArtist(int artistId) {
		return eventDAO.getEventsFromArtist(artistId);
	}
}
