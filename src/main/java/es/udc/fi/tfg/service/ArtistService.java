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
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

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
	
	public List<Artist> getArtists(int first, int max) {
		return artistDAO.getArtists(first, max);
	}
	
	public List<Artist> getArtistsKeywords(String keywords, int first, int max) {
		return artistDAO.getArtistsKeywords(keywords, first, max);
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

		List<Artist> allArtists = this.getArtists(0,-1);
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
			if(!(this.isFollowingArtist(artistsPoints[j][0], userId))) {
				recommendedArtists.add(this.getArtist(artistsPoints[j][0]));   
			}
		}
		
		//TODO: aqui elegimos la cantidad de artistas recomendados a devolver:
		List<Artist> topRecommendedArtists;
		if(recommendedArtists.size() > 4) {
			topRecommendedArtists = recommendedArtists.subList(0, 5);
		}
		else {
			topRecommendedArtists = recommendedArtists;
		}
		
		return topRecommendedArtists;
	}
	
	public List<Artist> getArtistsFromEvent(int eventId, int first, int max) {
		List<Integer> ids = artistDAO.getArtistsFromEvent(eventId, first, max);
        List<Artist> artists = new ArrayList<Artist>();
		for(Integer id : ids) {
			artists.add(this.getArtist(id));
        }
			
		return artists;
	}
	
	public List<Artist> getArtistsFromUser(int userId, int first, int max) {
		List<Integer> ids = artistDAO.getArtistsFromUser(userId, first, max);
        List<Artist> artists = new ArrayList<Artist>();
		for(Integer id : ids) {
			artists.add(this.getArtist(id));
        }
			
		return artists;
	}
	
	public List<Integer> getArtistsFromTag(int tagId) {
		List<Integer> ids = artistDAO.getArtistsFromTag(tagId);
	
		return ids;
	}
	
	public void createArtist(Artist artist) throws EntityNotCreatableException{
		artistDAO.addArtist(artist);
	}
	
	public int deleteArtist(int id) throws Exception{
		return artistDAO.deleteArtist(id);
	}
	
	public int updateArtist(int id, Artist artist) throws EntityNotUpdatableException{
		return artistDAO.updateArtist(id, artist);
	}
	
	public boolean existsArtist (Artist artist){
		List<Artist> artists = new ArrayList<Artist>();
		artists = artistDAO.getArtists(0,-1);
		boolean exists = false;
        for(Artist a : artists) {
        	if (artist.getName().equals(a.getName())){
        		exists = true;
        	}
        }
		return exists;
	}
	
	public int followArtist (int artistId, int userId) throws EntityNotCreatableException{
		return artistDAO.followArtist(artistId, userId);
	}
	
	public int unfollowArtist (int artistId, int userId) throws EntityNotRemovableException{
		return artistDAO.unfollowArtist(artistId, userId);
	}
	
	public boolean isFollowingArtist(int artistId, int userId) {
		return artistDAO.isFollowingArtist(artistId, userId);
	}
}
