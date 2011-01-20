package de.osmembrane.view.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;

/**
 * The view function, i.e. the visual representation of a model function on the
 * View, in the {@link LibraryPanel} and the one being dragged on the
 * {@link PipelinePanel}. Note, the actually drawn *in* the pipeline, are
 * {@link PipelineFunction}.
 * 
 * Unfortunately, this class has a strong dependency with {@link MainFrame} and
 * {@link MainFrameGlassPane} as well.
 * 
 * @author tobias_kuhn
 * 
 */
public class LibraryFunction extends DisplayTemplatePanel {

	private static final long serialVersionUID = 1663933392202927614L;

	/**
	 * The image resource that keeps an image template used to prerender the
	 * actual image that will be drawn on this function
	 */
	protected static ImageIcon displayTemplate = new ImageIcon(
			LibraryFunction.class
					.getResource("/de/osmembrane/resources/images/function.png"));

	/**
	 * The referenced model function
	 */
	protected AbstractFunction modelFunctionPrototype;

	/**
	 * The image that will be displayed normally
	 */
	protected Image display;

	/**
	 * The image that will be displayed, if highlighted
	 */
	protected Image displayHighlight;

	/**
	 * Whether this function is currently highlighted
	 */
	protected boolean highlighted;

	/**
	 * Whether this function is currently dragged
	 */
	protected boolean dragging;

	/**
	 * Where the starting click of the drag happened, if canDragAndDrop
	 */
	protected Point dragOffset;

	/**
	 * Initializes a new ViewFunction for the given model prototype function
	 * 
	 * @param modelFunctionPrototype
	 *            the model's prototype function this view function should
	 *            represent
	 * @param canDragAndDrop
	 *            whether this view function is in the library and can be
	 *            dragged onto the pipeline panel to create a new
	 *            {@link PipelineFunction}. Additionally, it gets highlighted
	 *            when the mouse cursor moves over it. All non-library
	 *            descendants are recommended to set this to false.
	 */
	public LibraryFunction(final AbstractFunction modelFunctionPrototype,
			final boolean canDragAndDrop) {
		this.modelFunctionPrototype = modelFunctionPrototype;
		setPreferredSize(new Dimension(displayTemplate.getIconWidth(),
				displayTemplate.getIconHeight()));

		Color color = modelFunctionPrototype.getParent().getColor();
		float[] colorRGB = color.getComponents(null);
		Color highlightColor = new Color(Math.min(1.0f, colorRGB[0] + 0.2f),
				Math.min(1.0f, colorRGB[1] + 0.2f), Math.min(1.0f,
						colorRGB[2] + 0.2f));
		this.setOpaque(false);

		display = derivateDisplay(displayTemplate, color,
				modelFunctionPrototype.getIcon());
		displayHighlight = derivateDisplay(displayTemplate, highlightColor,
				modelFunctionPrototype.getIcon());
		highlighted = false;
		dragging = false;

		addMouseListener(new MouseListener() {

			// drag & drop

			@Override
			public void mouseReleased(MouseEvent e) {
				dragging = false;

				if (canDragAndDrop) {
					IView mainFrame = ViewRegistry.getInstance().getMainFrame();
					MainFrame mf = (MainFrame) mainFrame;
					mf.endDragAndDrop(); // necessary to make the glass pane go
											// away

					// subtract the offset where it got clicked
					e.translatePoint(-dragOffset.x, -dragOffset.y);

					// convert the mouse event into the mainFrame and
					// pipeline panel components
					MouseEvent mainFrameEvent = SwingUtilities
							.convertMouseEvent(LibraryFunction.this, e, mf);
					MouseEvent pipelineEvent = SwingUtilities
							.convertMouseEvent(LibraryFunction.this, e,
									mf.getPipeline());

					Component c = mf.findComponentAt(mainFrameEvent.getPoint());
					if (mf.getPipeline().equals(c)) {
						mf.getPipeline().draggedOnto(LibraryFunction.this,
								pipelineEvent.getPoint());
					} else {
						Application.handleException(new ControlledException(
								this, ExceptionSeverity.WARNING,
								I18N.getInstance().getString(
										"View.Library.CannotDropFunction")));
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				dragging = true;
				dragOffset = e.getPoint();
			}

			// mouse move hint

			@Override
			public void mouseExited(MouseEvent e) {
				// show no hint
				IView mainFrame = ViewRegistry.getInstance().getMainFrame();
				MainFrame mf = (MainFrame) mainFrame;
				mf.getPipeline().setHint(InspectorPanel.VALID_EMPTY_HINT);

				if (canDragAndDrop) {
					highlighted = false;
					repaint();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// show hint for this function
				IView mainFrame = ViewRegistry.getInstance().getMainFrame();
				MainFrame mf = (MainFrame) mainFrame;
				mf.getPipeline().setHint(
						modelFunctionPrototype.getDescription());

				if (canDragAndDrop) {
					highlighted = true;
					repaint();
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		// nice drag & drop animation
		if (canDragAndDrop) {
			addMouseMotionListener(new MouseMotionListener() {

				@Override
				public void mouseMoved(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseDragged(MouseEvent e) {
					IView mainFrame = ViewRegistry.getInstance().getMainFrame();
					MainFrame mf = (MainFrame) mainFrame;

					// subtract the offset where it got clicked
					e.translatePoint(-dragOffset.x, -dragOffset.y);

					// convert the mouse event into the mainFrame and
					// pipeline panel components
					MouseEvent mainFrameEvent = SwingUtilities
							.convertMouseEvent(LibraryFunction.this, e,
									mf.getGlassPane());

					mf.paintDragAndDrop(LibraryFunction.this,
							mainFrameEvent.getPoint());
				}
			});
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		paintAt(g, new Point(0, 0));
	}

	/**
	 * Paints the component on g at position at
	 * 
	 * @param g
	 *            the graphics to draw upon
	 * @param at
	 *            where to draw
	 */
	protected void paintAt(Graphics g, Point at) {
		if (highlighted) {
			g.drawImage(displayHighlight, at.x, at.y, getWidth(), getHeight(),
					this);
		} else {
			g.drawImage(display, at.x, at.y, getWidth(), getHeight(), this);
		}

		printCenteredString(g, modelFunctionPrototype.getFriendlyName(), at.x,
				at.y + 0.8 * getHeight());
	}

	/**
	 * Prints a string centered and with line breaks at spaces fitting into
	 * Graphics g with y coordinate y
	 * 
	 * @param g
	 *            Graphics to draw upon
	 * @param str
	 *            String to display centered with line breaks
	 * @param x
	 *            the base x coordinate for the first character of the string
	 * @param y
	 *            the base y coordinate for the last line of the string
	 */
	private void printCenteredString(Graphics g, String str, double x, double y) {
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
			g.drawString(line, (int) x + (getWidth() - fm.stringWidth(line))
					/ 2, (int) y - i * fontHeight);
		}
	}

	/**
	 * @return the model function prototype associated with this view function
	 */
	public AbstractFunction getModelFunctionPrototype() {
		return this.modelFunctionPrototype;
	}

	/**
	 * @return true, if this function is currently dragged, false otherwise
	 */
	public boolean isDragging() {
		return this.dragging;
	}

	/**
	 * Forces the change of the highlight value.
	 * 
	 * @param highlight
	 *            true, if highlighted, false otherwise
	 */
	public void forceHighlight(boolean highlight) {
		this.highlighted = highlight;
		repaint();
	}

}
