package org.miniforecat.selection;

public class SelectionInput {

	private String selectionText;
	private int position;
	private String id;
	private int prefixLength;

	protected SelectionInput() {
	}

	public SelectionInput(String selectionText, int position, String id, int prefixLength) {
		this.selectionText = selectionText;
		this.position = position;
		this.id = id;
		this.prefixLength = prefixLength;
	}

	public String getSelectionText() {
		return selectionText;
	}

	public int getPosition() {
		return position;
	}

	public void setSelectionText(String selectionText) {
		this.selectionText = selectionText;
	}

	public String getId() {
		return id;
	}
	
	public int getPrefixLength()
	{
		return prefixLength;
	}

}
