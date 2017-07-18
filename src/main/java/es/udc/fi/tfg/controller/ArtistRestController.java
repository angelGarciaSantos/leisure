package es.udc.fi.tfg.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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

import es.udc.fi.tfg.dao.ArtistDAO;
import es.udc.fi.tfg.dao.EmployeeDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Employee;
import es.udc.fi.tfg.model.User;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.EventService;
import es.udc.fi.tfg.service.UserService;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@CrossOrigin
@RestController
public class ArtistRestController {
	private Logger logger = Logger.getLogger(ArtistRestController.class);
	
	@Autowired
	private ArtistService artistService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	@GetMapping("/artists/{first}/{max}")
	public ResponseEntity<List<Artist>> getArtists(@PathVariable int first, @PathVariable int max ) {
		logger.warn("Obteniendo todos los artistas.");
		return new ResponseEntity<List<Artist>>(artistService.getArtists(first, max), HttpStatus.OK);
	}	
	
	@GetMapping("/artists/keywords/{keywords}/{first}/{max}")
	public ResponseEntity<List<Artist>> getArtistsKeywords(@PathVariable String keywords, @PathVariable int first, @PathVariable int max) {
		logger.warn("Obteniendo artistas por palabras clave: " + keywords);
		return new ResponseEntity<List<Artist>>(artistService.getArtistsKeywords(keywords, first, max), HttpStatus.OK);
	}	
	
	@GetMapping("/artists/{id}")
	public ResponseEntity<Artist> getArtist(@PathVariable int id) {
		Artist artist = artistService.getArtist(id);
		if (artist == null){
			logger.error("No se ha encontrado el artista: " + id);
			return new ResponseEntity<Artist>(HttpStatus.NOT_FOUND);
		}
		else {
			logger.warn("Obteniendo artista con id: " + id);
			return new ResponseEntity<Artist>(artist, HttpStatus.OK);
		}
	}	
	
	@GetMapping("/artists/event/{eventId}/{first}/{max}")
	public ResponseEntity getArtistsFromEvent(@PathVariable int eventId, @PathVariable int first, @PathVariable int max) {
		if (eventService.getEvent(eventId) == null ) {
			logger.error("El evento " + eventId + " no existe.");
			return new ResponseEntity<String>("El evento " + eventId + " no existe.", HttpStatus.NOT_FOUND);
		}
		else {
			logger.warn("Obteniendo los artistas del evento :" + eventId);
			return new ResponseEntity<List<Artist>>(artistService.getArtistsFromEvent(eventId, first, max),HttpStatus.OK);		
		}
	}
	
	@GetMapping("/artists/user/{userId}/{first}/{max}")
	public ResponseEntity getArtistsFromUser(@PathVariable int userId, @PathVariable int first, @PathVariable int max) {
		if (userService.getUser(userId) == null ) {
			logger.error("El usuario " + userId + " no existe.");
			return new ResponseEntity<String>("El usuario " + userId + " no existe.", HttpStatus.NOT_FOUND);
		}
		else {
			logger.warn("Obteniendo los artistas seguidos por el usuario :" + userId);
			return new ResponseEntity<List<Artist>>(artistService.getArtistsFromUser(userId, first, max),HttpStatus.OK);		
		}
	}

	//TODO: mensaje error si no se creó correctamente
	@PostMapping(value = "/admin/artists")
	public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) throws EntityNotCreatableException {
		if (artistService.existsArtist(artist)){
			logger.error("El artista con nombre " + artist.getName() + " ya existe.");
			return new ResponseEntity<Artist>(HttpStatus.CONFLICT);	
		}
		else {
			artistService.createArtist(artist);
			logger.warn("Se ha creado el artista con nombre " + artist.getName());
			return new ResponseEntity<Artist>(HttpStatus.CREATED);
		}
	}
	
	@DeleteMapping("/admin/artists/{id}")
	public ResponseEntity<String> deleteArtist(@PathVariable int id) {
		if (artistService.getArtist(id) == null) {
			logger.error("No existe el Artista: " + id);
			return new ResponseEntity<String>("No existe el artista "+id, HttpStatus.NOT_FOUND);
		}
		else {
			int rows;
			try{
				rows = artistService.deleteArtist(id);
			}
			catch(EntityNotRemovableException e){
				return new ResponseEntity<String>(HttpStatus.LOCKED);
			}
			catch(Exception e){
				return new ResponseEntity<String>("No ha podido eliminarse el Artista: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			if (rows < 1) {
				logger.error("No ha podido eliminarse el Artista: " + id);
				return new ResponseEntity<String>("No ha podido eliminarse el Artista: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.warn("Se ha eliminado el artista: " + id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}

	@PutMapping("/admin/artists/{id}")
	public ResponseEntity<String> updateArtist(@PathVariable int id, @RequestBody Artist artist) throws EntityNotUpdatableException {
		if (artist.getId()!=id) {
			logger.error("Los ids no coinciden" + id);
			return new ResponseEntity<String>("Los ids no coinciden" + id, HttpStatus.BAD_REQUEST);
		}
		else {
			if (artistService.getArtist(artist.getId()) == null) {
				logger.error("No existe el artista: " + id);
				return new ResponseEntity<String>("No existe el artista: " + id, HttpStatus.NOT_FOUND);
			}
			else {
				int rows = artistService.updateArtist(id, artist);
				if (rows < 1) {
					logger.error("No ha podido actualizarse el artista: " + id);
					return new ResponseEntity<String>("No ha podido actualizarse el artista: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				else{
					logger.warn("Se ha actualizado el artista: " + id);
					return new ResponseEntity<String>(HttpStatus.OK);
				}
			}
		}
	}
	
	@PostMapping(value = "/private/artist/user/{artistId}/{userId}")
	public ResponseEntity<String> followArtist(@PathVariable int artistId, @PathVariable int userId) throws EntityNotCreatableException {
		if ((artistService.getArtist(artistId) == null ) || userService.getUser(userId) == null) {
			logger.error("El artista o usuario indicados no existen. Artista: "+
				artistId + " Usuario: " + userId);
			return new ResponseEntity<String>("El artista o usuario indicados no existen. Artista: "+
				artistId + " Usuario: " + userId, HttpStatus.NOT_FOUND);
		}
		else {
			int rows = artistService.followArtist(artistId, userId);
			if (rows < 1) {
				logger.error("No ha podido seguirse al artista " + artistId
						+ " con el usuario " + userId);
				return new ResponseEntity<String>("No ha podido seguirse al artista " + artistId
					+ " con el usuario " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.warn("Se ha seguido al artista: " + artistId + " con el usuario: "
					+ userId);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@DeleteMapping(value = "/private/artist/user/{artistId}/{userId}")
	public ResponseEntity<String> unfollowArtist(@PathVariable int artistId, @PathVariable int userId) throws EntityNotRemovableException {
		if ((artistService.getArtist(artistId) == null ) || userService.getUser(userId) == null) {
			logger.error("El artista o usuario indicados no existen. Artista: "+
					artistId + " Usuario: " + userId);
			return new ResponseEntity<String>("El artista o usuario indicados no existen. Artista: "+
				artistId + " Usuario: " + userId, HttpStatus.NOT_FOUND);
		}
		else {
			int rows = artistService.unfollowArtist(artistId, userId);
			if (rows < 1) {
				logger.error("No ha podido dejar de seguirse al artista: " + artistId
						+ " con el usuario: " + userId);
				return new ResponseEntity<String>("No ha podido dejar de seguirse al artista: " + artistId
					+ " con el usuario: " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.warn("Se ha dejado de seguir al artista: " + artistId + " con el usuario: "
						+ userId);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
	
	@GetMapping(value = "/private/artist/user/{artistId}/{userId}")
	public ResponseEntity isFollowingArtist(@PathVariable int artistId, @PathVariable int userId) {
		if ((artistService.getArtist(artistId) == null ) || userService.getUser(userId) == null) {
			logger.error("El artista o usuario indicados no existen. Artista: "+
					artistId + " Usuario: " + userId);
			return new ResponseEntity<String>("El artista o usuario indicados no existen. Artista: "+
				artistId + " Usuario: " + userId, HttpStatus.NOT_FOUND);
		}
		else {
			List<Boolean> list = new ArrayList<Boolean>();
			list.add(artistService.isFollowingArtist(artistId, userId));
			logger.warn("El artista: " + artistId + " sigue al usuario: "
					+ userId + ": " + list.get(0));		
			return new ResponseEntity<List<Boolean>>( list,HttpStatus.OK);
		}
	}
	
	@GetMapping(value = "/private/artists/user/{userId}")
	public ResponseEntity getRecommendedArtists(@PathVariable int userId) {
		if (userService.getUser(userId) == null) {
			logger.error("El usuario indicado no existe. Usuario: " + userId);
			return new ResponseEntity<String>("El usuario indicado no existe. Usuario: " + userId, HttpStatus.NOT_FOUND);
		}
		else {
			//List<Boolean> list = new ArrayList<Boolean>();
			//list.add(artistService.isFollowingArtist(artistId, userId));
			//logger.warn("El artista: " + artistId + " sigue al usuario: "
			//		+ userId + ": " + list.get(0));	
			List<Artist> list = new ArrayList<Artist>();
			list = artistService.getRecommendedArtist(userId);
			return new ResponseEntity<List<Artist>>( list,HttpStatus.OK);
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
