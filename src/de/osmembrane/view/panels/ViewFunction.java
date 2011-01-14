package de.osmembrane.view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import de.osmembrane.model.AbstractFunction;

/**
 * The view function, i.e. the visual representation of a model function on the
 * View, in the {@link LibraryPanel} and the one being dragged on the {@link PipelinePanel}.
 * Note, the actually drawn *in* the pipeline, are {@link PipelineFunction}.
 * 
 * @author tobias_kuhn
 * 
 */
public class ViewFunction extends JPanel {

	private static final long serialVersionUID = 1663933392202927614L;

	/**
	 * The image resource that keeps an image template used to prerender the
	 * actual image that will be drawn on this function
	 */
	private static ImageIcon displayTemplate = new ImageIcon(
			ViewFunction.class
					.getResource("/de/osmembrane/resources/images/function.png"));

	/**
	 * The referenced model function
	 */
	protected AbstractFunction modelFunctionPrototype;

	/**
	 * The image that will be displayed
	 */
	protected Image display;

	/**
	 * Initializes a new ViewFunction for the given model prototype function
	 * 
	 * @param modelFunctionPrototype
	 *            the model's prototype function this view function should represent
	 */
	public ViewFunction(AbstractFunction modelFunctionPrototype) {
		this.modelFunctionPrototype = modelFunctionPrototype;
		setPreferredSize(new Dimension(displayTemplate.getIconWidth(),
				displayTemplate.getIconHeight()));
		display = derivateDisplay(new Color(1.0f, 0.5f, 1.0f), null);
	}

	/**
	 * Derivates the correct pre-rendered display image from displayTemplate in
	 * the given color and with the given icon.
	 * 
	 * @param color
	 *            The color this function shall display
	 * @param icon
	 *            The icon this function shall display
	 * @return the pre-rendered image in the color and with the icon
	 */
	private Image derivateDisplay(Color color, Image icon) {
		// copy displayTemplate to a BufferedImage
		BufferedImage result = new BufferedImage(
				displayTemplate.getIconWidth(),
				displayTemplate.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D g = result.createGraphics();
		g.drawImage(displayTemplate.getImage(), 0, 0, this);

		WritableRaster r = result.getRaster();

		// Scan the lines with a float multiplication
		float[] colorRGB = color.getColorComponents(null);
		float[] pixelRow = new float[4 * result.getWidth()];

		for (int y = 0; y < result.getHeight(); y++) {
			r.getPixels(0, y, result.getWidth() - 1, 1, pixelRow);

			for (int x = 0; x < result.getWidth(); x++) {
				pixelRow[4 * x + 1] *= colorRGB[2];
				pixelRow[4 * x + 2] *= colorRGB[1];
				pixelRow[4 * x + 3] *= colorRGB[0];
			}

			r.setPixels(0, y, result.getWidth() - 1, 1, pixelRow);
		}

		// place the icon

		return result;
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(display, 0, 0, getWidth(), getHeight(), this);
		printCenteredString(g, modelFunctionPrototype.getFriendlyName(),
				0.8 * getHeight());
	}

	/**
	 * Prints a string centered and with line breaks at spaces fitting into
	 * Graphics g with y coordinate y
	 * 
	 * @param g
	 *            Graphics to draw upon
	 * @param str
	 *            String to display centered with line breaks
	 * @param y
	 *            the base y coordinate for the last line of the string
	 */
	private void printCenteredString(Graphics g, String str, double y) {
		// get applicable font
		g.setFont(g.getFont().deriveFont(Font.BOLD)
				.deriveFont(g.getFont().getSize() * getHeight() / 90.0f));

		// find out how large this is gonna be
		FontMetrics fm = g.getFontMetrics();
		int fontWidth = 0;
		int fontHeight = fm.getHeight();

		List<String> lines = new ArrayList<String>();
		String line = new String();

		// try to separate lines at " "
		for (String word : str.split(" ")) {
			int thisWidth = fm.stringWidth(word + " ");

			// if this line is wider than possible, make a new line
			if (fontWidth + thisWidth >= getWidth()) {

				// no word yet = sorry, but it can't fit
				if (line.isEmpty()) {
					lines.add(word);
				} else {
					lines.add(line);
					line = word;
				}

				fontWidth = 0;
			} else {
				// append
				fontWidth += thisWidth;
				line = line.concat(word + " ");
			}
		}

		if (!line.isEmpty()) {
			lines.add(line);
		}

		// print the lines
		for (int i = lines.size() - 1; i >= 0; i--) {
			line = lines.get((lines.size() - 1) - i);
			g.drawString(line, (getWidth() - fm.stringWidth(line)) / 2, 
					(int) y	- i * fontHeight);
		}
	}
	
	/**
	 * @return the model function prototype associated with this view function
	 */
	public AbstractFunction getModelFunctionPrototype() {
		return this.modelFunctionPrototype;
	}

}
