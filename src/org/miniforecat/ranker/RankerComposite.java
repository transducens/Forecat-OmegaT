package org.miniforecat.ranker;

import java.util.ArrayList;
import java.util.List;

import org.miniforecat.suggestions.SuggestionsInput;
import org.miniforecat.suggestions.SuggestionsOutput;

/**
 * Composes a pair of rankers so one is applied before the other.
 * 
 * @author Daniel Torregrosa
 * 
 */
public class RankerComposite extends RankerShared {

	private static final long serialVersionUID = -1229745996571442964L;
	RankerShared applyBefore = null;
	RankerShared applyAfter = null;

	protected RankerComposite() {

	}

	public RankerComposite(RankerShared before, RankerShared after) {
		applyBefore = before;
		applyAfter = after;
	}

	@Override
	public List<SuggestionsOutput> rankerService(SuggestionsInput rankInp, List<SuggestionsOutput> input) {
		ArrayList<SuggestionsOutput> outputSuggestionsList = new ArrayList<SuggestionsOutput>();

		// Only the last ranker should limit the number of suggestions to be shown
		int holdmaxSuggestions = maxSuggestions;
		maxSuggestions = Integer.MAX_VALUE;
		input = applyBefore.rankerService(rankInp, input);
		maxSuggestions = holdmaxSuggestions;
		input = applyAfter.rankerService(rankInp, input);

		for (SuggestionsOutput so : input) {
			outputSuggestionsList.add(so);
		}

		return outputSuggestionsList;
	}
}
