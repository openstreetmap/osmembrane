package de.osmembrane.view.panels;

import java.awt.Color;
import java.awt.Graphics;

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
	public PipelineLink(PipelinePanel pipeline, PipelineConnector linkSource,
			PipelineConnector linkDestination, Color color) {
		this.pipeline = pipeline;
		this.linkSource = linkSource;
		this.linkDestination = linkDestination;
		this.color = color;
		this.setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO Auto-generated method stub
		return super.contains(x, y);
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

}
