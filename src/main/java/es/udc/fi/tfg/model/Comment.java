package es.udc.fi.tfg.model;

public class Comment {	

	private Long id;
	private String text;
	
	public Comment (Long id, String text){
		this.id = id;
		this.text = text;
	}
	
	public Comment() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
