package model;

import java.io.Serializable;

public class Fichero implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String nombre;
	
	private Usuario emisor;
	
	private Usuario receptor;
	
	private String ruta;
	
	private String codificacion;
	
	public static int numficheros=0;// al introducir un nuevo fichero a la bd, se le asigna este numero como id y se incrementa en 1
	//al iniciarse el servidor, se busca en la bd de alguna mandera la forma de asignar los id(que ya se me ocurrira)
	
	public Fichero(String id, String nombre, Usuario emisor, Usuario receptor, String ruta, String codificacion) {
		this.id=id;
		this.nombre=nombre;
		this.emisor=emisor;
		this.receptor=receptor;
		this.ruta=ruta;
		this.codificacion=codificacion;
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
	
	public Usuario getEmisor() {
		return emisor;
	}
	
	public void setEmisor(Usuario emisor) {
		this.emisor = emisor;
	}
	
	public Usuario getReceptor() {
		return receptor;
	}
	
	public void setReceptor(Usuario receptor) {
		this.receptor = receptor;
	}
	
	public String getRuta() {
		return ruta;
	}
	
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	
	public String getCodificacion() {
		return codificacion;
	}
	
	public void setCodificacion(String codificacion) {
		this.codificacion = codificacion;
	}
	
	public boolean equals(Fichero f) {
		if(this==f) {
			return true;
		}else {
			if(this.id.equals(f.id)) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	public String toString() {
		
		return this.id+" "+this.nombre+" "+this.emisor.getId()+" "+this.receptor.getId()+" "+this.ruta+" "+this.codificacion+" ";
		
	}
	
}