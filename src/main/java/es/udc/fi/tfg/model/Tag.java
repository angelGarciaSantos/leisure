package es.udc.fi.tfg.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Tag {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="tag_id")
	private int id;
	@Column
	private String name;
	
	public Tag(int id, String name) {
		this.id = id;
		this.name = name;

	}
	
	public Tag(){
	}

	public Integer getId() {
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
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (id != tag.id) return false;
        if (name != null ? !name.equals(tag.name) : tag.name != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
    	Integer result = (int) (long) id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
