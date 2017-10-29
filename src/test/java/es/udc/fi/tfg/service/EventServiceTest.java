package es.udc.fi.tfg.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
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
import es.udc.fi.tfg.model.Event;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class EventServiceTest {
	@Autowired
	EventService eventService;
	
	
	@Test
    public void getEventsTest() {
        List<Event> events = eventService.getEvents(0, -1);
        assertEquals(11, events.size());
    }
	
	@Test
    public void getArtistsKeywordsTest() {
        List<Event> events = eventService.getEventsKeywords("Concierto", 0, -1);
        assertEquals(7, events.size());
    }
	
	@Test
    public void getEventTest() {
        Event event = eventService.getEvent(1);
        assertEquals("Metallica en concierto", event.getName());
    }
	
	@Test
    public void getRecommendedEventsTest() {
        List<Event> events = eventService.getRecommendedEvents(1);
        assertEquals(5, events.size());
    }
	
	@Test
    public void createAndDeleteEventTest() throws EntityNotCreatableException, EntityNotRemovableException {
        Event event = new Event();
        event.setName("Test");
        event.setDescription("Test");
        event.setBeginDate(new Date());
        event.setEndDate(new Date());
		
		eventService.createEvent(event, 1);	
		List<Event> events = eventService.getEventsKeywords("Test", 0, -1);
        
		assertEquals(events.get(0).getName(), event.getName());
		
		eventService.deleteEvent(events.get(0).getId());	
		List<Event> events2 = eventService.getEventsKeywords("Test", 0, -1);

		assertEquals(events2.size(), 0);
    }
	
	@Test
    public void updateEventTest() throws EntityNotCreatableException, EntityNotRemovableException, EntityNotUpdatableException {
        Event event = new Event();
        event.setName("Test");
        event.setDescription("Test");
        event.setBeginDate(new Date());
        event.setEndDate(new Date());
		
		eventService.createEvent(event, 1);	
		List<Event> events = eventService.getEventsKeywords("Test", 0, -1);
        
		assertEquals(events.get(0).getName(), event.getName());
		
		Event eventToUp = events.get(0);
		eventToUp.setName("New");
		eventService.updateEvent(eventToUp.getId(), eventToUp);
		
		List<Event> events2 = eventService.getEventsKeywords("New", 0, -1);
        
		assertEquals(events2.get(0).getName(), eventToUp.getName());
		
		eventService.deleteEvent(events2.get(0).getId());	
    }
	
	@Test
    public void addAndDeleteArtistToEventTest() throws EntityNotCreatableException, EntityNotRemovableException {				
		eventService.addArtistToEvent(1, 3);
		List<Integer> events = eventService.getEventsFromArtist(3);
		
		assertEquals(events.size(), 1);
		
		eventService.deleteArtistFromEvent(1, 3);
		
		List<Integer> events2 = eventService.getEventsFromArtist(3);
		
		assertEquals(events2.size(), 0);
    }
	
	@Test
    public void modifyLocalFromEventTest() throws EntityNotCreatableException, EntityNotRemovableException, EntityNotUpdatableException {				
        Event event = new Event();
        event.setName("Test");
        event.setDescription("Test");
        event.setBeginDate(new Date());
        event.setEndDate(new Date());
		
		eventService.createEvent(event, 1);	
		List<Event> events = eventService.getEventsKeywords("Test", 0, -1);
		Event eventTest = events.get(0);
		
		eventService.modifyLocalFromEvent(eventTest.getId(), 1);
		Event eventTest2 = eventService.getEvent(eventTest.getId());
		
		
		assertEquals(eventTest2.getLocal().getId(), 1);
		
		eventService.deleteEvent(eventTest2.getId());
    }
	
	@Test
    public void getEventsFromLocalTest() {
        List<Event> events = eventService.getEventsFromLocal(1);
        assertEquals(0, events.size());
    }
	
	@Test
    public void getNextEventsFromArtistTest() {
        List<Event> events = eventService.getNextEventsFromArtist(1);
        assertEquals(2, events.size());
    }
	
	
	@Test
    public void getNextEventsFromLocalTest() {
        List<Event> events = eventService.getNextEventsFromLocal(1);
        assertEquals(0, events.size());
    }
	
	@Test
    public void getNextEventsFromTagTest() {
        List<Event> events = eventService.getNextEventsFromTag(1);
        assertEquals(6, events.size());
    }
	
}
