package org.omegat.plugins.autocomplete.forecat.adapter;

import java.util.ArrayList;
import java.util.List;

import org.miniforecat.exceptions.BboxcatException;
import org.miniforecat.SessionBrowserSideConsole;
import org.miniforecat.SessionShared;
import org.miniforecat.languages.LanguagesInput;
import org.miniforecat.languages.LanguagesOutput;
import org.miniforecat.selection.SelectionInput;
import org.miniforecat.selection.SelectionOutput;
import org.miniforecat.suggestions.SuggestionsInput;
import org.miniforecat.suggestions.SuggestionsOutput;
import org.miniforecat.suggestions.SuggestionsRanker;
import org.miniforecat.suggestions.SuggestionsShared;
import org.miniforecat.translation.TranslationInput;
import org.miniforecat.translation.TranslationOutput;
import org.miniforecat.languages.LanguagesServerSide;
import org.miniforecat.ranker.RankerComposite;
import org.miniforecat.ranker.RankerLongestShortestFirst;
import org.miniforecat.ranker.RankerLongestShortestFromPosition;
import org.miniforecat.ranker.RankerNeuralNetwork;
import org.miniforecat.ranker.RankerPosition;
import org.miniforecat.ranker.RankerShortestLongestFirst;
import org.miniforecat.selection.SelectionShared;
import org.miniforecat.selection.SelectionEqualsShared;
import org.miniforecat.suggestions.SuggestionsBasic;
import org.miniforecat.translation.TranslationServerSide;

/**
 * Interface with the simplified implementation of Forecat
 * 
 * @author Daniel Torregrosa
 *
 */
public class ForecatMiniInterface extends IForecatInterface {

	protected LanguagesServerSide languages;
	protected TranslationServerSide translation;
	protected SuggestionsShared suggestions;
	protected SelectionShared selection;

	public ForecatMiniInterface() {
		super();
		iface = this;
		languages = new LanguagesServerSide();
		session = new SessionBrowserSideConsole();
		translation = new TranslationServerSide();
		selection = new SelectionEqualsShared();
	}

	public void useHeuristic() {
		suggestions = new SuggestionsRanker(new SuggestionsBasic(),
				new RankerComposite(new RankerPosition(), new RankerLongestShortestFromPosition()));
	}

	public void useNeural() {
		suggestions = new SuggestionsRanker(new SuggestionsBasic(), new RankerNeuralNetwork());
	}

	@Override
	public List<LanguagesOutput> getLanguages(ArrayList<LanguagesInput> inputLanguagesList) {
		List<LanguagesOutput> outputLanguagesList = null;
		try {
			outputLanguagesList = languages.languagesService(inputLanguagesList, session);
		} catch (BboxcatException e) {
			System.out.println("Forecat error: getting languages " + e.getMessage());
		}
		return outputLanguagesList;
	}

	@Override
	public TranslationOutput translate(TranslationInput inputTranslation) {
		TranslationOutput outputTranslation = null;

		try {
			outputTranslation = translation.translationService(inputTranslation, session);
			logTranslate(session);

		} catch (BboxcatException e) {
			System.out.println("Forecat error: translating " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Forecat error: translating " + e.getMessage());
			outputTranslation = new TranslationOutput(0, 0);
		}
		return outputTranslation;
	}

	@Override
	public List<SuggestionsOutput> getSuggestions(SuggestionsInput inputSuggestions) {
		ArrayList<SuggestionsOutput> outputSuggestionsList = null;
		try {
			outputSuggestionsList = (ArrayList<SuggestionsOutput>) suggestions.suggestionsService(inputSuggestions,
					session);
			logSuggestions(session, inputSuggestions, outputSuggestionsList);
		} catch (BboxcatException e) {
			System.out.println("Forecat error: getting suggestions " + e.getMessage());
		}
		return outputSuggestionsList;
	}

	@Override
	public SelectionOutput select(SelectionInput inputSelection) {
		SelectionOutput outputSelection = null;
		try {
			outputSelection = selection.selectionService(inputSelection, session);
			logSelect(session, inputSelection, outputSelection);
		} catch (BboxcatException e) {
			System.out.println("Forecat error: perforing selection " + e.getMessage());
		}
		return outputSelection;
	}
}
