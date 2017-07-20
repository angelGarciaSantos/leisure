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
import es.udc.fi.tfg.model.Tag;
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.LocalService;
import es.udc.fi.tfg.service.TagService;
import es.udc.fi.tfg.service.UserService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class EventRestControllerTest {
	private MockMvc mockMvc;

    @Mock
    private ArtistService artistService;
    
    @Mock
    private EventService eventService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private LocalService localService;
    
    @Mock
    private TagService tagService;

    @InjectMocks
    private EventRestController eventRestController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(eventRestController)
                //.addFilters(new CORSFilter())
                .build();
    }
    
// =========================================== Get All Events ==========================================
    
    @Test
    public void test_get_all_events_success() throws Exception {
        List<Event> events = Arrays.asList(
                new Event(1, "Event test uno", "Test", new Date(), new Date()),
                new Event(2, "Event test dos","Test", new Date(), new Date()));

        when(eventService.getEvents(0, -1)).thenReturn(events);

        mockMvc.perform(get("/events/0/-1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Event test uno")))
                .andExpect(jsonPath("$[0].description", is("Test")))                
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Event test dos")))
                .andExpect(jsonPath("$[1].description", is("Test")));

        verify(eventService, times(1)).getEvents(0, -1);
        verifyNoMoreInteractions(eventService);
    }
    
// =========================================== Get All Events Keywords ==========================================
    
    @Test
    public void test_get_all_events_keywords_success() throws Exception {
        List<Event> events = Arrays.asList(
                new Event(1, "Event test uno", "Test", new Date(), new Date()),
                new Event(2, "Event test dos","Test", new Date(), new Date()));

        when(eventService.getEventsKeywords("a", 0, -1)).thenReturn(events);

        mockMvc.perform(get("/events/keywords/a/0/-1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Event test uno")))
                .andExpect(jsonPath("$[0].description", is("Test")))                
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Event test dos")))
                .andExpect(jsonPath("$[1].description", is("Test")));

        verify(eventService, times(1)).getEventsKeywords("a", 0, -1);
        verifyNoMoreInteractions(eventService);
    }
    
 // =========================================== Get Event By ID =========================================

    @Test
    public void test_get_event_by_id_success() throws Exception {
        Event event = new Event(1, "Event test uno", "Test", new Date(), new Date());

        when(eventService.getEvent(1)).thenReturn(event);

        mockMvc.perform(get("/events/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Event test uno")))
                .andExpect(jsonPath("$.description", is("Test")));

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    public void test_get_event_by_id_fail_404_not_found() throws Exception {
        when(eventService.getEvent(1)).thenReturn(null);

        mockMvc.perform(get("/events/{id}", 1))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
    // =========================================== Create New Event ========================================

    @Test
    public void test_create_event_success() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");

        doNothing().when(eventService).createEvent(event, 1);

        mockMvc.perform(
                post("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(event)))
                .andExpect(status().isCreated());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));
        
        verify(eventService, times(1)).createEvent(event, 1);
        verifyNoMoreInteractions(eventService);
    }
    
// =========================================== Delete Existing Event ===================================
    
    @Test
    public void test_delete_event_success() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");

        when(eventService.getEvent(event.getId())).thenReturn(event);
        doReturn(1).when(eventService).deleteEvent(event.getId());

        mockMvc.perform(
                delete("/admin/events/{id}", event.getId()))
                .andExpect(status().isOk());

        verify(eventService, times(1)).getEvent(event.getId());
        verify(eventService, times(1)).deleteEvent(event.getId());
        verifyNoMoreInteractions(eventService);
    }

    @Test
    public void test_delete_event_fail_404_not_found() throws Exception {
    	Event event = new Event(999, "Event test uno", "Test");

        when(eventService.getEvent(event.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/admin/events/{id}", event.getId()))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(event.getId());
        verifyNoMoreInteractions(eventService);
    }  
    
 // =========================================== Update Existing Event ===================================

    @Test
    public void test_update_event_success() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");
    	
        when(eventService.getEvent(event.getId())).thenReturn(event);
        doReturn(1).when(eventService).updateEvent(1, event);

        mockMvc.perform(
                put("/admin/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(event)))
                .andExpect(status().isOk());

        verify(eventService, times(1)).getEvent(event.getId());
        verify(eventService, times(1)).updateEvent(1, event);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    public void test_update_event_fail_400_bad_request() throws Exception {
    	Event event = new Event(99, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(event);

        mockMvc.perform(
                put("/admin/events/2", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(event)))
                .andExpect(status().isBadRequest());

        verify(eventService, times(0)).getEvent(event.getId());
        verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void test_update_event_fail_404_not_found() throws Exception {
    	Event event = new Event(999, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(null);

        mockMvc.perform(
                put("/admin/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(event)))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(event.getId());
        verifyNoMoreInteractions(eventService);
    }
    
 // =========================================== Add Artist To Event ========================================

    @Test
    public void test_add_artist_to_event_success() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
        when(artistService.getArtist(artist.getId())).thenReturn(artist);
    	Event event = new Event(1, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(event);

        when(eventService.addArtistToEvent(1, 1)).thenReturn(1);

        mockMvc.perform(
                post("/admin/events/artist/{eventId}/{artistId}", 1, 1))
                .andExpect(status().isOk());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));
        
        verify(eventService, times(1)).addArtistToEvent(1, 1);
        verify(eventService, times(1)).getEvent(1);
        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(artistService);
    }
    
    @Test
    public void test_add_artist_to_event_fail_event_404_not_found() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(null);

        mockMvc.perform(
                post("/admin/events/artist/{eventId}/{artistId}", 1, 1))
                .andExpect(status().isNotFound());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));
        

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void test_add_artist_to_event_fail_artist_404_not_found() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(event);
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
        when(artistService.getArtist(artist.getId())).thenReturn(null);

        mockMvc.perform(
                post("/admin/events/artist/{eventId}/{artistId}", 1, 1))
                .andExpect(status().isNotFound());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));
        

        verify(eventService, times(1)).getEvent(1);
        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(artistService);
        verifyNoMoreInteractions(eventService);
    }
    
 // =========================================== Delete Artist From Event ========================================

    @Test
    public void test_delete_artist_from_event_success() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
        when(artistService.getArtist(artist.getId())).thenReturn(artist);
    	Event event = new Event(1, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(event);

        when(eventService.deleteArtistFromEvent(1, 1)).thenReturn(1);

        mockMvc.perform(
                delete("/admin/events/artist/{eventId}/{artistId}", 1, 1))
                .andExpect(status().isOk());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));
        
        verify(eventService, times(1)).deleteArtistFromEvent(1, 1);
        verify(eventService, times(1)).getEvent(1);
        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(artistService);
    }
    
    @Test
    public void test_delete_artist_from_event_fail_event_404_not_found() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/admin/events/artist/{eventId}/{artistId}", 1, 1))
                .andExpect(status().isNotFound());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));
        

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void test_delete_artist_from_event_fail_artist_404_not_found() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(event);
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
        when(artistService.getArtist(artist.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/admin/events/artist/{eventId}/{artistId}", 1, 1))
                .andExpect(status().isNotFound());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));
        

        verify(eventService, times(1)).getEvent(1);
        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(artistService);
        verifyNoMoreInteractions(eventService);
    }
    
 // =========================================== Modify Local From Event ========================================

    @Test
    public void test_modify_local_from_event_success() throws Exception {
    	Local local = new Local(1, "Local test", "Test", 10);
        when(localService.getLocal(local.getId())).thenReturn(local);
    	Event event = new Event(1, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(event);

        when(eventService.modifyLocalFromEvent(1, 1)).thenReturn(1);

        mockMvc.perform(
                put("/admin/events/local/{eventId}/{localId}", 1, 1))
                .andExpect(status().isOk());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));
        
        verify(eventService, times(1)).modifyLocalFromEvent(1, 1);
        verify(eventService, times(1)).getEvent(1);
        verify(localService, times(1)).getLocal(1);
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(localService);
    }
    
    @Test
    public void test_modify_local_from_event_fail_event_404_not_found() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(null);

        mockMvc.perform(
                put("/admin/events/local/{eventId}/{localId}", 1, 1))
                .andExpect(status().isNotFound());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));
        

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void test_modify_local_from_event_fail_local_404_not_found() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(event);
    	Local local = new Local(1, "Local test", "Test", 10);
        when(localService.getLocal(local.getId())).thenReturn(null);

        mockMvc.perform(
                put("/admin/events/local/{eventId}/{localId}", 1, 1))
                .andExpect(status().isNotFound());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));
        

        verify(eventService, times(1)).getEvent(1);
        verify(localService, times(1)).getLocal(1);
        verifyNoMoreInteractions(artistService);
        verifyNoMoreInteractions(localService);
    }
    
// =========================================== Get Events From Artist ==========================================
    
    @Test
    public void test_get_events_from_artist_success() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
    	
        when(artistService.getArtist(artist.getId())).thenReturn(artist);
    	
    	
    	List<Integer> events = new ArrayList<Integer>();
    	events.add(1);

        when(eventService.getEventsFromArtist(1)).thenReturn(events);

        mockMvc.perform(get("/event/artist/{artistId}", 1))
                .andExpect(status().isOk());

        verify(artistService, times(1)).getArtist(1);
        verify(eventService, times(1)).getEventsFromArtist(1);
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(artistService);
    }
    
    @Test
    public void test_get_events_from_artist_fail_artist_404_not_found() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
    	
        when(artistService.getArtist(artist.getId())).thenReturn(null);
    	
        mockMvc.perform(get("/event/artist/{artistId}", 1))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(artistService);
    }
    
// =========================================== Get Next Events From Artist ==========================================
    
    @Test
    public void test_get_next_events_from_artist_success() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
    	List<Event> events = Arrays.asList(
                new Event(1, "Event test uno", "Test", new Date(), new Date()),
                new Event(2, "Event test dos","Test", new Date(), new Date()));

    	
    	
        when(artistService.getArtist(artist.getId())).thenReturn(artist);
        when(eventService.getNextEventsFromArtist(1)).thenReturn(events);

        mockMvc.perform(get("/events/artist/{artistId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Event test uno")))
                .andExpect(jsonPath("$[0].description", is("Test")))                
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Event test dos")))
                .andExpect(jsonPath("$[1].description", is("Test")));

        verify(artistService, times(1)).getArtist(1);
        verify(eventService, times(1)).getNextEventsFromArtist(1);
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(artistService);
    }
    
    @Test
    public void test_next_events_from_artist_fail_artist_404_not_found() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
    	
        when(artistService.getArtist(artist.getId())).thenReturn(null);
    	
        mockMvc.perform(get("/events/artist/{artistId}", 1))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(artistService);
    }
    
    
// =========================================== Get Next Events From Local ==========================================
    
    @Test
    public void test_get_next_events_from_local_success() throws Exception {
    	Local local = new Local(1, "Local test", "Test", 10);
        when(localService.getLocal(local.getId())).thenReturn(local);
    	List<Event> events = Arrays.asList(
                new Event(1, "Event test uno", "Test", new Date(), new Date()),
                new Event(2, "Event test dos","Test", new Date(), new Date()));

        when(eventService.getNextEventsFromLocal(1)).thenReturn(events);

        mockMvc.perform(get("/events/local/{localId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Event test uno")))
                .andExpect(jsonPath("$[0].description", is("Test")))                
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Event test dos")))
                .andExpect(jsonPath("$[1].description", is("Test")));

        verify(localService, times(1)).getLocal(1);
        verify(eventService, times(1)).getNextEventsFromLocal(1);
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(localService);
    }
    
    @Test
    public void test_next_events_from_local_fail_local_404_not_found() throws Exception {
    	Local local = new Local(1, "Local test", "Test", 10);
    	
        when(localService.getLocal(local.getId())).thenReturn(null);
    	
        mockMvc.perform(get("/events/local/{localId}", 1))
                .andExpect(status().isNotFound());

        verify(localService, times(1)).getLocal(1);
        verifyNoMoreInteractions(localService);
    }    
    
// =========================================== Get Next Events From Tag ==========================================
    
    @Test
    public void test_get_next_events_from_tag_success() throws Exception {
    	Tag tag = new Tag(1, "Tag test");
        when(tagService.getTag(tag.getId())).thenReturn(tag);
    	List<Event> events = Arrays.asList(
                new Event(1, "Event test uno", "Test", new Date(), new Date()),
                new Event(2, "Event test dos","Test", new Date(), new Date()));

        when(eventService.getNextEventsFromTag(1)).thenReturn(events);

        mockMvc.perform(get("/events/tag/{tagId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Event test uno")))
                .andExpect(jsonPath("$[0].description", is("Test")))                
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Event test dos")))
                .andExpect(jsonPath("$[1].description", is("Test")));

        verify(tagService, times(1)).getTag(1);
        verify(eventService, times(1)).getNextEventsFromTag(1);
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(localService);
    }
    
    @Test
    public void test_next_events_from_tag_fail_tag_404_not_found() throws Exception {
    	Tag tag = new Tag(1, "Tag test");
    	
        when(tagService.getTag(tag.getId())).thenReturn(null);
    	
        mockMvc.perform(get("/events/tag/{tagId}", 1))
                .andExpect(status().isNotFound());

        verify(tagService, times(1)).getTag(1);
        verifyNoMoreInteractions(tagService);
    }       
    
// =========================================== Get Events From User ==========================================
    
    @Test
    public void test_get_events_from_user_success() throws Exception {
    	User user = new User(1, "aaa", "aaa", "aaa", 1);
    	
        when(userService.getUser(user.getId())).thenReturn(user);
    	
    	
    	List<Event> events = Arrays.asList(
                new Event(1, "Event test uno", "Test", new Date(), new Date()),
                new Event(2, "Event test dos","Test", new Date(), new Date()));

        when(eventService.getEventsFromUser(1, 0, -1)).thenReturn(events);

        mockMvc.perform(get("/events/user/{userId}/{first}/{max}", 1, 0, -1))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUser(1);
        verify(eventService, times(1)).getEventsFromUser(1, 0, -1);
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(userService);
    }
    
    @Test
    public void test_get_events_from_user_fail_user_404_not_found() throws Exception {
    	User user = new User(1, "aaa", "aaa", "aaa", 1);
    	
        when(userService.getUser(user.getId())).thenReturn(null);
    	
        mockMvc.perform(get("/events/user/{userId}/{first}/{max}", 1, 0, -1))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    
 // =========================================== Get Recommended Events ========================================

    @Test
    public void test_get_recommended_events_success() throws Exception {
    	List<Event> events = Arrays.asList(
                new Event(1, "Event test uno", "Test", new Date(), new Date()),
                new Event(2, "Event test dos","Test", new Date(), new Date()));
    	User user = new User(1, "aaa", "aaa", "aaa", 1);
    	
    	when(userService.getUser(1)).thenReturn(user);
        when(eventService.getRecommendedEvents(1)).thenReturn(events);

        mockMvc.perform(
                get("/private/events/user/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Event test uno")))
                .andExpect(jsonPath("$[0].description", is("Test")))                
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Event test dos")))
                .andExpect(jsonPath("$[1].description", is("Test")));


        verify(userService, times(1)).getUser(1);
        verify(eventService, times(1)).getRecommendedEvents(1);
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_get_recommended_events_fail_404_not_found() throws Exception {
    	User user = new User(999, "aaa", "aaa", "aaa", 1);

        when(userService.getUser(1)).thenReturn(null);

        mockMvc.perform(
                get("/private/events/user/{userId}", 1))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    
 // =========================================== Follow Event ========================================

    @Test
    public void test_follow_event_success() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");
    	User user = new User(1, "aaa", "aaa", "aaa", 1);

    	
    	when(eventService.getEvent(1)).thenReturn(event);
    	when(userService.getUser(1)).thenReturn(user);
    	
        when(eventService.followEvent(1,1)).thenReturn(1);

        mockMvc.perform(
                post("/private/event/user/{eventId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(event)))
                .andExpect(status().isOk());


        verify(eventService, times(1)).getEvent(1);
        verify(userService, times(1)).getUser(1);

        verify(eventService, times(1)).followEvent(1,1);
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(userService);

    }

    @Test
    public void test_follow_event_fail_artist_404_not_found() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");

        when(eventService.getEvent(event.getId())).thenReturn(null);

        mockMvc.perform(
                post("/private/event/user/{eventId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(event)))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void test_follow_event_fail_user_404_not_found() throws Exception {
    	User user = new User(999, "aaa", "aaa", "aaa", 1);
    	Event event = new Event(1, "Event test uno", "Test");

        when(eventService.getEvent(event.getId())).thenReturn(event);
        when(userService.getUser(user.getId())).thenReturn(null);

        mockMvc.perform(
                post("/private/event/user/{eventId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(event)))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(eventService);
    }
    
 // =========================================== Unfollow Event ========================================

    @Test
    public void test_unfollow_event_success() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");
    	User user = new User(1, "aaa", "aaa", "aaa", 1);

    	
    	when(eventService.getEvent(1)).thenReturn(event);
    	when(userService.getUser(1)).thenReturn(user);
    	
        when(eventService.unfollowEvent(1,1)).thenReturn(1);

        mockMvc.perform(
                delete("/private/event/user/{eventId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(event)))
                .andExpect(status().isOk());


        verify(eventService, times(1)).getEvent(1);
        verify(userService, times(1)).getUser(1);

        verify(eventService, times(1)).unfollowEvent(1,1);
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(userService);

    }

    @Test
    public void test_unfollow_event_fail_artist_404_not_found() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");

        when(eventService.getEvent(event.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/private/event/user/{eventId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(event)))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void test_unfollow_event_fail_user_404_not_found() throws Exception {
    	User user = new User(999, "aaa", "aaa", "aaa", 1);
    	Event event = new Event(1, "Event test uno", "Test");

        when(eventService.getEvent(event.getId())).thenReturn(event);
        when(userService.getUser(user.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/private/event/user/{eventId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(event)))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(eventService);
    }
    
 // =========================================== Is Following Artist ========================================

    @Test
    public void test_isfollowing_event_success() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");
    	User user = new User(1, "aaa", "aaa", "aaa", 1);
    	
    	when(eventService.getEvent(1)).thenReturn(event);
    	when(userService.getUser(1)).thenReturn(user);
    	
        when(eventService.isFollowingEvent(1,1)).thenReturn(true);

        mockMvc.perform(
                get("/private/event/user/{eventId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(event)))
                .andExpect(status().isOk());


        verify(eventService, times(1)).getEvent(1);
        verify(userService, times(1)).getUser(1);

        verify(eventService, times(1)).isFollowingEvent(1,1);
        verifyNoMoreInteractions(eventService);
        verifyNoMoreInteractions(userService);

    }

    @Test
    public void test_isfollowing_event_fail_artist_404_not_found() throws Exception {
    	Event event = new Event(1, "Event test uno", "Test");

        when(eventService.getEvent(event.getId())).thenReturn(null);

        mockMvc.perform(
                get("/private/event/user/{eventId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(event)))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void test_isfollowing_artist_fail_user_404_not_found() throws Exception {
    	User user = new User(999, "aaa", "aaa", "aaa", 1);
    	Event event = new Event(1, "Event test uno", "Test");

        when(eventService.getEvent(event.getId())).thenReturn(event);
        when(userService.getUser(user.getId())).thenReturn(null);

        mockMvc.perform(
                get("/private/event/user/{eventId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(event)))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(eventService);

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
