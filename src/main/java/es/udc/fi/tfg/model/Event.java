package es.udc.fi.tfg.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="event_id")
	private Long id;
    @Column
	private String name;
    @Column
    private String description;
    @Column
    private Date beginDate;
    @Column
    private Date endDate;
	
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "events")
    private Set<Artist> artists = new HashSet<Artist>(0);
    
    @ManyToOne(cascade = CascadeType.ALL)
    private Local local;
    
	public Event(long id, String name, String description, Date beginDate, Date endDate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.beginDate = beginDate;
		this.endDate = endDate;
	}
	
	public Event(long id, String name, String description, Date beginDate, Date endDate,Set<Artist> artists, Local local) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.artists = artists;
		this.local = local;
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

	public Set<Artist> getArtists() {
		return artists;
	}

	public void setArtists(Set<Artist> artists) {
		this.artists = artists;
	}
}
