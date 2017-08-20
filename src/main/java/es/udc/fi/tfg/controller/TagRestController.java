package es.udc.fi.tfg.controller;

import java.util.List;

import org.apache.log4j.Logger;
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
	private Logger logger = Logger.getLogger(TagRestController.class);
	@Autowired
	private TagService tagService;
	
	@Autowired
	private ArtistService artistService;
	
	@Autowired
	private EventService eventService;
	
	@GetMapping("/tags/{first}/{max}")
	public ResponseEntity<List<Tag>> getTags(@PathVariable int first, @PathVariable int max ) {
		logger.info("Obteniendo todos los tags.");
		return new ResponseEntity<List<Tag>>(tagService.getTags(first, max), HttpStatus.OK);
	}	
	
	@GetMapping("/tags/keywords/{keywords}/{first}/{max}")
	public ResponseEntity<List<Tag>> getTagsKeywords(@PathVariable String keywords, @PathVariable int first, @PathVariable int max) {
		logger.info("Obteniendo todos los tags con palabras clave: "+keywords);
		return new ResponseEntity<List<Tag>>(tagService.getTagsKeywords(keywords, first, max), HttpStatus.OK);
	}	
	
	@GetMapping("/tags/artist/{artistId}/{first}/{max}")
	public ResponseEntity getTagsFromArtist(@PathVariable int artistId, @PathVariable int first, @PathVariable int max) {
		if (artistService.getArtist(artistId) == null ) {
			logger.error("No se ha encontrado el artista: "+artistId);
			return new ResponseEntity<String>("El artista " + artistId + " no existe.", HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo todos los tags del artista: "+artistId);
			return new ResponseEntity<List<Tag>>(tagService.getTagsFromArtist(artistId, first, max),HttpStatus.OK);		
		}
	}
	
	@PostMapping("/admin/tag/artist/{tagId}/{artistId}")
	public ResponseEntity addTagToArtist(@PathVariable int tagId, @PathVariable int artistId) throws EntityNotCreatableException {
		if (tagService.getTag(tagId) == null || artistService.getArtist(artistId) == null ) {
			logger.error("No se ha encontrado el tag "+ tagId +" o artista: "+artistId);
			return new ResponseEntity<String>("No se ha encontrado el tag "+ tagId +" o artista: "+artistId, HttpStatus.NOT_FOUND);
		}
		else {
			int rows = tagService.addTagToArtist(tagId, artistId);
			if (rows < 1) {
				logger.error("No ha podido añadirse el tag: "+ tagId+" al artista: "+artistId);
				return new ResponseEntity<String>("No ha podido añadirse el tag " + tagId
					+ " al artista " + artistId, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Añadiendo el tag: "+tagId+" al artista: "+artistId);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@DeleteMapping("/admin/tag/artist/{tagId}/{artistId}")
	public ResponseEntity<String> deleteTagFromArtist(@PathVariable int tagId, @PathVariable int artistId) throws EntityNotRemovableException {
		if ((tagService.getTag(tagId) == null ) || artistService.getArtist(artistId) == null) {
			logger.error("No ha podido eliminarse el tag: "+tagId+" del artista: "+artistId+" no se han encontrado.");
			return new ResponseEntity<String>("El tag o artista indicados no existen. Tag: "+
				tagId + " Artista: " + artistId, HttpStatus.NOT_FOUND);
		}
		else {
			int rows = tagService.deleteTagFromArtist(tagId, artistId);
			if (rows < 1) {
				logger.error("No ha podido eliminarse el tag: "+tagId+" del artista: "+artistId);
				return new ResponseEntity<String>("No ha podido eliminarse el tag " + tagId
					+ " del artista " + artistId, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Eliminando el tag: "+tagId+" del artista: "+artistId);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@GetMapping("/tags/event/{eventId}")
	public ResponseEntity getTagsFromEvent(@PathVariable int eventId) {
		if (eventService.getEvent(eventId) == null ) {
			logger.error("No se ha encontrado el evento: "+eventId);
			return new ResponseEntity<String>("El evento " + eventId + " no existe.", HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo todos los tags del evento: "+eventId);
			return new ResponseEntity<List<Tag>>(tagService.getTagsFromEvent(eventId),HttpStatus.OK);		
		}
	}
	
	@GetMapping("/tags/{id}")
	public ResponseEntity<Tag> getTag(@PathVariable int id) {
		Tag tag = tagService.getTag(id);
		if (tag == null){
			logger.error("No se ha encontrado el tag: "+id);
			return new ResponseEntity<Tag>(HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo el tag: "+id);
			return new ResponseEntity<Tag>(tag, HttpStatus.OK);
		}
	}	
	
	@PostMapping(value = "/admin/tags")
	public ResponseEntity<Tag> createTag(@RequestBody Tag tag) throws EntityNotCreatableException {
		if (tagService.existsTag(tag)){
			logger.error("No se ha podido crear el tag: "+ tag.getName());
			return new ResponseEntity<Tag>(HttpStatus.CONFLICT);	
		}
		else {
			tagService.createTag(tag);
			logger.info("Creando el tag: "+tag.getName());
			return new ResponseEntity<Tag>(HttpStatus.CREATED);			
		}
	}
	
	@DeleteMapping("/admin/tags/{id}")
	public ResponseEntity<String> deleteTag(@PathVariable int id) {
		if (tagService.getTag(id)==null) {
			logger.error("No se ha encontrado el tag: "+id);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		else {
			int rows;
			try{
				rows = tagService.deleteTag(id);
			}
			catch(EntityNotRemovableException e){
				logger.error("No ha podido eliminarse el tag: "+id);
				return new ResponseEntity<String>(HttpStatus.LOCKED);
			}
			catch(Exception e){
				logger.error("No ha podido eliminarse el tag: "+id);
				return new ResponseEntity<String>("No ha podido eliminarse el Tag: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if (rows < 1) {
				logger.error("No ha podido eliminarse el tag: "+id);
				return new ResponseEntity<String>("No ha podido eliminarse el Tag "+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Eliminando el tag: "+id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}			
		}
	}

	@PutMapping("/admin/tags/{id}")
	public ResponseEntity<String> updateTag(@PathVariable int id, @RequestBody Tag tag) throws EntityNotUpdatableException {
		if (tag.getId()!=id) {
			logger.error("Los ids no coinciden: "+id);
			return new ResponseEntity<String>("Los ids no coinciden"+id, HttpStatus.BAD_REQUEST);
		}
		else if (tagService.getTag(id)==null) {
			logger.error("No se ha encontrado el tag: "+id);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		else {
			int rows = tagService.updateTag(id, tag);
			if (rows < 1) {
				logger.error("No ha podido actualizarse el tag: "+id);
				return new ResponseEntity<String>("No ha podido actualizarse el Tag "+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Actualizando el tag: "+id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
}
