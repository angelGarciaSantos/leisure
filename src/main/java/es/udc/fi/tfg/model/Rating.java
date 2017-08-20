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
@Table(name = "Rating")
public class Rating {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
    
	@Column
	private double rating;
	
	@Column(name = "date", nullable = false, updatable=true)
    @Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using=JsonDateSerializer.class)
    private Date date;
	
	@ManyToOne(cascade = CascadeType.ALL)
    private User user;
	
    @ManyToOne(cascade = CascadeType.ALL)
    private Event event;
	
	public Rating (int id, double rating){
		this.id = id;
		this.rating = rating;	
	}
	
	public Rating (int id, double rating, User user, Event event){
		this.id = id;
		this.rating = rating;
		this.user = user;
		this.event = event;
	}
	
	public Rating(){	
	}

    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
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
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating = (Rating) o;

        if (id != rating.id) return false;
        if (this.rating != rating.rating) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
    	Integer result = (int) (long) id;
 
        return result;
    }
}
