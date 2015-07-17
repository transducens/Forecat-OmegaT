package org.omegat.plugins.autocomplete.forecat;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.events.IApplicationEventListener;
import org.omegat.plugins.autocomplete.forecat.preferences.ForecatPreferencesDialog;

public class ForecatMenu {
   
//
    /** Option for activating the geometric recommender. */
    final JMenuItem forecatMenu;
    
   /**
     * Constructor of the class, which initialises the control variables in the
     * class and menus.
     * @param match_coloring Object that controls the coloring in the matcher.
     */
    public ForecatMenu() {

        forecatMenu = new JMenuItem("Forecat configuration");
        forecatMenu.setEnabled(true);
        forecatMenu.addActionListener(geometricRecommendingMenuItemActionListener);
            
        CoreEvents.registerApplicationEventListener(new IApplicationEventListener(){
            public void onApplicationStartup() {
                Core.getMainWindow().getMainMenu().getOptionsMenu().add(forecatMenu);
            }

            public void onApplicationShutdown() {
            }
        });
    }

    /**
     * Listener which acts when the option of the geometric recommender is chosen.
     */
    protected ActionListener geometricRecommendingMenuItemActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	new ForecatPreferencesDialog().setVisible(true);
        }
    };

}
