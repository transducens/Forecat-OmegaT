package org.omegat.gui.editor.autocompleter;

public class AutoCompleterItem {
	
	public AutoCompleterItem(String payload, String[] extras, int replacementLength) {
		this.payload = payload;
		this.extras = extras;
		this.cursorAdjust = 0;
		this.keepSelection = false;
		this.replacementLength = replacementLength;
	}

	public AutoCompleterItem(String payload, String[] extras, int cursorAdjust, boolean keepSelection,
			int replacementLength) {
		this.payload = payload;
		this.extras = extras;
		this.cursorAdjust = cursorAdjust;
		this.keepSelection = keepSelection;
		this.replacementLength = replacementLength;
	}

	public final String payload;
	public final String[] extras;
	public final int cursorAdjust;
	public final boolean keepSelection;
	public final int replacementLength;

}
