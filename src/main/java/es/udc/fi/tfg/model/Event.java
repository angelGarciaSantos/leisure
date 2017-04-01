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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import es.udc.fi.tfg.config.CustomJsonDateDeserializer;
import es.udc.fi.tfg.config.JsonDateSerializer;

@Entity
@Table(name = "Event")
public class Event {

	private Long id;
	private String name;
    private String description;
    private Date beginDate;
    private Date endDate;
   
    private Set<Artist> artists = new HashSet<Artist>(0);
    
    
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

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    @Column(name = "begin_date", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getBeginDate() {
		return beginDate;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

    @Column(name = "end_date", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getEndDate() {
		return endDate;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "events")
	public Set<Artist> getArtists() {
		return artists;
	}

	public void setArtists(Set<Artist> artists) {
		this.artists = artists;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}	
}
