package es.udc.fi.tfg.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.tfg.dao.ArtistDAO;
import es.udc.fi.tfg.dao.LocalDAO;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@Service
public class LocalService {
	@Autowired
	private LocalDAO localDAO;
	
	public List<Local> getLocals(int first, int max) {
		return localDAO.getLocals(first, max);
	}
	
	public List<Local> getLocalsKeywords(String keywords, int first, int max) {
		return localDAO.getLocalsKeywords(keywords, first, max);
	}
	
	public Local getLocal(int id){
		return localDAO.getLocal(id);
	}
	
	public boolean existsLocal (Local local){
		List<Local> locals = new ArrayList<Local>();
		locals = localDAO.getLocals(0,-1);
		boolean exists = false;
        for(Local a : locals) {
        	if (local.getName().equals(a.getName())){
        		exists = true;
        	}
        }
		return exists;
	}
	
	public void createLocal(Local local) throws EntityNotCreatableException{
		localDAO.addLocal(local);
	}
	
	public int deleteLocal(int id) throws EntityNotRemovableException{
		return localDAO.deleteLocal(id);
	}
	
	public int updateLocal(int id, Local local) throws EntityNotUpdatableException{
		return localDAO.updateLocal(id, local);
	}
}
