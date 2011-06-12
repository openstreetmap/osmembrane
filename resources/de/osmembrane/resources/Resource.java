/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */

package de.osmembrane.resources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.swing.ImageIcon;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.tools.IconLoader;

/**
 * Resource enum for various files used in OSMembrane.
 * 
 * @author jakob_jarosch
 */
public enum Resource {
    /**
     * The resource bundle for cursor icons.
     */
    CURSOR_ICON("/de/osmembrane/resources/cursors/", null, false),

    /**
     * The resource bundle for program icons in the tool bar and in the menus.
     */
    PROGRAM_ICON("/de/osmembrane/resources/images/icons/",
            new String[] { "resources/images/icons/" }, false),

    /**
     * The resource bundle for preset icons used in the presets from josm.
     */
    PRESET_ICON("/de/osmembrane/resources/images/icons/presets/",
            new String[] { "resources/images/icons/presets/" }, true),

    /**
     * The resource xml file for osmosis tasks used by OSMembrane.
     */
    OSMEMBRANE_XML("/de/osmembrane/resources/xml/osmosis-structure.xml",
            new String[] { "resources/xml/osmosis-structure.xml" }, true),

    /**
     * The resource xml file for josm presets used by OSMembrane.
     */
    PRESET_XML("/de/osmembrane/resources/xml/defaultpresets.xml",
            new String[] { "resources/xml/defaultpresets.xml" }, true),

    /**
     * The resource bundle for quickstart images.
     */
    QUICKSTART_IMAGE("/de/osmembrane/resources/images/quickstart/",
            new String[] { "resources/images/quickstart/" }, true);

    private String internalPath;
    private String[] externalPath;
    private boolean externalPrefered;
    private boolean silentLoad = false;

    /**
     * Create a new Resource enum.
     * 
     * @param internalPath
     *            path to the internal files
     * @param externalPath
     *            path to the externals files
     * @param externalPrefered
     *            select if internal or external files are preferred
     */
    Resource(String internalPath, String[] externalPath,
            boolean externalPrefered) {

        this.internalPath = internalPath;
        this.externalPath = externalPath;
        this.externalPrefered = externalPrefered;
    }

    /**
     * @see Resource#getURL(String)
     */

    public URL getURL() {
        return getURL("");
    }

    /**
     * Returns the URL to a given file in the resource
     * 
     * @param file
     *            inside the resource
     * @return the URL to the file inside the resource, or NULL if no resource
     *         was found
     */

    public URL getURL(String file) {
        URL url = null;
        url = selectURL(getLocalizedFile(file));
        if (url == null) {
            url = selectURL(file);
        }
        return url;
    }

    /**
     * @see Resource#getImageIcon(String, de.osmembrane.tools.IconLoader.Size)
     */
    public ImageIcon getImageIcon(IconLoader.Size size) {
        return getImageIcon("", size);

    }

    /**
     * Returns a image as a {@link ImageIcon} from the resource.
     * 
     * @param filename
     *            file inside the resource
     * @param size
     *            size for the {@link ImageIcon}
     * @return selected image icon which should be loaded, or NULL if no image
     *         was found
     */
    public ImageIcon getImageIcon(String filename, IconLoader.Size size) {
        return new IconLoader(getURL(filename), size, silentLoad).get();
    }

    /**
     * Returns a localized variant of the filename.
     * 
     * @param file
     *            filename which should be localized
     * @return localized variant of the filename
     */

    private String getLocalizedFile(String file) {
        Locale locale = (Locale) ModelProxy.getInstance().getSettings()
                .getValue(SettingType.ACTIVE_LANGUAGE);
        String newFile;
        int lastDot = file.lastIndexOf(".");

        if (lastDot > 0) {
            newFile = file.substring(0, lastDot);
            newFile += "." + locale.getLanguage() + file.substring(lastDot);
        } else {
            newFile = file;
        }

        return newFile;
    }

    /**
     * Returns the external path to a file, by iterating over all paths and
     * search for a available resource.
     * 
     * @param file
     *            which should be found
     * @return the URL to an external file, or NULL if the file was nowhere
     *         found
     */
    private URL getExternalUrl(String file) {
        if (externalPath == null) {
            return null;
        }
        for (String externalPath : this.externalPath) {
            File fileObject = new File(externalPath + file);
            if (fileObject.exists()) {
                try {
                    return fileObject.toURI().toURL();
                } catch (MalformedURLException e) {
                    /* try silently another one */
                }
            }
        }
        return null;
    }

    /**
     * Returns the correct file, and chooses the right one from internal or
     * external path.
     * 
     * @param file
     *            filename which should be loaded
     * @return the correct URL to the file, or NULL if the file couldn't be
     *         found anywhere.
     */
    private URL selectURL(String file) {
        URL url = null;
        if (externalPrefered) {
            url = getExternalUrl(file);
            if (url == null) {
                url = this.getClass().getResource(internalPath + file);
            }
        } else {
            url = this.getClass().getResource(internalPath + file);
            if (url == null) {
                url = getExternalUrl(file);
            }
        }

        return url;
    }
}
