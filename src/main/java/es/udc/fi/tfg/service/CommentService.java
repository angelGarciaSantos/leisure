package es.udc.fi.tfg.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.tfg.dao.CommentDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.model.Event;

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
		List<Comment> comments = commentDAO.getCommentsFromEvent(eventId);
		
		Collections.sort(comments, new Comparator<Comment>() {
			  public int compare(Comment o1, Comment o2) {
			      if (o1.getDate() == null || o2.getDate() == null)
			        return 0;
			      return o2.getDate().compareTo(o1.getDate());
			  }
			});
		
		return comments;
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
