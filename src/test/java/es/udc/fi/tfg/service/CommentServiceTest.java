package es.udc.fi.tfg.service;

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
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class CommentServiceTest {

	@Autowired
	CommentService commentService;
	
	@Test
    public void getCommentsTest() {
        List<Comment> comments = commentService.getComments();
        assertEquals(4, comments.size());
    }
	
	@Test
    public void getCommentTest() {
        Comment comment = commentService.getComment(1);
        assertEquals("Ha sido un conciertazo", comment.getText());
    }
	
	@Test
    public void getCommentsFromEventTest() {
        List<Comment> comments = commentService.getCommentsFromEvent(1, 0, -1);
        assertEquals(135, comments.size());
    }
	
	@Test
    public void createAndDeleteCommentTest() throws EntityNotCreatableException, EntityNotRemovableException {
        Comment comment = new Comment ("Test");	
		commentService.createComment(comment, 1, 1);
		
		List<Comment> comments = commentService.getComments();
		Comment comment2 = comments.get(comments.size() -1);
        assertEquals(comment.getText(), comment2.getText());
        
        commentService.deleteComment(comment2.getId());
        List<Comment> comments2 = commentService.getComments();
    }
	
	@Test
    public void updateCommentTest() throws EntityNotUpdatableException, EntityNotCreatableException, EntityNotRemovableException {
        Comment comment = new Comment ("Test");	
		commentService.createComment(comment, 1, 1);
		
		List<Comment> comments = commentService.getComments();
		Comment comment2 = comments.get(comments.size() -1);
		comment2.setText("New");
		commentService.updateComment(comment2.getId(), comment2);
		
		List<Comment> comments2 = commentService.getComments();
		Comment comment3 = comments2.get(comments2.size() -1);
		
		
        assertEquals("New", comment3.getText());
        assertEquals(comments.size(), 5);
        
        commentService.deleteComment(comment3.getId());
    }
	
}
