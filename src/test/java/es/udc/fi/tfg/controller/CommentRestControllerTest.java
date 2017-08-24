package es.udc.fi.tfg.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.fi.tfg.config.AppConfig;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Comment;
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.CommentService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.UserService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class CommentRestControllerTest {
	private MockMvc mockMvc;
	
	@Mock
    private ArtistService artistService;
    
    @Mock
    private EventService eventService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentRestController commentRestController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(commentRestController)
                //.addFilters(new CORSFilter())
                .build();
    }
    
// =========================================== Get All Comments ==========================================
    
    @Test
    public void test_get_all_comments_success() throws Exception {
        List<Comment> comments = Arrays.asList(
                new Comment(1, "Comentario uno"),
                new Comment(2, "Comentario dos"));

        when(commentService.getComments()).thenReturn(comments);

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].text", is("Comentario uno")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].text", is("Comentario dos")));

        verify(commentService, times(1)).getComments();
        verifyNoMoreInteractions(commentService);
    }
    
 // =========================================== Get Comment By ID =========================================

    @Test
    public void test_get_comment_by_id_success() throws Exception {
        Comment comment = new Comment(1, "Comentario uno");

        when(commentService.getComment(1)).thenReturn(comment);

        mockMvc.perform(get("/comments/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Comentario uno")));

        verify(commentService, times(1)).getComment(1);
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void test_get_by_id_fail_404_not_found() throws Exception {
        when(commentService.getComment(1)).thenReturn(null);

        mockMvc.perform(get("/comments/{id}", 1))
                .andExpect(status().isNotFound());

        verify(commentService, times(1)).getComment(1);
        verifyNoMoreInteractions(commentService);
    }
    
// =========================================== Get Comments From Event  ==========================================
    
    
    @Test
    public void test_get_comments_event_success() throws Exception {
        List<Comment> comments = Arrays.asList(
                new Comment(1, "Comentario uno"),
                new Comment(2, "Comentario dos"));
        
        Event event = new Event(1, "aaa", "aaa", new Date(), new Date());

        when(commentService.getCommentsFromEvent(1, 0, -1)).thenReturn(comments);
        when(eventService.getEvent(1)).thenReturn(event);
        
        mockMvc.perform(get("/comments/event/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].text", is("Comentario uno")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].text", is("Comentario dos")));

        verify(commentService, times(1)).getCommentsFromEvent(1, 0, -1);
        verifyNoMoreInteractions(commentService);
        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);

    }
    
    @Test
    public void test_get_comments_event_fail_404_not_found() throws Exception {
        when(eventService.getEvent(1)).thenReturn(null);

        mockMvc.perform(get("/comments/event/{id}", 1))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
 // =========================================== Create New Comment ========================================

    @Test
    public void test_create_comment_success() throws Exception {
    	
    	Event event = new Event(1, "aaa", "aaa", new Date(), new Date());
    	User user = new User(1, "aaa", "aaa", "aaa", 1);
    	when(eventService.getEvent(1)).thenReturn(event);
    	when(userService.getUser(1)).thenReturn(user);
    	
    	Comment comment = new Comment(1, "Comentario uno");
        doNothing().when(commentService).createComment(comment, 1, 1);

        mockMvc.perform(
                post("/private/comments/{eventId}/{userId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(comment)))
                .andExpect(status().isCreated());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));

        verify(eventService, times(1)).getEvent(1);
        verify(userService, times(1)).getUser(1);
        verify(commentService, times(1)).createComment(comment, 1, 1);
        verifyNoMoreInteractions(commentService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    public void test_create_comment_event_fail_404_not_found() throws Exception {
        when(eventService.getEvent(1)).thenReturn(null);

        Comment comment = new Comment(1, "Comentario uno");
        doNothing().when(commentService).createComment(comment, 1, 1);

        mockMvc.perform(
                post("/private/comments/{eventId}/{userId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(comment)))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }    
    
    @Test
    public void test_create_comment_user_fail_404_not_found() throws Exception {
    	Event event = new Event(1, "aaa", "aaa", new Date(), new Date());
    	when(eventService.getEvent(1)).thenReturn(event);   	  	
    	when(userService.getUser(1)).thenReturn(null);

        Comment comment = new Comment(1, "Comentario uno");
        doNothing().when(commentService).createComment(comment, 1, 1);

        mockMvc.perform(
                post("/private/comments/{eventId}/{userId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(comment)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(1);
        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(eventService);
    }    
    
// =========================================== Delete Existing Comment ===================================
    
    @Test
    public void test_delete_comment_success() throws Exception {
    	Comment comment = new Comment(1, "Comentario uno");

        when(commentService.getComment(comment.getId())).thenReturn(comment);
        doReturn(1).when(commentService).deleteComment(comment.getId());

        mockMvc.perform(
                delete("/private/comments/{id}", comment.getId()))
                .andExpect(status().isOk());

        verify(commentService, times(1)).getComment(comment.getId());
        verify(commentService, times(1)).deleteComment(comment.getId());
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void test_delete_comment_fail_404_not_found() throws Exception {
    	Comment comment = new Comment(999, "Comentario uno");
    	
        when(commentService.getComment(comment.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/private/comments/{id}", comment.getId()))
                .andExpect(status().isNotFound());

        verify(commentService, times(1)).getComment(comment.getId());
        verifyNoMoreInteractions(commentService);
    }  
    
 // =========================================== Update Existing Comment ===================================

    @Test
    public void test_update_comment_success() throws Exception {
    	Comment comment = new Comment(1, "Comentario uno");
    	
        when(commentService.getComment(comment.getId())).thenReturn(comment);
        doReturn(1).when(commentService).updateComment(1, comment);

        mockMvc.perform(
                put("/private/comments/{id}", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(comment)))
                .andExpect(status().isOk());

        verify(commentService, times(1)).getComment(comment.getId());
        verify(commentService, times(1)).updateComment(1, comment);
        verifyNoMoreInteractions(artistService);
    }

    @Test
    public void test_update_comment_fail_400_bad_request() throws Exception {
    	Comment comment = new Comment(999, "Comentario uno");
        when(commentService.getComment(comment.getId())).thenReturn(comment);

        mockMvc.perform(
                put("/private/comments/2", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(comment)))
                .andExpect(status().isBadRequest());

        verify(commentService, times(0)).getComment(comment.getId());
        verifyNoMoreInteractions(commentService);
    }
    
    @Test
    public void test_update_comment_fail_404_not_found() throws Exception {
    	Comment comment = new Comment(999, "Comentario uno");
        when(commentService.getComment(comment.getId())).thenReturn(null);

        mockMvc.perform(
                put("/private/comments/{id}", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(comment)))
                .andExpect(status().isNotFound());

        verify(commentService, times(1)).getComment(comment.getId());
        verifyNoMoreInteractions(commentService);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
}
