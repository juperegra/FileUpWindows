package model;

import java.io.Serializable;

public class Usuario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String nombre;
	
	private String apellidos;
	
	private String contrasegna;
	
	
	
	public Usuario() {
		
	}
	
	public Usuario(String id, String nombre, String apellidos, String contrasegna) {
		this.setId(id);
		this.setNombre(nombre);
		this.setApellidos(apellidos);
		this.setContraseña(contrasegna);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getContrasegna() {
		return contrasegna;
	}

	public void setContraseña(String contrasegna) {
		this.contrasegna = contrasegna;
	}

	
	public boolean equals(Usuario u) {
		
		if(this==u) {
			return true;
		}else {
			if (this.id.equals(u.id)) {// en la base de datos, id es clave, por eso, dos usuario seran iguales si sus id's son iguales
				return true;
			}else {
				return false;
			}
		}
		
	}
	
	
}
