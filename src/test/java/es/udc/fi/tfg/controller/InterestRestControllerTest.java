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
import es.udc.fi.tfg.model.Interest;
import es.udc.fi.tfg.model.Tag;
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.InterestService;
import es.udc.fi.tfg.service.TagService;
import es.udc.fi.tfg.service.UserService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class InterestRestControllerTest {
	private MockMvc mockMvc;

    @Mock
    private ArtistService artistService;
    
    @Mock
    private EventService eventService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private TagService tagService;
    
    @Mock
    private InterestService interestService;

    @InjectMocks
    private InterestRestController interestRestController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(interestRestController)
                //.addFilters(new CORSFilter())
                .build();
    }
    
// =========================================== Get All Interests ==========================================
    
    @Test
    public void test_get_all_interests_success() throws Exception {
        List<Interest> interests = Arrays.asList(
                new Interest(1, 10),
                new Interest(2, 20));

        when(interestService.getInterests()).thenReturn(interests);

        mockMvc.perform(get("/private/interests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].points", is(10)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].points", is(20)));


        verify(interestService, times(1)).getInterests();
        verifyNoMoreInteractions(interestService);
    }
    
 // =========================================== Get Interest By ID =========================================

    @Test
    public void test_get_interest_by_id_success() throws Exception {
        Interest interest = new Interest(1, 10);

        when(interestService.getInterest(1)).thenReturn(interest);

        mockMvc.perform(get("/private/interests/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.points", is(10)));

        verify(interestService, times(1)).getInterest(1);
        verifyNoMoreInteractions(interestService);
    }

    @Test
    public void test_get_interest_by_id_fail_404_not_found() throws Exception {
        when(interestService.getInterest(1)).thenReturn(null);

        mockMvc.perform(get("/private/interests/{id}", 1))
                .andExpect(status().isNotFound());

        verify(interestService, times(1)).getInterest(1);
        verifyNoMoreInteractions(interestService);
    }
    
 // =========================================== Exists Interest =========================================

    @Test
    public void test_exists_interest_success() throws Exception {
    	Tag tag = new Tag(1, "Tag test");
        when(tagService.getTag(tag.getId())).thenReturn(tag);
        User user = new User(1, "aaa", "aaa", "aaa", 1);
        when(userService.getUser(user.getId())).thenReturn(user);
    	
    	Interest interest = new Interest(1, 10);
        when(interestService.existsInterest(tag.getId(), user.getId())).thenReturn(1);

        mockMvc.perform(get("/private/interests/tag/user/{tagId}/{userId}", 1, 1))
                .andExpect(status().isOk());

        verify(interestService, times(1)).existsInterest(1, 1);
        verify(tagService, times(1)).getTag(1);
        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(interestService);
        verifyNoMoreInteractions(tagService);
        verifyNoMoreInteractions(userService);

    }

    @Test
    public void test_exists_interest_fail_tag_404_not_found() throws Exception {
        when(tagService.getTag(1)).thenReturn(null);

        mockMvc.perform(get("/private/interests/tag/user/{tagId}/{userId}", 1, 1))
                .andExpect(status().isNotFound());

        verify(tagService, times(1)).getTag(1);
        verifyNoMoreInteractions(tagService);
    }
    
    @Test
    public void test_exists_interest_fail_user_404_not_found() throws Exception {
    	Tag tag = new Tag(1, "Tag test");
        when(tagService.getTag(tag.getId())).thenReturn(tag);
        User user = new User(1, "aaa", "aaa", "aaa", 1);
        when(userService.getUser(user.getId())).thenReturn(null);
        mockMvc.perform(get("/private/interests/tag/user/{tagId}/{userId}", 1, 1))
                .andExpect(status().isNotFound());

        verify(tagService, times(1)).getTag(1);
        verifyNoMoreInteractions(tagService);
        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    
// =========================================== Get Interests From User ==========================================
    
    @Test
    public void test_get_interests_from_user_success() throws Exception {
    	User user = new User(1, "aaa", "aaa", "aaa", 1);
        when(userService.getUser(user.getId())).thenReturn(user);
    	
    	List<Interest> interests = Arrays.asList(
                new Interest(1, 10),
                new Interest(2, 20));

        when(interestService.getInterestsFromUser(1)).thenReturn(interests);

        mockMvc.perform(get("/private/interests/user/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].points", is(10)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].points", is(20)));

        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
        verify(interestService, times(1)).getInterestsFromUser(1);
        verifyNoMoreInteractions(interestService);
    }
    
    @Test
    public void test_get_interests_from_user_fail_user_404_not_found() throws Exception {
        User user = new User(1, "aaa", "aaa", "aaa", 1);
        when(userService.getUser(user.getId())).thenReturn(null);
        mockMvc.perform(get("/private/interests/user/{userId}", 1))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    
 // =========================================== Create Interest By Event ========================================

    @Test
    public void test_create_interest_by_event_success() throws Exception {
    	User user = new User(1, "aaa", "aaa", "aaa", 1);
        when(userService.getUser(user.getId())).thenReturn(user);
    	Event event = new Event(1, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(event);
    	
    	
    	Interest interest = new Interest(1, 10);

        doNothing().when(interestService).createInterestByEvent(interest, 1, 1);

        mockMvc.perform(
                post("/private/interests/event/{eventId}/{userId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(interest)))
                .andExpect(status().isCreated());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));

        verify(eventService, times(1)).getEvent(1);
        verify(userService, times(1)).getUser(1);
        verify(interestService, times(1)).createInterestByEvent(interest, 1, 1);
        verifyNoMoreInteractions(artistService);
    }
    
    @Test
    public void test_create_interest_by_event_fail_event_404_not_found() throws Exception {
    	Interest interest = new Interest(1, 10);
    	Event event = new Event(1, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(null);
    	
        mockMvc.perform(
                post("/private/interests/event/{eventId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(interest)))
                .andExpect(status().isNotFound());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void test_create_interest_by_event_fail_user_404_not_found() throws Exception {
    	Interest interest = new Interest(1, 10);
    	Event event = new Event(1, "Event test uno", "Test");
        when(eventService.getEvent(event.getId())).thenReturn(event);
        when(userService.getUser(1)).thenReturn(null);	
        mockMvc.perform(
                post("/private/interests/event/{eventId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(interest)))
                .andExpect(status().isNotFound());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    
    
 // =========================================== Create  New Interest ========================================

    @Test
    public void test_create_interest_success() throws Exception {
    	User user = new User(1, "aaa", "aaa", "aaa", 1);
        when(userService.getUser(user.getId())).thenReturn(user);
    	Tag tag = new Tag(1, "Tag test");
        when(tagService.getTag(tag.getId())).thenReturn(tag);
    	
    	
    	Interest interest = new Interest(1, 10);

        doNothing().when(interestService).createInterest(interest, 1, 1);

        mockMvc.perform(
                post("/private/interests/{tagId}/{userId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(interest)))
                .andExpect(status().isCreated());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));

        verify(tagService, times(1)).getTag(1);
        verify(userService, times(1)).getUser(1);
        verify(interestService, times(1)).createInterest(interest, 1, 1);
        verifyNoMoreInteractions(tagService);
        verifyNoMoreInteractions(userService);
    }
    
    @Test
    public void test_create_interest_fail_tag_404_not_found() throws Exception {
    	Interest interest = new Interest(1, 10);

        when(tagService.getTag(1)).thenReturn(null);
    	
        mockMvc.perform(
                post("/private/interests/{tagId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(interest)))
                .andExpect(status().isNotFound());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));

        verify(tagService, times(1)).getTag(1);
        verifyNoMoreInteractions(tagService);
    }
    
    @Test
    public void test_create_interest_fail_user_404_not_found() throws Exception {
    	Interest interest = new Interest(1, 10);
    	Tag tag = new Tag(1, "Tag test");
        when(tagService.getTag(tag.getId())).thenReturn(tag);
        when(userService.getUser(1)).thenReturn(null);	
        mockMvc.perform(
                post("/private/interests/{tagId}/{userId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(interest)))
                .andExpect(status().isNotFound());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));

        verify(tagService, times(1)).getTag(1);
        verifyNoMoreInteractions(tagService);
        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    
// =========================================== Delete Existing Interest ===================================
    
    @Test
    public void test_delete_interest_success() throws Exception {
    	Interest interest = new Interest(1, 10);

        when(interestService.getInterest(interest.getId())).thenReturn(interest);
        doReturn(1).when(interestService).deleteInterest(interest.getId());

        mockMvc.perform(
                delete("/private/interests/{id}", interest.getId()))
                .andExpect(status().isOk());

        verify(interestService, times(1)).getInterest(interest.getId());
        verify(interestService, times(1)).deleteInterest(interest.getId());
        verifyNoMoreInteractions(interestService);
    }

    @Test
    public void test_delete_interest_fail_404_not_found() throws Exception {
    	Interest interest = new Interest(99, 10);

        when(interestService.getInterest(interest.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/private/interests/{id}", interest.getId()))
                .andExpect(status().isNotFound());

        verify(interestService, times(1)).getInterest(interest.getId());
        verifyNoMoreInteractions(interestService);
    }  
    
 // =========================================== Update Existing Interest ===================================

    @Test
    public void test_update_interest_success() throws Exception {
    	Interest interest = new Interest(1, 10);
    	
    	when(interestService.getInterest(interest.getId())).thenReturn(interest);
        doReturn(1).when(interestService).updateInterest(1, interest);

        mockMvc.perform(
                put("/private/interests/{id}", interest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(interest)))
                .andExpect(status().isOk());

        verify(interestService, times(1)).getInterest(interest.getId());
        verify(interestService, times(1)).updateInterest(1, interest);
        verifyNoMoreInteractions(interestService);
    }

    @Test
    public void test_update_interest_fail_400_bad_request() throws Exception {
    	Interest interest = new Interest(9, 10);
        when(interestService.getInterest(interest.getId())).thenReturn(interest);

        mockMvc.perform(
                put("/private/interests/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(interest)))
                .andExpect(status().isBadRequest());

        verify(interestService, times(0)).getInterest(interest.getId());
        verifyNoMoreInteractions(interestService);
    }
    
    @Test
    public void test_update_interest_fail_404_not_found() throws Exception {
    	Interest interest = new Interest(9, 10);
        when(interestService.getInterest(interest.getId())).thenReturn(null);

        mockMvc.perform(
                put("/private/interests/{id}", interest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(interest)))
                .andExpect(status().isNotFound());

        verify(interestService, times(1)).getInterest(interest.getId());
        verifyNoMoreInteractions(interestService);
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
