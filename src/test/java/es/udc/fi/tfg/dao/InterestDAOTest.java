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
import es.udc.fi.tfg.model.Interest;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class InterestDAOTest {
	@Autowired
	InterestDAO interestDAO;
	
	@Test
    public void getInterestsTest() {
        List<Interest> interests = interestDAO.getInterests();
        assertEquals(4, interests.size());
    }
	
	@Test
    public void getInterestTest() {
        Interest interest = interestDAO.getInterest(1);
        assertEquals(1, interest.getPoints());
    }
	
	@Test
    public void getInterestsFromUserTest() {
        List<Interest> interests = interestDAO.getInterestsFromUser(1);
        assertEquals(4, interests.size());
    }
	
	@Test
    public void createDeleteExistsInterestTest() throws EntityNotCreatableException, EntityNotRemovableException {
		Interest interest = new Interest();
		interest.setPoints(1);
		interestDAO.addInterest(interest, 1, 2);
		
		int exists = interestDAO.existsInterest(1, 2);
		
		assertEquals(interestDAO.getInterest(exists).getPoints(), 1);
		interestDAO.deleteInterest(exists);
		
		int exists2 = interestDAO.existsInterest(1, 2);
		
		assertEquals(exists2, -1);
		
    }
	
	@Test
    public void updateInterestTest() throws EntityNotCreatableException, EntityNotRemovableException, EntityNotUpdatableException {
		Interest interest = new Interest();
		interest.setPoints(1);
		interestDAO.addInterest(interest, 1, 2);
		
		int exists = interestDAO.existsInterest(1, 2);
		
		assertEquals(interestDAO.getInterest(exists).getPoints(), 1);
		
		Interest updated = interestDAO.getInterest(exists);
		updated.setPoints(5);
		interestDAO.updateInterest(exists, interest);
			
		assertEquals(interestDAO.getInterest(exists).getPoints(), 1);
		
		interestDAO.deleteInterest(exists);
		
		int exists2 = interestDAO.existsInterest(1, 2);
		
		assertEquals(exists2, -1);
		
    }
	
	
}
