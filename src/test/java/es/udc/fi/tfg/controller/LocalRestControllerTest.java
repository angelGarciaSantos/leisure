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
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.LocalService;
import es.udc.fi.tfg.service.UserService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class LocalRestControllerTest {
	private MockMvc mockMvc;

    @Mock
    private ArtistService artistService;
    
    @Mock
    private EventService eventService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private LocalService localService;

    @InjectMocks
    private LocalRestController localRestController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(localRestController)
                //.addFilters(new CORSFilter())
                .build();
    }
    
 // =========================================== Get All Locals ==========================================
    
    @Test
    public void test_get_all_locals_success() throws Exception {	
    	List<Local> locals = Arrays.asList(
                new Local(1, "Local test uno", "Test", 10),
                new Local(2, "Local test dos", "Test", 20));

        when(localService.getLocals(0, -1)).thenReturn(locals);

        mockMvc.perform(get("/locals/0/-1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Local test uno")))
                .andExpect(jsonPath("$[0].description", is("Test")))
                .andExpect(jsonPath("$[0].capacity", is(10)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Local test dos")))
                .andExpect(jsonPath("$[1].description", is("Test")))
                .andExpect(jsonPath("$[1].capacity", is(20)));

        verify(localService, times(1)).getLocals(0, -1);
        verifyNoMoreInteractions(localService);
    }
    
// =========================================== Get All Locals Keywords ==========================================
    
    @Test
    public void test_get_all_locals_keywords_success() throws Exception {   	
    	List<Local> locals = Arrays.asList(
                new Local(1, "Local test uno", "Test", 10),
                new Local(2, "Local test dos", "Test", 20));

        when(localService.getLocalsKeywords("a",0, -1)).thenReturn(locals);

        mockMvc.perform(get("/locals/keywords/{keywords}/{first}/{max}", "a", 0, -1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Local test uno")))
                .andExpect(jsonPath("$[0].description", is("Test")))
                .andExpect(jsonPath("$[0].capacity", is(10)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Local test dos")))
                .andExpect(jsonPath("$[1].description", is("Test")))
                .andExpect(jsonPath("$[1].capacity", is(20)));

        verify(localService, times(1)).getLocalsKeywords("a", 0, -1);
        verifyNoMoreInteractions(localService);
    }
    
 // =========================================== Get Local By ID =========================================

    @Test
    public void test_get_local_by_id_success() throws Exception {
        Local local = new Local(1, "Local test uno", "Test", 10);

        when(localService.getLocal(1)).thenReturn(local);

        mockMvc.perform(get("/locals/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Local test uno")))
                .andExpect(jsonPath("$.description", is("Test")))
                .andExpect(jsonPath("$.capacity", is(10)));

        verify(localService, times(1)).getLocal(1);
        verifyNoMoreInteractions(localService);
    }

    @Test
    public void test_get_local_by_id_fail_404_not_found() throws Exception {
        when(localService.getLocal(1)).thenReturn(null);

        mockMvc.perform(get("/locals/{id}", 1))
                .andExpect(status().isNotFound());

        verify(localService, times(1)).getLocal(1);
        verifyNoMoreInteractions(localService);
    }
    
 // =========================================== Create New Local ========================================

    @Test
    public void test_create_local_success() throws Exception {
    	Local local = new Local(1, "Local test uno", "Test", 10);

        when(localService.existsLocal(local)).thenReturn(false);
        doNothing().when(localService).createLocal(local);

        mockMvc.perform(
                post("/admin/locals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(local)))
                .andExpect(status().isCreated());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));

        verify(localService, times(1)).existsLocal(local);
        verify(localService, times(1)).createLocal(local);
        verifyNoMoreInteractions(localService);
    }

    @Test
    public void test_create_local_fail_409_conflict() throws Exception {
    	Local local = new Local(1, "Name exists", "Test", 10);

        when(localService.existsLocal(local)).thenReturn(true);

        mockMvc.perform(
                post("/admin/locals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(local)))
                .andExpect(status().isConflict());

        verify(localService, times(1)).existsLocal(local);
        verifyNoMoreInteractions(localService);
    }
    
// =========================================== Delete Existing Local ===================================
    
    @Test
    public void test_delete_local_success() throws Exception {
    	Local local = new Local(1, "Local test uno", "Test", 10);

        when(localService.getLocal(local.getId())).thenReturn(local);
        doReturn(1).when(localService).deleteLocal(local.getId());

        mockMvc.perform(
                delete("/admin/locals/{id}", local.getId()))
                .andExpect(status().isOk());

        verify(localService, times(1)).getLocal(local.getId());
        verify(localService, times(1)).deleteLocal(local.getId());
        verifyNoMoreInteractions(localService);
    }

    @Test
    public void test_delete_local_fail_404_not_found() throws Exception {
    	Local local = new Local(99, "Name exists", "Test", 10);

        when(localService.getLocal(local.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/admin/locals/{id}", local.getId()))
                .andExpect(status().isNotFound());

        verify(localService, times(1)).getLocal(local.getId());
        verifyNoMoreInteractions(localService);
    }  
    
 // =========================================== Update Existing Local ===================================

    @Test
    public void test_update_local_success() throws Exception {
    	Local local = new Local(1, "Local test uno", "Test", 10);
    	
        when(localService.getLocal(local.getId())).thenReturn(local);
        doReturn(1).when(localService).updateLocal(1, local);

        mockMvc.perform(
                put("/admin/locals/{id}", local.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(local)))
                .andExpect(status().isOk());

        verify(localService, times(1)).getLocal(local.getId());
        verify(localService, times(1)).updateLocal(1, local);
        verifyNoMoreInteractions(localService);
    }

    @Test
    public void test_update_local_fail_400_bad_request() throws Exception {
    	Local local = new Local(1, "Local test uno", "Test", 10);
        when(localService.getLocal(local.getId())).thenReturn(local);

        mockMvc.perform(
                put("/admin/locals/2", local.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(local)))
                .andExpect(status().isBadRequest());

        verify(localService, times(0)).getLocal(local.getId());
        verifyNoMoreInteractions(localService);
    }
    
    @Test
    public void test_update_local_fail_404_not_found() throws Exception {
    	Local local = new Local(99, "Local test uno", "Test", 10);
        when(localService.getLocal(local.getId())).thenReturn(null);

        mockMvc.perform(
                put("/admin/locals/{id}", local.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(local)))
                .andExpect(status().isNotFound());

        verify(localService, times(1)).getLocal(local.getId());
        verifyNoMoreInteractions(localService);
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
