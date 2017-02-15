package es.udc.fi.tfg.model;

public class Local {

	private Long id;
	private String name;
	private String description;
	private int capacity;
	private double rating;
	
	public Local(long id, String name, String description, int capacity, double rating) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.capacity = capacity;
		this.rating = rating;
	}
	
	public Local(){
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

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
}
