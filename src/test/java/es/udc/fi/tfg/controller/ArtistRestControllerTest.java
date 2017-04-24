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

import java.util.Arrays;
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
import es.udc.fi.tfg.service.ArtistService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ArtistRestControllerTest {
	private MockMvc mockMvc;

    @Mock
    private ArtistService artistService;

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
                new Artist(1, "Metallica", "Trash Metal", 8),
                new Artist(2, "Black Keys","Blues Rock", 9));

        when(artistService.getArtists()).thenReturn(artists);

        mockMvc.perform(get("/artists"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Metallica")))
                .andExpect(jsonPath("$[0].description", is("Trash Metal")))
                .andExpect(jsonPath("$[0].rating", is(8.0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Black Keys")))
                .andExpect(jsonPath("$[1].description", is("Blues Rock")))
                .andExpect(jsonPath("$[1].rating", is(9.0)));

        verify(artistService, times(1)).getArtists();
        verifyNoMoreInteractions(artistService);
    }
    
 // =========================================== Get Artist By ID =========================================

    @Test
    public void test_get_by_id_success() throws Exception {
        Artist artist = new Artist(1, "Metallica", "Trash Metal", 8);

        when(artistService.getArtist(1)).thenReturn(artist);

        mockMvc.perform(get("/artists/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Metallica")))
                .andExpect(jsonPath("$.description", is("Trash Metal")))
                .andExpect(jsonPath("$.rating", is(8.0)));

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
    
 // =========================================== Create New Artist ========================================

    @Test
    public void test_create_artist_success() throws Exception {
    	Artist artist = new Artist();
    	artist.setName("Metallica");
    	artist.setDescription("Trash Metal");
    	artist.setRating(8.0);

        when(artistService.existsArtist(artist)).thenReturn(false);
        doNothing().when(artistService).createArtist(artist);

        mockMvc.perform(
                post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(artist)));
                //.andExpect(status().isCreated());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));

        verify(artistService, times(1)).existsArtist(artist);
        verify(artistService, times(1)).createArtist(artist);
        verifyNoMoreInteractions(artistService);
    }

    @Test
    public void test_create_artist_fail_409_conflict() throws Exception {
    	Artist artist = new Artist(1, "Name exists", "Bla bla", 7);

        when(artistService.existsArtist(artist)).thenReturn(true);

        mockMvc.perform(
                post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(artist)))
                .andExpect(status().isConflict());

        verify(artistService, times(1)).existsArtist(artist);
        verifyNoMoreInteractions(artistService);
    }
    
 // =========================================== Update Existing Artist ===================================

    @Test
    public void test_update_artist_success() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", 8);
    	
        when(artistService.getArtist(artist.getId())).thenReturn(artist);
        doReturn(1).when(artistService).updateArtist(1, artist);

        mockMvc.perform(
                put("/artists/{id}", artist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(artist)))
                .andExpect(status().isOk());

        verify(artistService, times(1)).getArtist(artist.getId());
        verify(artistService, times(1)).updateArtist(1, artist);
        verifyNoMoreInteractions(artistService);
    }

    @Test
    public void test_update_artist_fail_404_not_found() throws Exception {
        Artist artist = new Artist(999, "artist not found", "no no no", 5);
        when(artistService.getArtist(artist.getId())).thenReturn(null);

        mockMvc.perform(
                put("/artists/{id}", artist.getId())
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
    
    @Test
    public void test_delete_artist_success() throws Exception {
    	Artist artist = new Artist(1, "Metallica", "Trash Metal", 8);

        when(artistService.getArtist(artist.getId())).thenReturn(artist);
        doReturn(1).when(artistService).deleteArtist(artist.getId());

        mockMvc.perform(
                delete("/artists/{id}", artist.getId()))
                .andExpect(status().isOk());

        verify(artistService, times(1)).getArtist(artist.getId());
        verify(artistService, times(1)).deleteArtist(artist.getId());
        verifyNoMoreInteractions(artistService);
    }

    @Test
    public void test_delete_artist_fail_404_not_found() throws Exception {
    	Artist artist = new Artist(999, "artist not found", "no no no", 5);

        when(artistService.getArtist(artist.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/artists/{id}", artist.getId()))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).getArtist(artist.getId());
        verifyNoMoreInteractions(artistService);
    }  
}
