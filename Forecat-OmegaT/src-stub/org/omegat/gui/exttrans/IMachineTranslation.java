package org.omegat.gui.exttrans;

import org.omegat.util.Language;

public interface IMachineTranslation {
	 public String getTranslation(Language sLang, Language tLang, String text);
}
