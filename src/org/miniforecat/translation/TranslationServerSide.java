package org.miniforecat.translation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.miniforecat.SessionShared;
import org.miniforecat.utils.PropertiesShared;
import org.miniforecat.utils.SubIdProvider;
import org.miniforecat.utils.UtilsShared;
import org.omegat.core.Core;
import org.omegat.core.machinetranslators.BaseTranslate;
import org.omegat.gui.exttrans.IMachineTranslation;
import org.omegat.gui.exttrans.MachineTranslateTextArea;
import org.omegat.plugins.autocomplete.forecat.preferences.ForecatPreferences;
import org.omegat.util.Language;
import org.omegat.util.Preferences;

public class TranslationServerSide {

	public TranslationOutput translationService(TranslationInput inputTranslation, SessionShared session)
			throws Exception {

		final Map<String, List<SourceSegment>> segmentPairs = new HashMap<String, List<SourceSegment>>();
		final Map<String, Integer> segmentCounts = new HashMap<String, Integer>();
		int currentId = 1;
		Object aux = session.getAttribute("SuggestionId");
		if (aux != null) {
			currentId = (Integer) aux;
		}
		List<SourceSegment> sourceSegments = sliceIntoSegments(slice(inputTranslation.getSourceText()),
				inputTranslation.getMaxSegmentLenth(), inputTranslation.getMinSegmentLenth(), currentId);
		session.setAttribute("segmentPairs", segmentPairs);
		session.setAttribute("segmentCounts", segmentCounts);

		translateOmegaTMT(sourceSegments, inputTranslation.getSourceCode(), inputTranslation.getTargetCode(),
				segmentPairs, segmentCounts);

		return new TranslationOutput(segmentPairs.size(), sourceSegments.size());
	}

	protected static String[] slice(String text) {
		return text.split("\\s+");
	}

	public static List<SourceSegment> sliceIntoSegments(String[] words, int maxSegmentLength, int minSegmentLength,
			int currentId) {

		int wordCount = words.length;
		int partialId = 0;

		if (wordCount * maxSegmentLength > PropertiesShared.maxSegments) {
			wordCount = (int) Math.floor(PropertiesShared.maxSegments / maxSegmentLength);
		}

		List<SourceSegment> sourceSegments = new ArrayList<SourceSegment>();

		int numchars = 0;

		for (int i = 0; i < wordCount; ++i) {
			String sourceText = "";
			String delim = "";
			for (int j = 0; j < maxSegmentLength; ++j) {
				if (i + j < words.length) {
					sourceText += delim + words[i + j];
					delim = " ";
					if (j >= (minSegmentLength - 1)) {
						sourceSegments.add(new SourceSegment(sourceText, i, false, currentId + partialId, numchars));
						partialId++;
					}
				} else {
					break;
				}
			}
			numchars += words[i].length() + 1;
		}
		return sourceSegments;
	}

	protected static void addSegments(Map<String, List<SourceSegment>> segmentPairs, Map<String, Integer> segmentCounts,
			String targetText, String engine, SourceSegment sourceSegment) {
		// sourceSegment does not contain any engines yet
		if (!targetText.isEmpty()) {
			addSegment(segmentPairs, segmentCounts, targetText, engine, sourceSegment);
			// String targetTextLowercase =
			// UtilsShared.uncapitalizeFirstLetter(targetText);
			// addSegment(segmentPairs, segmentCounts, targetTextLowercase,
			// engine, new SourceSegment(sourceSegment));
		}
	}

	protected static void addSegment(Map<String, List<SourceSegment>> segmentPairs, Map<String, Integer> segmentCounts,
			String targetText, String engine, SourceSegment sourceSegment) {
		SourceSegment s = null;

		SubIdProvider.addElement(targetText, sourceSegment);

		if (!segmentPairs.containsKey(targetText)) {
			segmentPairs.put(targetText, new ArrayList<SourceSegment>());
			segmentCounts.put(targetText, 0);
		} else {
			s = SourceSegment.searchByTextAndPosition(segmentPairs.get(targetText),
					sourceSegment.getSourceSegmentText(), sourceSegment.getPosition());
		}
		if (s != null) {
			s.addEngine(engine);
		} else {
			sourceSegment.addEngine(engine);
			sourceSegment.setUsed(false);
			segmentPairs.get(targetText).add(sourceSegment);
		}
		segmentCounts.put(targetText, segmentCounts.get(targetText) + 1);
	}

	public boolean showUnknown = false;

	protected List<IMachineTranslation> getOmegaTMT() {
		IMachineTranslation mt[];
		ArrayList<IMachineTranslation> ret = new ArrayList<IMachineTranslation>();
		MachineTranslateTextArea mtta = Core.getMachineTranslatePane();
		Field f;
		try {
			f = MachineTranslateTextArea.class.getDeclaredField("translators");
			f.setAccessible(true);
			mt = (IMachineTranslation[]) f.get(mtta);

			Method getNameMethod = IMachineTranslation.class.getDeclaredMethod("getName");
			getNameMethod.setAccessible(true);
			for (IMachineTranslation m : mt) {
				String nameMethod = getNameMethod.invoke(m).toString();
				if (!Preferences.getPreference(ForecatPreferences.FORECAT_IGNORE_OMEGAT_ENGINES)
						.contains(":" + nameMethod.replace(":", ";") + ":")) {
					if (Preferences.getPreference(ForecatPreferences.FORECAT_ENABLED_OMEGAT_ENGINES)
							.contains(":" + nameMethod.replace(":", ";") + ":")) {
						ret.add(m);
					}
				}
				// System.out.println(getNameMethod.invoke(m));
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return ret;
	}

	private void translateOmegaTMT(List<SourceSegment> sourceSegments, Language sLang, Language tLang,
			Map<String, List<SourceSegment>> segmentPairs, Map<String, Integer> segmentCounts) {

		List<IMachineTranslation> enabledMT = getOmegaTMT();
		String translation = null;
		boolean oldEnabledField = false;
		Method m;
		Field enabledField = null;

		if (enabledMT.size() == 0) {
			System.err.println("No MT system enabled for forecat_PTS");
			return;
		}

		for (IMachineTranslation im : enabledMT) {
			try {
				// Check if the IMachineTranslation implementation provides a
				// massTranslate method
				m = im.getClass().getDeclaredMethod("massTranslate", Language.class, Language.class, List.class);
				// If so, use it
				ArrayList<String> segments = new ArrayList<String>();
				for (SourceSegment ss : sourceSegments) {
					segments.add(ss.getSourceSegmentText());
				}
				@SuppressWarnings("unchecked")
				List<String> translations = (List<String>) m.invoke(im, sLang, tLang, segments);
				for (int i = 0; i < sourceSegments.size(); i++) {
					
					/*	Only remove the capitalization of the first letter if a word starts in capital. 
					 * 	If the second word is capitalized, we suppose its an acronym
					 * 
					 */
					
					String casedTranslation = translations.get(i);
					if (casedTranslation.length() > 1) {
						if (Character.isUpperCase(casedTranslation.charAt(0))
								&& !Character.isUpperCase(casedTranslation.charAt(1))) {
							casedTranslation = Character.toLowerCase(casedTranslation.charAt(0))
									+ casedTranslation.substring(1);
						}
					}

					addSegment(segmentPairs, segmentCounts, casedTranslation, "omegaTMT", sourceSegments.get(i));
				}
			} catch (IllegalArgumentException | NoSuchMethodException | SecurityException | IllegalAccessException
					| InvocationTargetException e) {
				// Else, translate segment by segment
				e.printStackTrace();
				e.getCause().printStackTrace();
				try {
					enabledField = BaseTranslate.class.getDeclaredField("enabled");
					enabledField.setAccessible(true);
					oldEnabledField = enabledField.getBoolean(im);
					enabledField.set(((BaseTranslate) im), true);
				} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
					ex.printStackTrace();
				}

				for (SourceSegment ss : sourceSegments) {
					try {
						translation = im.getTranslation(sLang, tLang, ss.getSourceSegmentText());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					addSegments(segmentPairs, segmentCounts, translation.toLowerCase(), "OmegaTMT", ss);
				}
				try {
					enabledField.set(((BaseTranslate) im), oldEnabledField);
				} catch (IllegalArgumentException | IllegalAccessException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
