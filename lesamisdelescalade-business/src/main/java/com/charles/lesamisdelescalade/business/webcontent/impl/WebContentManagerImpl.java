package com.charles.lesamisdelescalade.business.webcontent.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import com.charles.lesamisdelescalade.business.webcontent.WebContentManager;
import com.charles.lesamisdelescalade.consumer.WebContentDao;
import com.charles.lesamisdelescalade.model.beans.Commentaire;
import com.charles.lesamisdelescalade.model.beans.Cotation;
import com.charles.lesamisdelescalade.model.beans.Departement;
import com.charles.lesamisdelescalade.model.beans.Longueur;
import com.charles.lesamisdelescalade.model.beans.Secteur;
import com.charles.lesamisdelescalade.model.beans.Site;
import com.charles.lesamisdelescalade.model.beans.Utilisateur;
import com.charles.lesamisdelescalade.model.beans.Voie;
import com.charles.lesamisdelescalade.model.dto.CriteresSql;
import com.charles.lesamisdelescalade.model.dto.SitePageData;

/**
 * 
 * @author Charles
 *
 */
@Service
public class WebContentManagerImpl implements WebContentManager {

	@Autowired
	private WebContentDao webContentDao;

	/* Logger for LoginManagerImpl class */
	private static final Logger logger = LoggerFactory.getLogger(WebContentManagerImpl.class);

	/* ========================================================================== */
	/* Display site page */
	/* ========================================================================== */

	/**
	 * Set bean SitePageData with site, secteur, voie and longueur data according to
	 * the choosen site
	 * 
	 * @param siteId
	 * @return sitePageData
	 */
	@Override
	public SitePageData getSitePageData(int siteId) {
		SitePageData sitePageData = new SitePageData();
		sitePageData.setSite(findSiteById(siteId));
		sitePageData.setSecteurs(getAllSecteurBySite(siteId));
		sitePageData.setVoies(findVoiesBySite(siteId));
		sitePageData.setLongueurs(findLongueursBySite(siteId));

		for (Secteur secteur : sitePageData.getSecteurs()) {
			secteur.setVoiesCount(getVoieCountBySecteurs(secteur.getId()));
			try {
				secteur.setCotationMin(getMinCotation(secteur.getId()));
				secteur.setCotationMax(getMaxCotation(secteur.getId()));
			} catch (NullPointerException e) {
				secteur.setCotationMin("NA");
				secteur.setCotationMax("NA");
			}

		}

		return sitePageData;
	}
	
	@Override
	public HashMap<Integer, String> getHashMapAllUtilisateurOnlyIdAndName(){
		return convertUtilisateurListToHashMap(findAllUtilisateurOnlyIdAndName());
	}

	/* ========================================================================== */
	/* Site data from database */
	/* ========================================================================== */

	/**
	 * Find site by id input
	 * 
	 * @param siteId
	 * @return Site
	 */
	
	public Site findSiteById(int siteId) {
		return webContentDao.findSite(siteId);
	}

	@Override
	public List<Site> findAllSiteByDepartement(int departementId) {
		return webContentDao.findAllSiteByDepartement(departementId);
	}

	@Override
	public int getSiteIdBySecteurId(int secteurId) {
		return webContentDao.getSiteIdBySecteurId(secteurId);
	}

	@Override
	public void addOfficialTagOnSite(int siteId) {
		webContentDao.addOfficialTagOnSite(siteId);
	}

	@Override
	public void deleteOfficialTagOnSite(int siteId) {
		webContentDao.deleteOfficialTagOnSite(siteId);
	}

	@Override
	public List<Site> findAllSite() {
		return webContentDao.findAllSite();
	}

	@Override
	public List<Site> findAllSiteByCotation(int cotationId) {
		return webContentDao.findAllSiteByCotation(cotationId);
	}

	@Override
	public List<Site> findAllSiteBySecteurCount(int secteurCount) {
		return webContentDao.findAllSiteBySecteurCount(secteurCount);
	}

	@Override
	public List<Integer> getSecteurCountBySite() {
		return webContentDao.getSecteurCountBySite();
	}

	@Override
	public List<Site> findAllSiteByMultiCritere(int departementId, int cotationId, int secteurCount, String nom) {

		if (departementId > 0 || cotationId > 0 || secteurCount > 0 || !nom.isEmpty()) {
			if (!nom.isEmpty()) {
				return webContentDao.findAllSiteByName("%"+nom+"%");

			} else {

				CriteresSql criteresSql = createSqlRequestToFindAllSiteByMultiCritere(departementId, cotationId,
						secteurCount);
				return webContentDao.findAllSiteByMultiCritere(criteresSql.getCriteresSql(), criteresSql.getSql());
			}
		} else {
			return webContentDao.findAllSite();
		}

	}

	/* ========================================================================== */
	/* Secteur data from database */
	/* ========================================================================== */

	@Override
	public List<Secteur> getAllSecteurBySite(int siteId) {
		return webContentDao.findAllSecteurBySite(siteId);
	}

	@Override
	public int getSecteurIdByVoieId(int voieId) {
		return webContentDao.getSecteurIdByVoieId(voieId);
	}

	/* ========================================================================== */
	/* Voie data from database */
	/* ========================================================================== */

	public List<Voie> findVoiesBySite(int siteId) {
		return webContentDao.findVoieBySite(siteId);
	}

	public int getVoieCountBySecteurs(int secteurId) {
		return webContentDao.getVoieCountBySecteur(secteurId);
	}

	@Override
	public List<Voie> findAllVoieBySecteur(int secteurId) {
		return webContentDao.findAllVoieBySecteur(secteurId);
	}

	/* ========================================================================== */
	/* Longueur data from database */
	/* ========================================================================== */

	public List<Longueur> findLongueursBySite(int siteId) {
		return webContentDao.findLongueurBySite(siteId);
	}

	@Override
	public List<Longueur> findAllLongueurByVoie(int voieId) {
		return webContentDao.findAllLongueurByVoie(voieId);
	}

	/* ========================================================================== */
	/* Cotation data from database */
	/* ========================================================================== */

	public String getMinCotation(int secteurId) {
		return webContentDao.getSecteurMinCotation(secteurId);
	}

	public String getMaxCotation(int secteurId) {
		return webContentDao.getSecteurMaxCotation(secteurId);
	}

	@Override
	public List<Cotation> findAllCotation() {
		return webContentDao.findAllCotation();
	}

	/* ========================================================================== */
	/* Departement data from database */
	/* ========================================================================== */

	@Override
	public List<Departement> findAllDepartement() {
		return webContentDao.findAllDepartement();
	}

	@Override
	public int getDepartementIdBySiteId(int siteId) {
		return webContentDao.getDepartementIdBySiteId(siteId);
	}
	
	/* ========================================================================== */
	/* Commentaire data from database */
	/* ========================================================================== */
	
	@Override
	public List<Commentaire> findAllCommentaireBySite(int siteId){
		return webContentDao.findAllCommentaireBySite(siteId);
	}
	
	@Override 
	public void updateCommentaire(Commentaire commentaire, Utilisateur utilisateur) {
		Date dNow = new Date( );
	      SimpleDateFormat ft = 
	      new SimpleDateFormat ("yyyy.MM.dd 'à' hh:mm:ss");
		String enteteCommentaire = "Commentaire modifié par " + utilisateur.getNom() + " le " + ft.format(dNow) + "."  ;
		commentaire.setTexte(commentaire.getTexte() + "<br/>" + enteteCommentaire);
		webContentDao.updateCommentaire(commentaire, utilisateur.getId());
		
	}
	
	@Override
	public void updateCommentaireStatus(int commentaireId) {
		webContentDao.updateCommentaireStatus(commentaireId);
	}
	
	/* ========================================================================== */
	/* Utilisateur data from database */
	/* ========================================================================== */
	
	public List<Utilisateur> findAllUtilisateurOnlyIdAndName(){
		return webContentDao.findAllUtilisateurOnlyIdAndName();
	}
	

	/* ========================================================================== */
	/* Add web content */
	/* ========================================================================== */

	/**
	 * Add new site to database
	 * 
	 * @param Site
	 * @return Boolean
	 */
	@Override
	public Boolean addSite(Site site) {
		Boolean siteAddedWithSuccess;
		logger.info("Add site attempt");
		try {
			webContentDao.addSite(site);
			siteAddedWithSuccess = true;
			logger.debug("Site added with success - site id: " + site.getId() + " - site name: " + site.getNom());
		} catch (DuplicateKeyException e) {
			siteAddedWithSuccess = false;
			logger.warn("Site add failed - Cause: site name already exist");
		}
		return siteAddedWithSuccess;
	}

	/**
	 * Add new secteur to database
	 * 
	 * @param Secteur
	 * @return Boolean
	 */
	@Override
	public Boolean addSecteur(Secteur secteur) {
		Boolean secteurAddedWithSuccess;
		logger.info("Add secteur attempt");
		try {
			webContentDao.addSecteur(secteur);
			secteurAddedWithSuccess = true;
			logger.debug("Secteur added with success - secteur id: " + secteur.getId() + " - secteur name: "
					+ secteur.getNom());
		} catch (DuplicateKeyException e) {
			secteurAddedWithSuccess = false;
			logger.warn("Secteur add failed - Cause: secteur name already exist");
		}
		return secteurAddedWithSuccess;
	}

	/**
	 * Add new voie to database
	 * 
	 * @param Voie
	 * @return String
	 */
	@Override
	public String addVoie(Voie voie) {
		String causeError = "";
		logger.info("Add voie attempt");
		try {
			webContentDao.findVoieByNumeroAndSecteur(voie.getNumero(), voie.getSecteur_id());
			causeError = "numero";
			logger.debug("Voie add failed - Cause: voie numero already exist");
		} catch (EmptyResultDataAccessException e) {

		}
		try {
			webContentDao.findVoieByNomAndSecteur(voie.getNom(), voie.getSecteur_id());
			causeError = "nom";
			logger.debug("Voie add failed - Cause: voie name already exist");
		} catch (EmptyResultDataAccessException e) {

		}
		if (causeError.equals("")) {
			webContentDao.addVoie(voie);
			logger.debug("Voie added with success - voie id: " + voie.getId() + " - voie numero: " + voie.getNumero()
					+ " - voie name: " + voie.getNom());
		}
		return causeError;
	}

	/**
	 * Add new longueur to database
	 * 
	 * @param Longueur
	 * @return Boolean
	 */
	@Override
	public Boolean addLongueur(Longueur longueur) {
		Boolean NumeroIsAlreadyUsed;
		logger.info("Add longueur attempt");
		try {
			webContentDao.findLongueurByNumeroAndVoie(longueur.getNumero(), longueur.getVoie_id());
			NumeroIsAlreadyUsed = true;
			logger.debug("Longueur added with success - longueur id: " + longueur.getId() + " - longueur numero: "
					+ longueur.getNumero());
		} catch (EmptyResultDataAccessException e) {
			webContentDao.addLongeur(longueur);
			NumeroIsAlreadyUsed = false;
			logger.debug("Longueur add failed - Cause: voie numero already exist");
		}

		return NumeroIsAlreadyUsed;
	}
	
	@Override
	public void addCommentaire(Commentaire commentaire) {
		webContentDao.addCommentaire(commentaire);
	}

	/* ========================================================================== */
	/* Utils method */
	/* ========================================================================== */

	private CriteresSql createSqlRequestToFindAllSiteByMultiCritere(int departementId, int cotationId,
			int secteurCount) {
		String sql = "";
		ArrayList<Integer> criteres = new ArrayList<Integer>();
		if (departementId > 0) {
			sql = "select * from site where departement_id=? ";
			criteres.add(departementId);
		}
		if (cotationId > 0) {
			if (!sql.isEmpty()) {
				sql = sql + "intersect ";
			}
			sql = sql
					+ "select distinct site.* from site inner join secteur on site.id = secteur.site_id inner join voie on secteur.id=voie.secteur_id inner join longueur on voie.id = longueur.voie_id where longueur.cotation_id = ? ";
			criteres.add(cotationId);
		}
		if (secteurCount > 0) {
			if (!sql.isEmpty()) {
				sql = sql + "intersect ";
			}
			sql = sql
					+ "select site.* from site inner join secteur on site.id = secteur.site_id group by site.id having count(secteur.id)=?";
			criteres.add(secteurCount);
		}
		Object[] criteresSql = criteres.toArray();
		return new CriteresSql(sql, criteresSql);

	}
	
	
	public HashMap<Integer, String> convertUtilisateurListToHashMap(List<Utilisateur> utilisateurs){
		HashMap<Integer, String> map= new HashMap<Integer, String>();
		for (Utilisateur u: utilisateurs) map.put(u.getId(), u.getNom());
		return map;
		
	}
	
	
	
	
}
