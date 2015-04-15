/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL: https://osmembrane.de/svn/sources/src/de/osmembrane/model/settings/SettingType.java $ ($Revision: 821 $)
 * Last changed: $Date: 2011-02-15 15:54:41 +0100 (Di, 15 Feb 2011) $
 */

package de.osmembrane.model.settings;

/**
 * How often should OSMembrane search for updates.
 * 
 * @author jakob_jarosch
 */
public enum SettingsTypeUpdateInterval {
    /** once a day */
    ONCE_A_DAY(new Long(24 * 60 * 60)),
    /** once a week */
    ONCE_A_WEEK(new Long(7 * 24 * 60 * 60)),
    /** never */
    NEVER(new Long(0));

    private Long timeDiff;

    /**
     * Creates a new {@link SettingsTypeUpdateInterval}.
     * 
     * @param timeDiff
     *            interval described in seconds
     */
    SettingsTypeUpdateInterval(Long timeDiff) {
        this.timeDiff = timeDiff;
    }

    /**
     * Returns the gap between two updates.
     * 
     * @return gap in seconds.
     */
    public Long getTimeDiff() {
        return timeDiff;
    }
}
