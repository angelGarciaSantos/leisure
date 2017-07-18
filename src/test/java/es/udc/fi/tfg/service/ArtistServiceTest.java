package es.udc.fi.tfg.service;

import static org.junit.Assert.*;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import es.udc.fi.tfg.config.AppConfig;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.util.EntityNotCreatableException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class ArtistServiceTest{
	
	@Autowired
	ArtistService artistService;
	
	@Test
    public void getArtistsTest() {
        List<Artist> artists = artistService.getArtists(0, -1);

        assertEquals(7, artists.size());

    }
	
	@Test
    public void getArtistsKeywordsTest() {
        List<Artist> artists = artistService.getArtistsKeywords("Metallica", 0, -1);

        assertEquals(1, artists.size());

    }
	
	@Test
    public void getArtistTest() {
        Artist artist = artistService.getArtist(1);
        
        assertEquals("Metallica", artist.getName());
//        assertEquals(1, artist.getDescription());
        assertEquals("https://pbs.twimg.com/profile_images/766360293953802240/kt0hiSmv.jpg", artist.getImage());
    }
	
	@Test
    public void getArtistsPointsTest() {
        Integer[][] points = artistService.getArtistsPoints(1);
        
        assertEquals(Integer.valueOf(2), points[0][0]);
    }
	
	@Test
    public void getRecommendedArtistTest() {
		List<Artist> artists = artistService.getRecommendedArtist(1); 
		
        assertEquals(1, artists.size());
        assertEquals("Metallica", artists.get(0).getName());

    }
	
	@Test
    public void getArtistsFromEventTest() {
		List<Artist> artists = artistService.getArtistsFromEvent(1, 0, -1); 
		
        assertEquals(1, artists.size());
    }
	
	@Test
    public void getArtistsFromUserTest() {
		List<Artist> artists = artistService.getArtistsFromUser(1, 0, -1); 
		
        assertEquals(6, artists.size());
    }
	
	@Test
    public void getArtistsFromTagTest() {
		List<Integer> artists = artistService.getArtistsFromTag(1); 
		
        assertEquals(2, artists.size());
    }
	
	@Test
    public void createAndDeleteArtistTest() throws Exception {
		Artist artist = new Artist("Test", "Test", "image");
		artistService.createArtist(artist);
		
		List<Artist> artistResult = artistService.getArtistsKeywords("Test", 0, -1);
        assertEquals(artistResult.get(0).getName(), artist.getName());
        
        artistService.deleteArtist(artistResult.get(0).getId());
        
		List<Artist> artistResult2 = artistService.getArtistsKeywords("Test", 0, -1);
        assertEquals(artistResult2.size(), 0);
    }
	
	@Test
    public void updateArtistTest() throws Exception {
		Artist artist = new Artist("Test", "Test", "image");
		artistService.createArtist(artist);
		
		List<Artist> artistResult = artistService.getArtistsKeywords("Test", 0, -1);
		Artist toUpdate = artistResult.get(0);
		toUpdate.setName("New");
		artistService.updateArtist(toUpdate.getId(), toUpdate);

		Artist updated = artistService.getArtist(toUpdate.getId());
		
		
        assertEquals("New", updated.getName());

		
        artistService.deleteArtist(updated.getId());
    }
	
	@Test
    public void existsArtistTest() throws Exception {
		Artist artist = new Artist("Test", "Test", "image");
		artistService.createArtist(artist);
		
		boolean result = artistService.existsArtist(artist);
		
        assertEquals(true, result);

		List<Artist> artistResult = artistService.getArtistsKeywords("Test", 0, -1);

        artistService.deleteArtist(artistResult.get(0).getId());
    }

	@Test
	public void followArtistTest() throws Exception {
		Artist artist = new Artist("Test", "Test", "image");
		artistService.createArtist(artist);
		List<Artist> artistResult = artistService.getArtistsKeywords("Test", 0, -1);

		
		
		artistService.followArtist(artistResult.get(0).getId(), 1);
		boolean result = artistService.isFollowingArtist(artistResult.get(0).getId(), 1);
		
		assertEquals(true, result);
		
		artistService.unfollowArtist(artistResult.get(0).getId(), 1);
		boolean result2 = artistService.isFollowingArtist(artistResult.get(0).getId(), 1);

		
		assertEquals(false, result2);
		
        artistService.deleteArtist(artistResult.get(0).getId());

	}
	
}
