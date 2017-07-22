package es.udc.fi.tfg.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Local {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column
	private String name;
	@Column
	private String description;
	@Column
	private int capacity;
	@Column
	private double lat;
	@Column
	private double lng;
	@Column
    private String image;
	
	public Local(int id, String name, String description, int capacity) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.capacity = capacity;
		this.image = "http://guitarchic.net/wp-content/uploads/2017/01/1-1.jpg";
	}
	
	public Local(String name, String description, int capacity, double lat, double lng) {
		this.name = name;
		this.description = description;
		this.capacity = capacity;
		this.lat = lat;
		this.lng = lng;
		this.image = "http://guitarchic.net/wp-content/uploads/2017/01/1-1.jpg";
	}
	
	public Local(String name, String description, int capacity, double lat, double lng, String image) {
		this.name = name;
		this.description = description;
		this.capacity = capacity;
		this.lat = lat;
		this.lng = lng;
		this.image = image;
	}
	
	public Local(){
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

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Local local = (Local) o;

        if (id != local.id) return false;
        if (name != null ? !name.equals(local.name) : local.name != null) return false;
        if (description != null ? !description.equals(local.description) : local.description != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
    	Integer result = (int) (long) id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
