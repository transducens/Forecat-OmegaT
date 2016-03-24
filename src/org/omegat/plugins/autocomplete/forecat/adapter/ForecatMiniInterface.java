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
import org.miniforecat.suggestions.SuggestionsShared;
import org.miniforecat.translation.TranslationInput;
import org.miniforecat.translation.TranslationOutput;
import org.miniforecat.languages.LanguagesServerSide;
import org.miniforecat.selection.SelectionShared;
import org.miniforecat.selection.SelectionEqualsShared;
import org.miniforecat.suggestions.SuggestionsBasic;
import org.miniforecat.translation.TranslationServerSide;

/**
 * Interface with the simplified implementation of Forecat
 * @author Daniel Torregrosa
 *
 */
public class ForecatMiniInterface extends IForecatInterface {

	protected LanguagesServerSide languages;
	protected TranslationServerSide translation;
	protected SuggestionsShared suggestions;
	protected SelectionShared selection;
	protected SessionShared session;
	
	public ForecatMiniInterface() {
		super();
		iface = this;
		languages = new LanguagesServerSide();
		session = new SessionBrowserSideConsole();
		suggestions = new SuggestionsBasic();
		translation = new TranslationServerSide();
		selection = new SelectionEqualsShared();
	}

	@Override
	public List<LanguagesOutput> getLanguages(
			ArrayList<LanguagesInput> inputLanguagesList) {
		List<LanguagesOutput> outputLanguagesList = null;
		try {
			outputLanguagesList = languages.languagesService(
					inputLanguagesList, session);
		} catch (BboxcatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(1);
		}
		return outputLanguagesList;
	}

	@Override
	public TranslationOutput translate(TranslationInput inputTranslation) {
		TranslationOutput outputTranslation = null;

		try {
			outputTranslation = translation.translationService(
					inputTranslation, session);
			logTranslate(session);
			
		} catch (BboxcatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (Exception e){
			e.printStackTrace();
			outputTranslation = new TranslationOutput(0,0);
		}
		return outputTranslation;
	}

	@Override
	public List<SuggestionsOutput> getSuggestions(
			SuggestionsInput inputSuggestions) {
		ArrayList<SuggestionsOutput> outputSuggestionsList = null;
		try {
			outputSuggestionsList = (ArrayList<SuggestionsOutput>) suggestions
					.suggestionsService(inputSuggestions, session);
			logSuggestions(session, inputSuggestions, outputSuggestionsList);
		} catch (BboxcatException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return outputSuggestionsList;
	}

	@Override
	public SelectionOutput select(SelectionInput inputSelection) {
		SelectionOutput outputSelection = null;
		try {
			outputSelection = selection.selectionService(inputSelection,
					session);
			logSelect(session, inputSelection, outputSelection);
		} catch (BboxcatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(1);
		}
		return outputSelection;
	}
}
