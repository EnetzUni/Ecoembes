package es.deusto.sd.ecoembes.dto;

public class CredentialsDTO {
	
	//igual q en el ejemplo d auctions, q esto es para el login y todo eso, asiq le ponemos el email y la contraseña y ya esta

	private String email;
	private String password;

	// Getters y Setters
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
