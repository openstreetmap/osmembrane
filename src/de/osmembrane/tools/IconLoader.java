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
		SMALL(16),

		/**
		 * Normal icon for toolbar.
		 */
		NORMAL(32),

		/**
		 * Big icon in original size (64px).
		 */
		BIG(64);

		/**
		 * The size of the loaded icon.
		 */
		private int size;

		/**
		 * Creates a new Size enum.
		 * 
		 * @param size
		 *            of the icon.
		 */
		private Size(int size) {
			this.size = size;
		}

		/**
		 * Returns the size of the icon.
		 * 
		 * @return size of the icon
		 */
		public int getSize() {
			return size;
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
			image = new BufferedImage(size.getSize(), size.getSize(),
					BufferedImage.TYPE_INT_ARGB);

			/*
			 * Copy image resized, with alpha channel and bicubic rendering
			 * algorithm
			 */
			Graphics2D g2 = image.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);

			g2.drawImage(tempImg, 0, 0, size.getSize(), size.getSize(), null);
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
