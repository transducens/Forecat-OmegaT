package org.omegat.plugins.autocomplete.forecat.adapter;

import java.util.ArrayList;
import java.util.List;

import org.miniforecat.languages.LanguagesInput;
import org.miniforecat.languages.LanguagesOutput;
import org.miniforecat.selection.SelectionInput;
import org.miniforecat.selection.SelectionOutput;
import org.miniforecat.suggestions.SuggestionsInput;
import org.miniforecat.suggestions.SuggestionsOutput;
import org.miniforecat.translation.TranslationInput;
import org.miniforecat.translation.TranslationOutput;

public abstract class IForecatInterface {

	protected static IForecatInterface iface;

	
	public IForecatInterface()
	{

	}
	
	public static IForecatInterface getForecatInterface() {
		return iface;
	}
	
	public abstract List<LanguagesOutput> getLanguages(
			ArrayList<LanguagesInput> inputLanguagesList);

	public abstract TranslationOutput translate(
			TranslationInput inputTranslation);

	public abstract List<SuggestionsOutput> getSuggestions(
			SuggestionsInput inputSuggestions);

	public abstract SelectionOutput select(SelectionInput inputSelection);

}