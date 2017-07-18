package es.udc.fi.tfg.controller;

import static org.hamcrest.CoreMatchers.is;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.fi.tfg.config.AppConfig;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.UserService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ArtistRestControllerTest {
	private MockMvc mockMvc;

    @Mock
    private ArtistService artistService;
    
    @Mock
    private EventService eventService;
    
    @Mock
    private UserService userService;

    @InjectMocks
    private ArtistRestController artistRestController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(artistRestController)
                //.addFilters(new CORSFilter())
                .build();
    }
    
    // =========================================== Get All Artists ==========================================
    
    @Test
    public void test_get_all_success() throws Exception {
        List<Artist> artists = Arrays.asList(
                new Artist(1, "Metallica", "Trash Metal", "image"),
                new Artist(2, "Black Keys","Blues Rock", "image"));

        when(artistService.getArtists(0, -1)).thenReturn(artists);

        mockMvc.perform(get("/artists/0/-1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Metallica")))
                .andExpect(jsonPath("$[0].description", is("Trash Metal")))
                .andExpect(jsonPath("$[0].image", is("image")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Black Keys")))
                .andExpect(jsonPath("$[1].description", is("Blues Rock")))
                .andExpect(jsonPath("$[1].image", is("image")));

        verify(artistService, times(1)).getArtists(0, -1);
        verifyNoMoreInteractions(artistService);
    }
    
    
// =========================================== Get All Artists Keywords ==========================================
    
    @Test
    public void test_get_all_keywords_success() throws Exception {
        List<Artist> artists = Arrays.asList(
                new Artist(1, "Metallica", "Trash Metal", "image"),
                new Artist(2, "Black Keys","Blues Rock", "image"));

        when(artistService.getArtistsKeywords("e", 0, -1)).thenReturn(artists);

        mockMvc.perform(get("/artists/keywords/e/0/-1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Metallica")))
                .andExpect(jsonPath("$[0].description", is("Trash Metal")))
                .andExpect(jsonPath("$[0].image", is("image")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Black Keys")))
                .andExpect(jsonPath("$[1].description", is("Blues Rock")))
                .andExpect(jsonPath("$[1].image", is("image")));

        verify(artistService, times(1)).getArtistsKeywords("e", 0, -1);
        verifyNoMoreInteractions(artistService);
    }
    
 // =========================================== Get Artist By ID =========================================

    @Test
    public void test_get_by_id_success() throws Exception {
        Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");

        when(artistService.getArtist(1)).thenReturn(artist);

        mockMvc.perform(get("/artists/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Metallica")))
                .andExpect(jsonPath("$.description", is("Trash Metal")))
                .andExpect(jsonPath("$.image", is("image")));

        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(artistService);
    }

    @Test
    public void test_get_by_id_fail_404_not_found() throws Exception {
        when(artistService.getArtist(1)).thenReturn(null);

        mockMvc.perform(get("/artists/{id}", 1))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(artistService);
    }
    
// =========================================== Get Artists From Event  ==========================================
    
    
    @Test
    public void test_get_artists_event_success() throws Exception {
        List<Artist> artists = Arrays.asList(
                new Artist(1, "Metallica", "Trash Metal", "image"),
                new Artist(2, "Black Keys","Blues Rock", "image"));
        
        Event event = new Event(1, "aaa", "aaa", new Date(), new Date());

        when(artistService.getArtistsFromEvent(1, 0, -1)).thenReturn(artists);
        when(eventService.getEvent(1)).thenReturn(event);
        
        mockMvc.perform(get("/artists/event/{id}/0/-1", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Metallica")))
                .andExpect(jsonPath("$[0].description", is("Trash Metal")))
                .andExpect(jsonPath("$[0].image", is("image")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Black Keys")))
                .andExpect(jsonPath("$[1].description", is("Blues Rock")))
                .andExpect(jsonPath("$[1].image", is("image")));

        verify(artistService, times(1)).getArtistsFromEvent(1, 0, -1);
        verifyNoMoreInteractions(artistService);
        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);

    }
    
    @Test
    public void test_get_artists_event_fail_404_not_found() throws Exception {
        when(eventService.getEvent(1)).thenReturn(null);

        mockMvc.perform(get("/artists/event/{id}/0/-1", 1))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
    
// =========================================== Get Artists From User  ==========================================
    
    
    @Test
    public void test_get_artists_user_success() throws Exception {
        List<Artist> artists = Arrays.asList(
                new Artist(1, "Metallica", "Trash Metal", "image"),
                new Artist(2, "Black Keys","Blues Rock", "image"));
        
        User user = new User(1, "aaa", "aaa", "aaa", 1);

        when(artistService.getArtistsFromUser(1, 0, -1)).thenReturn(artists);
        when(userService.getUser(1)).thenReturn(user);
        
        mockMvc.perform(get("/artists/user/{id}/0/-1", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Metallica")))
                .andExpect(jsonPath("$[0].description", is("Trash Metal")))
                .andExpect(jsonPath("$[0].image", is("image")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Black Keys")))
                .andExpect(jsonPath("$[1].description", is("Blues Rock")))
                .andExpect(jsonPath("$[1].image", is("image")));

        verify(artistService, times(1)).getArtistsFromUser(1, 0, -1);
        verifyNoMoreInteractions(artistService);
        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);

    }
    
    @Test
    public void test_get_artists_user_fail_404_not_found() throws Exception {
        when(userService.getUser(1)).thenReturn(null);

        mockMvc.perform(get("/artists/user/{id}/0/-1", 1))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    
    
    
    
 // =========================================== Create New Artist ========================================

    @Test
    public void test_create_artist_success() throws Exception {
    	Artist artist = new Artist();
    	artist.setName("Metallica");
    	artist.setDescription("Trash Metal");
    	artist.setImage("image");

        when(artistService.existsArtist(artist)).thenReturn(false);
        doNothing().when(artistService).createArtist(artist);

        mockMvc.perform(
                post("/admin/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(artist)))
                .andExpect(status().isCreated());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));

        verify(artistService, times(1)).existsArtist(artist);
        verify(artistService, times(1)).createArtist(artist);
        verifyNoMoreInteractions(artistService);
    }

    @Test
    public void test_create_artist_fail_409_conflict() throws Exception {
    	Artist artist = new Artist(1, "Name exists", "Bla bla", "image");

        when(artistService.existsArtist(artist)).thenReturn(true);

        mockMvc.perform(
                post("/admin/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(artist)))
                .andExpect(status().isConflict());

        verify(artistService, times(1)).existsArtist(artist);
        verifyNoMoreInteractions(artistService);
    }
    
 // =========================================== Update Existing Artist ===================================

    @Test
    public void test_update_artist_success() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
    	
        when(artistService.getArtist(artist.getId())).thenReturn(artist);
        doReturn(1).when(artistService).updateArtist(1, artist);

        mockMvc.perform(
                put("/admin/artists/{id}", artist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(artist)))
                .andExpect(status().isOk());

        verify(artistService, times(1)).getArtist(artist.getId());
        verify(artistService, times(1)).updateArtist(1, artist);
        verifyNoMoreInteractions(artistService);
    }

    @Test
    public void test_update_artist_fail_400_bad_request() throws Exception {
        Artist artist = new Artist(100, "artist bad request", "no no no", "image");
        when(artistService.getArtist(artist.getId())).thenReturn(artist);

        mockMvc.perform(
                put("/admin/artists/2", artist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(artist)))
                .andExpect(status().isBadRequest());

        verify(artistService, times(0)).getArtist(artist.getId());
        verifyNoMoreInteractions(artistService);
    }
    
    @Test
    public void test_update_artist_fail_404_not_found() throws Exception {
        Artist artist = new Artist(999, "artist not found", "no no no", "image");
        when(artistService.getArtist(artist.getId())).thenReturn(null);

        mockMvc.perform(
                put("/admin/artists/{id}", artist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(artist)))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(artist.getId());
        verifyNoMoreInteractions(artistService);
    }
    
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
 // =========================================== Delete Existing Artist ===================================
    
    @Test
    public void test_delete_artist_success() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");

        when(artistService.getArtist(artist.getId())).thenReturn(artist);
        doReturn(1).when(artistService).deleteArtist(artist.getId());

        mockMvc.perform(
                delete("/admin/artists/{id}", artist.getId()))
                .andExpect(status().isOk());

        verify(artistService, times(1)).getArtist(artist.getId());
        verify(artistService, times(1)).deleteArtist(artist.getId());
        verifyNoMoreInteractions(artistService);
    }

    @Test
    public void test_delete_artist_fail_404_not_found() throws Exception {
    	Artist artist = new Artist(999, "artist not found", "no no no", "image");

        when(artistService.getArtist(artist.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/admin/artists/{id}", artist.getId()))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(artist.getId());
        verifyNoMoreInteractions(artistService);
    }  
    
 // =========================================== Follow Artist ========================================

    @Test
    public void test_follow_artist_success() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
    	User user = new User(1, "aaa", "aaa", "aaa", 1);

    	
    	when(artistService.getArtist(1)).thenReturn(artist);
    	when(userService.getUser(1)).thenReturn(user);
    	
        when(artistService.followArtist(1,1)).thenReturn(1);

        mockMvc.perform(
                post("/private/artist/user/{artistId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(artist)))
                .andExpect(status().isOk());


        verify(artistService, times(1)).getArtist(1);
        verify(userService, times(1)).getUser(1);

        verify(artistService, times(1)).followArtist(1,1);
        verifyNoMoreInteractions(artistService);
    }

    @Test
    public void test_follow_artist_fail_artist_404_not_found() throws Exception {
    	Artist artist = new Artist(999, "artist not found", "no no no", "image");

        when(artistService.getArtist(artist.getId())).thenReturn(null);

        mockMvc.perform(
                post("/private/artist/user/{artistId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(artist)))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(artistService);
    }
    
    @Test
    public void test_follow_artist_fail_user_404_not_found() throws Exception {
    	User user = new User(999, "aaa", "aaa", "aaa", 1);
    	Artist artist = new Artist(1, "aaa", "aaa", "image");

        when(artistService.getArtist(artist.getId())).thenReturn(artist);
        when(userService.getUser(user.getId())).thenReturn(null);

        mockMvc.perform(
                post("/private/artist/user/{artistId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(artist)))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(1);
        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    
 // =========================================== Unfollow Artist ========================================

    @Test
    public void test_unfollow_artist_success() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
    	User user = new User(1, "aaa", "aaa", "aaa", 1);

    	
    	when(artistService.getArtist(1)).thenReturn(artist);
    	when(userService.getUser(1)).thenReturn(user);
    	
        when(artistService.unfollowArtist(1,1)).thenReturn(1);

        mockMvc.perform(
                delete("/private/artist/user/{artistId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(artist)))
                .andExpect(status().isOk());


        verify(artistService, times(1)).getArtist(1);
        verify(userService, times(1)).getUser(1);

        verify(artistService, times(1)).unfollowArtist(1,1);
        verifyNoMoreInteractions(artistService);
    }

    @Test
    public void test_unfollow_artist_fail_artist_404_not_found() throws Exception {
    	Artist artist = new Artist(999, "artist not found", "no no no", "image");

        when(artistService.getArtist(artist.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/private/artist/user/{artistId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(artist)))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(artistService);
    }
    
    @Test
    public void test_unfollow_artist_fail_user_404_not_found() throws Exception {
    	User user = new User(999, "aaa", "aaa", "aaa", 1);
    	Artist artist = new Artist(1, "aaa", "aaa", "image");

        when(artistService.getArtist(artist.getId())).thenReturn(artist);
        when(userService.getUser(user.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/private/artist/user/{artistId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(artist)))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(1);
        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    
 // =========================================== Is Following Artist ========================================

    @Test
    public void test_isfollowing_artist_success() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
    	User user = new User(1, "aaa", "aaa", "aaa", 1);
    	
    	when(artistService.getArtist(1)).thenReturn(artist);
    	when(userService.getUser(1)).thenReturn(user);
    	
        when(artistService.isFollowingArtist(1,1)).thenReturn(true);

        mockMvc.perform(
                get("/private/artist/user/{artistId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(artist)))
                .andExpect(status().isOk());


        verify(artistService, times(1)).getArtist(1);
        verify(userService, times(1)).getUser(1);

        verify(artistService, times(1)).isFollowingArtist(1,1);
        verifyNoMoreInteractions(artistService);
    }

    @Test
    public void test_isfollowing_artist_fail_artist_404_not_found() throws Exception {
    	Artist artist = new Artist(999, "artist not found", "no no no", "image");

        when(artistService.getArtist(artist.getId())).thenReturn(null);

        mockMvc.perform(
                get("/private/artist/user/{artistId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(artist)))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(artistService);
    }
    
    @Test
    public void test_isfollowing_artist_fail_user_404_not_found() throws Exception {
    	User user = new User(999, "aaa", "aaa", "aaa", 1);
    	Artist artist = new Artist(1, "aaa", "aaa", "image");

        when(artistService.getArtist(artist.getId())).thenReturn(artist);
        when(userService.getUser(user.getId())).thenReturn(null);

        mockMvc.perform(
                get("/private/artist/user/{artistId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(artist)))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(1);
        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    
 // =========================================== Get Recommended Artists ========================================

    @Test
    public void test_get_recommended_artists_success() throws Exception {
        List<Artist> artists = Arrays.asList(
                new Artist(1, "Metallica", "Trash Metal", "image"),
                new Artist(2, "Black Keys","Blues Rock", "image"));
    	User user = new User(1, "aaa", "aaa", "aaa", 1);
    	
    	when(userService.getUser(1)).thenReturn(user);
        when(artistService.getRecommendedArtist(1)).thenReturn(artists);

        mockMvc.perform(
                get("/private/artists/user/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Metallica")))
                .andExpect(jsonPath("$[0].description", is("Trash Metal")))
                .andExpect(jsonPath("$[0].image", is("image")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Black Keys")))
                .andExpect(jsonPath("$[1].description", is("Blues Rock")))
                .andExpect(jsonPath("$[1].image", is("image")));


        verify(userService, times(1)).getUser(1);
        verify(artistService, times(1)).getRecommendedArtist(1);
        verifyNoMoreInteractions(artistService);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_get_recommended_artists_fail_404_not_found() throws Exception {
    	User user = new User(999, "aaa", "aaa", "aaa", 1);

        when(userService.getUser(1)).thenReturn(null);

        mockMvc.perform(
                get("/private/artists/user/{userId}", 1))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    

    
    
    
}
