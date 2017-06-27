package es.udc.fi.tfg.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.tfg.dao.CommentDAO;
import es.udc.fi.tfg.dao.InterestDAO;
import es.udc.fi.tfg.dao.RatingDAO;
import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.model.Interest;
import es.udc.fi.tfg.model.Rating;
import es.udc.fi.tfg.model.Tag;

@Service
public class InterestService {
	@Autowired 
	private InterestDAO interestDAO;
	
	@Autowired 
	private TagService tagService;
	
	public List<Interest> getInterests(){
		return interestDAO.getInterests();
	}
	
	public Interest getInterest (int id) {
		return interestDAO.getInterest(id);
	}
	
	public List<Interest> getInterestsFromUser(int userId){
		return interestDAO.getInterestsFromUser(userId);
	}
	
	public void createInterest(Interest interest, int tagId, int userId){
		interest.setPoints(1);
		interestDAO.addInterest(interest, tagId, userId);
	}
	
	public int existsInterest (int tagId, int userId){
		return interestDAO.existsInterest(tagId, userId);
	}
	
	public void createInterestByEvent(Interest interest, int eventId, int userId){
		List<Tag> tags = tagService.getTagsFromEvent(eventId);
		int points = 0;
		for (Tag tag : tags){
			points = 0;
			if (this.existsInterest(tag.getId(), userId) != -1){
				points = interestDAO.getInterest(this.existsInterest(tag.getId(),userId)).getPoints();
				interest.setPoints(points + 1);
				interestDAO.updateInterest(this.existsInterest(tag.getId(), userId), interest);
			}
			else {
				interest.setPoints(1);
				interestDAO.addInterest(interest, tag.getId(), userId);
			}
			
		}
	}
	
	public int deleteInterest(int id){
		return interestDAO.deleteInterest(id);
	}
	
	public int updateInterest(int id, Interest interest){
		return interestDAO.updateInterest(id, interest);
	}
}
