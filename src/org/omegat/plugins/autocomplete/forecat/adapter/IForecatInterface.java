package org.omegat.plugins.autocomplete.forecat.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.miniforecat.SessionShared;
import org.miniforecat.languages.LanguagesInput;
import org.miniforecat.languages.LanguagesOutput;
import org.miniforecat.selection.SelectionInput;
import org.miniforecat.selection.SelectionOutput;
import org.miniforecat.suggestions.SuggestionsInput;
import org.miniforecat.suggestions.SuggestionsOutput;
import org.miniforecat.translation.SourceSegment;
import org.miniforecat.translation.TranslationInput;
import org.miniforecat.translation.TranslationOutput;
import org.omegat.plugins.sessionlog.SessionLogPluginAccesser;

public abstract class IForecatInterface {

	protected static IForecatInterface iface;

	public IForecatInterface() {

	}

	public static IForecatInterface getForecatInterface() {
		return iface;
	}

	public abstract List<LanguagesOutput> getLanguages(ArrayList<LanguagesInput> inputLanguagesList);

	public abstract TranslationOutput translate(TranslationInput inputTranslation);

	public abstract List<SuggestionsOutput> getSuggestions(SuggestionsInput inputSuggestions);

	public abstract SelectionOutput select(SelectionInput inputSelection);

	protected void logTranslate(SessionShared session) {
		@SuppressWarnings("unchecked")
		Map<String, List<SourceSegment>> segmentPairs = (Map<String, List<SourceSegment>>) session
				.getAttribute("segmentPairs");
		SessionLogPluginAccesser.init();

		for (Entry<String, List<SourceSegment>> entry : segmentPairs.entrySet()) {
			for (SourceSegment s : entry.getValue()) {
				SessionLogPluginAccesser.GenericEvent(
						"GENERATED_SUGGESTION", "TRANSLATED", s.getId() + "," + s.getPosition() + ","
								+ s.getCharPosition() + "," + s.getSourceSegmentText().split(" ").length,
						entry.getKey());
			}
		}
	}

	protected void logSuggestions(SessionShared session, SuggestionsInput inputSuggestions,
			List<SuggestionsOutput> outputSuggestions) {
		for (SuggestionsOutput so : outputSuggestions) {
			SessionLogPluginAccesser
					.GenericEvent("OFFERED_SUGGESTION", "OFFERED",
							so.getPosition() + "," + so.getSuggestionFeasibility() + ","
									+ inputSuggestions.getPosition() + "," + inputSuggestions.getPrefixText(),
							so.getId());
		}

	}

	protected void logSelect(SessionShared session, SelectionInput inputSelection, SelectionOutput outputSelection) {
		SessionLogPluginAccesser.GenericEvent("USED_SUGGESTION", "USED", "" + inputSelection.getPrefixLength(),
				inputSelection.getId());
	}

}