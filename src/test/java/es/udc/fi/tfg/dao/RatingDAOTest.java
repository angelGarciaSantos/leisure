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
import es.udc.fi.tfg.model.Rating;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class RatingDAOTest {
	@Autowired
	RatingDAO ratingDAO;
	
	@Test
    public void getRatingsTest() {
        List<Rating> ratings = ratingDAO.getRatings();
        assertEquals(2, ratings.size());
    }
	
	@Test
    public void getRatingTest() {
        Rating rating = ratingDAO.getRating(1);
        assertEquals(rating.getUser().getId(), 1);
    }
	
	@Test
    public void getRatingsFromEventTest() {
        List<Rating> ratings = ratingDAO.getRatingsFromEvent(1);
        assertEquals(1, ratings.size());
    }
}
