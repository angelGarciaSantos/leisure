package es.udc.fi.tfg.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
	private int id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String image;
//    @Column
//    @ElementCollection(targetClass=Event.class)
//    private Set<Event> events = new HashSet<Event>(0);
    
//    @JsonIgnore
//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinTable(name = "user_artist", catalog = "leisuredb", joinColumns = {
//			@JoinColumn(name = "artist_id", nullable = false, updatable = false) },
//			inverseJoinColumns = { @JoinColumn(name = "user_id",
//					nullable = false, updatable = false) })
//    private Set<User> users = new HashSet<User>();
    
    //@JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "tag_artist", catalog = "leisuredb", joinColumns = {
			@JoinColumn(name = "artist_id", nullable = false, updatable = false) },
			inverseJoinColumns = { @JoinColumn(name = "tag_id",
					nullable = false, updatable = false) })
    private Set<Tag> tags = new HashSet<Tag>();
	
	public Artist(int id, String name, String description, String image) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.image = image;
	}
	
	public Artist(String name, String description, String image) {
		this.name = name;
		this.description = description;
		this.image = image;
	}
	
	public Artist(String name, String description) {
		this.name = name;
		this.description = description;
		this.image = "https://pbs.twimg.com/profile_images/628298219630534656/g3OhoQ5k_400x400.jpg";
	}
	
	public Artist(){
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
//	@ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="artists")
//	public Set<Event> getEvents() {
//		return events;
//	}
//
//	public void setEvents(Set<Event> events) {
//		this.events = events;
//	}

//	public Set<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(Set<User> users) {
//		this.users = users;
//	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;

        if (id != artist.id) return false;
        if (name != null ? !name.equals(artist.name) : artist.name != null) return false;
        if (description != null ? !description.equals(artist.description) : artist.description != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
    	Integer result = (int) (long) id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
