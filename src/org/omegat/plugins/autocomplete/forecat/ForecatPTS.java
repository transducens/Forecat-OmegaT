package org.omegat.plugins.autocomplete.forecat;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;

import org.miniforecat.languages.LanguagesInput;
import org.miniforecat.languages.LanguagesOutput;
import org.miniforecat.ranker.RankerNeuralNetwork;
import org.miniforecat.selection.SelectionInput;
import org.miniforecat.translation.TranslationInput;
import org.miniforecat.translation.TranslationOutput;
import org.miniforecat.utils.AlignmentsHelper;
import org.omegat.core.Core;
import org.omegat.core.machinetranslators.BaseTranslate;
import org.omegat.gui.editor.autocompleter.AutoCompleterItem;
import org.omegat.plugins.autocomplete.forecat.adapter.ForecatMiniInterface;
import org.omegat.plugins.autocomplete.forecat.adapter.ForecatWebServiceInterface;
import org.omegat.plugins.autocomplete.forecat.adapter.IForecatInterface;
import org.omegat.plugins.autocomplete.forecat.gui.ForecatAutoCompleteView;
import org.omegat.plugins.autocomplete.forecat.preferences.ForecatPreferences;
import org.omegat.plugins.sessionlog.SessionLogPluginAccesser;
import org.omegat.util.Language;

/**
 * Main class of the OmegaT plugin. Extends BaseTranslate for creating the
 * translation segments whenever the user proceeds to the next sentence.
 * 
 * @author Daniel Torregrosa
 * 
 */
public class ForecatPTS extends BaseTranslate {

	private IForecatInterface iface;
	private static ForecatPTS self;

	private static int maxSegmentLength = 4;
	private static int minSegmentLength = 1;

	public static boolean isEnabled()
	{
		return self.enabled;
	}
	
	public static void useSuggestion(AutoCompleterItem item) {
		if (item != null) {
			// System.out.println(item.extras[1]);
			if (item.extras.length > 5) {
				self.iface
						.select(new SelectionInput(item.payload, 0, item.extras[1], Integer.parseInt(item.extras[5])));
			} else {
				self.iface.select(new SelectionInput(item.payload, 0, item.extras[1], item.payload.split(" ").length));
			}
		}
	}

	public enum InterfaceType {
		LOCAL, API
	}

	public static void initInterface(InterfaceType it) {
		if (it.equals(InterfaceType.LOCAL)) {
			self.iface = new ForecatMiniInterface();
		} else if (it.equals(InterfaceType.API)) {
			self.iface = new ForecatWebServiceInterface();
		}

		ArrayList<LanguagesInput> inputLanguagesList = new ArrayList<LanguagesInput>();
		List<LanguagesOutput> outputLanguagesList = null;
		LanguagesInput languagesInput = new LanguagesInput("apertium", "");
		inputLanguagesList.add(languagesInput);

		outputLanguagesList = self.iface.getLanguages(inputLanguagesList);

		StringBuilder sb = new StringBuilder();
		for (LanguagesOutput s : outputLanguagesList) {
			sb.append(s.getSourceName());
			sb.append(" to ");
			sb.append(s.getTargetName());
			sb.append("; ");
		}
		System.out.println("Languages: " + sb.toString());
	}

	/**
	 * Initialize and add the plugin
	 */
	public ForecatPTS() {
		self = this;
		new ForecatMenu();

		// VM wide Cookie management
		CookieManager cookieManager = new CookieManager();
		CookieHandler.getDefault();
		CookieHandler.setDefault(cookieManager);

		addForecatView();
		ForecatPreferences.init();
	}

	@Override
	protected String getPreferenceName() {
		return "Forecat-OmegaT";
	}

	public String getName() {
		return "Forecat-OmegaT";
	}

	@Override
	protected String translate(Language sLang, Language tLang, String text) {
		ForecatAutoCompleteView.setSourceSentence(text);
		TranslationInput inputTranslation = new TranslationInput(text, sLang, tLang, maxSegmentLength,
				minSegmentLength);
		TranslationOutput outputTranslation = null;
		outputTranslation = IForecatInterface.getForecatInterface().translate(inputTranslation);

		return ("Number of segments: " + outputTranslation.getNumberSegments() + "\n");
	}

	public static void addForecatView() {
		Core.getEditor().getAutoCompleter().addView(new ForecatAutoCompleteView());
	}

	public static int getMaxSegmentLength() {
		return maxSegmentLength;
	}

	public static void setMaxSegmentLength(int msl) {
		maxSegmentLength = msl;
	}

	public static int getMinSegmentLength() {
		return minSegmentLength;
	}

	public static void setMinSegmentLength(int msl) {
		minSegmentLength = msl;
	}

}
