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
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.model.Tag;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.TagService;
import es.udc.fi.tfg.service.UserService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class TagRestControllerTest {
	private MockMvc mockMvc;

    @Mock
    private ArtistService artistService;
    
    @Mock
    private EventService eventService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private TagService tagService;

    @InjectMocks
    private TagRestController tagRestController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(tagRestController)
                //.addFilters(new CORSFilter())
                .build();
    }
    
// =========================================== Get All Tags ==========================================
    
    @Test
    public void test_get_all_tags_success() throws Exception {
        List<Tag> tags = Arrays.asList(
                new Tag(1, "Tag uno"),
                new Tag(2, "Tag dos"));

        when(tagService.getTags(0, -1)).thenReturn(tags);

        mockMvc.perform(get("/tags/0/-1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Tag uno")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Tag dos")));

        verify(tagService, times(1)).getTags(0, -1);
        verifyNoMoreInteractions(tagService);
    }
    
// =========================================== Get Tags Keywords ==========================================
    
    @Test
    public void test_get_tags_keywords_success() throws Exception {
        List<Tag> tags = Arrays.asList(
                new Tag(1, "Tag uno"),
                new Tag(2, "Tag dos"));

        when(tagService.getTagsKeywords("a", 0, -1)).thenReturn(tags);

        mockMvc.perform(get("/tags/keywords/a/0/-1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Tag uno")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Tag dos")));

        verify(tagService, times(1)).getTagsKeywords("a", 0, -1);
        verifyNoMoreInteractions(tagService);
    }
    
// =========================================== Get Tags From Artist ==========================================
    
    @Test
    public void test_get_tags_from_artist_success() throws Exception {
    	 Artist artist = new Artist(1, "Metallica", "Trash Metal", "image");
         when(artistService.getArtist(1)).thenReturn(artist);

    	List<Tag> tags = Arrays.asList(
                new Tag(1, "Tag uno"),
                new Tag(2, "Tag dos"));

        when(tagService.getTagsFromArtist(1, 0, -1)).thenReturn(tags);

        mockMvc.perform(get("/tags/artist/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Tag uno")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Tag dos")));

        verify(tagService, times(1)).getTagsFromArtist(1, 0, -1);
        verifyNoMoreInteractions(tagService);
        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(artistService);
    }
    
    @Test
    public void test_get_tags_from_artist_fail_404_not_found() throws Exception {
         when(artistService.getArtist(1)).thenReturn(null);

        mockMvc.perform(get("/tags/artist/1"))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(1);
        verifyNoMoreInteractions(artistService);
    }
    
// =========================================== Get Tags From Event ==========================================
    
    @Test
    public void test_get_tags_from_event_success() throws Exception {
        Event event = new Event(1, "Event test uno", "Test", new Date(), new Date());
        when(eventService.getEvent(1)).thenReturn(event);

    	List<Tag> tags = Arrays.asList(
                new Tag(1, "Tag uno"),
                new Tag(2, "Tag dos"));

        when(tagService.getTagsFromEvent(1)).thenReturn(tags);

        mockMvc.perform(get("/tags/event/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Tag uno")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Tag dos")));

        verify(tagService, times(1)).getTagsFromEvent(1);
        verifyNoMoreInteractions(tagService);
        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void test_get_tags_from_event_fail_404_not_found() throws Exception {
         when(eventService.getEvent(1)).thenReturn(null);

        mockMvc.perform(get("/tags/event/1"))
                .andExpect(status().isNotFound());

        verify(eventService, times(1)).getEvent(1);
        verifyNoMoreInteractions(eventService);
    }
    
 // =========================================== Get Tag By ID =========================================
    @Test
    public void test_get_tag_by_id_success() throws Exception {
    	Tag tag = new Tag(1, "Tag uno");
    	when(tagService.getTag(1)).thenReturn(tag);
    	
        mockMvc.perform(get("/tags/{id}", 1))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Tag uno")));

		verify(tagService, times(1)).getTag(1);
		verifyNoMoreInteractions(tagService);
    }
    
    @Test
    public void test_get_tag_by_id_fail_404_not_found() throws Exception {
        when(tagService.getTag(1)).thenReturn(null);

        mockMvc.perform(get("/tags/{id}", 1))
                .andExpect(status().isNotFound());

        verify(tagService, times(1)).getTag(1);
        verifyNoMoreInteractions(tagService);
    }
    
 // =========================================== Create New Tag ========================================

    @Test
    public void test_create_tag_success() throws Exception {
    	Tag tag = new Tag(1, "Tag uno");
    	when(tagService.getTag(1)).thenReturn(tag);

        when(tagService.existsTag(tag)).thenReturn(false);
        doNothing().when(tagService).createTag(tag);

        mockMvc.perform(
                post("/admin/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tag)))
                .andExpect(status().isCreated());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));

        verify(tagService, times(1)).existsTag(tag);
        verify(tagService, times(1)).createTag(tag);
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void test_create_tag_fail_409_conflict() throws Exception {
    	Tag tag = new Tag(1, "Tag uno");
    	

        when(tagService.existsTag(tag)).thenReturn(true);

        mockMvc.perform(
                post("/admin/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tag)))
                .andExpect(status().isConflict());

        verify(tagService, times(1)).existsTag(tag);
        verifyNoMoreInteractions(tagService);
    }
    
// =========================================== Delete Existing Artist ===================================
    
    @Test
    public void test_delete_tag_success() throws Exception {
    	Tag tag = new Tag(1, "Tag uno");

        when(tagService.getTag(tag.getId())).thenReturn(tag);
        doReturn(1).when(tagService).deleteTag(tag.getId());

        mockMvc.perform(
                delete("/admin/tags/{id}", tag.getId()))
                .andExpect(status().isOk());

        verify(tagService, times(1)).getTag(tag.getId());
        verify(tagService, times(1)).deleteTag(tag.getId());
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void test_delete_tag_fail_404_not_found() throws Exception {
    	Tag tag = new Tag(99, "Tag uno");

        when(tagService.getTag(tag.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/admin/tags/{id}", tag.getId()))
                .andExpect(status().isNotFound());

        verify(tagService, times(1)).getTag(tag.getId());
        verifyNoMoreInteractions(tagService);
    }      
    
 // =========================================== Update Existing Tag ===================================

    @Test
    public void test_update_tag_success() throws Exception {
    	Tag tag = new Tag(1, "Tag uno");
    	
        when(tagService.getTag(tag.getId())).thenReturn(tag);
        doReturn(1).when(tagService).updateTag(1, tag);

        mockMvc.perform(
                put("/admin/tags/{id}", tag.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tag)))
                .andExpect(status().isOk());

        verify(tagService, times(1)).getTag(tag.getId());
        verify(tagService, times(1)).updateTag(1, tag);
        verifyNoMoreInteractions(tagService);
    }

    @Test
    public void test_update_tag_fail_400_bad_request() throws Exception {
    	Tag tag = new Tag(1, "Tag uno");
        when(tagService.getTag(tag.getId())).thenReturn(tag);

        mockMvc.perform(
                put("/admin/tags/2", tag.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tag)))
                .andExpect(status().isBadRequest());

        verify(tagService, times(0)).getTag(tag.getId());
        verifyNoMoreInteractions(tagService);
    }
    
    @Test
    public void test_update_tag_fail_404_not_found() throws Exception {
    	Tag tag = new Tag(1, "Tag uno");
        when(tagService.getTag(tag.getId())).thenReturn(null);

        mockMvc.perform(
                put("/admin/tags/{id}", tag.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tag)))
                .andExpect(status().isNotFound());

        verify(tagService, times(1)).getTag(tag.getId());
        verifyNoMoreInteractions(tagService);
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
