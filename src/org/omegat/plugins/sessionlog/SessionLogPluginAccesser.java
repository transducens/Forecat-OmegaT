package org.omegat.plugins.sessionlog;

import java.lang.reflect.Method;

import org.omegat.plugins.sessionlog.loggers.BaseLogger;

public class SessionLogPluginAccesser {
	private static BaseLogger logPluginCache = null;
	private static int count = 0;

	private static int MAX_RETRIES = 10;

	public static void init() {
		if (logPluginCache == null) {
			if (count == MAX_RETRIES) {
				System.err.println("Maxium number of retries for logger access. Logger may not be available.");
				count++;
			}

			else if (count < MAX_RETRIES && logPluginCache == null) {
				try {
					count++;
					Class c = Class.forName("org.omegat.plugins.sessionlog.SessionLogPlugin");
					Method m = c.getMethod("getLogger", null);
					logPluginCache = (BaseLogger) m.invoke(null, null);
				} catch (Exception ex) {
					System.out.println("LogPlugin access error: not found " + ex.getMessage());
				}
			}
		}
	}

	public static void GenericEvent(String eventType, String code, String param, String message) {
		if (logPluginCache != null) {
			logPluginCache.GenericEvent(eventType, code, param, message);
		}
	}

}
