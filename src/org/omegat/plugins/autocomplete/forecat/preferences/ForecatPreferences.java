package org.omegat.plugins.autocomplete.forecat.preferences;

import org.omegat.plugins.autocomplete.forecat.ForecatPTS;
import org.omegat.plugins.autocomplete.forecat.ForecatPTS.InterfaceType;
import org.omegat.plugins.autocomplete.forecat.adapter.ForecatWebServiceInterface;
import org.omegat.util.Preferences;

public class ForecatPreferences {

	public static String FORECAT_ENABLED_OMEGAT_ENGINES = "forecat_enabled_omegat_mt";
	public static String FORECAT_IGNORE_OMEGAT_ENGINES = "forecat_ignore_omegat_mt";

	public static String FORECAT_USE_API = "forecat_use_api";
	public static String FORECAT_API_URL = "forecat_api_url";

	public static String FORECAT_MINIMUM_SUBSEGMENT_LENGTH = "forecat_minimum_subsegment_length";
	public static String FORECAT_MAXIMUM_SUBSEGMENT_LENGTH = "forecat_maximum_subsegment_length";

	public static void populate() {
		if (!Preferences.existsPreference(FORECAT_ENABLED_OMEGAT_ENGINES))
			Preferences.setPreference(FORECAT_ENABLED_OMEGAT_ENGINES, "::");
		if (!Preferences.existsPreference(FORECAT_IGNORE_OMEGAT_ENGINES))
			Preferences.setPreference(FORECAT_IGNORE_OMEGAT_ENGINES, ":Forecat-OmegaT:");

		if (!Preferences.existsPreference(FORECAT_USE_API))
			Preferences.setPreference(FORECAT_USE_API, "false");
		if (!Preferences.existsPreference(FORECAT_API_URL))
			Preferences.setPreference(FORECAT_API_URL, "http://forecat-970.appspot.com/rest/services/");

		if (!Preferences.existsPreference(FORECAT_MINIMUM_SUBSEGMENT_LENGTH))
			Preferences.setPreference(FORECAT_MINIMUM_SUBSEGMENT_LENGTH, 1);
		if (!Preferences.existsPreference(FORECAT_MAXIMUM_SUBSEGMENT_LENGTH))
			Preferences.setPreference(FORECAT_MAXIMUM_SUBSEGMENT_LENGTH, 1);
	}

	public static void init() {
		populate();

		ForecatWebServiceInterface.BASEURL = Preferences.getPreference(FORECAT_API_URL);

		if (Preferences.getPreference(FORECAT_USE_API).equals("false")) {
			ForecatPTS.initInterface(InterfaceType.LOCAL);
		} else {
			ForecatPTS.initInterface(InterfaceType.API);
		}
		
		ForecatPTS.setMinSegmentLength(Integer.parseInt(Preferences.getPreference(FORECAT_MINIMUM_SUBSEGMENT_LENGTH)));
		ForecatPTS.setMaxSegmentLength(Integer.parseInt(Preferences.getPreference(FORECAT_MAXIMUM_SUBSEGMENT_LENGTH)));
	}
}
