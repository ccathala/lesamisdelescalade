package com.charles.lesamisdelescalade.webapp.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.charles.lesamisdelescalade.business.utils.bean.CommentaireManager;
import com.charles.lesamisdelescalade.business.utils.bean.SiteManager;
import com.charles.lesamisdelescalade.business.webcontent.WebContentManager;
import com.charles.lesamisdelescalade.model.beans.Commentaire;
import com.charles.lesamisdelescalade.model.beans.Secteur;
import com.charles.lesamisdelescalade.model.beans.Utilisateur;
import com.charles.lesamisdelescalade.model.dto.SitePageData;

/**
 * Controller class in relation with site jsp
 * 
 * @author Charles
 *
 */
@Controller
public class SiteController {

	@Autowired
	private WebContentManager webContentManager;
	@Autowired
	private SiteManager siteManager;
	@Autowired
	private CommentaireManager commentaireManager;

	// Set logger
	private static final Logger logger = LoggerFactory.getLogger(SiteController.class);

	/**
	 * Display chosen site page
	 * 
	 * @param locale
	 * @param model
	 * @param sessionUtilisateur
	 * @return
	 */
	@RequestMapping(value = "/site/{siteId}", method = RequestMethod.GET)
	public String displaySite(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "siteId") int siteId) {

		logger.info("Requête d'accès à l'url /site/" + siteId);
		SitePageData sitePageData = webContentManager.setSitePageData(siteId);
		model.addAttribute("site", sitePageData.getSite());
		model.addAttribute("secteurs", sitePageData.getSecteurs());
		model.addAttribute("voies", sitePageData.getVoies());
		model.addAttribute("longueurs", sitePageData.getLongueurs());
		model.addAttribute("sessionUtilisateur", sessionUtilisateur);
		model.addAttribute("commentaires", commentaireManager.findAllCommentaireBySite(siteId));
		model.addAttribute("commentaire", new Commentaire());
		model.addAttribute("utilisateurs", webContentManager.getHashMapAllUtilisateurOnlyIdAndName());
		model.addAttribute("departements", webContentManager.getHashMapAllDepartement());
		model.addAttribute("secteur", new Secteur());
		return "site";
	}

	/**
	 * Add official tag on current site page, refresh site page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param siteId
	 * @return
	 */
	@RequestMapping(value = "/addTag/{siteId}", method = RequestMethod.GET)
	public String addTag(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "siteId") int siteId) {

		logger.info("Requête d'accès à l'url /addTag/" + siteId);
		siteManager.addOfficialTagOnSite(siteId);
		return "redirect:/site/" + siteId;
	}

	/**
	 * Delete official tag on current site page, refresh site page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param siteId
	 * @return
	 */
	@RequestMapping(value = "/deleteTag/{siteId}", method = RequestMethod.GET)
	public String deleteTag(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "siteId") int siteId) {

		logger.info("Requête d'accès à l'url /deleteTag/" + siteId);
		siteManager.deleteOfficialTagOnSite(siteId);
		return "redirect:/site/" + siteId;
	}

	/**
	 * Add comment on current site page, refresh site page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param commentaire
	 * @return
	 */
	@RequestMapping(value = "/site/processAddCommentaire", method = RequestMethod.POST)
	public String addCommentaire(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@ModelAttribute(value = "commentaire") Commentaire commentaire) {

		logger.info("Requête d'accès à l'url /site/processAddCommentaire");
		commentaireManager.addCommentaire(commentaire);
		return "redirect:/site/" + commentaire.getSite_id();
	}

	/**
	 * Update comment on current site page, refresh site page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param commentaire
	 * @param commentaireId
	 * @return
	 */
	@RequestMapping(value = "/site/processUpdateCommentaire/{commentaireId}", method = RequestMethod.POST)
	public String updateCommentaire(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@ModelAttribute(value = "commentaire") Commentaire commentaire,
			@PathVariable(value = "commentaireId") int commentaireId) {

		logger.info("Requête d'accès à l'url /site/processUpdateCommentaire/" + commentaireId);
		webContentManager.updateCommentaire(commentaire, sessionUtilisateur);
		return "redirect:/site/" + commentaire.getSite_id();
	}

	/**
	 * Delete comment on current site page, refresh site page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param siteId
	 * @param commentaireId
	 * @return
	 */
	@RequestMapping(value = "/site/processDeleteCommentaire/{siteId}/{commentaireId}", method = RequestMethod.GET)
	public String deleteCommentaire(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "siteId") int siteId, @PathVariable(value = "commentaireId") int commentaireId) {

		logger.info("Requête d'accès à l'url /site/processDeleteCommentaire/" + siteId + "/" + commentaireId);
		commentaireManager.updateCommentaireStatus(commentaireId);
		return "redirect:/site/" + siteId;
	}

	/**
	 * Set picture on current site page, refresh site page
	 * 
	 * @param sessionUtilisateur
	 * @param siteId
	 * @param pictureUrl
	 * @return
	 */
	@RequestMapping(value = "/site/processEditPicture/{siteId}", method = RequestMethod.GET)
	public String editSitePicture(
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "siteId") int siteId, @RequestParam(value = "pictureUrl") String pictureUrl) {

		logger.info("Requête d'accès à l'url /site/processEditPicture/" + siteId + "/" + pictureUrl);
		siteManager.editPicture(siteId, pictureUrl);
		return "redirect:/site/" + siteId;

	}

	/**
	 * Set picture on current site page, refresh site page
	 * 
	 * @param sessionUtilisateur
	 * @param siteId
	 * @param pictureUrl
	 * @return
	 */
	@RequestMapping(value = "/site/processDeleteSitePicture/{siteId}", method = RequestMethod.GET)
	public String deleteSitePicture(
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "siteId") int siteId) {

		logger.info("Requête d'accès à l'url /site/processEditPicture/" + siteId);
		logger.info("Suppression de la photo du site id :" + siteId);
		siteManager.editPicture(siteId, null);
		return "redirect:/site/" + siteId;

	}

	/**
	 * Handle add secteur request
	 * 
	 * @param model
	 * @param secteur
	 * @param result
	 * @param sessionUtilisateur
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/site/processAddSecteurFromSitePage", method = RequestMethod.POST)
	public String addSecteur(Model model, @Valid @ModelAttribute(value = "secteur") Secteur secteur,
			BindingResult result,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			RedirectAttributes redirectAttributes) {

		logger.info("Requête d'accès à l'url /site/processAddSecteur");

		if (result.hasErrors()) {
			return "redirect:/site/" + secteur.getSite_id() + "#addSecteurModal";
//			model.addAttribute("secteur", secteur);
//				model.addAllAttributes(
//						addWebContentFormUtil.getAddSecteurAttributesWhenValidationErrors(secteur, result, sessionUtilisateur));
		} else {
			webContentManager.addSecteur(secteur);
//				model.addAllAttributes(addWebContentFormUtil.getAddSecteurAttributes(secteur, secteurAddedWithSuccess, sessionUtilisateur));
		}
		return "redirect:/site/" + secteur.getSite_id();
	}
}
