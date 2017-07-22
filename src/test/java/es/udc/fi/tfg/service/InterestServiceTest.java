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
import es.udc.fi.tfg.model.Interest;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class InterestServiceTest {
	@Autowired
	InterestService interestService;
	
	@Test
    public void getInterestsTest() {
        List<Interest> interests = interestService.getInterests();
        assertEquals(5, interests.size());
    }
	
	@Test
    public void getInterestTest() {
        Interest interest = interestService.getInterest(1);
        assertEquals(1, interest.getPoints());
    }
	
	@Test
    public void getInterestsFromUserTest() {
        List<Interest> interests = interestService.getInterestsFromUser(1);
        assertEquals(5, interests.size());
    }
	
	@Test
    public void createDeleteExistsInterestTest() throws EntityNotCreatableException, EntityNotRemovableException {
		Interest interest = new Interest();
		interest.setPoints(10);
		interestService.createInterest(interest, 1, 2);
		
		int exists = interestService.existsInterest(1, 2);
		
		assertEquals(interestService.getInterest(exists).getPoints(), 1);
		interestService.deleteInterest(exists);
		
		int exists2 = interestService.existsInterest(1, 2);
		
		assertEquals(exists2, -1);
		
    }
	
	@Test
    public void updateInterestTest() throws EntityNotCreatableException, EntityNotRemovableException, EntityNotUpdatableException {
		Interest interest = new Interest();
		interest.setPoints(10);
		interestService.createInterest(interest, 1, 2);
		
		int exists = interestService.existsInterest(1, 2);
		
		assertEquals(interestService.getInterest(exists).getPoints(), 1);
		
		Interest updated = interestService.getInterest(exists);
		updated.setPoints(5);
		interestService.updateInterest(exists, interest);
			
		assertEquals(interestService.getInterest(exists).getPoints(), 1);
		
		interestService.deleteInterest(exists);
		
		int exists2 = interestService.existsInterest(1, 2);
		
		assertEquals(exists2, -1);
		
    }
	
//	@Test
//    public void createInterestByEventTest() throws EntityNotCreatableException, EntityNotRemovableException, EntityNotUpdatableException {
//		Interest interest = new Interest();
//		interest.setPoints(10);
//		interestService.createInterestByEvent(interest, 1, 2);
//		
//		int exists = interestService.existsInterest(1, 2);
//		
//		assertEquals(interestService.getInterest(exists).getPoints(), 1);
//		
//		Interest updated = interestService.getInterest(exists);
//		updated.setPoints(5);
//		interestService.updateInterest(exists, interest);
//			
//		assertEquals(interestService.getInterest(exists).getPoints(), 1);
//		
//		interestService.deleteInterest(exists);
//		
//		int exists2 = interestService.existsInterest(1, 2);
//		
//		assertEquals(exists2, -1);
//		
//    }
	
	
}
