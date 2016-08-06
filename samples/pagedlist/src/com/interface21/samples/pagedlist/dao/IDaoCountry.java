package com.interface21.samples.pagedlist.dao;

import java.util.List;
import java.util.Locale;

import com.interface21.samples.pagedlist.appli.ICountry;

/**
 * @author Jean-Pierre PAWLAK
 */
public interface IDaoCountry {

	public List getAllCountries(Locale locale);

	public List getFilteredCountries(String name, String code, Locale locale);

	public ICountry getCountry(String code, Locale locale);

}
