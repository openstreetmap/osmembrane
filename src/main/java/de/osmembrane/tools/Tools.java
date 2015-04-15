/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL: https://osmembrane.de/svn/sources/src/de/osmembrane/tools/IconLoader.java $ ($Revision: 821 $)
 * Last changed: $Date: 2011-02-15 15:54:41 +0100 (Di, 15 Feb 2011) $
 */

package de.osmembrane.tools;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Some tools for general usage.
 * 
 * @author jakob_jarosch
 */
public class Tools {

    /**
     * Creates a file from an URL.
     * 
     * @param url
     *            which should be converted
     * @return to a File converted URL
     */
    public static File urlToFile(URL url) {
        if (url == null) {
            return null;
        }
        File f;
        try {
            f = new File(url.toURI());
        } catch (URISyntaxException e) {
            f = new File(url.getPath());
        }
        return f;
    }
}
