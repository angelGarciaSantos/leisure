package es.udc.fi.tfg.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import es.udc.fi.tfg.config.JsonDateSerializer;

@Entity
@Table(name = "Comment")
public class Comment {	

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
    
	@Column
	private String text;
	
	@Column(name = "date", nullable = false, updatable=true)
    @Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using=JsonDateSerializer.class)
    private Date date;

	@ManyToOne(cascade = CascadeType.ALL)
	private User user;
    
	@ManyToOne(cascade = CascadeType.ALL)
	private Event event;
	
	public Comment (int id, String text){
		this.id = id;
		this.text = text;
	}
	
	public Comment (int id, String text, User user, Event event){
		this.id = id;
		this.text = text;
		this.user = user;
		this.event = event;
	}
	
	public Comment() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
    
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}
