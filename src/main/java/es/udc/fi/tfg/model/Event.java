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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import es.udc.fi.tfg.config.CustomJsonDateDeserializer;
import es.udc.fi.tfg.config.JsonDateSerializer;

@Entity
@Table(name = "Event")
public class Event {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
	private int id;
	
	@Column
	private String name;
	
	@Column
    private String description;
	
	@Column(name = "begin_date", nullable = false, updatable=true)
    @Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using=JsonDateSerializer.class)
    private Date beginDate;
	
	@Column(name = "end_date", nullable = false, updatable=true)
    @Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using=JsonDateSerializer.class)
    private Date endDate;
   
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "event_artist", catalog = "leisuredb", joinColumns = {
			@JoinColumn(name = "event_id", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "artist_id",
					nullable = false, updatable = false) })
    private Set<Artist> artists = new HashSet<Artist>();
    
    @ManyToOne(cascade = CascadeType.ALL)
    private Local local;
    
	public Event(int id, String name, String description, Date beginDate, Date endDate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.beginDate = beginDate;
		this.endDate = endDate;
	}
	
	public Event(int id, String name, String description, Date beginDate, Date endDate, Local local) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.local = local;
	}
	
	public Event(){
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	//@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	//@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

    //@JsonIgnore
//	@Column
//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
//	@JoinTable(name = "event_artist", catalog = "leisuredb", joinColumns = {
//			@JoinColumn(name = "event_id", nullable = false, updatable = false) },
//			inverseJoinColumns = { @JoinColumn(name = "artist_id",
//					nullable = false, updatable = false) })
	public Set<Artist> getArtists() {
		return artists;
	}

	public void setArtists(Set<Artist> artists) {
		this.artists = artists;
	}

	
	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}	
}
