package org.miniforecat.suggestions;

import java.util.List;
import java.util.Map;

import org.miniforecat.ranker.RankerShared;
import org.miniforecat.suggestions.SuggestionsInput;
import org.miniforecat.suggestions.SuggestionsOutput;
import org.miniforecat.suggestions.SuggestionsShared;
import org.miniforecat.translation.SourceSegment;

public class SuggestionsRanker extends SuggestionsShared {

	SuggestionsShared base;
	RankerShared ranker;

	@Override
	public List<SuggestionsOutput> obtainSuggestions(SuggestionsInput input,
			Map<String, List<SourceSegment>> segmentPairs, Map<String, Integer> segmentCounts) {
		List<SuggestionsOutput> output = base.obtainSuggestions(input, segmentPairs, segmentCounts);
		try {
			output = ranker.rankerService(input, output);
		} catch (Exception e) {
			System.out.println("Forecat error: obtainSuggestions");
		}
		return output;
	}

	protected SuggestionsRanker() {

	}

	public SuggestionsRanker(SuggestionsShared base, RankerShared ranker) {
		super();
		this.base = base;
		this.ranker = ranker;
	}

}
