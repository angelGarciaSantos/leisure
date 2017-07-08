package es.udc.fi.tfg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Tag;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.TagService;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@CrossOrigin
@RestController
public class TagRestController {
	@Autowired
	private TagService tagService;
	
	@Autowired
	private ArtistService artistService;
	
	@Autowired
	private EventService eventService;
	
	@GetMapping("/tags/{first}/{max}")
	public ResponseEntity<List<Tag>> getTags(@PathVariable int first, @PathVariable int max ) {
		return new ResponseEntity<List<Tag>>(tagService.getTags(first, max), HttpStatus.OK);
	}	
	
	@GetMapping("/tags/keywords/{keywords}/{first}/{max}")
	public ResponseEntity<List<Tag>> getTagsKeywords(@PathVariable String keywords, @PathVariable int first, @PathVariable int max) {
		return new ResponseEntity<List<Tag>>(tagService.getTagsKeywords(keywords, first, max), HttpStatus.OK);
	}	
	
	@GetMapping("/tags/artist/{artistId}")
	public ResponseEntity getTagsFromArtist(@PathVariable int artistId) {
		if (artistService.getArtist(artistId) == null ) {
			return new ResponseEntity<String>("El artista " + artistId + " no existe.", HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<List<Tag>>(tagService.getTagsFromArtist(artistId),HttpStatus.OK);		
		}
	}
	
	@GetMapping("/tags/event/{eventId}")
	public ResponseEntity getTagsFromEvent(@PathVariable int eventId) {
		if (eventService.getEvent(eventId) == null ) {
			return new ResponseEntity<String>("El evento " + eventId + " no existe.", HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<List<Tag>>(tagService.getTagsFromEvent(eventId),HttpStatus.OK);		
		}
	}
	
	@GetMapping("/tags/{id}")
	public ResponseEntity<Tag> getTag(@PathVariable int id) {
		Tag tag = tagService.getTag(id);
		if (tag == null){
			return new ResponseEntity<Tag>(HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<Tag>(tag, HttpStatus.OK);
		}
	}	
	
	@PostMapping(value = "/private/tags")
	public ResponseEntity<Tag> createTag(@RequestBody Tag tag) throws EntityNotCreatableException {
		tagService.createTag(tag);
		return new ResponseEntity<Tag>(HttpStatus.CREATED);
	}
	
	@DeleteMapping("/private/tags/{id}")
	public ResponseEntity<String> deleteTag(@PathVariable int id) {
		int rows;
		try{
			rows = tagService.deleteTag(id);
		}
		catch(EntityNotRemovableException e){
			return new ResponseEntity<String>(HttpStatus.LOCKED);
		}
		catch(Exception e){
			return new ResponseEntity<String>("No ha podido eliminarse el Tag: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (rows < 1) {
			return new ResponseEntity<String>("No ha podido eliminarse el Tag "+id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else{
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	@PutMapping("/private/tags/{id}")
	public ResponseEntity<String> updateTag(@PathVariable int id, @RequestBody Tag tag) throws EntityNotUpdatableException {
		if (tag.getId()!=id) {
			return new ResponseEntity<String>("Los ids no coinciden"+id, HttpStatus.BAD_REQUEST);
		}
		else {
			int rows = tagService.updateTag(id, tag);
			if (rows < 1) {
				return new ResponseEntity<String>("No ha podido actualizarse el Tag "+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
}
