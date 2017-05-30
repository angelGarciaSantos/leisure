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

import es.udc.fi.tfg.model.Tag;
import es.udc.fi.tfg.service.TagService;

@CrossOrigin
@RestController
public class TagRestController {
	@Autowired
	private TagService tagService;
	
	@GetMapping("/tags")
	public ResponseEntity<List<Tag>> getTags() {
		return new ResponseEntity<List<Tag>>(tagService.getTags(), HttpStatus.OK);
	}	
	
	@GetMapping("/tags/keywords/{keywords}")
	public ResponseEntity<List<Tag>> getTagsKeywords(@PathVariable String keywords) {
		return new ResponseEntity<List<Tag>>(tagService.getTagsKeywords(keywords), HttpStatus.OK);
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
	
	@PostMapping(value = "/tags")
	public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
		tagService.createTag(tag);
		return new ResponseEntity<Tag>(HttpStatus.CREATED);
	}
	
	@DeleteMapping("/tags/{id}")
	public ResponseEntity<String> deleteTag(@PathVariable int id) {
		int rows = tagService.deleteTag(id);
		if (rows < 1) {
			return new ResponseEntity<String>("No ha podido eliminarse el Tag "+id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else{
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	@PutMapping("/tags/{id}")
	public ResponseEntity<String> updateTag(@PathVariable int id, @RequestBody Tag tag) {
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
