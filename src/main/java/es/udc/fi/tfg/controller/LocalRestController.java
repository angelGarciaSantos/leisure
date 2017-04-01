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

@CrossOrigin
@RestController
public class LocalRestController {
	@Autowired
	private LocalDAO localDAO;

	
	@GetMapping("/locals")
	public List getLocals() {
		return localDAO.getLocals();
	}	
	
	@GetMapping("/locals/{id}")
	public Local getLocal(@PathVariable int id) {
		return localDAO.getLocal(id);
	}	
	
	@PostMapping(value = "/locals")
	public ResponseEntity createLocal(@RequestBody Local local) {

		localDAO.addLocal(local);

		return new ResponseEntity(local, HttpStatus.OK);
	}
	
	@DeleteMapping("/locals/{id}")
	public ResponseEntity deleteLocal(@PathVariable int id) {

		if (0 == localDAO.deleteLocal(id)) {
			return new ResponseEntity("No Local found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(id, HttpStatus.OK);

	}

	@PutMapping("/locals/{id}")
	public ResponseEntity updateLocal(@PathVariable int id, @RequestBody Local local) {

		int rows = localDAO.updateLocal(id, local);
		if (0 == rows) {
			return new ResponseEntity("No Local found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(rows, HttpStatus.OK);
	}
}
