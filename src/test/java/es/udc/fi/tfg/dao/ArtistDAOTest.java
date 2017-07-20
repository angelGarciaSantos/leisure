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
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.util.EntityNotCreatableException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class ArtistDAOTest {
	@Autowired
	ArtistDAO artistDAO;
	
	@Test
    public void addAndDeleteArtistTest() throws Exception {
		Artist artist = new Artist("Test", "Test", "image");
		artistDAO.addArtist(artist);
		
		List<Artist> artistResult = artistDAO.getArtistsKeywords("Test", 0, -1);
        assertEquals(artistResult.get(0).getName(), artist.getName());
        
        artistDAO.deleteArtist(artistResult.get(0).getId());
        
		List<Artist> artistResult2 = artistDAO.getArtistsKeywords("Test", 0, -1);
        assertEquals(artistResult2.size(), 0);

    }
	
	@Test
    public void getArtistsTest() {
        List<Artist> artists = artistDAO.getArtists(0, -1);

        assertEquals(7, artists.size());
    }
	
	@Test
    public void getArtistsKeywordsTest() {
        List<Artist> artists = artistDAO.getArtistsKeywords("Metallica", 0, -1);

        assertEquals(1, artists.size());

    }
	
	@Test
    public void getArtistTest() {
        Artist artist = artistDAO.getArtist(1);
        
        assertEquals("Metallica", artist.getName());
//        assertEquals(1, artist.getDescription());
        assertEquals("https://pbs.twimg.com/profile_images/766360293953802240/kt0hiSmv.jpg", artist.getImage());
    }
	
	@Test
    public void getArtistsFromEventTest() {
		List<Integer> artists = artistDAO.getArtistsFromEvent(1, 0, -1); 
		
        assertEquals(1, artists.size());
    }
	
	@Test
    public void getArtistsFromUserTest() {
		List<Integer> artists = artistDAO.getArtistsFromUser(1, 0, -1); 
		
        assertEquals(6, artists.size());
    }
	
	@Test
    public void getArtistsFromTagTest() {
		List<Integer> artists = artistDAO.getArtistsFromTag(1); 
		
        assertEquals(2, artists.size());
    }
	
	@Test
    public void updateArtistTest() throws Exception {
		Artist artist = new Artist("Test", "Test", "image");
		artistDAO.addArtist(artist);
		
		List<Artist> artistResult = artistDAO.getArtistsKeywords("Test", 0, -1);
		Artist toUpdate = artistResult.get(0);
		toUpdate.setName("New");
		artistDAO.updateArtist(toUpdate.getId(), toUpdate);

		Artist updated = artistDAO.getArtist(toUpdate.getId());
		
		
        assertEquals("New", updated.getName());

		
        artistDAO.deleteArtist(updated.getId());
    }
	
	@Test
	public void followArtistTest() throws Exception {
		Artist artist = new Artist("Test", "Test", "image");
		artistDAO.addArtist(artist);
		List<Artist> artistResult = artistDAO.getArtistsKeywords("Test", 0, -1);

		
		
		artistDAO.followArtist(artistResult.get(0).getId(), 1);
		boolean result = artistDAO.isFollowingArtist(artistResult.get(0).getId(), 1);
		
		assertEquals(true, result);
		
		artistDAO.unfollowArtist(artistResult.get(0).getId(), 1);
		boolean result2 = artistDAO.isFollowingArtist(artistResult.get(0).getId(), 1);

		
		assertEquals(false, result2);
		
        artistDAO.deleteArtist(artistResult.get(0).getId());

	}
	
}
