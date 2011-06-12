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

package de.osmembrane.view.panels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * A {@link JPanel} component with the ability to create colored {@link Image}s
 * with icons from an {@link ImageIcon} display template.
 * 
 * To create a displayable {@link Image}, just call
 * {@link DisplayTemplatePanel#derivateDisplay} with the desired {@link Color}
 * and icon.
 * 
 * Howsoever it is capable of pre-rendering all objects necessary into a map and
 * get them out once they're needed without any extra overhead.
 * 
 * <b>It is recommended you use the static ability of prerendering since that
 * will save a lot of memory and processing power.</b>
 * 
 * @author tobias_kuhn
 * 
 */
public abstract class DisplayTemplatePanel extends JPanel {

    protected static final long serialVersionUID = -2046897951374601603L;

    /**
     * Map used for the prerendered objects.
     */
    private static Map<Object, List<Image>> prerender;

    /**
     * Internal {@link DisplayTemplatePanel} for prerendering.
     * (derivateDisplay() calls are only possible on instances)
     */
    private static DisplayTemplatePanel prerenderPanel;

    /**
     * Renders the correct pre-rendered display image based on template in the
     * given color and with the given icon.
     * 
     * @param template
     *            the ImageIcon to colorize and iconify
     * @param color
     *            The Color this image shall display in
     * @param icon
     *            The icon this image shall display
     * @return the pre-rendered Image in the color and with the icon
     */
    protected Image derivateDisplay(ImageIcon template, Color color, Image icon) {
        // copy displayTemplate to a BufferedImage
        BufferedImage result = new BufferedImage(template.getIconWidth(),
                template.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D g = result.createGraphics();
        g.drawImage(template.getImage(), 0, 0, this);

        WritableRaster r = result.getRaster();

        // Scan the lines with a float multiplication
        float[] colorRGB = color.getColorComponents(null);
        float[] pixelRow = new float[4 * result.getWidth()];

        for (int y = 0; y < result.getHeight(); y++) {
            r.getPixels(0, y, result.getWidth() - 1, 1, pixelRow);

            for (int x = 0; x < result.getWidth(); x++) {
                pixelRow[4 * x + 0] *= colorRGB[0];
                pixelRow[4 * x + 1] *= colorRGB[1];
                pixelRow[4 * x + 2] *= colorRGB[2];
            }

            r.setPixels(0, y, result.getWidth() - 1, 1, pixelRow);
        }

        // place the icon
        if (icon != null) {
            g.drawImage(icon, getWidth() - (icon.getWidth(this) / 2),
                    getHeight() - (icon.getHeight(this) / 2), this);
        }

        return result;
    }

    /**
     * Calls
     * {@link DisplayTemplatePanel#derivateDisplay(ImageIcon, Color, Image)} for
     * the object forObject and stores it in the list for forObject for quick
     * further access.
     * 
     * If there is already an image stored for forObject, it is overwritten. You
     * can easily test this by checking
     * {@link DisplayTemplatePanel#givePrerender(Object)} == null.
     * 
     * @param forObject
     *            object to identify the prerendering
     * @see #derivateDisplay(ImageIcon, Color, Image)
     * @return the immediately created Image
     */
    protected static Image prerenderDisplay(Object forObject,
            ImageIcon template, Color color, Image icon) {
        if (prerender == null) {
            prerender = new HashMap<Object, List<Image>>();
        }
        if (prerenderPanel == null) {
            prerenderPanel = new DisplayTemplatePanel() {
                private static final long serialVersionUID = -8556169733942717510L;
            };
        }

        if (prerender.get(forObject) == null) {
            prerender.put(forObject, new ArrayList<Image>());
        }
        Image created = prerenderPanel.derivateDisplay(template, color, icon);
        prerender.get(forObject).add(created);
        return created;
    }

    /**
     * Returns the prerendered {@link Image} created for forObject
     * 
     * @param forObject
     *            object to identify the prerendering
     * @return the prerendered images for forObject, or null if none exists
     */
    protected static List<Image> givePrerender(Object forObject) {
        return (prerender != null) ? prerender.get(forObject) : null;
    }

}
