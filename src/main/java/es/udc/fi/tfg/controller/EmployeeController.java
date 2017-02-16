package es.udc.fi.tfg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.tfg.dao.EmployeeDAO;
import es.udc.fi.tfg.model.Employee;

@RestController
public class EmployeeController {

	
	@Autowired
	private EmployeeDAO employeeDAO;

	
	@GetMapping("/employees")
	public List getEmployees() {
		return employeeDAO.getEmployees();
	}

//	@GetMapping("/customers/{id}")
//	public ResponseEntity getCustomer(@PathVariable("id") Long id) {
//
//		Customer customer = customerDAO.get(id);
//		if (customer == null) {
//			return new ResponseEntity("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
//		}
//
//		return new ResponseEntity(customer, HttpStatus.OK);
//	}

	@PostMapping(value = "/employees")
	public ResponseEntity createEmployee(@RequestBody Employee employee) {

		employeeDAO.addEmployee(employee);

		return new ResponseEntity(employee, HttpStatus.OK);
	}

	@DeleteMapping("/employees/{id}")
	public ResponseEntity deleteEmployee(@PathVariable int id) {

		if (0 == employeeDAO.deleteEmployee(id)) {
			return new ResponseEntity("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(id, HttpStatus.OK);

	}

	@PutMapping("/employees/{id}")
	public ResponseEntity updateEmployee(@PathVariable int id, @RequestBody Employee employee) {

		int rows = employeeDAO.updateEmployee(id, employee);
		if (0 == rows) {
			return new ResponseEntity("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(rows, HttpStatus.OK);
	}

}