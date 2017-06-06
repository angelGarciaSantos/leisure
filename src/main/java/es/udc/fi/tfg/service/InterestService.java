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

@Service
public class InterestService {
	@Autowired 
	private InterestDAO interestDAO;
	
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
		interestDAO.addInterest(interest, tagId, userId);
	}
	
	public int deleteInterest(int id){
		return interestDAO.deleteInterest(id);
	}
	
	public int updateInterest(int id, Interest interest){
		return interestDAO.updateInterest(id, interest);
	}
}
