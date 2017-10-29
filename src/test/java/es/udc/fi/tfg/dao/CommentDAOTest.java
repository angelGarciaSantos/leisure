package es.udc.fi.tfg.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import es.udc.fi.tfg.config.AppConfig;
import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class CommentDAOTest {
	@Autowired
	CommentDAO commentDAO;
	
	@Test
    public void getCommentsTest() {
        List<Comment> comments = commentDAO.getComments();
        assertEquals(91, comments.size());
    }
	
	@Test
    public void getCommentTest() {
        Comment comment = commentDAO.getComment(1);
        assertEquals("Ha sido un conciertazo", comment.getText());
    }
	
	@Test
    public void getCommentsFromEventTest() {
        List<Comment> comments = commentDAO.getCommentsFromEvent(1, 0, -1);
        assertEquals(2, comments.size());
    }
	
	@Test
    public void createAndDeleteCommentTest() throws EntityNotCreatableException, EntityNotRemovableException {
        Comment comment = new Comment ("Test");	
		commentDAO.addComment(comment, 1, 1);
		
		List<Comment> comments = commentDAO.getComments();
		Comment comment2 = comments.get(comments.size() -1);
        assertEquals(comment.getText(), comment2.getText());
        assertEquals(comments.size(), 5);
        
        commentDAO.deleteComment(comment2.getId());
        List<Comment> comments2 = commentDAO.getComments();
        assertEquals(comments2.size(), 4);
    }
	
	@Test
    public void updateCommentTest() throws EntityNotUpdatableException, EntityNotCreatableException, EntityNotRemovableException {
        Comment comment = new Comment ("Test");	
		commentDAO.addComment(comment, 1, 1);
		
		List<Comment> comments = commentDAO.getComments();
		Comment comment2 = comments.get(comments.size() -1);
		comment2.setText("New");
		commentDAO.updateComment(comment2.getId(), comment2);
		
		List<Comment> comments2 = commentDAO.getComments();
		Comment comment3 = comments2.get(comments2.size() -1);
		
		
        assertEquals("New", comment3.getText());
        assertEquals(comments.size(), 5);
        
        commentDAO.deleteComment(comment3.getId());
    }
}
