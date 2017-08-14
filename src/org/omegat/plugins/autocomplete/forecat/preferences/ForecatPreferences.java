package org.omegat.plugins.autocomplete.forecat.preferences;

import org.miniforecat.ranker.RankerNeuralNetwork;
import org.miniforecat.ranker.RankerShared;
import org.miniforecat.suggestions.SuggestionsRanker;
import org.omegat.plugins.autocomplete.forecat.ForecatPTS;
import org.omegat.plugins.autocomplete.forecat.ForecatPTS.InterfaceType;
import org.omegat.plugins.autocomplete.forecat.adapter.ForecatWebServiceInterface;
import org.omegat.plugins.autocomplete.forecat.adapter.IForecatInterface;
import org.omegat.util.Preferences;

import com.googlecode.fannj.Fann;

public class ForecatPreferences {

	public static final String FORECAT_THRESHOLD = "forecat_threshold";
	public static final String FORECAT_ABSOLUTE_AVG = "forecat_absolute_avg";
	public static final String FORECAT_ABSOLUTE_STDEV = "forecat_absolute_stdev";
	public static final String FORECAT_RELATIVE_AVG = "forecat_relative_avg";
	public static final String FORECAT_RELATIVE_STDEV = "forecat_relative_stdev";

	public static final String FORECAT_ENABLED_OMEGAT_ENGINES = "forecat_enabled_omegat_mt";
	public static final String FORECAT_IGNORE_OMEGAT_ENGINES = "forecat_ignore_omegat_mt";

	public static final String FORECAT_USE_API = "forecat_use_api";
	public static final String FORECAT_API_URL = "forecat_api_url";

	public static final String FORECAT_MINIMUM_SUBSEGMENT_LENGTH = "forecat_minimum_subsegment_length";
	public static final String FORECAT_MAXIMUM_SUBSEGMENT_LENGTH = "forecat_maximum_subsegment_length";
	public static final String FORECAT_MAXIMUM_SUGGESTIONS = "forecat_maximum_suggestions";
	public static final String FORECAT_SUGGESTION_RANKER = "forecat_suggestion_ranker";
	public static final String FORECAT_ANN_FILE = "forecat_ann_file";

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
			Preferences.setPreference(FORECAT_MAXIMUM_SUBSEGMENT_LENGTH, 4);
		if (!Preferences.existsPreference(FORECAT_THRESHOLD))
			Preferences.setPreference(FORECAT_THRESHOLD, -1);

		if (!Preferences.existsPreference(FORECAT_ABSOLUTE_AVG))
			Preferences.setPreference(FORECAT_ABSOLUTE_AVG, 0);
		if (!Preferences.existsPreference(FORECAT_ABSOLUTE_STDEV))
			Preferences.setPreference(FORECAT_ABSOLUTE_STDEV, 1);
		if (!Preferences.existsPreference(FORECAT_RELATIVE_AVG))
			Preferences.setPreference(FORECAT_RELATIVE_AVG, 0);
		if (!Preferences.existsPreference(FORECAT_RELATIVE_STDEV))
			Preferences.setPreference(FORECAT_RELATIVE_STDEV, 1);

		if (!Preferences.existsPreference(FORECAT_MAXIMUM_SUGGESTIONS))
			Preferences.setPreference(FORECAT_MAXIMUM_SUGGESTIONS, 4);
		if (!Preferences.existsPreference(FORECAT_SUGGESTION_RANKER))
			Preferences.setPreference(FORECAT_SUGGESTION_RANKER, "heuristic");
		if (!Preferences.existsPreference(FORECAT_ANN_FILE))
			Preferences.setPreference(FORECAT_ANN_FILE, "");
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
		RankerShared.setThreshold(Float.parseFloat(Preferences.getPreference(FORECAT_THRESHOLD)));
		RankerShared.setMaxSuggestions(Integer.parseInt(Preferences.getPreference(FORECAT_MAXIMUM_SUGGESTIONS)));

		RankerNeuralNetwork.diffAvg = Float.parseFloat(Preferences.getPreference(FORECAT_ABSOLUTE_AVG));
		RankerNeuralNetwork.diffDev = Float.parseFloat(Preferences.getPreference(FORECAT_ABSOLUTE_STDEV));
		RankerNeuralNetwork.ratioAvg = Float.parseFloat(Preferences.getPreference(FORECAT_RELATIVE_AVG));
		RankerNeuralNetwork.ratioDev = Float.parseFloat(Preferences.getPreference(FORECAT_RELATIVE_STDEV));

		if (!Fann.hasFann())
		{
			Preferences.setPreference(FORECAT_SUGGESTION_RANKER, "heuristic");
		}
		
		if ("neural".equals(Preferences.getPreference(FORECAT_SUGGESTION_RANKER))) {
			IForecatInterface.getForecatInterface().useNeural();
			RankerNeuralNetwork.setAnnFile(Preferences.getPreference(FORECAT_ANN_FILE));
			RankerNeuralNetwork.init();
		} else if ("heuristic".equals(Preferences.getPreference(FORECAT_SUGGESTION_RANKER))) {
			IForecatInterface.getForecatInterface().useHeuristic();
		}

	}
}
