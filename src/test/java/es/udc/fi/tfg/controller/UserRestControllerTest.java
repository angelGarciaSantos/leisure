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
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.UserService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class UserRestControllerTest {
	private MockMvc mockMvc;

    @Mock
    private ArtistService artistService;
    
    @Mock
    private EventService eventService;
    
    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestController userRestController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userRestController)
                //.addFilters(new CORSFilter())
                .build();
    }
    
// =========================================== Login  ==========================================
    
    @Test
    public void test_login_success() throws Exception {
    	User user = new User(1, "aaa", "aaa", "angel", 1);
    	User user2 = new User(1, "aaa", "aaa", "LZzBna6lYQ2Mk", 1);
    	when(userService.getUserEmail("aaa")).thenReturn(user2);
    	
        mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk());
    }
    
// =========================================== Get Login  ==========================================
    
    @Test
    public void test_get_login_success() throws Exception {
    	User user = new User(1, "aaa", "aaa", "angel", 1);
    	User user2 = new User(1, "aaa", "aaa", "LZzBna6lYQ2Mk", 1);
    	when(userService.getUser(1)).thenReturn(user2);
    	
        mockMvc.perform(
                get("/private/login/{id}", 1))
                .andExpect(status().isOk());
    }
    
// =========================================== Login update  ==========================================
    
    @Test
    public void test_login_update_success() throws Exception {
    	User user = new User(1, "aaa", "aaa", "angel", 1);
    	User user2 = new User(1, "aaa", "aaa", "LZzBna6lYQ2Mk", 1);
    	when(userService.updateUser(1, user)).thenReturn(1);
    	
        mockMvc.perform(
                put("/private/login/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk());
    }
    
// =========================================== Register  ==========================================
    
//    @Test
//    public void test_register_success() throws Exception {
//    	User user = new User(1, "aaa", "aaa", "angel", 1);
//    	User user2 = new User(1, "aaa", "aaa", "LZzBna6lYQ2Mk", 1);
//    	when(userService.getUserEmail("user")).thenReturn(null);
//    	doNothing().when(userService).createUser(user);
//    	when(userService.getUserEmail("user")).thenReturn(user);
//
//        mockMvc.perform(
//                post("/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(user)))
//                .andExpect(status().isOk());
//    }
//    
//    
//    public static String asJsonString(final Object obj) {
//        try {
//            final ObjectMapper mapper = new ObjectMapper();
//            return mapper.writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    
    
// =========================================== Logout  ==========================================
    
    @Test
    public void test_logout_success() throws Exception {
    	User user = new User(1, "aaa", "aaa", "angel", 1);
    	User user2 = new User(1, "aaa", "aaa", "LZzBna6lYQ2Mk", 1);
    	when(userService.getUserEmail("aaa")).thenReturn(user2);
    	
        mockMvc.perform(
                delete("/logout"))
                .andExpect(status().isOk());
    }
    
    
// =========================================== Login info ==========================================
    
    @Test
    public void test_login_info_success() throws Exception {
        mockMvc.perform(
                get("/logininfo"))
                .andExpect(status().isOk());
    }
    
// =========================================== Get All Users ==========================================
    
    @Test
    public void test_get_all_users_success() throws Exception {
    	List<User> users = Arrays.asList(
    			new User(1, "aaa", "aaa", "aaa", 1),
    			new User(2, "eee", "eee", "eee", 1));

        when(userService.getUsers(0, -1)).thenReturn(users);

        mockMvc.perform(get("/admin/users/{first}/{max}", 0, -1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("aaa")))
                .andExpect(jsonPath("$[0].email", is("aaa")))
                .andExpect(jsonPath("$[0].password", is("aaa")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("eee")))
                .andExpect(jsonPath("$[1].email", is("eee")))
                .andExpect(jsonPath("$[1].password", is("eee")));

        verify(userService, times(1)).getUsers(0, -1);
        verifyNoMoreInteractions(userService);
    }
    
// =========================================== Get Users Keywords ==========================================
    
    @Test
    public void test_get_all_users_keywords_success() throws Exception {
    	List<User> users = Arrays.asList(
    			new User(1, "aaa", "aaa", "aaa", 1),
    			new User(2, "eee", "eee", "eee", 1));

        when(userService.getUsersKeywords("a", 0, -1)).thenReturn(users);

        mockMvc.perform(get("/admin/users/keywords/a/{first}/{max}", 0, -1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("aaa")))
                .andExpect(jsonPath("$[0].email", is("aaa")))
                .andExpect(jsonPath("$[0].password", is("aaa")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("eee")))
                .andExpect(jsonPath("$[1].email", is("eee")))
                .andExpect(jsonPath("$[1].password", is("eee")));

        verify(userService, times(1)).getUsersKeywords("a",0, -1);
        verifyNoMoreInteractions(userService);
    }
    
// =========================================== Get Users Email ==========================================
    
    @Test
    public void test_get_user_email_success() throws Exception {
    	User user = new User(1, "aaa", "aaa", "aaa", 1);    	
        when(userService.getUserEmail("a")).thenReturn(user);


        mockMvc.perform(get("/admin/user/email/{email}", "a"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserEmail("a");
        verifyNoMoreInteractions(userService);
    }
    
    
 // =========================================== Get User By ID =========================================

    @Test
    public void test_get_user_by_id_success() throws Exception {
    	User user = new User(1, "aaa", "aaa", "aaa", 1);    	
        when(userService.getUser(1)).thenReturn(user);

        mockMvc.perform(get("/admin/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("aaa")))
                .andExpect(jsonPath("$.email", is("aaa")))
                .andExpect(jsonPath("$.password", is("aaa")));

        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_get_user_by_id_fail_404_not_found() throws Exception {
        when(userService.getUser(1)).thenReturn(null);

        mockMvc.perform(get("/admin/users/{id}", 1))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(1);
        verifyNoMoreInteractions(userService);
    }
    
 // =========================================== Create New User ========================================

    @Test
    public void test_create_user_success() throws Exception {
    	User user = new User(1, "aaa", "aaa", "aaa", 1);    	

        when(userService.existsUser(user)).thenReturn(false);
        doNothing().when(userService).createUser(user);

        mockMvc.perform(
                post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isCreated());
        		//TODO: ???
                //.andExpect(header().string("location", containsString("/artists/0")));

        verify(userService, times(1)).existsUser(user);
        verify(userService, times(1)).createUser(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_create_user_fail_409_conflict() throws Exception {
    	User user = new User(1, "aaa", "aaa", "aaa", 1);  

        when(userService.existsUser(user)).thenReturn(true);

        mockMvc.perform(
                post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isConflict());

        verify(userService, times(1)).existsUser(user);
        verifyNoMoreInteractions(userService);
    }
    
 // =========================================== Update Existing User ===================================

    @Test
    public void test_update_user_success() throws Exception {
    	User user = new User(1, "aaa", "aaa", "aaa", 1);  
    	
        when(userService.getUser(user.getId())).thenReturn(user);
        doReturn(1).when(userService).updateUser(1, user);

        mockMvc.perform(
                put("/admin/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUser(user.getId());
        verify(userService, times(1)).updateUser(1, user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_update_user_fail_400_bad_request() throws Exception {
    	User user = new User(100, "aaa", "aaa", "aaa", 1);  
        when(userService.getUser(user.getId())).thenReturn(user);

        mockMvc.perform(
                put("/admin/users/2", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).getUser(user.getId());
        verifyNoMoreInteractions(userService);
    }
    
    @Test
    public void test_update_user_fail_404_not_found() throws Exception {
    	User user = new User(100, "aaa", "aaa", "aaa", 1);  
        when(userService.getUser(user.getId())).thenReturn(null);

        mockMvc.perform(
                put("/admin/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(user.getId());
        verifyNoMoreInteractions(userService);
    }
    
    
 // =========================================== Delete Existing User ===================================
    
    @Test
    public void test_delete_user_success() throws Exception {
    	User user = new User(1, "aaa", "aaa", "aaa", 1);  

        when(userService.getUser(user.getId())).thenReturn(user);
        doReturn(1).when(userService).deleteUser(user.getId());

        mockMvc.perform(
                delete("/admin/users/{id}", user.getId()))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUser(user.getId());
        verify(userService, times(1)).deleteUser(user.getId());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_delete_user_fail_404_not_found() throws Exception {
    	User user = new User(99, "aaa", "aaa", "aaa", 1); 

        when(userService.getUser(user.getId())).thenReturn(null);

        mockMvc.perform(
                delete("/admin/users/{id}", user.getId()))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(user.getId());
        verifyNoMoreInteractions(userService);
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
