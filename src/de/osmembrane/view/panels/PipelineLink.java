package de.osmembrane.view.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

/**
 * The visual (and selectable) component which displays the links between
 * connectors.
 * 
 * @author tobias_kuhn
 */
public class PipelineLink extends JPanel {

	private static final long serialVersionUID = 8878462047772169198L;

	/**
	 * actual source and destination of the connection
	 */
	private PipelineConnector linkSource;
	private PipelineConnector linkDestination;

	/**
	 * Color this connection will be drawn in
	 */
	private Color color;

	/**
	 * The coordinates of the line to draw
	 */
	private Line2D line;

	/**
	 * The specific widths of the actual line in object coordinates
	 */
	private static final double LINE_DRAWING_WIDTH = 5.0;
	private static final double LINE_SELECTION_WIDTH = 15.0;

	/**
	 * Pipeline this connector is drawn on.
	 */
	private PipelinePanel pipeline;

	/**
	 * Creates a new pipeline link.
	 * 
	 * @param pipeline
	 *            the pipeline to drawn on (and forward events to)
	 * @param linkSource
	 *            the *true* source for the connection, i.e. the actual starting
	 *            point
	 * @param linkDestination
	 *            the *true* destination for the connection, i.e. the actual
	 *            ending point
	 * @param color
	 *            the link will be drawn in
	 */
	public PipelineLink(final PipelinePanel pipeline,
			PipelineConnector linkSource, PipelineConnector linkDestination,
			Color color) {
		this.pipeline = pipeline;
		this.linkSource = linkSource;
		this.linkDestination = linkDestination;
		this.color = color;
		this.line = new Line2D.Double();

		this.setOpaque(false);

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				switch (pipeline.getActiveTool()) {
				case DEFAULT_MAGIC_TOOL:
				case SELECTION_TOOL:
					pipeline.selected(PipelineLink.this);
					break;
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}

	/**
	 * Regenerates the line to be drawn, if one or more connectors have moved.
	 * It is assumed the link's size and location is already correctly set.
	 */
	public void regenerateLine() {
		Point2D left;
		Point2D right;

		Point offset = new Point(linkSource.getWidth() / 2,
				linkSource.getHeight() / 2);

		if (linkSource.getX() < linkDestination.getX()) {
			// arrow goes like left -----> right
			if (linkSource.getY() < linkDestination.getY()) {
				// left top to right bottom
				left = new Point2D.Double(offset.x, offset.y);
				right = new Point2D.Double(getWidth() - offset.x, getHeight()
						- offset.y);
			} else {
				// left bottom to right top
				left = new Point2D.Double(offset.x, getHeight() - offset.y);
				right = new Point2D.Double(getWidth() - offset.x, offset.y);
			}
		} else {
			// arrow goes like left <----- right
			if (linkSource.getY() < linkDestination.getY()) {
				// right top to left bottom
				left = new Point2D.Double(offset.x, getHeight() - offset.y);
				right = new Point2D.Double(getWidth() - offset.x, offset.y);
			} else {
				// right bottom to left top
				left = new Point2D.Double(offset.x, offset.y);
				right = new Point2D.Double(getWidth() - offset.x, getHeight()
						- offset.y);
			}
		}

		line.setLine(left, right);
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(color);

		Polygon p = new Polygon();
		int drawWidth = pipeline.objToWindowDelta(new Point2D.Double(0.0,
				LINE_DRAWING_WIDTH)).y;
		
		// use the dot product, Luke
		double deltaX = line.getX2() - line.getX1();
		double deltaY = line.getY2() - line.getY1(); 
		double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		double alpha = Math.acos(-1.0 * (line.getX2() - line.getX1()) / length);
		
		// if alpha too large to get returned by acos
		if (line.getY2() > line.getY1()) {
			alpha = 2.0 * Math.PI - alpha;
		}
		
		// alpha = the clockwise counted angle from the x-axis to the left		
		// y = sin(alpha), x = cos(alpha)

		p.addPoint((int) (line.getX1() + (Math.cos(alpha - 0.25 * Math.PI) * drawWidth)),
				   (int) (line.getY1() + (Math.sin(alpha - 0.25 * Math.PI) * drawWidth)));
		p.addPoint((int) (line.getX1() + (Math.cos(alpha + 0.25 * Math.PI) * drawWidth)),
				   (int) (line.getY1() + (Math.sin(alpha + 0.25 * Math.PI) * drawWidth)));

		p.addPoint((int) (line.getX2() + (Math.cos(alpha + 0.25 * Math.PI) * drawWidth)),
				   (int) (line.getY2() + (Math.sin(alpha + 0.25 * Math.PI) * drawWidth)));
		p.addPoint((int) (line.getX2() + (Math.cos(alpha - 0.25 * Math.PI) * drawWidth)),
				   (int) (line.getY2() + (Math.sin(alpha - 0.25 * Math.PI) * drawWidth)));

		g.fillPolygon(p);
		
		// some radius, some tip
		double arrowRadius = getLinkDestination().getWidth() / 2.0;

		Polygon arrowHead = new Polygon();
		if (linkSource.getX() < linkDestination.getX()) {						
			// arrow goes like left -----> right
			double tipLength = (length - arrowRadius) / length;
			Point2D arrowTip = new Point2D.Double(line.getX1() + (line.getX2() - line.getX1()) * tipLength,
					line.getY1() + (line.getY2() - line.getY1()) * tipLength);
			
			arrowHead.addPoint((int) (arrowTip.getX() + arrowRadius * Math.cos(alpha - 0.25 * Math.PI)),
					(int) (arrowTip.getY() + arrowRadius * Math.sin(alpha - 0.25 * Math.PI)));			
			arrowHead.addPoint((int) arrowTip.getX(), (int) arrowTip.getY());			
			arrowHead.addPoint((int) (arrowTip.getX() + arrowRadius * Math.cos(alpha + 0.25 * Math.PI)),
					(int) (arrowTip.getY() + arrowRadius * Math.sin(alpha + 0.25 * Math.PI)));
			
		} else {
			// arrow goes like left <----- right
			double tipLength = arrowRadius / length;
			Point2D arrowTip = new Point2D.Double(line.getX1() + (line.getX2() - line.getX1()) * tipLength,
					line.getY1() + (line.getY2() - line.getY1()) * tipLength);
			
			arrowHead.addPoint((int) (arrowTip.getX() + arrowRadius * Math.cos(alpha - 0.75 * Math.PI)),
					(int) (arrowTip.getY() + arrowRadius * Math.sin(alpha - 0.75 * Math.PI)));			
			arrowHead.addPoint((int) arrowTip.getX(), (int) arrowTip.getY());			
			arrowHead.addPoint((int) (arrowTip.getX() + arrowRadius * Math.cos(alpha + 0.75 * Math.PI)),
					(int) (arrowTip.getY() + arrowRadius * Math.sin(alpha + 0.75 * Math.PI)));
			
		}
		
		g.fillPolygon(arrowHead);		
	}

	@Override
	public boolean contains(int x, int y) {
		return line.ptSegDist(x, y) <= LINE_SELECTION_WIDTH;
	}

	/**
	 * Checks whether this link is established between a specific pair of
	 * connectors.
	 * 
	 * @param questFrom
	 *            start point of connection asked
	 * @param questTo
	 *            end point of connection asked
	 * @return whether this link is between questFrom and questTo, regardless of
	 *         the orientation of the actual connection
	 */
	public boolean links(PipelineConnector questFrom, PipelineConnector questTo) {
		return (linkSource.equals(questFrom) && linkDestination.equals(questTo))
				|| (linkSource.equals(questTo) && linkDestination
						.equals(questFrom));
	}

	/**
	 * @return the link source connector (i.e. an out connector)
	 */
	public PipelineConnector getLinkSource() {
		return this.linkSource;
	}

	/**
	 * @return the link destination connector (i.e. an in connector)
	 */
	public PipelineConnector getLinkDestination() {
		return this.linkDestination;
	}

}
