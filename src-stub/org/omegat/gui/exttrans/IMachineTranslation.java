package org.omegat.gui.exttrans;

import org.omegat.util.Language;

public interface IMachineTranslation {
	String getTranslation(Language sLang, Language tLang, String text);
	String getName();
	default void setEnabled(boolean enabled){};
	boolean isEnabled();
	String getCachedTranslation(Language sLang, Language tLang, String text);
}
