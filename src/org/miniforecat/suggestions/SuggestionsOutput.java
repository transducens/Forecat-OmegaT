package org.miniforecat.suggestions;

public class SuggestionsOutput implements Comparable<SuggestionsOutput> {

	String suggestionText;
	String originalText;
	double suggestionFeasibility;
	int wordPosition;
	int charPosition;
	String id;

	protected SuggestionsOutput() {
	}

	public SuggestionsOutput(String suggestionText, String origText, double suggestionFeasibility, String id,
			int wordPosition, int charPosition) {
		this.suggestionText = suggestionText;
		this.originalText = origText;
		this.suggestionFeasibility = suggestionFeasibility;
		this.id = id;
		this.wordPosition = wordPosition;
		this.charPosition = charPosition;
	}

	public void setSuggestionText(String suggestionText) {
		this.suggestionText = suggestionText;
	}

	public String getSuggestionText() {
		return suggestionText;
	}

	public void setSuggestionFeasibility(double suggestionFeasibility) {
		this.suggestionFeasibility = suggestionFeasibility;
	}

	public double getSuggestionFeasibility() {
		return suggestionFeasibility;
	}

	public String getId() {
		return id;
	}

	public int getWordPosition() {
		return wordPosition;
	}

	public int getCharPosition() {
		return charPosition;
	}

	public int getSuggestionWordLength() {
		if ("".equals(suggestionText))
			return 0;
		return suggestionText.split(" ").length;
	}

	public int getSuggestionCharLength() {
		return suggestionText.length();
	}

	public String getOriginal() {
		return originalText;
	}

	public int getOriginalWordLength() {
		if ("".equals(originalText))
			return 0;
		return originalText.split(" ").length;
	}

	public int getOriginalCharLength() {
		return originalText.length();
	}

	@Override
	public int compareTo(SuggestionsOutput s) {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		if (this == s) {
			return EQUAL;
		}

		int comparison = Double.compare(this.suggestionFeasibility, s.suggestionFeasibility);
		if (comparison != EQUAL) {
			return -comparison;
		}

		if (this.suggestionText.length() < s.suggestionText.length()) {
			return BEFORE;
		}
		if (this.suggestionText.length() > s.suggestionText.length()) {
			return AFTER;
		}

		return this.suggestionText.compareTo(s.suggestionText);

	}
}
