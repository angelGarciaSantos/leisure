package es.udc.fi.tfg.service;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.tfg.dao.TagDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.model.Tag;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@Service
public class TagService {
	@Autowired
	private TagDAO tagDAO;
	
	@Autowired
	private EventService eventService;
	
	
	public List<Tag> getTags(int first, int max) {
		return tagDAO.getTags(first, max);
	}
	
	public List<Tag> getTagsKeywords(String keywords, int first, int max) {
		return tagDAO.getTagsKeywords(keywords, first, max);
	}
	
	public List<Tag> getTagsFromArtist(int artistId, int first, int max) {
		List<Integer> ids = tagDAO.getTagsFromArtist(artistId, first, max);
        List<Tag> tags = new ArrayList<Tag>();
		for(Integer id : ids) {
			tags.add(this.getTag(id));
        }
			
		return tags;
	}
	
	public List<Tag> getTagsFromEvent(int eventId) {
		//recuperar el evento.
		//recorrer los artistas del evento, y para cada uno, obtener los tags del artista.
		Event event = eventService.getEvent(eventId);
		Set<Artist> artists = event.getArtists();
		List<Integer> totalTags = new ArrayList<Integer>();
		for (Artist artist : artists) {
			for (Tag tag : artist.getTags()){
				if(!totalTags.contains(tag.getId())){
					totalTags.add(tag.getId());
				}
			}
		}
		
		List<Tag> tags = new ArrayList<Tag>();
		for(Integer id : totalTags) {
			tags.add(this.getTag(id));
        }
			
		return tags;
	}
	
	public Tag getTag(int id){
		return tagDAO.getTag(id);
	}
	
	public void createTag(Tag tag) throws EntityNotCreatableException{
		tagDAO.addTag(tag);
	}
	
	public int deleteTag(int id) throws EntityNotRemovableException{
		return tagDAO.deleteTag(id);
	}
	
	public int updateTag(int id, Tag tag) throws EntityNotUpdatableException{
		return tagDAO.updateTag(id, tag);
	}
	
	public boolean existsTag (Tag tag){
		List<Tag> tags = new ArrayList<Tag>();
		tags = tagDAO.getTags(0,-1);
		boolean exists = false;
        for(Tag a : tags) {
        	if (tag.getName().equals(a.getName())){
        		exists = true;
        	}
        }
		return exists;
	}
	
	public int addTagToArtist(int tagId, int artistId) throws EntityNotCreatableException{
		return tagDAO.addTagToArtist(tagId, artistId);
	}
	
	public int deleteTagFromArtist (int tagId, int artistId) throws EntityNotRemovableException{
		return tagDAO.deleteTagFromArtist(tagId, artistId);
	}
}
