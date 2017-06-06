package es.udc.fi.tfg.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.tfg.dao.ArtistDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Interest;
import es.udc.fi.tfg.model.Tag;

@Service
public class ArtistService {
	
	@Autowired
	private ArtistDAO artistDAO;
	
	@Autowired
	private InterestService interestService;
	
	 public int sortFunction(int[] a, int[] b) {
	 	if (a[1] == b[1]) {
	 		return 0;
	 	}
	 	else {
	 		return (a[1] > b[1]) ? -1 : 1;
	 	}
	 }
	
	public List<Artist> getArtists() {
		return artistDAO.getArtists();
	}
	
	public List<Artist> getArtistsKeywords(String keywords) {
		return artistDAO.getArtistsKeywords(keywords);
	}
	
	public Artist getArtist(int id){
		return artistDAO.getArtist(id);
	}
	
	public Integer[][] getArtistsPoints(int userId){
		List<Interest> interests = interestService.getInterestsFromUser(userId);
		Integer[][] tagsPoints;
		tagsPoints = new Integer[interests.size()][];
//		tagsPoints[0] = new Integer [2];
//		tagsPoints[0][0] = 1;
//		tagsPoints[0][1] = 2;
	 		
		int i = 0;
		for(Interest interest : interests){
//			Integer[] content = {1,1};
//			tagsPoints.add(content);
			tagsPoints[i] = new Integer[2];
			tagsPoints[i][0] = interest.getTag().getId();
	 		tagsPoints[i][1] = interest.getPoints();
	 		i++;
		}
		
		System.out.println(tagsPoints);
	 	Arrays.sort(tagsPoints, new Comparator<Integer[]>() {	    
 		    public int compare(Integer[] s1, Integer[] s2) {
 		        Integer t1 = s1[1];
 		        Integer t2 = s2[1];
 		        return t2.compareTo(t1);
 		    }
	 	});
		System.out.println(tagsPoints);

		List<Artist> allArtists = this.getArtists();
		Integer[][] artistsPoints;
		artistsPoints = new Integer[allArtists.size()][];
		int sum = 0;
		i = 0;
		for (Artist artist : allArtists) {
			sum = 0;
			for (Tag tag : artist.getTags()){
				for(int j=0;j<tagsPoints.length;j++){
					if(tagsPoints[j][0] == tag.getId()){
						sum += tagsPoints[j][1];
					}
				}
			}
			artistsPoints[i] = new Integer[2];
			artistsPoints[i][0] = artist.getId();
			artistsPoints[i][1] = sum;
			i++;
		}
		
		Arrays.sort(artistsPoints, new Comparator<Integer[]>() {	    
 		    public int compare(Integer[] s1, Integer[] s2) {
 		        Integer t1 = s1[1];
 		        Integer t2 = s2[1];
 		        return t2.compareTo(t1);
 		    }
	 	});
				
		return artistsPoints;
	}
	
	public List<Artist> getRecommendedArtist(int userId){

		Integer[][] artistsPoints = this.getArtistsPoints(userId);
				
		List<Artist> recommendedArtists = new ArrayList<Artist>();

		for (int j=0;j<artistsPoints.length;j++){
			recommendedArtists.add(this.getArtist(artistsPoints[j][0]));   
		}
		
		return recommendedArtists;
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
	
	public boolean isFollowingArtist(int artistId, int userId) {
		if (artistDAO.isFollowingArtist(artistId, userId) == 1) {
			return true;
		}
		else {
			return false;
		}
	}
}
