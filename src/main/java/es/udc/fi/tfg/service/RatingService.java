package es.udc.fi.tfg.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.tfg.dao.CommentDAO;
import es.udc.fi.tfg.dao.RatingDAO;
import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.model.Rating;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@Service
public class RatingService {
	@Autowired 
	private RatingDAO ratingDAO;
	
	@Autowired
	private EventService eventService;
	
	public List<Rating> getRatings(){
		return ratingDAO.getRatings();
	}
	
	public Rating getRating (int id) {
		return ratingDAO.getRating(id);
	}
	
	public List<Rating> getRatingsFromEvent(int eventId){
		return ratingDAO.getRatingsFromEvent(eventId);
	}
	
	public List<Double> getRatingFromEvent(int eventId){
		List<Rating> ratings = ratingDAO.getRatingsFromEvent(eventId);	
		List<Double> a = new ArrayList<Double>();
		int total = ratings.size();
		if (total == 0){
			a.add((double) -1);
		}
		else {
			double sum = 0;
			for (Rating rating : ratings) {
			    sum += rating.getRating();
			}
			
			double result = sum / total;
			
			a.add(result);
		}	
		
		return a;
	}
	
	public List<Double> getRatingFromArtist(int artistId){
		List<Integer> eventIds = eventService.getEventsFromArtist(artistId);
		
		int total = eventIds.size();
		List<Double> a = new ArrayList<Double>();
		if(total == 0) {
			a.add((double) -1);
		}
		else {
			double sumArtist = 0;
			double actual = 0;
			for (Integer eventId : eventIds) {
				actual = this.getRatingFromEvent(eventId).get(0);
				if (actual == -1) {
					total--;
				}	
				else {
					sumArtist += actual;
				}
				
			}
			double result;
			if (total > 0) {
				result = sumArtist / total;	
			}
			else {
				result = -1;
			}
				
			
			a.add(result);
		}
		
		return a;
	}
	
	public boolean existsRating(int eventId, int userId){
		return ratingDAO.existsRating(eventId, userId);
	}
	
	public void createRating(Rating rating, int eventId, int userId) throws EntityNotCreatableException, EntityNotUpdatableException{
		if(!this.existsRating(eventId, userId)){
			ratingDAO.addRating(rating, eventId, userId);
		}
		else {
			ratingDAO.updateRating(rating, eventId, userId);
		}		
	}
	
	public int deleteRating(int id) throws EntityNotRemovableException{
		return ratingDAO.deleteRating(id);
	}
	
	public int updateRating(Rating rating, int eventId, int userId) throws EntityNotUpdatableException{
		return ratingDAO.updateRating(rating, eventId, userId);
	}
}
