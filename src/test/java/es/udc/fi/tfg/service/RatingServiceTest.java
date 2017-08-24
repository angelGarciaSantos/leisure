package es.udc.fi.tfg.service;

import static org.junit.Assert.*;

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
import es.udc.fi.tfg.model.Rating;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class RatingServiceTest {
	@Autowired
	RatingService ratingService;
	
	@Test
    public void getRatingsTest() {
        List<Rating> ratings = ratingService.getRatings();
        assertEquals(2, ratings.size());
    }
	
	@Test
    public void getRatingTest() {
        Rating rating = ratingService.getRating(1);
        assertEquals(rating.getUser().getId(), 1);
    }
	
	@Test
    public void getRatingsFromEventTest() {
        List<Rating> ratings = ratingService.getRatingsFromEvent(1, 0, -1);
        assertEquals(1, ratings.size());
    }
	
//	@Test
//    public void getRatingFromEventTest() {
//        List<Double> rating = ratingService.getRatingFromEvent(1);
//    }
	
	
	
	
}
