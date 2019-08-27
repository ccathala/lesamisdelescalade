package com.charles.lesamisdelescalade.business.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.charles.lesamisdelescalade.business.interfaces.LoginManager;
import com.charles.lesamisdelescalade.consumer.interfaces.UtilisateurManager;
import com.charles.lesamisdelescalade.model.beans.Utilisateur;

/* Login Package Interface */
@Service
public class LoginImpl implements LoginManager {


	/* Logger for LoginImpl class */
	private static final Logger logger = LoggerFactory.getLogger(LoginImpl.class);

	/* Dependency injection Interface Utilisateur */
	@Autowired
	private UtilisateurManager utilisateurManager;

	

	/**
	 * Search existing user in database, throws exception if input email don't exist
	 */
	@Override
	public Utilisateur searchUserByMail(Utilisateur utilisateur) throws EmptyResultDataAccessException {

		/* Search User by email in database and return this user */
		return utilisateurManager.findByEmail(utilisateur.getEmail());

	}

	/**
	 * Verify password correspondence
	 */
	@Override
	public boolean passwordIsCorresponding(Utilisateur utilisateurSession, Utilisateur utilisateurFromDatabase) {

		if (BCrypt.checkpw(utilisateurSession.getPassword(), utilisateurFromDatabase.getPassword())) {
			/* Password is correct */

			/* Display connection success log */
			logger.debug("Connexion de l'utilisateur ID: " + utilisateurSession.getId() + ".");

			return true;
		} else {
			/* Password is not correct */

			/* Display connection failed log */
			logger.debug("Echec de connexion - Mot de passe incorrect");

			return false;
		}

	}

	/**
	 * Fill user session bean with missing attributes
	 */
	@Override
	public void fillUserSessionBean(Utilisateur utilisateurSession, Utilisateur utilisateurFromDatabase) {

		utilisateurSession.setId(utilisateurFromDatabase.getId());
		utilisateurSession.setNom(utilisateurFromDatabase.getNom());
		utilisateurSession.setRole_id(utilisateurFromDatabase.getRole_id());

	}
	
	/**
	 * Register new user in database, if registration fail return error message
	 */
	@Override
	public String registerNewUser(Utilisateur utilisateurRegister) {
		
		/* Property declaration */
		boolean nameIsAlreadyUsed;
		boolean emailIsAlreadyUsed;
		boolean passwordIsWellConfirmed;
		String messageError = "";
		
		/* Test unsername, if exception is catch, input username is not already used */
		try {
			utilisateurManager.findByUsername(utilisateurRegister.getNom());
			nameIsAlreadyUsed = true;
			messageError = "Echec de l'inscription - Le nom saisi est déja utilisé";
			logger.info("Registration attempt failed - Username already exist");
		} catch (EmptyResultDataAccessException e) {
			nameIsAlreadyUsed = false;
		}
		
		/* Test email, if exception is catch, input email is not already used */
		try {
			utilisateurManager.findByEmail(utilisateurRegister.getEmail());
			emailIsAlreadyUsed = true;
			messageError = "Echec de l'inscription - L'adresse email saisie est déja utilisée";
			logger.info("Registration attempt failed - Email address already exist");
		} catch (EmptyResultDataAccessException e) {
			emailIsAlreadyUsed = false;
		}
		
		/* Check correspondence between password and passwordConfirmation */
		if (utilisateurRegister.getPassword().equals(utilisateurRegister.getConfirmPassword())) {
			
			/* inputs match*/
			passwordIsWellConfirmed = true;
		} else {
			
			/* inputs don't match*/
			passwordIsWellConfirmed = false;
			messageError = "Echec de l'inscription - Les mots de passe saisis ne sont pas identiques";
			logger.info("Registration attempt failed - Password and confirm password don't match");
		}
		
		/* Check that all conditions for creating a new user are reached */
		if (!nameIsAlreadyUsed && !emailIsAlreadyUsed && passwordIsWellConfirmed) {
			
			/* Conditions are reached*/
			utilisateurRegister.setPassword(BCrypt.hashpw(utilisateurRegister.getPassword(), BCrypt.gensalt()));
			utilisateurManager.addUtilisateur(utilisateurRegister);
			logger.info("Registration successful");
			

		}
		/* Return error message, can be empty */
		return messageError;

	}

	

}