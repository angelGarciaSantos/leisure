package es.udc.fi.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.tfg.dao.CommentDAO;
import es.udc.fi.tfg.dao.RatingDAO;
import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.model.Rating;

@Service
public class RatingService {
	@Autowired 
	private RatingDAO ratingDAO;
	
	public List<Rating> getRatings(){
		return ratingDAO.getRatings();
	}
	
	public Rating getRating (int id) {
		return ratingDAO.getRating(id);
	}
	
	public List<Rating> getRatingsFromEvent(int eventId){
		return ratingDAO.getRatingsFromEvent(eventId);
	}
	
	public void createRating(Rating rating, int eventId, int userId){
		ratingDAO.addRating(rating, eventId, userId);
	}
	
	public int deleteRating(int id){
		return ratingDAO.deleteRating(id);
	}
	
	public int updateRating(int id, Rating rating){
		return ratingDAO.updateRating(id, rating);
	}
}
