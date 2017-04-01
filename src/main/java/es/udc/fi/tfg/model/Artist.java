package es.udc.fi.tfg.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="artist_id")
	private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private double rating;
    
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "event_artist", catalog = "leisuredb", joinColumns = {
			@JoinColumn(name = "artist_id", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "event_id",
					nullable = false, updatable = false) })
    private Set<Event> events = new HashSet<Event>(0);
    
	
	public Artist(long id, String name, String description, double d) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.rating = d;
	}
	
	public Artist(long id, String name, String description, double d, Set<Event> events) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.rating = d;
		this.events = events;
	}
	
	public Artist(){
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

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}
}
