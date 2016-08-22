package org.miniforecat.suggestions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.miniforecat.suggestions.SuggestionsInput;
import org.miniforecat.suggestions.SuggestionsOutput;
import org.miniforecat.suggestions.SuggestionsShared;
import org.miniforecat.translation.SourceSegment;
import org.miniforecat.utils.SubIdProvider;
import org.miniforecat.utils.UtilsShared;

public class SuggestionsBasic extends SuggestionsShared {
	@Override
	public List<SuggestionsOutput> obtainSuggestions(SuggestionsInput input,
			Map<String, List<SourceSegment>> segmentPairs, Map<String, Integer> segmentCounts) {

		SortedSet<SuggestionsOutput> preoutput = new TreeSet<SuggestionsOutput>();
		String originalTyped = input.getPrefixText();
		input.setPrefixText(input.getPrefixText().toLowerCase());

		Iterator<Entry<String, List<SourceSegment>>> it = segmentPairs.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<SourceSegment>> e = it.next();

			int closerPosition = -1;
			int closerDifference = 0;
			String closerId = "";
			for (SourceSegment ss : segmentPairs.get(e.getKey())) {
				if (closerPosition == -1) {
					closerPosition = ss.getPosition();
					closerDifference = Math.abs(ss.getPosition() - input.getPrefixStart());
					closerId = ss.getId() + "." + SubIdProvider.getSubId(e.getKey(), ss);
				} else if (Math.abs(ss.getPosition() - input.getPrefixStart()) < closerDifference) {
					closerPosition = ss.getPosition();
					closerDifference = Math.abs(ss.getPosition() - input.getPrefixStart());
					closerId = ss.getId() + "." + SubIdProvider.getSubId(e.getKey(), ss);
				}
			}
			if (UtilsShared.isPrefix(input.getPrefixText(), e.getKey()) && segmentCounts.get(e.getKey()) > 0) {
				preoutput.add(new SuggestionsOutput(e.getKey(), e.getKey().length(), closerId, closerPosition,
						e.getKey().split(" ").length));
			}
		}

		ArrayList<SuggestionsOutput> output = new ArrayList<SuggestionsOutput>(preoutput);

		manageCaps(output, originalTyped);

		return output;
	}

	public static void manageCaps(ArrayList<SuggestionsOutput> outputSuggestionsList, String typed) {
		String newString = "";
		int atchar = 0;
		for (SuggestionsOutput sg : outputSuggestionsList) {
			newString = "";
			for (atchar = 0; atchar < typed.length() && atchar < sg.getSuggestionText().length(); atchar++) {
				if (Character.isUpperCase(typed.charAt(atchar))) {
					newString += Character.toUpperCase(sg.getSuggestionText().charAt(atchar));
				} else {
					newString += sg.getSuggestionText().charAt(atchar);
				}
			}

			while (atchar < sg.getSuggestionText().length()) {
				newString += sg.getSuggestionText().charAt(atchar);
				atchar++;
			}

			sg.setSuggestionText(newString);
		}
	}
}
