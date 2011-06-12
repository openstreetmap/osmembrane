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

package de.osmembrane.tools;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Simple loader for icons.
 * 
 * @author jakob_jarosch
 */
public class IconLoader {

    private static final long serialVersionUID = 2011011813350001L;

    /**
     * Save the loaded {@link BufferedImage}.
     */
    private BufferedImage image;

    /**
     * Enumeration for icon-size.
     * 
     * @author jakob_jarosch
     */
    public enum Size {
        /**
         * Small icon for menubar.
         */
        SMALL(16, 16),

        /**
         * Normal icon for toolbar.
         */
        NORMAL(32, 32),

        /**
         * Big icon in original size (256px).
         */
        BIG(256, 256),

        /**
         * A very big icon (512px).
         */
        VERYBIG(512, 512),

        /**
         * Use the original size of the image.
         */
        ORIGINAL(0, 0);

        /**
         * The size of the loaded icon.
         */
        private int width;
        private int height;

        /**
         * Creates a new Size enum.
         * 
         * @param size
         *            of the icon.
         */
        private Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        /**
         * Returns the width of the icon.
         * 
         * @return size of the icon (x)
         */
        public int getWidth() {
            return width;
        }

        /**
         * Returns the height of the icon.
         * 
         * @return size of the icon (y)
         */
        public int getHeight() {
            return height;
        }
    }

    /**
     * Constructor for {@link IconLoader}, loads the icon.
     * 
     * @param file
     *            filename of the icon (only filename, no path)
     * @param size
     *            desired icon size
     */
    public IconLoader(URL file, Size size, boolean silentLoad) {

        try {
            if (file == null) {
                throw new IOException();
            }

            /* Load the icon to an BufferedImage */
            BufferedImage tempImg = ImageIO.read(file);

            int width = (size.getWidth() > 0) ? size.getWidth() : tempImg
                    .getWidth();
            int height = (size.getHeight() > 0) ? size.getHeight() : tempImg
                    .getHeight();

            image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_ARGB);

            /*
             * Copy image resized, with alpha channel and bicubic rendering
             * algorithm
             */
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g2.drawImage(tempImg, 0, 0, width, height, null);
        } catch (IOException e) {
            /* do nothing, imageIcon would be null. */
        }
    }

    /**
     * Returns the {@link ImageIcon}.
     * 
     * @return the {@link ImageIcon}
     */
    public ImageIcon get() {
        if (image == null) {
            return null;
        }
        return new ImageIcon(image);
    }
}
