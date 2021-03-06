package com.charles.lesamisdelescalade.model.beans;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Bean Secteur
 * 
 * @author Charles
 *
 */
public class Secteur {
	
	private int id;
	@NotEmpty
	@Size(min=3, message="doit contenir au minimum 3 caractères")
	private String nom;
	@NotEmpty
	@Size(min=30, message="doit contenir au minimum 30 caractères")
	private String description;
	@NotNull
	private int site_id;
	
	private int voiesCount;
	private String cotationMin;
	private String cotationMax;
	
	
	public Secteur() {
	
	}
	public Secteur(int id, String nom, String description, int site_id) {
		this.id = id;
		this.nom = nom;
		this.description = description;
		this.site_id = site_id;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getSite_id() {
		return site_id;
	}
	public void setSite_id(int site_id) {
		this.site_id = site_id;
	}
	
	public int getVoiesCount() {
		return voiesCount;
	}
	public void setVoiesCount(int voiesCount) {
		this.voiesCount = voiesCount;
	}
	public String getCotationMin() {
		return cotationMin;
	}
	public void setCotationMin(String cotationMin) {
		this.cotationMin = cotationMin;
	}
	public String getCotationMax() {
		return cotationMax;
	}
	public void setCotationMax(String cotationMax) {
		this.cotationMax = cotationMax;
	}
	@Override
	public String toString() {
		return "Secteur [id=" + id + ", nom=" + nom + ", description=" + description + ", site_id=" + site_id + "]";
	}
	
	
	
	

}
