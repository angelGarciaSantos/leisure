package es.udc.fi.tfg.model;

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

@Entity
@Table(name = "Comment")
public class Comment {	

	private Long id;
    
	private String text;

	private User user;
    
	private Event event;
	
	public Comment (Long id, String text){
		this.id = id;
		this.text = text;
	}
	
	public Comment (Long id, String text, User user, Event event){
		this.id = id;
		this.text = text;
		this.user = user;
		this.event = event;
	}
	
	public Comment() {
	}
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
    
	@ManyToOne(cascade = CascadeType.ALL)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}
