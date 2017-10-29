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
import es.udc.fi.tfg.model.Tag;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class TagServiceTest {
	@Autowired
	TagService tagService;
	
	@Test
    public void getTagsTest() {
        List<Tag> tags = tagService.getTags(0, -1);
        assertEquals(8, tags.size());
    }
	
	@Test
    public void getTagsKeywords() {
        List<Tag> tags = tagService.getTagsKeywords("Rock", 0, -1);
        assertEquals(1, tags.size());
    }
	
	@Test
    public void getTagsFromArtistTest() {
        List<Tag> tags = tagService.getTagsFromArtist(1, 0, -1);
        assertEquals(1, tags.size());
    }
	
	@Test
    public void getTagsFromEventTest() {
        List<Tag> tags = tagService.getTagsFromEvent(1);
        assertEquals(2, tags.size());
    }
	
	@Test
    public void getTagTest() {
        Tag tag = tagService.getTag(1);
        assertEquals("Rock", tag.getName());
    }
	
	@Test
    public void createDeleteExistsUpdateTagTest() throws EntityNotRemovableException, EntityNotCreatableException, EntityNotUpdatableException {
        Tag tag = new Tag();
        tag.setName("Test");
        tagService.createTag(tag);
        
        assertEquals(tagService.existsTag(tag), true);
		
        Tag result = tagService.getTagsKeywords("Test", 0, -1).get(0);
        
        result.setName("New");
        tagService.updateTag(result.getId(), result);
        
        Tag result2 = tagService.getTagsKeywords("New", 0, -1).get(0);
        
        assertEquals(tagService.existsTag(result2), true);

        
		tagService.deleteTag(result2.getId());
		
        assertEquals(tagService.existsTag(result2), false);
    }
	
	
}
