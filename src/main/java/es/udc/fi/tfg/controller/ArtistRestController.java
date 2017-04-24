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
import es.udc.fi.tfg.service.ArtistService;

@CrossOrigin
@RestController
public class ArtistRestController {
	@Autowired
	private ArtistService artistService;

	@GetMapping("/artists")
	public ResponseEntity<List<Artist>> getArtists() {
		return new ResponseEntity<List<Artist>>(artistService.getArtists(), HttpStatus.OK);
	}	
	
	@GetMapping("/artists/{id}")
	public ResponseEntity<Artist> getArtist(@PathVariable int id) {
		Artist artist = artistService.getArtist(id);
		if (artist == null){
			return new ResponseEntity<Artist>(HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<Artist>(artist, HttpStatus.OK);
		}
	}	
	
	//TODO: mensaje error si no se cre� correctamente
	@PostMapping(value = "/artists")
	public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
		if (artistService.existsArtist(artist)){
			return new ResponseEntity<Artist>(HttpStatus.CONFLICT);	
		}
		else {
			artistService.createArtist(artist);
			return new ResponseEntity<Artist>(HttpStatus.CREATED);
		}
	}
	
	@DeleteMapping("/artists/{id}")
	public ResponseEntity<String> deleteArtist(@PathVariable int id) {
		if (artistService.getArtist(id) == null) {
			return new ResponseEntity<String>("No existe el artista "+id, HttpStatus.NOT_FOUND);
		}
		else {
			int rows = artistService.deleteArtist(id);
			if (rows < 1) {
				return new ResponseEntity<String>("No ha podido eliminarse el Artista"+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}

	@PutMapping("/artists/{id}")
	public ResponseEntity<String> updateArtist(@PathVariable int id, @RequestBody Artist artist) {
		if (artist.getId()!=id) {
			return new ResponseEntity<String>("Los ids no coinciden"+id, HttpStatus.BAD_REQUEST);
		}
		else {
			if (artistService.getArtist(artist.getId()) == null) {
				return new ResponseEntity<String>("No existe el artista "+id, HttpStatus.NOT_FOUND);
			}
			else {
				int rows = artistService.updateArtist(id, artist);
				if (rows < 1) {
					return new ResponseEntity<String>("No ha podido actualizarse el Artista"+id, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				else{
					return new ResponseEntity<String>(HttpStatus.OK);
				}
			}
		}
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
