package com.charles.lesamisdelescalade.webapp.utils;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import com.charles.lesamisdelescalade.business.utils.bean.DepartementManager;
import com.charles.lesamisdelescalade.business.utils.bean.LongueurManager;
import com.charles.lesamisdelescalade.business.utils.bean.SecteurManager;
import com.charles.lesamisdelescalade.business.utils.bean.SiteManager;
import com.charles.lesamisdelescalade.business.utils.bean.VoieManager;
import com.charles.lesamisdelescalade.model.beans.Longueur;
import com.charles.lesamisdelescalade.model.beans.Secteur;
import com.charles.lesamisdelescalade.model.beans.Site;
import com.charles.lesamisdelescalade.model.beans.Utilisateur;
import com.charles.lesamisdelescalade.model.beans.Voie;
 
@Component
public class AddWebContentFormUtil {

	@Autowired
	private SiteManager siteManager;
	@Autowired
	private SecteurManager secteurManager;
	@Autowired
	private VoieManager voieManager;
	@Autowired
	private LongueurManager longueurManager;
	@Autowired
	private DepartementManager departementManager;

	public HashMap<String, Object> getAddWebContentAttributes(Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("site", new Site());
		attributes.put("secteur", new Secteur());
		attributes.put("voie", new Voie());
		attributes.put("longueur", new Longueur());
		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;
	}

	public HashMap<String, Object> getAddSiteAttributesWhenValidationErrors(Site site, BindingResult result,
			Utilisateur sessionUtilisateur) {

		HashMap<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("departementIdSite", site.getDepartement_id());
		attributes.put("collapseClassSite", "show");
		attributes.put("collapseAriaSite", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);

		if (result.getFieldErrorCount("departement_id") > 0) {
			attributes.put("messageErrorSite", "Erreur - Vous devez sélectionner un département.");
		}
		return attributes;
	}

	public HashMap<String, Object> getAddSiteAttributes(Site site, BindingResult result, Boolean siteAddedWithSuccess,
			Utilisateur sessionUtilisateur) {

		HashMap<String, Object> attributes = new HashMap<String, Object>();

		if (siteAddedWithSuccess) {
			attributes.put("messageSuccessSite", "Site ajouté avec succès");
		} else {
			attributes.put("messageErrorSite", "Erreur - Le site est déjà enregistré");
			attributes.put("departementIdSite", site.getDepartement_id());
		}
		attributes.put("site", new Site());
		attributes.put("secteur", new Secteur());
		attributes.put("voie", new Voie());
		attributes.put("longueur", new Longueur());
		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("collapseClassSite", "show");
		attributes.put("collapseAriaSite", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;

	}

	public HashMap<String, Object> getAddSecteurAttributesWhenDepartementIsSet(int departementId,
			Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (departementId == 0) {
			attributes.put("messageErrorSecteur", "Erreur - Aucun département n'a été sélectionné");
		} else {
			attributes.put("departementIdSecteur", departementId);
			attributes.put("sites", siteManager.findAllSiteByDepartement(departementId));
		}

		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("site", new Site());
		attributes.put("secteur", new Secteur());
		attributes.put("voie", new Voie());
		attributes.put("longueur", new Longueur());
		attributes.put("collapseClassSecteur", "show");
		attributes.put("collapseAriaSecteur", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;
	}

	public HashMap<String, Object> getAddSecteurAttributesWhenValidationErrors(Secteur secteur, BindingResult result,
			Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (result.getFieldErrorCount("site_id") > 0) {
			attributes.put("messageErrorSecteur", "Erreur - Auncun site n'a été sélectionné");
		} else {
			int departementId = departementManager.getDepartementIdBySiteId(secteur.getSite_id());
			attributes.put("departementIdSecteur", departementId);
			attributes.put("siteIdSecteur", secteur.getSite_id());
			attributes.put("sites", siteManager.findAllSiteByDepartement(departementId));
		}

		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("site", new Site());
		attributes.put("collapseClassSecteur", "show");
		attributes.put("collapseAriaSecteur", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);

		return attributes;

	}

	public HashMap<String, Object> getAddSecteurAttributes(Secteur secteur, Boolean secteurAddedWithSuccess,
			Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (secteurAddedWithSuccess) {
			attributes.put("messageSuccessSecteur", "Secteur ajouté avec succès");
		} else {
			int departementId = departementManager.getDepartementIdBySiteId(secteur.getSite_id());
			attributes.put("departementIdSecteur", departementId);
			attributes.put("siteIdSecteur", secteur.getSite_id());
			attributes.put("messageErrorSecteur", "Erreur - Le secteur est déjà enregistré");
		}

		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("site", new Site());
		attributes.put("secteur", new Secteur());
		attributes.put("voie", new Voie());
		attributes.put("longueur", new Longueur());
		attributes.put("collapseClassSecteur", "show");
		attributes.put("collapseAriaSecteur", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);

		return attributes;

	}

	public HashMap<String, Object> getAddVoieAttributesWhenDepartementIsSet(int departementId,
			Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (departementId == 0) {
			attributes.put("messageErrorVoie", "Erreur - Aucun département n'a été sélectionné");
		} else {
			attributes.put("departementIdVoie", departementId);
			attributes.put("sites", siteManager.findAllSiteByDepartement(departementId));
		}

		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("site", new Site());
		attributes.put("secteur", new Secteur());
		attributes.put("voie", new Voie());
		attributes.put("longueur", new Longueur());
		attributes.put("collapseClassVoie", "show");
		attributes.put("collapseAriaVoie", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;
	}

	public HashMap<String, Object> getAddVoieAttributesWhenSiteIsSet(int siteId, Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (siteId == 0) {
			attributes.put("messageErrorVoie", "Erreur - Aucun site n'a été sélectionné");
		} else {
			int departementId = departementManager.getDepartementIdBySiteId(siteId);
			attributes.put("secteurs", secteurManager.getAllSecteurBySite(siteId));
			attributes.put("departementIdVoie", departementId);
			attributes.put("sites", siteManager.findAllSiteByDepartement(departementId));
			attributes.put("siteIdVoie", siteId);
		}

		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("site", new Site());
		attributes.put("secteur", new Secteur());
		attributes.put("voie", new Voie());
		attributes.put("longueur", new Longueur());
		attributes.put("collapseClassVoie", "show");
		attributes.put("collapseAriaVoie", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;
	}

	public HashMap<String, Object> getAddVoieAttributesWhenSecteurIsSet(int secteurId, Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (secteurId == 0) {
			attributes.put("messageErrorVoie", "Erreur - Aucun secteur n'a été sélectionné");
		} else {
			int siteId = siteManager.getSiteIdBySecteurId(secteurId);
			int departementId = departementManager.getDepartementIdBySiteId(siteId);
			attributes.put("departementIdVoie", departementId);
			attributes.put("siteIdVoie", siteId);
			attributes.put("sites", siteManager.findAllSiteByDepartement(departementId));
			attributes.put("secteurs", secteurManager.getAllSecteurBySite(siteId));
			attributes.put("secteurIdVoie", secteurId);
			attributes.put("voies", voieManager.findAllVoieBySecteur(secteurId));

		}

		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("site", new Site());
		attributes.put("secteur", new Secteur());
		attributes.put("voie", new Voie());
		attributes.put("longueur", new Longueur());
		attributes.put("collapseClassVoie", "show");
		attributes.put("collapseAriaVoie", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;
	}

	public HashMap<String, Object> getAddVoieAttributesWhenValidationErrors(Voie voie, Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		int siteId = siteManager.getSiteIdBySecteurId(voie.getSecteur_id());
		int departementId = departementManager.getDepartementIdBySiteId(siteId);
		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("departementIdVoie", departementId);
		attributes.put("siteIdVoie", siteId);
		attributes.put("sites", siteManager.findAllSiteByDepartement(departementId));
		attributes.put("site", new Site());
		attributes.put("secteurs", secteurManager.getAllSecteurBySite(siteId));
		attributes.put("secteurIdVoie", voie.getSecteur_id());
		attributes.put("voies", voieManager.findAllVoieBySecteur(voie.getSecteur_id()));
		attributes.put("collapseClassVoie", "show");
		attributes.put("collapseAriaVoie", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;
	}

	public HashMap<String, Object> getAddVoieAttributes(Voie voie, String error, Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (error.contentEquals("")) {
			attributes.put("messageSuccessVoie", "Voie ajoutée avec succès");
		} else if (error.contentEquals("numero")) {
			attributes.put("messageErrorVoie",
					"Erreur - La voie n°" + voie.getNumero() + " est déja enregistrée sur ce site.");
		} else if (error.contentEquals("nom")) {
			attributes.put("messageErrorVoie",
					"Erreur - La voie nommée " + voie.getNom() + " est déja enregistrée sur ce site.");
		}

		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("site", new Site());
		attributes.put("secteur", new Secteur());
		attributes.put("voie", new Voie());
		attributes.put("longueur", new Longueur());
		attributes.put("collapseClassVoie", "show");
		attributes.put("collapseAriaVoie", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;
	}

	public HashMap<String, Object> getAddLongueurAttributesWhenDepartementIsSet(int departementId,
			Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (departementId == 0) {
			attributes.put("messageErrorLongueur", "Erreur - Aucun département n'a été sélectionné");
		} else {
			attributes.put("departementIdLongueur", departementId);
			attributes.put("sites", siteManager.findAllSiteByDepartement(departementId));
		}

		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("site", new Site());
		attributes.put("secteur", new Secteur());
		attributes.put("voie", new Voie());
		attributes.put("longueur", new Longueur());
		attributes.put("collapseClassLongueur", "show");
		attributes.put("collapseAriaLongueur", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;
	}

	public HashMap<String, Object> getAddLongueurAttributesWhenSiteIsSet(int siteId, Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (siteId == 0) {
			attributes.put("messageErrorLongueur", "Erreur - Aucun site n'a été sélectionné");
		} else {
			int departementId = departementManager.getDepartementIdBySiteId(siteId);
			attributes.put("departementIdLongueur", departementId);
			attributes.put("sites", siteManager.findAllSiteByDepartement(departementId));
			attributes.put("siteIdLongueur", siteId);
			attributes.put("secteurs", secteurManager.getAllSecteurBySite(siteId));
		}

		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("site", new Site());
		attributes.put("secteur", new Secteur());
		attributes.put("voie", new Voie());
		attributes.put("longueur", new Longueur());
		attributes.put("collapseClassLongueur", "show");
		attributes.put("collapseAriaLongueur", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;
	}

	public HashMap<String, Object> getAddLongueurAttributesWhenSecteurIsSet(int secteurId,
			Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (secteurId == 0) {
			attributes.put("messageErrorLongueur", "Erreur - Aucun secteur n'a été sélectionné");
		} else {
			int siteId = siteManager.getSiteIdBySecteurId(secteurId);
			int departementId = departementManager.getDepartementIdBySiteId(siteId);
			attributes.put("departementIdLongueur", departementId);
			attributes.put("siteIdLongueur", siteId);
			attributes.put("sites", siteManager.findAllSiteByDepartement(departementId));
			attributes.put("secteurs", secteurManager.getAllSecteurBySite(siteId));
			attributes.put("secteurIdLongueur", secteurId);
			attributes.put("voies", voieManager.findAllVoieBySecteur(secteurId));
		}

		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("site", new Site());
		attributes.put("secteur", new Secteur());
		attributes.put("voie", new Voie());
		attributes.put("longueur", new Longueur());
		attributes.put("cotations", longueurManager.findAllLongueurCotation());
		attributes.put("collapseClassLongueur", "show");
		attributes.put("collapseAriaLongueur", true);
		//attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;
	}

	public HashMap<String, Object> getAddLongueurAttributesWhenVoieIsSet(int voieId, Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (voieId == 0) {
			attributes.put("messageErrorLongueur", "Erreur - Aucune voie n'a été sélectionné");
		} else {
			int secteurId = secteurManager.getSecteurIdByVoieId(voieId);
			int siteId = siteManager.getSiteIdBySecteurId(secteurId);
			int departementId = departementManager.getDepartementIdBySiteId(siteId);
			attributes.put("departementIdLongueur", departementId);
			attributes.put("siteIdLongueur", siteId);
			attributes.put("sites", siteManager.findAllSiteByDepartement(departementId));
			attributes.put("secteurs", secteurManager.getAllSecteurBySite(siteId));
			attributes.put("secteurIdLongueur", secteurId);
			attributes.put("voies", voieManager.findAllVoieBySecteur(secteurId));
			attributes.put("longueurs", longueurManager.findAllLongueurByVoie(voieId));
			attributes.put("voieIdLongueur", voieId);
		}

		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("site", new Site());
		attributes.put("secteur", new Secteur());
		attributes.put("voie", new Voie());
		attributes.put("longueur", new Longueur());
		attributes.put("cotations", longueurManager.findAllLongueurCotation());
		attributes.put("collapseClassLongueur", "show");
		attributes.put("collapseAriaLongueur", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;
	}

	public HashMap<String, Object> getAddLongueurAttributesWhenValidationErrors(Longueur longueur, int departementId,
			int siteId, int secteurId, Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (longueur.getCotation_id() < 1) {
			attributes.put("messageErrorLongueur", "Erreur - Aucune cotation n'a été sélectionné");
		}
		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("departementIdLongueur", departementId);
		attributes.put("siteIdLongueur", siteId);
		attributes.put("sites", siteManager.findAllSiteByDepartement(departementId));
		attributes.put("site", new Site());
		attributes.put("secteurs", secteurManager.getAllSecteurBySite(siteId));
		attributes.put("secteurIdLongueur", secteurId);
		attributes.put("voies", voieManager.findAllVoieBySecteur(secteurId));
		attributes.put("voieIdLongueur", longueur.getVoie_id());
		attributes.put("cotations", longueurManager.findAllLongueurCotation());
		attributes.put("cotationIdLongueur", longueur.getCotation_id());
		attributes.put("collapseClassLongueur", "show");
		attributes.put("collapseAriaLongueur", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;
	}

	public HashMap<String, Object> getAddLongueurAttributes(Longueur longueur, int departementId, int siteId,
			int secteurId, Boolean isNumberInputAlreadyUsed, Utilisateur sessionUtilisateur) {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		if (!isNumberInputAlreadyUsed) {
			attributes.put("messageSuccessLongueur", "Longueur ajoutée avec succès");
		} else {
			attributes.put("messageErrorLongueur",
					"Erreur - La longueur n°" + longueur.getNumero() + " est déja enregistrée sur cette voie.");
			attributes.put("departementIdLongueur", departementId);
			attributes.put("siteIdLongueur", siteId);
			attributes.put("sites", siteManager.findAllSiteByDepartement(departementId));
			attributes.put("secteurs", secteurManager.getAllSecteurBySite(siteId));
			attributes.put("secteurIdLongueur", secteurId);
			attributes.put("voies", voieManager.findAllVoieBySecteur(secteurId));
			attributes.put("cotations", longueurManager.findAllLongueurCotation());
		}

		attributes.put("departements", departementManager.findAllDepartement());
		attributes.put("site", new Site());
		attributes.put("secteur", new Secteur());
		attributes.put("voie", new Voie());
		attributes.put("longueur", new Longueur());
		attributes.put("collapseClassLongueur", "show");
		attributes.put("collapseAriaLongueur", true);
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		return attributes;
	}

}
