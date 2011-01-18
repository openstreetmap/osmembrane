package de.osmembrane.tools;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.resources.Constants;

/**
 * Simple loader for icons
 * 
 * @author jakob_jarosch
 */
public class IconLoader {

	private static final long serialVersionUID = 2011011813350001L;

	private BufferedImage image;

	public enum Size {
		SMALL(16), NORMAL(32), BIG(64);

		private int size;

		private Size(int size) {
			this.size = size;
		}

		public int getSize() {
			return size;
		}
	}

	public IconLoader(String file, Size size) {
		File fileObject = new File(Constants.ICONS_PATH + file);
		try {
			BufferedImage tempImg = ImageIO.read(fileObject);
			image = new BufferedImage(size.getSize(), size.getSize(),
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2 = image.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			
			g2.drawImage(tempImg, 0, 0, size.getSize(), size.getSize(), null);
		} catch (IOException e) {
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.CRITICAL_UNEXPECTED_BEHAVIOR, I18N
							.getInstance().getString(
									"Exception.CanReadIconFile", fileObject)));
		}
	}

	public ImageIcon get() {
		return new ImageIcon(image);
	}
}
