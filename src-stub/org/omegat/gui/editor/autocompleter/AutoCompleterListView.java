package org.omegat.gui.editor.autocompleter;

import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JList;

public class AutoCompleterListView implements AbstractAutoCompleterView {

	public AutoCompleterListView(String string) {
		// TODO Auto-generated constructor stub
	}

	public String itemToString(AutoCompleterItem item) {
		// TODO Auto-generated method stub
		return null;
	}

	public AutoCompleterItem getSelectedValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean shouldCloseOnSelection() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<AutoCompleterItem> computeListData(String prevText, boolean contextualOnly) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean processKeys(KeyEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public JList<AutoCompleterItem> getList() {
		return null;
	}

	@Override
	public void updateViewData() {
	}

}
