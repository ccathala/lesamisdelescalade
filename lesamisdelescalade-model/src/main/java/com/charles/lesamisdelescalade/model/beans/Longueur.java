package com.charles.lesamisdelescalade.model.beans;

public class Longueur {
	
	private int id;
	private int numero;
	private int cotation_id;
	private int voie_id;
	
	private String cotation;

	public Longueur() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Longueur(int id, int numero, int cotation_id, int voie_id, String cotation) {
		super();
		this.id = id;
		this.numero = numero;
		this.cotation_id = cotation_id;
		this.voie_id = voie_id;
		this.cotation = cotation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getCotation_id() {
		return cotation_id;
	}

	public void setCotation_id(int cotation_id) {
		this.cotation_id = cotation_id;
	}

	public int getVoie_id() {
		return voie_id;
	}

	public void setVoie_id(int voie_id) {
		this.voie_id = voie_id;
	}

	public String getCotation() {
		return cotation;
	}

	public void setCotation(String cotation) {
		this.cotation = cotation;
	}

	@Override
	public String toString() {
		return "Longueur [id=" + id + ", numero=" + numero + ", cotation_id=" + cotation_id + ", voie_id=" + voie_id
				+ ", cotation=" + cotation + "]";
	}
	
	
	
	
	

}
