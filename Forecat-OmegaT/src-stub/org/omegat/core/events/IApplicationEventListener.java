package org.omegat.core.events;

public interface IApplicationEventListener {
    /**
     * Called on application startup after all components created and registered
     * in Core.
     */
    void onApplicationStartup();

    /**
     * Called on application shutdown.
     * 
     * CAN BE EXECUTED IN ANY THREAD !
     */
    void onApplicationShutdown();
}
