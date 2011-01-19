package de.osmembrane.view.panels;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * A {@link JPanel} component with the ability to create colored images with
 * icons from a private static ImageIcon displayTemplate (that every descendant
 * should implement).
 * 
 * To create a displayable image, just call derivateDisplay with the desired
 * color and icon.
 * 
 * @author tobias_kuhn
 * 
 */
public abstract class DisplayTemplatePanel extends JPanel {

	protected static final long serialVersionUID = -2046897951374601603L;

	/**
	 * Renders the correct pre-rendered display image based on template in the
	 * given color and with the given icon.
	 * 
	 * @param template
	 *            the image to colorize and iconify
	 * @param color
	 *            The color this image shall display in
	 * @param icon
	 *            The icon this image shall display
	 * @return the pre-rendered image in the color and with the icon
	 */
	protected Image derivateDisplay(ImageIcon template, Color color, Image icon) {
		// copy displayTemplate to a BufferedImage
		BufferedImage result = new BufferedImage(
				template.getIconWidth(),
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

}
