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
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class UserServiceTest {
	@Autowired
	UserService userService;
	
	@Test
    public void getUsersTest() {
        List<User> users = userService.getUsers(0, -1);
        assertEquals(2, users.size());
    }
	
	@Test
    public void getUsersKeywordsTest() {
        List<User> users = userService.getUsersKeywords("Angel", 0, -1);
        assertEquals(1, users.size());
    }
	
	@Test
    public void getUserEmailTest() {
        User user = userService.getUserEmail("angel@angel.com");
        assertEquals("Angel", user.getName());
    }
	
	@Test
    public void getUser() {
        User user = userService.getUser(1);
        assertEquals("Angel", user.getName());
    }
	
	@Test
    public void createUpdateDeleteExistsUser() throws EntityNotCreatableException, EntityNotUpdatableException, EntityNotRemovableException {
        User user = new User();
        user.setName("Test");
        user.setEmail("Test");
        user.setPassword("Test");
        user.setType(1);
        
        userService.createUser(user);
        assertEquals(userService.existsUser(user), true);
        List<User> users = userService.getUsersKeywords("Test", 0, -1);
        assertEquals(1, users.size());
        
        User update = users.get(0);
        
        update.setName("New");
        
        userService.updateUser(update.getId(), update);
        
        List<User> users2 = userService.getUsersKeywords("New", 0, -1);
        assertEquals(1, users2.size());
        
        userService.deleteUser(users2.get(0).getId());
        
        List<User> users3 = userService.getUsersKeywords("New", 0, -1);
        assertEquals(0, users3.size());
        assertEquals(userService.existsUser(user), false);

    }
}
