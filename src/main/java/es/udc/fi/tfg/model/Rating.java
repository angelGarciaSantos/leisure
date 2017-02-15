package es.udc.fi.tfg.model;

public class Rating {

	private Long id;
	private double rating;
	
	public Rating (Long id, double rating){
		this.id = id;
		this.rating = rating;	
	}
	
	public Rating(){	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
}
