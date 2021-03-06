package com.charles.lesamisdelescalade.model.beans;

/**
 * Bean Departement
 * 
 * @author Charles
 *
 */
public class Departement {
	
	private int id;
	private String nom;
	private String code;
	
	public Departement() {
		super();
	}
		
	public Departement(int id, String nom, String code) {
		super();
		this.id = id;
		this.nom = nom;
		this.code = code;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Departement [id=" + id + ", nom=" + nom + ", code=" + code + "]";
	}
	
	
	
	

}
