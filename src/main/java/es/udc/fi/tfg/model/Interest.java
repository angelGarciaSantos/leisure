package es.udc.fi.tfg.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Interest {

	private int id;
    
	private int points;
    private User user;
    private Tag tag;
	
	public Interest (int id, int points){
		this.id = id;
		this.points = points;	
	}
	
	public Interest (int id, int points, User user, Tag tag){
		this.id = id;
		this.points = points;
		this.user = user;
		this.tag = tag;
	}
	
	public Interest(){	
	}

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	
    @ManyToOne(cascade = CascadeType.ALL)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JoinColumn(name="tag_id")
    @ManyToOne(cascade = CascadeType.ALL)
	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interest interest = (Interest) o;

        if (id != interest.id) return false;
        if (points !=(interest.points)) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
    	Integer result = (int) (long) id;
        return result;
    }
}
