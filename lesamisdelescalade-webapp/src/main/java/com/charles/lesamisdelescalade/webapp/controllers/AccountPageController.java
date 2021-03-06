package com.charles.lesamisdelescalade.webapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.charles.lesamisdelescalade.business.utils.bean.DepartementManager;
import com.charles.lesamisdelescalade.business.utils.bean.PossesseurTopoManager;
import com.charles.lesamisdelescalade.business.utils.bean.ReservationTopoManager;
import com.charles.lesamisdelescalade.business.webcontent.WebContentManager;
import com.charles.lesamisdelescalade.model.beans.PossesseurTopo;
import com.charles.lesamisdelescalade.model.beans.Utilisateur;

/**
 * Controller class in relation with account page jsp
 * 
 * @author Charles
 *
 */
@Controller
public class AccountPageController {

	@Autowired
	private WebContentManager webContentManager;
	@Autowired
	private DepartementManager departementManager;
	@Autowired
	private ReservationTopoManager reservationTopoManager;
	@Autowired
	private PossesseurTopoManager possesseurTopoManager;

	// Set logger
	private static final Logger logger = LoggerFactory.getLogger(AccountPageController.class);

	/**
	 * Record user id and return account page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/goToAccountPage", method = RequestMethod.GET)
	public String goToAccountPage(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			RedirectAttributes redirectAttributes) {

		logger.info("Requête d'accès à l'url /goToAccountPage");
		redirectAttributes.addFlashAttribute("sessionUtilisateur", sessionUtilisateur);
		return "redirect:/accountPage/" + sessionUtilisateur.getId();

	}

	/**
	 * Display account page
	 * 
	 * @param model
	 * @param sessionUtilisateurId
	 * @param sessionUtilisateur
	 * @param departementId
	 * @return
	 */
	@RequestMapping(value = "/accountPage/{sessionUtilisateurId}", method = RequestMethod.GET)
	public String displayAccountPage(Model model,
			@PathVariable(value = "sessionUtilisateurId") int sessionUtilisateurId,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@RequestParam(value = "departementId", required = false) Integer departementId) {

		logger.info("Requête d'accès à l'url /accountPage/" + sessionUtilisateurId);
		model.addAttribute("sessionUtilisateur", sessionUtilisateur);
		if (sessionUtilisateurId != sessionUtilisateur.getId()) {
			return "redirect:/";
		} else {
			model.addAttribute("departements", departementManager.findAllDepartement());
			model.addAttribute("departementId", departementId);
			model.addAttribute("myTopos", webContentManager.findAllMyTopoByUtilisateurId(sessionUtilisateurId));
			model.addAttribute("receivedReservationRequest",
					webContentManager.findAllReceivedReservationRequestByUtilisateurId(sessionUtilisateur.getId()));
			model.addAttribute("sentReservationRequest",
					webContentManager.findAllSentReservationRequestByUtilisateurId(sessionUtilisateur.getId()));
			if (departementId != null) {
				model.addAttribute("newPossesseurTopo", new PossesseurTopo());
				model.addAttribute("topos",
						webContentManager.getTopoListForAccountPageFilteredByDepartementId(departementId));
			}

			return "accountPage";
		}
	}

	/**
	 * record departement and return account page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param departementId
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/processChooseDepartementToAddTopo", method = RequestMethod.GET)
	public String processChooseDepartementToAddTopo(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@RequestParam(value = "departementId") int departementId, RedirectAttributes redirectAttributes) {
		logger.info("Requête d'accès à l'url /processChooseDepartementToAddTopo");
		if (departementId == 0) {
			return "redirect:/goToAccountPage";
		} else {
			redirectAttributes.addFlashAttribute("departementId", departementId);
			redirectAttributes.addFlashAttribute("sessionUtilisateur", sessionUtilisateur);
			return "redirect:/accountPage/" + sessionUtilisateur.getId();
		}
	}

	/**
	 * Handle add topo request
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param newPossesseurTopo
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/accountPage/processAddOwnedTopo", method = RequestMethod.POST)
	public String addOwnedTopo(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@ModelAttribute(value = "newPossesseurTopo") PossesseurTopo newPossesseurTopo,
			RedirectAttributes redirectAttributes) {
		logger.info("Requête d'accès à l'url /accountPage/processAddOwnedTopo");
		if (webContentManager.addPossesseurTopo(newPossesseurTopo)) {
			redirectAttributes.addFlashAttribute("messageAddPossesseurTopoSuccessfully",
					"Le topo a été ajouté avec succès dans votre espace personnel.");
		} else {
			redirectAttributes.addFlashAttribute("messageAddPossesseurTopoError",
					"Erreur - Vous possédez déjà ce topo.");
		}
		return "redirect:/accountPage/" + sessionUtilisateur.getId();

	}

	/**
	 * Handle set topo availibilty request and return account page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param topoId
	 * @param utilisateurId
	 * @param available
	 * @return
	 */
	@RequestMapping(value = "/setTopoAvailability/{topoId}/{utilisateurId}/{available}", method = RequestMethod.GET)
	public String setTopoAvailability(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "topoId") int topoId, @PathVariable(value = "utilisateurId") int utilisateurId,
			@PathVariable(value = "available") Boolean available) {

		logger.info("Requête d'accès à l'url /setTopoAvailability/" + topoId + "/" + utilisateurId + "/" + available);
		if (utilisateurId != sessionUtilisateur.getId()) {
			return "redirect:/";
		} else {
			possesseurTopoManager.setTopoAvailability(new PossesseurTopo(topoId, utilisateurId, available, null));
			return "redirect:/accountPage/" + sessionUtilisateur.getId();
		}

	}

	/**
	 * Handle delete owned topo request and return account page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param topoId
	 * @param utilisateurId
	 * @return
	 */
	@RequestMapping(value = "/deleteOwnedTopo/{topoId}/{utilisateurId}", method = RequestMethod.GET)
	public String deleteOwnedTopo(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "topoId") int topoId, @PathVariable(value = "utilisateurId") int utilisateurId) {

		logger.info("Requête d'accès à l'url /deleteOwnedTopo/" + topoId + "/" + utilisateurId);
		if (utilisateurId != sessionUtilisateur.getId()) {
			return "redirect:/";
		} else {
			possesseurTopoManager.deleteOwnedTopo(topoId, utilisateurId);
			return "redirect:/accountPage/" + sessionUtilisateur.getId();
		}

	}

	// ==================================================================================================================
	// Received Request Section
	// ==================================================================================================================

	/**
	 * Handle accept reservation request and return account page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param reservationRequestId
	 * @param possesseurId
	 * @param topoId
	 * @return
	 */
	@RequestMapping(value = "/processUpdateReservationRequestStatusToAccepted/{reservationRequestId}/{possesseurId}/{topoId}",
			method = RequestMethod.GET)
	public String processUpdateReservationRequestStatusToAccepted(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "reservationRequestId") int reservationRequestId,
			@PathVariable(value = "possesseurId") int possesseurId, @PathVariable(value = "topoId") int topoId) {

		logger.info("Requête d'accès à l'url /processUpdateReservationRequestStatusToAccepted/" + reservationRequestId
				+ "/" + possesseurId + "/" + topoId);
		if (possesseurId != sessionUtilisateur.getId()) {
			return "redirect:/";
		} else {
			webContentManager.acceptTopoReservation(reservationRequestId,
					new PossesseurTopo(topoId, possesseurId, null, null));
			return "redirect:/accountPage/" + sessionUtilisateur.getId();
		}
	}

	/**
	 * Handle refused reservation request and return account page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param reservationRequestId
	 * @param possesseurId
	 * @return
	 */
	@RequestMapping(value = "/processUpdateReservationRequestStatusToRefused/{reservationRequestId}/{possesseurId}", method = RequestMethod.GET)
	public String processUpdateReservationRequestStatusToRefused(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "reservationRequestId") int reservationRequestId,
			@PathVariable(value = "possesseurId") int possesseurId) {

		logger.info("Requête d'accès à l'url /processUpdateReservationRequestStatusToRefused/" + reservationRequestId
				+ "/" + possesseurId);
		if (possesseurId != sessionUtilisateur.getId()) {
			return "redirect:/";
		} else {
			reservationTopoManager.updateReservationRequestStatusToRefused(reservationRequestId);
			return "redirect:/accountPage/" + sessionUtilisateur.getId();
		}
	}

	/**
	 * Handle set over reservation request and return account page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param reservationRequestId
	 * @param possesseurId
	 * @param topoId
	 * @return
	 */
	@RequestMapping(value = "/processUpdateReservationRequestStatusToEnded/{reservationRequestId}/{possesseurId}/{topoId}", method = RequestMethod.GET)
	public String processUpdateReservationRequestStatusToEnded(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "reservationRequestId") int reservationRequestId,
			@PathVariable(value = "possesseurId") int possesseurId, @PathVariable(value = "topoId") int topoId) {

		logger.info("Requête d'accès à l'url /processUpdateReservationRequestStatusToEnded/" + reservationRequestId
				+ "/" + possesseurId + "/" + topoId);
		if (possesseurId != sessionUtilisateur.getId()) {
			return "redirect:/";
		} else {
			webContentManager.setOverTopoReservation(reservationRequestId,
					new PossesseurTopo(topoId, possesseurId, null, null));
			return "redirect:/accountPage/" + sessionUtilisateur.getId();
		}
	}

	/**
	 * Handle cancel reservation request and return account page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param reservationRequestId
	 * @param possesseurId
	 * @return
	 */
	@RequestMapping(value = "/processUpdateReservationRequestStatusToCancelled/{reservationRequestId}/{possesseurId}", method = RequestMethod.GET)
	public String processUpdateReservationRequestStatusToCancelled(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "reservationRequestId") int reservationRequestId,
			@PathVariable(value = "possesseurId") int possesseurId) {

		logger.info("Requête d'accès à l'url /processUpdateReservationRequestStatusToCancelled/" + reservationRequestId
				+ "/" + possesseurId);
		if (possesseurId != sessionUtilisateur.getId()) {
			return "redirect:/";
		} else {
			reservationTopoManager.updateReservationRequestStatusToCancelled(reservationRequestId);
			return "redirect:/accountPage/" + sessionUtilisateur.getId();
		}
	}

	/**
	 * Handle set not visible reservation request for owner and return account page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param reservationRequestId
	 * @param possesseurId
	 * @return
	 */
	@RequestMapping(value = "/processSetReservationVisibilityForOwnerToFalse/{reservationRequestId}/{possesseurId}", method = RequestMethod.GET)
	public String processSetReservationVisibilityToFalse(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "reservationRequestId") int reservationRequestId,
			@PathVariable(value = "possesseurId") int possesseurId) {

		logger.info("Requête d'accès à l'url /processSetReservationVisibilityForOwnerToFalse/" + reservationRequestId
				+ "/" + possesseurId);
		if (possesseurId != sessionUtilisateur.getId()) {
			return "redirect:/";
		} else {
			reservationTopoManager.setReservationVisibilityForOwnerToFalse(reservationRequestId);
			return "redirect:/accountPage/" + sessionUtilisateur.getId();
		}
	}

	/**
	 * Handle set not visible reservation request for requester and return account
	 * page
	 * 
	 * @param model
	 * @param sessionUtilisateur
	 * @param reservationRequestId
	 * @param possesseurId
	 * @return
	 */
	@RequestMapping(value = "/processSetReservationVisibilityForRequesterToFalse/{reservationRequestId}/{possesseurId}", method = RequestMethod.GET)
	public String processSetReservationVisibilityForRequesterToFalse(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@PathVariable(value = "reservationRequestId") int reservationRequestId,
			@PathVariable(value = "possesseurId") int possesseurId) {

		logger.info("Requête d'accès à l'url /processSetReservationVisibilityForRequesterToFalse/"
				+ reservationRequestId + "/" + possesseurId);
		if (possesseurId != sessionUtilisateur.getId()) {
			return "redirect:/";
		} else {
			reservationTopoManager.setReservationVisibilityForRequesterToFalse(reservationRequestId);
			return "redirect:/accountPage/" + sessionUtilisateur.getId();
		}
	}

}
