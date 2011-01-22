package de.osmembrane.view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.Connector;
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
	 * The {@link ImageIcon} resource that keeps an image template used to
	 * prerender the actual image that will be drawn on this connector
	 */
	protected static ImageIcon displayTemplate = new ImageIcon(
			PipelineConnector.class
					.getResource("/de/osmembrane/resources/images/connector.png"));

	/**
	 * The model {@link Connector} associated with this connector
	 */
	private AbstractConnector modelConnector;

	/**
	 * The parent {@link PipelineFunction} associated with this connector
	 */
	private PipelineFunction parentFunction;

	/**
	 * {@link PipelinePanel} this connector is drawn on.
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
	 * the id this connector has in a group of connectors. one group of
	 * connectors is all the in- or out-connectors of a function
	 */
	private int id;

	/**
	 * amount of the connectors in the group of connectors. one group of
	 * connectors is all the in- or out-connectors of a function
	 */
	private int amount;

	/**
	 * list of {@link PipelineLink}s going from this connector to other
	 * connectors
	 */
	private List<PipelineLink> links;

	/**
	 * Creates a new connector for a model {@link Connector} on a
	 * {@link PipelineFunction}.
	 * 
	 * @param modelConnector
	 *            connector in the model which is represented
	 * @param parentFunction
	 *            the function this connector belongs to
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
			final PipelineFunction parentFunction,
			final PipelinePanel pipeline, boolean isOutpipes, int id, int amount) {
		this.modelConnector = modelConnector;
		setPreferredSize(new Dimension(displayTemplate.getIconWidth(),
				displayTemplate.getIconHeight()));
		this.pipeline = pipeline;
		this.parentFunction = parentFunction;

		this.isOutpipes = isOutpipes;
		this.id = id;
		this.amount = amount;

		this.links = new ArrayList<PipelineLink>();

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				MouseEvent pipelineEvent = SwingUtilities.convertMouseEvent(
						PipelineConnector.this, e, pipeline);
				pipeline.dispatchEvent(pipelineEvent);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				MouseEvent pipelineEvent = SwingUtilities.convertMouseEvent(
						PipelineConnector.this, e, pipeline);

				if (pipeline.getActiveTool() == Tool.DEFAULT_MAGIC_TOOL) {
					pipeline.connect(parentFunction);
				} else {
					pipeline.dispatchEvent(pipelineEvent);
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

		// find right color
		Color color = getConnectionColor(modelConnector.getType());
		this.setOpaque(false);

		display = derivateDisplay(displayTemplate, color, null);

		// create links
		if (isOutpipes) {
			createLinks();
		}
	}

	/**
	 * Creates all the outflowing {@link PipelineLink}s from this connector.
	 */
	private void createLinks() {
		for (AbstractConnector ac : modelConnector.getConnections()) {
			PipelineLink pl = new PipelineLink(pipeline, this,
					pipeline.findConnector(ac),
					getConnectionColor(modelConnector.getType()));

			links.add(pl);
		}
	}

	/**
	 * Arranges all {@link PipelineLink}s to conform to this connector
	 */
	public void arrangeLinks() {
		for (PipelineLink pl : links) {
			PipelineConnector dest = pl.getLinkDestination();

			/* set size, set location */
			if (this.getX() < dest.getX()) {
				// arrow goes like ----->
				if (this.getY() < dest.getY()) {
					// left top to right bottom
					pl.setLocation(this.getX(), this.getY());
					pl.setSize(dest.getWidth() + dest.getX() - this.getX(),
							dest.getHeight() + dest.getY() - this.getY());
				} else {
					// left bottom to right top
					pl.setLocation(this.getX(), dest.getY());
					pl.setSize(dest.getWidth() + dest.getX() - this.getX(),
							this.getHeight() + this.getY() - dest.getY());
				}
			} else {
				// arrow goes like <-----
				if (this.getY() < dest.getY()) {
					// right top to left bottom
					pl.setLocation(dest.getX(), this.getY());
					pl.setSize(this.getWidth() + this.getX() - dest.getX(),
							dest.getHeight() + dest.getY() - this.getY());
				} else {
					// right bottom to left top
					pl.setLocation(dest.getX(), dest.getY());
					pl.setSize(this.getWidth() + this.getX() - dest.getX(),
							this.getHeight() + this.getY() - dest.getY());
				}
			}

			pl.regenerateLine();
		}

	}

	/**
	 * Knows the {@link Color} that has to be applied for a specific
	 * {@link ConnectorType}. Can be called from all {@link PipelineConnector}s
	 * and {@link PipelineLink}s.
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
	 * @return the model {@link Connector} represented by this
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
	 * @return the id this connector has in a group of connectors. one group of
	 *         connectors is all the in- or out-connectors of a function
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return amount of the connectors in the group of connectors. one group of
	 *         connectors is all the in- or out-connectors of a function
	 */
	public int getAmount() {
		return this.amount;
	}

	/**
	 * @return the list of {@link PipelineLink}s flowing out from this connector
	 */
	public List<PipelineLink> getLinks() {
		return this.links;
	}

	/**
	 * Creates a new {@link PipelineLink} to toConnector and returns it.
	 * 
	 * @param toConnector
	 *            the target of the connection from here
	 * @return the newly created link
	 */
	public PipelineLink addLinkTo(PipelineConnector toConnector) {
		PipelineLink pl = new PipelineLink(pipeline, this, toConnector,
				getConnectionColor(modelConnector.getType()));
		links.add(pl);
		return pl;
	}

	/**
	 * Removes the {@link PipelineLink} to toConnector and returns it.
	 * 
	 * @param toConnector
	 *            the target of the connection from here
	 * @return the removed link, or null if none found
	 */
	public PipelineLink removeLinkTo(PipelineConnector toConnector) {
		for (int i = 0; i < links.size(); i++) {
			PipelineLink pl = links.get(i);

			if (pl.links(this, toConnector)) {
				links.remove(i);
				return pl;
			}
		}

		return null;
	}

	/**
	 * @return the parent {@link PipelineFunction} this connector belongs to
	 */
	public PipelineFunction getParentFunction() {
		return this.parentFunction;
	}

}
