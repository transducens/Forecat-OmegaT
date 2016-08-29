package org.miniforecat.suggestions;

public class SuggestionsInput {

	private String fixedPrefix;
	private String lastWordPrefix;
	private String sourceText;
	private int position;
	private boolean fromUsed;
	private int lastUsedStart, lastUsedEnd;

	protected SuggestionsInput() {
	}

	public SuggestionsInput(String fixedPrefix, String lastWordPrefix, int position, String sourceText,
			boolean fromUsed, int lastUsedStart, int lastUsedEnd) {
		this.fixedPrefix = fixedPrefix;
		this.lastWordPrefix = lastWordPrefix;
		this.position = position;
		this.sourceText = sourceText;
		this.fromUsed = fromUsed;
		this.lastUsedStart = lastUsedStart;
		this.lastUsedEnd = lastUsedEnd;
	}

	public String getFixedPrefix() {
		return fixedPrefix;
	}

	public void setFixedPrefix(String fixedPrefix) {
		this.fixedPrefix = fixedPrefix;
	}

	public int getFixedPrefixWordLength() {
		if (fixedPrefix.equals(""))
			return 0;
		return fixedPrefix.split(" ").length;
	}

	public int getFixedPrefixCharLength() {
		return fixedPrefix.length();
	}

	public String getLastWordPrefix() {
		return lastWordPrefix;
	}

	public void setPrefixText(String lastWordPrefix) {
		this.lastWordPrefix = lastWordPrefix;
	}

	public int getPosition() {
		return position;
	}

	public String getSourceText() {
		return sourceText;
	}

	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
	}

	public int getSourceWordLength() {
		if ("".equals(sourceText))
			return 0;
		return sourceText.split(" ").length;
	}

	public int getSourceCharLength() {
		return sourceText.length();
	}

	public boolean getFromused() {
		return fromUsed;
	}

	public int getLastUsedStart() {
		return lastUsedStart;
	}

	public int getLastUsedEnd() {
		return lastUsedEnd;
	}

}
