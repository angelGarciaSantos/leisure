package es.udc.fi.tfg.dao;

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
import es.udc.fi.tfg.model.Tag;
import es.udc.fi.tfg.util.EntityNotCreatableException;
import es.udc.fi.tfg.util.EntityNotRemovableException;
import es.udc.fi.tfg.util.EntityNotUpdatableException;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class TagDAOTest {
	@Autowired
	TagDAO tagDAO;
	
	@Test
    public void getTagsTest() {
        List<Tag> tags = tagDAO.getTags(0, -1);
        assertEquals(6, tags.size());
    }
	
	@Test
    public void getTagsKeywords() {
        List<Tag> tags = tagDAO.getTagsKeywords("Rock", 0, -1);
        assertEquals(1, tags.size());
    }
	
	@Test
    public void getTagsFromArtistTest() {
        List<Integer> tags = tagDAO.getTagsFromArtist(1);
        assertEquals(1, tags.size());
    }
	
	@Test
    public void getTagTest() {
        Tag tag = tagDAO.getTag(1);
        assertEquals("Rock", tag.getName());
    }
	
	@Test
    public void createDeleteExistsUpdateTagTest() throws EntityNotRemovableException, EntityNotCreatableException, EntityNotUpdatableException {
        Tag tag = new Tag();
        tag.setName("Test");
        tagDAO.addTag(tag);
        
		
        Tag result = tagDAO.getTagsKeywords("Test", 0, -1).get(0);
        
        assertEquals(result.getName(), "Test");
        
        result.setName("New");
        tagDAO.updateTag(result.getId(), result);
        
        Tag result2 = tagDAO.getTagsKeywords("New", 0, -1).get(0);
        
        assertEquals(result2.getName(), "New");
        
        tagDAO.deleteTag(result2.getId());
    }
}
