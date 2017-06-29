package es.udc.fi.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.tfg.dao.ArtistDAO;
import es.udc.fi.tfg.dao.LocalDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.util.EntityNotRemovableException;

@Service
public class LocalService {
	@Autowired
	private LocalDAO localDAO;
	
	public List<Local> getLocals() {
		return localDAO.getLocals();
	}
	
	public List<Local> getLocalsKeywords(String keywords) {
		return localDAO.getLocalsKeywords(keywords);
	}
	
	public Local getLocal(int id){
		return localDAO.getLocal(id);
	}
	
	public void createLocal(Local local){
		localDAO.addLocal(local);
	}
	
	public int deleteLocal(int id) throws EntityNotRemovableException{
		return localDAO.deleteLocal(id);
	}
	
	public int updateLocal(int id, Local local){
		return localDAO.updateLocal(id, local);
	}
}
