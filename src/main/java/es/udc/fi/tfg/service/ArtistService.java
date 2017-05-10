package es.udc.fi.tfg.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.tfg.dao.ArtistDAO;
import es.udc.fi.tfg.model.Artist;

@Service
public class ArtistService {
	
	@Autowired
	private ArtistDAO artistDAO;
	
	public List<Artist> getArtists() {
		return artistDAO.getArtists();
	}
	
	public List<Artist> getArtistsKeywords(String keywords) {
		return artistDAO.getArtistsKeywords(keywords);
	}
	
	public Artist getArtist(int id){
		return artistDAO.getArtist(id);
	}
	
	public List<Artist> getArtistsFromEvent(int eventId) {
		List<Integer> ids = artistDAO.getArtistsFromEvent(eventId);
        List<Artist> artists = new ArrayList<Artist>();
		for(Integer id : ids) {
			artists.add(this.getArtist(id));
        }
			
		return artists;
	}
	
	public void createArtist(Artist artist){
		artistDAO.addArtist(artist);
	}
	
	public int deleteArtist(int id){
		return artistDAO.deleteArtist(id);
	}
	
	public int updateArtist(int id, Artist artist){
		return artistDAO.updateArtist(id, artist);
	}
	
	public boolean existsArtist (Artist artist){
		List<Artist> artists = new ArrayList<Artist>();
		artists = artistDAO.getArtists();
		boolean exists = false;
        for(Artist a : artists) {
        	if (artist.getName().equals(a.getName())){
        		exists = true;
        	}
        }
		return exists;
	}
	
	public int followArtist (int artistId, int userId){
		return artistDAO.followArtist(artistId, userId);
	}
	
	public int unfollowArtist (int artistId, int userId){
		return artistDAO.unfollowArtist(artistId, userId);
	}
}
