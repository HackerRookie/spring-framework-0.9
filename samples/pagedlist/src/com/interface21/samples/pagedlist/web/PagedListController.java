/*
 * Créé le 23 mai 2003
 */
package com.interface21.samples.pagedlist.web;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import com.interface21.samples.pagedlist.appli.ICountry;
import com.interface21.samples.pagedlist.dao.IDaoCountry;
import com.interface21.util.PagedListSourceProvider;
import com.interface21.util.RefreshablePagedListHolder;
import com.interface21.validation.BindException;
import com.interface21.web.bind.BindUtils;
import com.interface21.web.servlet.ModelAndView;
import com.interface21.web.servlet.mvc.multiaction.MultiActionController;
import com.interface21.web.servlet.support.RequestContextUtils;

/**
 * @author Jean-Pierre PAWLAK
 */
public class PagedListController extends MultiActionController {

	static final private String COUNTRIES_ATTR = "countries";

	private IDaoCountry daoCountry;

	public void setDaoCountry(IDaoCountry daoCountry) {
		this.daoCountry = daoCountry;
	}

	public ModelAndView showMain(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		RefreshablePagedListHolder listHolder = (RefreshablePagedListHolder) request.getSession(true).getAttribute(COUNTRIES_ATTR);
		if (null == listHolder) {
			listHolder = new RefreshablePagedListHolder();
			listHolder.setSourceProvider(new CountriesProvider());
			listHolder.setFilter(new CountriesFilter());
			request.getSession(true).setAttribute(COUNTRIES_ATTR, listHolder);
		}
		BindException ex = BindUtils.bind(request, listHolder, "countries");
		listHolder.setLocale(RequestContextUtils.getLocale(request));
		boolean forceRefresh = request.getParameter("forceRefresh") != null;
		listHolder.refresh(forceRefresh);
		return new ModelAndView("mainView", ex.getModel());
	}

	public ModelAndView showDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		Locale locale = RequestContextUtils.getLocale(request);
		ICountry country = daoCountry.getCountry(request.getParameter("code"), locale);
		return new ModelAndView("detailView", "country", country);
	}


	private class CountriesProvider implements PagedListSourceProvider {

		public List loadList(Locale loc, Object filter) {
			CountriesFilter cf = (CountriesFilter) filter;
			return daoCountry.getFilteredCountries(cf.getName(), cf.getCode(), loc);
		}
	}


	public static class CountriesFilter {

		private String name;
		private String code;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public boolean equals(Object obj) {
			return (obj instanceof CountriesFilter ? equals((CountriesFilter) obj) : false);
		}

		public boolean equals(CountriesFilter cf) {
			if (cf == this) return true;
			boolean result = (name == null ? cf.name == null : name.equals(cf.name));
			if (result) {
				result = (code == null ? cf.code == null : code.equals(cf.code));
			}
			return result;
		}

		public int hashCode() {
			int hash = 17;
			hash = 37 * hash + (name == null ? 0 : name.hashCode());
			hash = 37 * hash + (code == null ? 0 : code.hashCode());
			return hash;
		}

	}

}
