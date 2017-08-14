package org.miniforecat.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.miniforecat.ranker.Pair;
import org.miniforecat.ranker.RankerPressureBasic;
import org.miniforecat.translation.SourceSegment;
import org.omegat.plugins.autocomplete.forecat.adapter.IForecatInterface;

public class AlignmentsHelper {

	/**
	 * Computes the alignments as described in Miquel Esplà-Gomis, Felipe
	 * Sánchez-Martínez, Mikel L Forcada. A Simple Approach to Use Bilingual
	 * Information Sources for Word Alignment. En Procesamiento del Lenguaje
	 * Natural, 49 (XXVIII Conferència de la Sociedad Española de Procesamiento
	 * del Lenguaje Natural, 5-7.9.2012, Castelló de la Plana), p. 93–100.
	 * 
	 * @param target
	 *            Target sentence
	 * @param sourceWords
	 *            Number of words at source
	 * @param targetSplit
	 *            Target split at word level
	 * @param segmentPairs
	 *            Suggestions
	 * @param currentChar
	 *            Position of the character being typed
	 */
	public static void computeAlignments(String target, int sourceWords, String[] targetSplit, int currentChar) {
		int targetWords = targetSplit.length;
		double[][] presures = new double[sourceWords][targetWords];
		ArrayList<Pair<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>, Double>> alignments = new ArrayList<Pair<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>, Double>>();
		String[] sugSplit;
		String[] sugSourceSplit;
		String[] typedSplit;
		String lastWordPrefix = "";
		int startingSugPos;
		int tsug;
		int tw;
		int twaux;
		boolean completeWord = false;
		Map<String, List<SourceSegment>> segmentPairs = (Map<String, List<SourceSegment>>) IForecatInterface
				.getSession().getAttribute("segmentPairs");
		
		if (segmentPairs == null)
		{
			System.out.println("FORECAT ERROR: NO SEGMENTPAIRS");
			return;
		}
		
		typedSplit = target.substring(0, currentChar).split(" ");
		
//		System.out.println("TARGET " + targetWords + " SOURCE " + sourceWords + " TYPEDSPLIT " + typedSplit.length);

		tw = typedSplit.length;

		completeWord = (currentChar == target.length() || target.charAt(currentChar) == ' ');
		lastWordPrefix = typedSplit[tw - 1];

		for (int xx = 0; xx < sourceWords; xx++) {
			for (int yy = 0; yy < targetWords; yy++) {
				presures[xx][yy] = 0;
			}
		}
		for (Entry<String, List<SourceSegment>> entry : segmentPairs.entrySet()) {
			sugSplit = entry.getKey().split(" ");
			for (twaux = 0; twaux < tw; twaux++) {
				// Comparar palabra a palabra la sugerencia con lo que se
				// pretende escribir
				int sugSplitLength = sugSplit.length;
				for (tsug = 0; tsug < sugSplitLength && tsug + twaux < tw; tsug++) {
					if (!sugSplit[tsug].equals(targetSplit[twaux + tsug])) {
						break;
					}
				}
				if (!completeWord && tsug + twaux == tw && tsug < sugSplitLength) {
					// Comprobar si la sugerencia puede encajar al final de lo
					// tecleado
					if (sugSplit[tsug].startsWith(lastWordPrefix) && (sugSplitLength + twaux <= targetWords))
						tsug = sugSplitLength;
				}
				if (tsug == sugSplitLength) {
					for (SourceSegment ss : entry.getValue()) {
						sugSourceSplit = ss.getSourceSegmentText().split(" ");
						startingSugPos = ss.getPosition();

						int sugSourceSplitLength = sugSourceSplit.length;

						alignments.add(new Pair<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>, Double>(
								new Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>(
										new Pair<Integer, Integer>(startingSugPos, twaux),
										new Pair<Integer, Integer>(sugSourceSplitLength + startingSugPos,
												sugSplitLength + twaux)),
								(1) / ((double) sugSplitLength * sugSourceSplitLength * entry.getValue().size())));

						for (int yaux = twaux; yaux < sugSplitLength + twaux; yaux++) {
							for (int xaux = startingSugPos; xaux < sugSourceSplitLength + startingSugPos; xaux++) {
								presures[xaux][yaux] += (1)
										/ ((double) sugSplitLength * sugSourceSplitLength * entry.getValue().size());
							}

						}
					}
				}
			}
		}

		RankerPressureBasic.setPressures(presures);
		RankerPressureBasic.setAlignments(alignments);
	}

}
