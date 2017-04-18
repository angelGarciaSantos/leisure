package es.udc.fi.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.tfg.dao.CommentDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Comment;

@Service
public class CommentService {
	@Autowired 
	private CommentDAO commentDAO;
	
	public List<Comment> getComments(){
		return commentDAO.getComments();
	}
	
	public Comment getComment (int id) {
		return commentDAO.getComment(id);
	}
	
	public List<Comment> getCommentsFromEvent(int eventId){
		return commentDAO.getCommentsFromEvent(eventId);
	}
	
	public void createComment(Comment comment, int eventId, int userId){
		commentDAO.addComment(comment, eventId, userId);
	}
	
	public int deleteComment(int id){
		return commentDAO.deleteComment(id);
	}
	
	public int updateComment(int id, Comment comment){
		return commentDAO.updateComment(id, comment);
	}
}
