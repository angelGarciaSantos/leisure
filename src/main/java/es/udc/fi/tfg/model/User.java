package es.udc.fi.tfg.model;

public class User {

	private Long id;
	private String name;
	private String email;
	//TODO: cambiar por enum.
	private int type;
	
	public User(Long id, String name, String email, int type){
		this.id = id;
		this.name = name;
		this.email = email;
		this.type = type;
	}
	
	public User(){
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}