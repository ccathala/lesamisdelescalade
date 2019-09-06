package com.charles.lesamisdelescalade.consumer;

import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.charles.lesamisdelescalade.model.beans.Utilisateur;

public interface UtilisateurDao {
	
	public void addUtilisateur(Utilisateur utilisateur);
	
	public void editUtilisateur(Utilisateur utilisateur, int utilisateurId);
	
	public void deleteUtilisateur(int utilisateurId);
	
	public Utilisateur find(int utilisateurId);
	
	public Utilisateur findByEmail(String utilisateurEmail) throws EmptyResultDataAccessException;
	
	public Utilisateur findByUsername(String utilisateurNom) throws EmptyResultDataAccessException;
	
	public List<Utilisateur> findAll();
	

}