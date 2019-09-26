package com.charles.lesamisdelescalade.webapp.utils;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.charles.lesamisdelescalade.business.webcontent.WebContentManager;
import com.charles.lesamisdelescalade.model.beans.Site;
import com.charles.lesamisdelescalade.model.beans.Utilisateur;
import com.charles.lesamisdelescalade.model.dto.SearchSiteData;

@Component
public class SearchSiteFormUtil {
	
	@Autowired
	private WebContentManager webContentManager;
	
	public HashMap<String, Object> getSearchSiteAttributes(Utilisateur sessionUtilisateur, SearchSiteData searchSiteData, List<Site> sites ){
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("departements", webContentManager.findAllDepartement());
		attributes.put("cotations", webContentManager.findAllCotation());
		attributes.put("secteurCount", webContentManager.getSecteurCountBySite());
		attributes.put("sessionUtilisateur", sessionUtilisateur);
		if(searchSiteData.getNom()==null && searchSiteData.getDepartementId()==0 && searchSiteData.getCotationId()==0 && searchSiteData.getSecteurCount()==0) {
			attributes.put("searchSiteData", new SearchSiteData());
			attributes.put("sites", webContentManager.findAllSite());
		}else {
			attributes.put("searchSiteData", searchSiteData);
			attributes.put("sites", sites);
		}
		return attributes;
	}

}
