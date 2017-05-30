package es.udc.fi.tfg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.tfg.dao.TagDAO;
import es.udc.fi.tfg.model.Tag;

@Service
public class TagService {
	@Autowired
	private TagDAO tagDAO;
	
	public List<Tag> getTags() {
		return tagDAO.getTags();
	}
	
	public List<Tag> getTagsKeywords(String keywords) {
		return tagDAO.getTagsKeywords(keywords);
	}
	
	public Tag getTag(int id){
		return tagDAO.getTag(id);
	}
	
	public void createTag(Tag tag){
		tagDAO.addTag(tag);
	}
	
	public int deleteTag(int id){
		return tagDAO.deleteTag(id);
	}
	
	public int updateTag(int id, Tag tag){
		return tagDAO.updateTag(id, tag);
	}
}
