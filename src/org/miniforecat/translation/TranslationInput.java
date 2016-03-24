package org.miniforecat.translation;

import org.omegat.util.Language;

public class TranslationInput {

	String sourceText;
	Language sourceCode;
	Language targetCode;
	int maxSegmentLength;
	int minSegmentLength;

	protected TranslationInput() {
	}

	public TranslationInput(String sourceText, Language sLang, Language tLang,
			int maxSegmentLength, int minSegmentLength) {
		this.sourceText = sourceText;
		this.sourceCode = sLang;
		this.targetCode = tLang;
		this.maxSegmentLength = maxSegmentLength;
		this.minSegmentLength = minSegmentLength;
	}

	public String getSourceText() {
		return sourceText;
	}

	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
	}

	public Language getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(Language sourceCode) {
		this.sourceCode = sourceCode;
	}

	public Language getTargetCode() {
		return targetCode;
	}

	public void setTargetCode(Language targetCode) {
		this.targetCode = targetCode;
	}

	public int getMaxSegmentLenth() {
		return maxSegmentLength;
	}

	public void setMaxSegmentLength(int maxSegmentLength) {
		this.maxSegmentLength = maxSegmentLength;
	}

	public int getMinSegmentLenth() {
		return minSegmentLength;
	}

	public void setMinSegmentLength(int maxSegmentLength) {
		this.minSegmentLength = maxSegmentLength;
	}
}
