package org.miniforecat.ranker;

import java.io.Serializable;
import java.util.List;

import org.miniforecat.suggestions.SuggestionsOutput;

public abstract class RankerShared implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7693233597371066385L;
	protected static int maxSuggestions = Integer.MAX_VALUE;

	public static void setMaxSuggestions(int value) {
		maxSuggestions = value;
	}

	public abstract List<SuggestionsOutput> rankerService(RankerInput rankinp,
			List<SuggestionsOutput> input);

}
