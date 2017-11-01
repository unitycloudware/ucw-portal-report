/* Copyright 2017 Unity{Cloud}Ware - UCW Industries Ltd. All rights reserved.
 */

package com.unitycloudware.portal.tutorial.report.plugin;

import java.util.Properties;

import org.nsys.event.Event;
import org.nsys.plugin.PluginContext;
import org.nsys.util.ConfigurationManager;
import org.nsys.daemon.event.SystemStartedEvent;
import org.nsys.daemon.plugin.AbstractManagementAgentPlugin;
import org.nsys.portal.event.PortalStartedEvent;

import com.unitycloudware.portal.tutorial.report.ReportConfig;

/**
 * Report Plugin
 *
 * @author Tomas Hrdlicka <tomas@hrdlicka.co.uk>
 * @see <a href="http://unitycloudware.com">Unity{Cloud}Ware</a>
 */
public class ReportPlugin extends AbstractManagementAgentPlugin {

    public static final String PORTAL_REDIRECT = "nsys.portal.redirect";
    public static final String PORTAL_REDIRECT_DEFAULT = "/ucw-report";

    @Override
    public void load(final PluginContext context) {
        getLog().debugFormat("Starting plugin %s", getName());

        ReportConfig.loadConfig();

        addComponents();
        scheduleJobs();
    }

    @Override
    public void unload(final PluginContext context) {
        getLog().debugFormat("Stopped plugin %s", getName());
    }

    @Override
    public void handleEvent(final PluginContext context, final Event event) {
        if (event != null) {

            if (event instanceof PortalStartedEvent) {
                // UCW Portal has been started successfully!
            }

            else if (event instanceof SystemStartedEvent) {
                configurePortalComponents();
            }
        }
    }

    protected void addComponents() {
    }

    protected void scheduleJobs() {
    }

    protected void configurePortalComponents() {
        customPortalConfig();
    }

    protected void customPortalConfig() {
        Properties customPortalConfig = new Properties();
        customPortalConfig.setProperty(PORTAL_REDIRECT, PORTAL_REDIRECT_DEFAULT);

        ConfigurationManager.getInstance().merge(customPortalConfig, true);
    }
}