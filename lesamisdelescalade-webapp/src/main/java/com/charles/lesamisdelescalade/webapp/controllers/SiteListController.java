package com.charles.lesamisdelescalade.webapp.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.charles.lesamisdelescalade.business.webcontent.WebContentManager;
import com.charles.lesamisdelescalade.model.beans.Site;
import com.charles.lesamisdelescalade.model.beans.Utilisateur;
import com.charles.lesamisdelescalade.model.dto.SearchSiteData;
import com.charles.lesamisdelescalade.webapp.utils.SearchSiteFormUtil;

@Controller
public class SiteListController {

	@Autowired
	private WebContentManager webContentManager;

	@Autowired
	private SearchSiteFormUtil searchSiteFormUtil;
	
	@ModelAttribute("sites")
	public List<Site> setSites() {
		return new ArrayList<Site>();
	}

	@RequestMapping(value = "/siteList", method = RequestMethod.GET)
	public String siteList(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@ModelAttribute(value = "searchSiteData") SearchSiteData searchSiteData,
			@ModelAttribute(value="sites") List<Site> sites) {

		model.addAllAttributes(searchSiteFormUtil.getSearchSiteAttributes(sessionUtilisateur, searchSiteData, sites));
		return "siteList";
	}

	@RequestMapping(value = "/searchSite", method = RequestMethod.POST)
	public String searchSite(Model model,
			@SessionAttribute(value = "sessionUtilisateur", required = false) Utilisateur sessionUtilisateur,
			@Valid @ModelAttribute(value = "searchSiteData") SearchSiteData searchSiteData,
			RedirectAttributes redirectAttributes, BindingResult result) {

		if (result.hasErrors()) {
			return "redirect:/siteList";
		} else {
			redirectAttributes.addFlashAttribute("sites",
					webContentManager.findAllSiteByMultiCritere(searchSiteData.getDepartementId(),
							searchSiteData.getCotationId(), searchSiteData.getSecteurCount()));
			redirectAttributes.addFlashAttribute("searchSiteData", searchSiteData);
			return "redirect:/siteList";
		}

	}
}
