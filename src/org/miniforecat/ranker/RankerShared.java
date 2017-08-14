package org.miniforecat.ranker;

import java.io.Serializable;
import java.util.List;

import org.miniforecat.suggestions.SuggestionsInput;
import org.miniforecat.suggestions.SuggestionsOutput;

public abstract class RankerShared implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7693233597371066385L;
	protected static int maxSuggestions = Integer.MAX_VALUE;
	protected static float threshold = -1;

	public static void setMaxSuggestions(int value) {
		maxSuggestions = value;
	}

	public static float getThreshold() {
		return threshold;
	}

	public static void setThreshold(float th) {
		threshold = th;
	}

	public abstract List<SuggestionsOutput> rankerService(SuggestionsInput rankinp, List<SuggestionsOutput> input);

}
