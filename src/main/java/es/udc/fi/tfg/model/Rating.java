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

@Entity
public class Rating {

	private Long id;
    
	private double rating;
    private User user;
    private Event event;
	
	public Rating (Long id, double rating){
		this.id = id;
		this.rating = rating;	
	}
	
	public Rating (Long id, double rating, User user, Event event){
		this.id = id;
		this.rating = rating;
		this.user = user;
		this.event = event;
	}
	
	public Rating(){	
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
	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
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
