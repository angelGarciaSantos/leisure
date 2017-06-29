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

import es.udc.fi.tfg.dao.ArtistDAO;
import es.udc.fi.tfg.dao.LocalDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.LocalService;
import es.udc.fi.tfg.util.EntityNotRemovableException;

@CrossOrigin
@RestController
public class LocalRestController {
	@Autowired
	private LocalService localService;
	
	@GetMapping("/locals")
	public ResponseEntity<List<Local>> getLocals() {
		return new ResponseEntity<List<Local>>(localService.getLocals(), HttpStatus.OK);
	}	
	
	@GetMapping("/locals/keywords/{keywords}")
	public ResponseEntity<List<Local>> getLocalsKeywords(@PathVariable String keywords) {
		return new ResponseEntity<List<Local>>(localService.getLocalsKeywords(keywords), HttpStatus.OK);
	}	
	
	@GetMapping("/locals/{id}")
	public ResponseEntity<Local> getLocal(@PathVariable int id) {
		Local local = localService.getLocal(id);
		if (local == null){
			return new ResponseEntity<Local>(HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<Local>(local, HttpStatus.OK);
		}
	}	
	
	@PostMapping(value = "/private/locals")
	public ResponseEntity<Local> createLocal(@RequestBody Local local) {
		localService.createLocal(local);
		return new ResponseEntity<Local>(HttpStatus.CREATED);
	}
	
	@DeleteMapping("/private/locals/{id}")
	public ResponseEntity<String> deleteLocal(@PathVariable int id) {
		int rows;
		try {
			rows = localService.deleteLocal(id);
		}
		catch(EntityNotRemovableException e){
			return new ResponseEntity<String>(HttpStatus.LOCKED);
		}
		catch(Exception e){
			return new ResponseEntity<String>("No ha podido eliminarse el Local: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (rows < 1) {
			return new ResponseEntity<String>("No ha podido eliminarse el Local "+id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else{
			return new ResponseEntity<String>(HttpStatus.OK);
		}
	}

	@PutMapping("/private/locals/{id}")
	public ResponseEntity<String> updateLocal(@PathVariable int id, @RequestBody Local local) {
		if (local.getId()!=id) {
			return new ResponseEntity<String>("Los ids no coinciden"+id, HttpStatus.BAD_REQUEST);
		}
		else {
			int rows = localService.updateLocal(id, local);
			if (rows < 1) {
				return new ResponseEntity<String>("No ha podido actualizarse el Local "+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
}
