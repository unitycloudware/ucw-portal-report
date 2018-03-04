/* Copyright 2017, 2018 Unity{Cloud}Ware - UCW Industries Ltd. All rights reserved.
 */

package com.unitycloudware.portal.tutorial.report;

import java.util.Properties;

import org.nsys.util.ConfigurationManager;

/**
 * Report Config
 *
 * @author Tomas Hrdlicka <tomas@hrdlicka.co.uk>
 * @see <a href="http://unitycloudware.com">Unity{Cloud}Ware</a>
 */
public class ReportConfig {

    public static final String CONFIG_NAME = "ucw-report.cfg";
    public static final String CONFIG_NAME_INTERNAL = "ucw-report.cfg.internal";
    public static final String VERSION = "ucw.report.version";
    public static final String PLUGIN_KEY = "ucw.report.pluginKey";

    public static void loadConfig() {
        ConfigurationManager config = ConfigurationManager.getInstance();

        Properties props = config.loadConfig(String.format("/%s", CONFIG_NAME), ReportConfig.class);

        if (props != null) {
            config.merge(props);
        }

        props = config.loadConfig(String.format("/%s", CONFIG_NAME_INTERNAL), ReportConfig.class);

        if (props != null) {
            config.merge(props, true);
        }
    }

    public static String getVersion() {
        String version = ConfigurationManager.getInstance().getProperty(VERSION);
        return ConfigurationManager.getVersion(version);
    }

    public static String getBuildNumber() {
        String version = ConfigurationManager.getInstance().getProperty(VERSION);
        return ConfigurationManager.getBuildNumber(version);
    }

    public static String getPluginKey() {
        return ConfigurationManager.getInstance().getProperty(PLUGIN_KEY);
    }
}