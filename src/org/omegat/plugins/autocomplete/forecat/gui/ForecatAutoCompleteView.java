package org.omegat.plugins.autocomplete.forecat.gui;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.apache.commons.lang.StringUtils;
import org.miniforecat.ranker.Pair;
import org.miniforecat.suggestions.SuggestionsInput;
import org.miniforecat.suggestions.SuggestionsOutput;
import org.miniforecat.utils.AlignmentsHelper;
import org.omegat.gui.editor.autocompleter.AutoCompleterItem;
import org.omegat.gui.editor.autocompleter.AutoCompleterListView;
import org.omegat.plugins.autocomplete.forecat.ForecatPTS;
import org.omegat.plugins.autocomplete.forecat.adapter.ForecatMiniInterface;
import org.omegat.plugins.sessionlog.SessionLogPluginAccesser;
import org.omegat.util.StaticUtils;

public class ForecatAutoCompleteView extends AutoCompleterListView {

	ArrayList<AutoCompleterItem> lastResult = null;

	public ForecatAutoCompleteView() {
		super("Forecat");
	}

	public static String sourceSentence = "";
	private static int currentSegmentPos;
	private static int lastSegmentUsed;
	private static int lastSegmentLength;
	private static int sourceWords;

	public static void setSourceSentence(String ss) {
		sourceSentence = ss;
		sourceWords = sourceSentence.split(" ").length;
	}

	@Override
	public AutoCompleterItem getSelectedValue() {
		AutoCompleterItem toRet;
		lastSegmentUsed = currentSegmentPos;
		if (itemBeingTabbed >= 0) {
			AutoCompleterItem selected = lastResult.get(itemBeingTabbed);
			String words[] = selected.payload.split(" ");
			StringBuilder realSuggestion = new StringBuilder();
			int j = 0;

			while (j < numberWordsTabbed && j < words.length) {
				realSuggestion.append(words[j] + " ");
				j++;
			}
			toRet = new AutoCompleterItem(realSuggestion.toString(),
					new String[] { selected.extras[0], selected.extras[1], selected.extras[2], selected.extras[3],
							selected.extras[4], "" + numberWordsTabbed },
					selected.cursorAdjust, selected.keepSelection, selected.replacementLength);
			ForecatPTS.useSuggestion(toRet);
		} else {
			toRet = super.getSelectedValue();
			if (toRet != null) {
				ForecatPTS.useSuggestion(toRet);
			}
		}
		lastSegmentLength = Integer.parseInt(toRet.extras[4]);
		return toRet;
	}

	@Override
	public boolean shouldCloseOnSelection() {
		return false;
	}

	@Override
	public List<AutoCompleterItem> computeListData(String prevText, boolean contextualOnly) {

		int currentSegmentStart = prevText.length() - 1;
		int numWords = StringUtils.countMatches(prevText, " ");
		int index = 0;

		itemBeingTabbed = -1;
		numberWordsTabbed = 0;

		while (currentSegmentStart > 0 && prevText.charAt(currentSegmentStart) != ' ') {
			currentSegmentStart--;
		}

		if (currentSegmentStart != 0)
			currentSegmentStart++;

		currentSegmentPos = currentSegmentStart;
		AlignmentsHelper.computeAlignments(prevText, sourceWords, prevText.split(" "), prevText.length());
		String currentPrefix = prevText.substring(0, currentSegmentStart);
		String currentSegment = prevText.substring(currentSegmentStart);

		if ("".equals(currentSegment)) {
			return new ArrayList<AutoCompleterItem>();
		}

		ArrayList<AutoCompleterItem> result = new ArrayList<AutoCompleterItem>();

		List<SuggestionsOutput> entries = ForecatMiniInterface.getForecatInterface()
				.getSuggestions(new SuggestionsInput(currentPrefix, currentSegment, numWords, sourceSentence,
						((currentSegmentPos - 1) == lastSegmentUsed), lastSegmentUsed, lastSegmentLength));
		for (SuggestionsOutput sug : entries) {
			index++;
			String niceText;
			if (index < 10) {
				niceText = "<html><body style=300px; border-bottom: 1px solid black;'><font size=-2 color=gray'>"
						+ index + "</font>" + sug.getSuggestionText() + "</body></html>";
			} else {
				niceText = "<html><body style=300px; border-bottom: 1px solid black;'>" + sug.getSuggestionText()
						+ "</body></html>";
			}

			result.add(
					new AutoCompleterItem(sug.getSuggestionText() + " ",
							new String[] { niceText, sug.getId(), "" + sug.getSuggestionFeasibility(),
									"" + sug.getWordPosition(), "" + sug.getOriginalWordLength() },
							currentSegment.length()));
		}
		lastResult = result;
		return result;
	}

	protected int itemBeingTabbed = -1;
	protected int numberWordsTabbed = 0;

	protected void formatSuggestion() {
		ArrayList<AutoCompleterItem> newEntryList = new ArrayList<AutoCompleterItem>();
		AutoCompleterItem ac;
		int selectedIndex = getList().getSelectedIndex(), i, j;
		for (i = 0; i < lastResult.size(); i++) {
			ac = lastResult.get(i);
			if (i == itemBeingTabbed) {
				String words[] = ac.payload.split(" ");
				StringBuilder toRet = new StringBuilder();
				toRet.append("<html><body style=300px; border-bottom: 1px solid black;'><font size=-2 color=gray'>");
				if (i < 9)
					toRet.append(i + 1);
				toRet.append("</font><font color='red'>");
				j = 0;
				while (j < numberWordsTabbed && j < words.length) {
					toRet.append(words[j] + " ");
					j++;
				}
				toRet.append("</font>");
				while (j < words.length) {
					toRet.append(words[j] + " ");
					j++;
				}
				toRet.append("</body></html>");
				ac.extras[0] = toRet.toString();
				newEntryList.add(new AutoCompleterItem(ac.payload, ac.extras, ac.cursorAdjust, ac.keepSelection,
						ac.replacementLength));
			} else {
				if (i < 9) {
					ac.extras[0] = "<html><body style=300px; border-bottom: 1px solid black;'><font size=-2 color=gray'>"
							+ (i + 1) + "</font>" + ac.payload + "</body></html>";
				} else {
					ac.extras[0] = "<html><body style=300px; border-bottom: 1px solid black;'>" + ac.payload
							+ "</body></html>";
				}
				newEntryList.add(ac);
			}
		}
		getList().setModel(new DefaultListModel<>());
		getList().setListData(newEntryList.toArray(new AutoCompleterItem[newEntryList.size()]));
		getList().setSelectedIndex(selectedIndex);
	}

	protected void resetTab() {
		itemBeingTabbed = -1;
		numberWordsTabbed = 0;

		formatSuggestion();
	}

	@Override
	public boolean processKeys(KeyEvent e) {
		if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0) {
			// 0 to 9 are contiguous according to java doc
			// https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html#VK_0
			// We use 1 as the first element
			if (e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_9) {
				int selected = e.getKeyCode() - KeyEvent.VK_1;
				SessionLogPluginAccesser.GenericEvent("autocompleteAction", "VK_" + (selected + 1), "",
						"Selected the " + (selected + 1) + " suggestion.");
				JList<AutoCompleterItem> list = super.getList();
				list.setSelectedIndex(selected);
				e.setKeyCode(KeyEvent.VK_ENTER);
				e.setModifiers(0);
				return false;
			}
		}
		if (StaticUtils.isKey(e, KeyEvent.VK_TAB, 0)) {
			itemBeingTabbed = getList().getSelectedIndex();
			numberWordsTabbed += 1;
			formatSuggestion();
			return true;
		} else if (!StaticUtils.isKey(e, KeyEvent.VK_ENTER, 0)) {
			resetTab();
		}

		return super.processKeys(e);
	}

	@Override
	public String itemToString(AutoCompleterItem item) {
		return item.extras[0];
	}

}
