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
import es.udc.fi.tfg.dao.EmployeeDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Employee;

@CrossOrigin
@RestController
public class ArtistRestController {
	@Autowired
	private ArtistDAO artistDAO;

	
	@GetMapping("/artists")
	public List getArtists() {
		return artistDAO.getArtists();
	}	
	
	@GetMapping("/artists/{id}")
	public Artist getArtist(@PathVariable int id) {
		return artistDAO.getArtist(id);
	}	
	
	@PostMapping(value = "/artists")
	public ResponseEntity createArtist(@RequestBody Artist artist) {

		artistDAO.addArtist(artist);

		return new ResponseEntity(artist, HttpStatus.OK);
	}
	
	@DeleteMapping("/artists/{id}")
	public ResponseEntity deleteArtist(@PathVariable int id) {

		if (0 == artistDAO.deleteArtist(id)) {
			return new ResponseEntity("No Artist found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(id, HttpStatus.OK);

	}

	@PutMapping("/artists/{id}")
	public ResponseEntity updateArtist(@PathVariable int id, @RequestBody Artist artist) {

		int rows = artistDAO.updateArtist(id, artist);
		if (0 == rows) {
			return new ResponseEntity("No Artist found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(rows, HttpStatus.OK);
	}
	
	
	
//Lo de abajo es el antiguo controlador de prueba (usando el dao de prueba).	
	
//	@Autowired
//	private ArtistDAO artistDAO;
//
//	
//	@GetMapping("/artists")
//	public List getArtists() {
//		return artistDAO.list();
//	}
//
//	@GetMapping("/artists/{id}")
//	public ResponseEntity getArtist(@PathVariable("id") Long id) {
//
//		Artist artist = artistDAO.get(id);
//		if (artist == null) {
//			return new ResponseEntity("No Artist found for ID " + id, HttpStatus.NOT_FOUND);
//		}
//
//		return new ResponseEntity(artist, HttpStatus.OK);
//	}
//
//	@PostMapping(value = "/artists")
//	public ResponseEntity createArtist(@RequestBody Artist artist) {
//
//		artistDAO.create(artist);
//
//		return new ResponseEntity(artist, HttpStatus.OK);
//	}
//
//	@DeleteMapping("/artists/{id}")
//	public ResponseEntity deleteArtist(@PathVariable Long id) {
//
//		if (null == artistDAO.delete(id)) {
//			return new ResponseEntity("No Artist found for ID " + id, HttpStatus.NOT_FOUND);
//		}
//
//		return new ResponseEntity(id, HttpStatus.OK);
//
//	}
//
//	@PutMapping("/artists/{id}")
//	public ResponseEntity updateArtist(@PathVariable Long id, @RequestBody Artist artist) {
//
//		artist = artistDAO.update(id, artist);
//
//		if (null == artist) {
//			return new ResponseEntity("No Artist found for ID " + id, HttpStatus.NOT_FOUND);
//		}
//
//		return new ResponseEntity(artist, HttpStatus.OK);
//	}
}
