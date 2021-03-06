package org.miniforecat.ranker;

import java.util.ArrayList;
import java.util.List;

import org.miniforecat.suggestions.SuggestionsInput;
import org.miniforecat.suggestions.SuggestionsOutput;
import org.miniforecat.utils.Quicksort;

/**
 * Alternates the shortest and longest suggestions
 * 
 * @author Daniel Torregrosa
 * 
 */
public class RankerShortestLongestFirst extends RankerShared {

	private static final long serialVersionUID = -5509552226829972825L;

	@Override
	public List<SuggestionsOutput> rankerService(SuggestionsInput rankInp, List<SuggestionsOutput> input) {
		ArrayList<SuggestionsOutput> outputSuggestionsList = new ArrayList<SuggestionsOutput>();
		ArrayList<Integer> sortList = new ArrayList<Integer>();

		SuggestionsOutput so;

		for (int index = 0; index < input.size(); index++) {
			sortList.add(index);
			so = input.get(index);
			so.setSuggestionFeasibility(so.getSuggestionText().length());
		}
		Quicksort q = new Quicksort();
		q.sort(sortList, input);

		boolean first = false;
		for (int index = 0; index < maxSuggestions && index < input.size(); index++) {
			if (first) {
				outputSuggestionsList.add(input.get(sortList.get(sortList.size() - index / 2 - 1)));
			} else {
				outputSuggestionsList.add(input.get(sortList.get(index / 2)));
			}
			first = !first;
		}

		return outputSuggestionsList;
	}

}
