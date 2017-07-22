package es.udc.fi.tfg.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import es.udc.fi.tfg.config.AppConfig;
import es.udc.fi.tfg.model.Artist;
import es.udc.fi.tfg.model.Local;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class LocalServiceTest {
	@Autowired
	LocalService localService;
	
	@Test
    public void getLocalsTest() {
        List<Local> locals = localService.getLocals(0, -1);
        assertEquals(3, locals.size());
    }
	
	@Test
    public void getLocalsKeywordsTest() {
        List<Local> locals = localService.getLocalsKeywords("Playa", 0, -1);
        assertEquals(1, locals.size());
    }
	
	@Test
    public void getLocalTest() {
        Local local = localService.getLocal(1);
        assertEquals("Playa Club", local.getName());
    }
	
	@Test
    public void createDeleteExistsLocalTest() throws EntityNotCreatableException, EntityNotRemovableException {
        Local local = new Local();
        local.setCapacity(10);
        local.setName("Test");
        local.setDescription("Test");
        
        localService.createLocal(local);
        
        List<Local> locals = localService.getLocalsKeywords("Test", 0, -1);
        assertEquals(locals.size(), 1);
        assertEquals(localService.existsLocal(locals.get(0)), true);
        
        localService.deleteLocal(locals.get(0).getId());
        
        assertEquals(localService.existsLocal(locals.get(0)), false);
    }
	
	@Test
    public void updateLocalTest() throws EntityNotCreatableException, EntityNotRemovableException, EntityNotUpdatableException {
        Local local = new Local();
        local.setCapacity(10);
        local.setName("Test");
        local.setDescription("Test");
        
        localService.createLocal(local);
        
        List<Local> locals = localService.getLocalsKeywords("Test", 0, -1);
        assertEquals(locals.size(), 1);
        assertEquals(localService.existsLocal(locals.get(0)), true);
        
        Local update = locals.get(0);
        update.setName("New");
        
        localService.updateLocal(update.getId(), update);
        
        List<Local> locals2 = localService.getLocalsKeywords("New", 0, -1);
        assertEquals(locals2.size(), 1);
        
        
        localService.deleteLocal(locals2.get(0).getId());
        
        assertEquals(localService.existsLocal(locals2.get(0)), false);
    }
	
	
}
