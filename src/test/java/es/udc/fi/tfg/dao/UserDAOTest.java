package es.udc.fi.tfg.dao;

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
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class UserDAOTest {
	@Autowired
	UserDAO userDAO;
	
	@Test
    public void getUsersTest() {
        List<User> users = userDAO.getUsers(0, -1);
        assertEquals(2, users.size());
    }
	
	@Test
    public void getUsersKeywordsTest() {
        List<User> users = userDAO.getUsersKeywords("Angel", 0, -1);
        assertEquals(1, users.size());
    }
	
	@Test
    public void getUserEmailTest() {
        User user = userDAO.getUserEmail("angel@angel.com");
        assertEquals("Angel", user.getName());
    }
	
	@Test
    public void getUser() {
        User user = userDAO.getUser(1);
        assertEquals("Angel", user.getName());
    }
	
	@Test
    public void createUpdateDeleteExistsUser() throws EntityNotCreatableException, EntityNotUpdatableException, EntityNotRemovableException {
        User user = new User();
        user.setName("Test");
        user.setEmail("Test");
        user.setPassword("Test");
        user.setType(1);
        
        userDAO.addUser(user);
        List<User> users = userDAO.getUsersKeywords("Test", 0, -1);
        assertEquals(1, users.size());
        
        User update = users.get(0);
        
        update.setName("New");
        
        userDAO.updateUser(update.getId(), update);
        
        List<User> users2 = userDAO.getUsersKeywords("New", 0, -1);
        assertEquals(1, users2.size());
        
        userDAO.deleteUser(users2.get(0).getId());
        
        List<User> users3 = userDAO.getUsersKeywords("New", 0, -1);
        assertEquals(0, users3.size());
    }
}
