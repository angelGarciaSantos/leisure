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
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.model.Rating;
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.LocalService;
import es.udc.fi.tfg.service.RatingService;
import es.udc.fi.tfg.service.UserService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class RatingRestControllerTest {
	private MockMvc mockMvc;

    @Mock
    private ArtistService artistService;
    
    @Mock
    private EventService eventService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private RatingService ratingService;
    
    @Mock
    private LocalService localService;

    @InjectMocks
    private RatingRestController ratingRestController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(ratingRestController)
                //.addFilters(new CORSFilter())
                .build();
    }
    
// =========================================== Get All Ratings ==========================================
    
    @Test
    public void test_get_all_ratings_success() throws Exception {
        List<Rating> ratings = Arrays.asList(
                new Rating(1, 5.0),
                new Rating(2, 6.0));

        when(ratingService.getRatings()).thenReturn(ratings);

        mockMvc.perform(get("/ratings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].rating", is(5.0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].rating", is(6.0)));

        verify(ratingService, times(1)).getRatings();
        verifyNoMoreInteractions(ratingService);
    }
    
 // =========================================== Get Rating By ID =========================================

    @Test
    public void test_get_rating_by_id_success() throws Exception {
        Rating rating = new Rating(1, 5);

        when(ratingService.getRating(1)).thenReturn(rating);

        mockMvc.perform(get("/ratings/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.rating", is(5.0)));

        verify(ratingService, times(1)).getRating(1);
        verifyNoMoreInteractions(ratingService);
    }

    @Test
    public void test_get_rating_by_id_fail_404_not_found() throws Exception {
        when(ratingService.getRating(1)).thenReturn(null);

        mockMvc.perform(get("/ratings/{id}", 1))
                .andExpect(status().isNotFound());

        verify(ratingService, times(1)).getRating(1);
        verifyNoMoreInteractions(ratingService);
    }
    
// =========================================== Get Ratings From Event ==========================================
    
    @Test
    public void test_get_ratings_from_event_success() throws Exception {
        Event event = new Event(1, "Event test uno", "Test", new Date(), new Date());
        when(eventService.getEvent(1)).thenReturn(event);
    	
    	List<Rating> ratings = Arrays.asList(
                new Rating(1, 5.0),
                new Rating(2, 6.0));

        when(ratingService.getRatingsFromEvent(1)).thenReturn(ratings);

        mockMvc.perform(get("/ratings/event/{eventId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].rating", is(5.0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].rating", is(6.0)));

        verify(ratingService, times(1)).getRatingsFromEvent(1);
        verifyNoMoreInteractions(ratingService);
        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void test_get_ratings_from_event_fail_404_not_found() throws Exception {
        when(eventService.getEvent(1)).thenReturn(null);

        mockMvc.perform(get("/ratings/event/{eventId}", 1))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
// =========================================== Get Rating From Event ==========================================
    
    @Test
    public void test_get_rating_from_event_success() throws Exception {
        Event event = new Event(1, "Event test uno", "Test", new Date(), new Date());
        when(eventService.getEvent(1)).thenReturn(event);
    	
    	List<Double> ratings = Arrays.asList(
                5.0);

        when(ratingService.getRatingFromEvent(1)).thenReturn(ratings);

        mockMvc.perform(get("/rating/event/{eventId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", is(5.0)));

        verify(ratingService, times(1)).getRatingFromEvent(1);
        verifyNoMoreInteractions(ratingService);
        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void test_get_rating_from_event_fail_404_not_found() throws Exception {
        when(eventService.getEvent(1)).thenReturn(null);

        mockMvc.perform(get("/rating/event/{eventId}", 1))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
// =========================================== Get Rating From Artist ==========================================
    
    @Test
    public void test_get_rating_from_artist_success() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
        when(artistService.getArtist(1)).thenReturn(artist);
    	
    	List<Double> ratings = Arrays.asList(
                5.0);

        when(ratingService.getRatingFromArtist(1)).thenReturn(ratings);

        mockMvc.perform(get("/rating/artist/{artistId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", is(5.0)));

        verify(ratingService, times(1)).getRatingFromArtist(1);
        verifyNoMoreInteractions(ratingService);
        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(artistService);
    }
    
    @Test
    public void test_get_rating_from_artist_fail_404_not_found() throws Exception {
        when(artistService.getArtist(1)).thenReturn(null);

        mockMvc.perform(get("/rating/artist/{artistId}", 1))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(artistService);
    }
    
// =========================================== Get Rating From Local ==========================================
    
    @Test
    public void test_get_rating_from_local_success() throws Exception {
    	Local local = new Local(1, "Local test uno", "Test", 10);
        when(localService.getLocal(1)).thenReturn(local);
    	
    	List<Double> ratings = Arrays.asList(
                5.0);

        when(ratingService.getRatingFromLocal(1)).thenReturn(ratings);

        mockMvc.perform(get("/rating/local/{localId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", is(5.0)));

        verify(ratingService, times(1)).getRatingFromLocal(1);
        verifyNoMoreInteractions(ratingService);
        verify(localService, times(1)).getLocal(1);
        verifyNoMoreInteractions(localService);
    }
    
    @Test
    public void test_get_rating_from_local_fail_404_not_found() throws Exception {
        when(localService.getLocal(1)).thenReturn(null);

        mockMvc.perform(get("/rating/local/{localId}", 1))
                .andExpect(status().isNotFound());

        verify(localService, times(1)).getLocal(1);
        verifyNoMoreInteractions(localService);
    }
    
    // =========================================== Create New Rating ========================================

    @Test
    public void test_create_rating_success() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test", new Date(), new Date());
    	when(eventService.getEvent(1)).thenReturn(event);
    	User user = new User(1, "aaa", "aaa", "aaa", 1);
        when(userService.getUser(user.getId())).thenReturn(user);
        
    	Rating rating = new Rating(1, 5);

        doNothing().when(ratingService).createRating(rating, 1 ,1);

        mockMvc.perform(
                post("/private/ratings/{eventId}/{userId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rating)))
                .andExpect(status().isCreated());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));

        verify(ratingService, times(1)).createRating(rating, 1, 1);
        verifyNoMoreInteractions(ratingService);
        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    
    
    @Test
    public void test_create_rating_fail_event_404_not_found() throws Exception {
        when(eventService.getEvent(1)).thenReturn(null);
        Rating rating = new Rating(1, 5);
        
        
        mockMvc.perform(
                post("/private/ratings/{eventId}/{userId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rating)))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void test_create_rating_fail_user_404_not_found() throws Exception {
    	Rating rating = new Rating(1, 5);
    	Event event = new Event(1, "Event test uno", "Test", new Date(), new Date());
    	when(eventService.getEvent(1)).thenReturn(event);
    	User user = new User(1, "aaa", "aaa", "aaa", 1);
        when(userService.getUser(user.getId())).thenReturn(null);

        mockMvc.perform(
                post("/private/ratings/{eventId}/{userId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rating)))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    
// =========================================== Delete Existing Rating ===================================
    
    @Test
    public void test_delete_rating_success() throws Exception {
    	Rating rating = new Rating(1, 5);

        when(ratingService.getRating(rating.getId())).thenReturn(rating);
        doReturn(1).when(ratingService).deleteRating(rating.getId());

        mockMvc.perform(
                delete("/private/ratings/{id}", rating.getId()))
                .andExpect(status().isOk());

        verify(ratingService, times(1)).getRating(rating.getId());
        verify(ratingService, times(1)).deleteRating(rating.getId());
        verifyNoMoreInteractions(ratingService);
    }

    @Test
    public void test_delete_rating_fail_404_not_found() throws Exception {
    	Rating rating = new Rating(99, 5);

        when(ratingService.getRating(rating.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/private/ratings/{id}", rating.getId()))
                .andExpect(status().isNotFound());

        verify(ratingService, times(1)).getRating(rating.getId());
        verifyNoMoreInteractions(ratingService);
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
