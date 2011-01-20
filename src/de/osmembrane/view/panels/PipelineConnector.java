package de.osmembrane.view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.ConnectorType;

/**
 * Represents a function connector, i.e. {@link AbstractConnector} in the View
 * and the pipeline ({@link PipelinePanel}).
 * 
 * @author tobias_kuhn
 * 
 */
public class PipelineConnector extends DisplayTemplatePanel {

	private static final long serialVersionUID = -3777471525969957175L;

	/**
	 * The image resource that keeps an image template used to prerender the
	 * actual image that will be drawn on this connector
	 */
	protected static ImageIcon displayTemplate = new ImageIcon(
			PipelineConnector.class
					.getResource("/de/osmembrane/resources/images/connector.png"));

	/**
	 * The model-connector associated with this connector
	 */
	private AbstractConnector modelConnector;

	/**
	 * Pipeline this connector is drawn on.
	 */
	private PipelinePanel pipeline;

	/**
	 * The image to use for display
	 */
	private Image display;

	/**
	 * Whether this is an outpipes connector (true) or an inpipes connector
	 * (false)
	 */
	private boolean isOutpipes;

	/**
	 * id in a group of in- or out-connectors per function
	 */
	private int id;

	/**
	 * amount of the connectors in the group of in- or out-connectors per
	 * function
	 */
	private int amount;

	/**
	 * Creates a new connector for a model connector on a pipeline function.
	 * 
	 * @param modelConnector
	 *            connector in the model which is represented
	 * @param pipeline
	 *            Pipeline this connector is drawn on
	 * @param isOutpipes
	 *            true, if this is an outpipes connector, false if this is an
	 *            inpipes connector
	 * @param id
	 *            unique id for this connector starting by 0 < ofCount
	 * @param amount
	 *            the amount of this kind of connectors (out or in) on the
	 *            specific function they are created for
	 */
	public PipelineConnector(AbstractConnector modelConnector,
			PipelinePanel pipeline, boolean isOutpipes, int id, int amount) {
		this.modelConnector = modelConnector;
		setPreferredSize(new Dimension(displayTemplate.getIconWidth(),
				displayTemplate.getIconHeight()));
		this.pipeline = pipeline;
		this.isOutpipes = isOutpipes;
		this.id = id;
		this.amount = amount;

		// find right color
		Color color = getConnectionColor(modelConnector.getType());
		this.setOpaque(false);

		display = derivateDisplay(displayTemplate, color, null);
	}

	/**
	 * Knows the color that has to be applied for a specific ConnectorType. Can
	 * be called from all {@link PipelineConnector}s and {@link PipelineLink}s.
	 * 
	 * @param connectorType
	 *            type of the connector to apply color to
	 * @return the corresponding color for the connectorType
	 */
	protected static Color getConnectionColor(ConnectorType connectorType) {
		switch (connectorType) {
		case CHANGE:
			return new Color(1.0f, 0.5f, 0.5f);
		case DATASET:
			return new Color(0.5f, 1.0f, 0.5f);
		case ENTITY:
			return new Color(0.5f, 0.5f, 1.0f);
		default:
			return new Color(1.0f, 1.0f, 1.0f);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(display, 0, 0, getWidth(), getHeight(), this);
	}

	/**
	 * @return the model connector represented by this
	 */
	public AbstractConnector getModelConnector() {
		return this.modelConnector;
	}

	/**
	 * @return whether this connector represents in pipes or out pipes
	 */
	public boolean isOutpipes() {
		return this.isOutpipes;
	}

	/**
	 * @return unique id for a group of connectors
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return amount of connectors in a group of connectors
	 */
	public int getAmount() {
		return this.amount;
	}

}
