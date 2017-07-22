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

import es.udc.fi.tfg.dao.ArtistDAO;
import es.udc.fi.tfg.dao.LocalDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.service.ArtistService;
import es.udc.fi.tfg.service.LocalService;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@CrossOrigin
@RestController
public class LocalRestController {
	private Logger logger = Logger.getLogger(LocalRestController.class);
	@Autowired
	private LocalService localService;
	
	@GetMapping("/locals/{first}/{max}")
	public ResponseEntity<List<Local>> getLocals(@PathVariable int first, @PathVariable int max ) {
		logger.info("Obteniendo todos los locales.");
		return new ResponseEntity<List<Local>>(localService.getLocals(first, max), HttpStatus.OK);
	}	
	
	@GetMapping("/locals/keywords/{keywords}/{first}/{max}")
	public ResponseEntity<List<Local>> getLocalsKeywords(@PathVariable String keywords, @PathVariable int first, @PathVariable int max) {
		logger.info("Obteniendo todos los locales por palabras clave: "+keywords);
		return new ResponseEntity<List<Local>>(localService.getLocalsKeywords(keywords, first, max), HttpStatus.OK);
	}	
	
	@GetMapping("/locals/{id}")
	public ResponseEntity<Local> getLocal(@PathVariable int id) {
		Local local = localService.getLocal(id);
		if (local == null){
			logger.error("No se ha encontrado el local: "+id);
			return new ResponseEntity<Local>(HttpStatus.NOT_FOUND);
		}
		else {
			logger.info("Obteniendo local: "+id);
			return new ResponseEntity<Local>(local, HttpStatus.OK);
		}
	}	
	
	@PostMapping(value = "/admin/locals")
	public ResponseEntity<Local> createLocal(@RequestBody Local local) throws EntityNotCreatableException {
		if (localService.existsLocal(local)){
			logger.error("No ha podido crearse el local: "+local.getName());
			return new ResponseEntity<Local>(HttpStatus.CONFLICT);	
		}
		else {
			logger.info("Creando local: "+local.getName());
			localService.createLocal(local);
			return new ResponseEntity<Local>(HttpStatus.CREATED);			
		}
	}
	
	@DeleteMapping("/admin/locals/{id}")
	public ResponseEntity<String> deleteLocal(@PathVariable int id) {
		int rows;
		if (localService.getLocal(id)==null){
			logger.error("No se encuentra el local: "+id);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);		
		}
		else {
			try {
				rows = localService.deleteLocal(id);
			}
			catch(EntityNotRemovableException e){
				logger.error("No ha podido eliminarse el local: "+id);
				return new ResponseEntity<String>(HttpStatus.LOCKED);
			}
			catch(Exception e){
				logger.error("No ha podido eliminarse el local: "+id);
				return new ResponseEntity<String>("No ha podido eliminarse el Local: " + id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if (rows < 1) {
				logger.error("No ha podido eliminarse el local: "+id);
				return new ResponseEntity<String>("No ha podido eliminarse el Local "+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Eliminando local: "+id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}			
		}
	}

	@PutMapping("/admin/locals/{id}")
	public ResponseEntity<String> updateLocal(@PathVariable int id, @RequestBody Local local) throws EntityNotUpdatableException {
		if (local.getId()!=id) {
			logger.error("los ids no coinciden: "+id);
			return new ResponseEntity<String>("Los ids no coinciden"+id, HttpStatus.BAD_REQUEST);
		}
		else if (localService.getLocal(id)==null){
			logger.error("No ha encontrado el local: "+id);
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);		
		}
		else {
			int rows = localService.updateLocal(id, local);
			if (rows < 1) {
				logger.error("No ha podido actualizarse el local: "+id);
				return new ResponseEntity<String>("No ha podido actualizarse el Local "+id, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else{
				logger.info("Se ha actualizado el local: "+id);
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
	}
}
