package es.udc.fi.tfg.model;

import java.util.Date;

public class Event {
	
	private Long id;
	private String name;
	private String description;
	private Date beginDate;
	private Date endDate;
	
	public Event(long id, String name, String description, Date beginDate, Date endDate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.beginDate = beginDate;
		this.endDate = endDate;
	}
	
	public Event(){
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
