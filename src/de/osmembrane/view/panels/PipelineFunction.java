package de.osmembrane.view.panels;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.AbstractFunction;

/**
 * The pipeline function, i.e. the visual representation of a model function
 * that is actually drawn in the pipeline. Note, the functions in the
 * {@link LibraryPanel} and the one being dragged on the {@link PipelinePanel}
 * are just {@link LibraryFunction}.
 * 
 * @author tobias_kuhn
 * 
 */
public class PipelineFunction extends LibraryFunction {

	private static final long serialVersionUID = -7573627124702293974L;

	/**
	 * The function in the model that is represented by this pipeline function
	 */
	private AbstractFunction modelFunction;

	/**
	 * Pipeline to add this to
	 */
	private PipelinePanel pipeline;

	/**
	 * List of connectors this functions has
	 */
	private List<PipelineConnector> connectors;

	/**
	 * Creates a new pipeline function from an AbstractFunction out of the model
	 * 
	 * @param modelFunction
	 *            the function out of the model
	 * @param pipeline
	 *            the pipeline panel to add it to
	 */
	public PipelineFunction(AbstractFunction modelFunction,
			final PipelinePanel pipeline) {
		// pretend this is a prototype
		super(modelFunction, false);
		this.modelFunction = modelFunction;
		this.pipeline = pipeline;
		this.connectors = new ArrayList<PipelineConnector>();

		createConnectors(modelFunction.getInConnectors(), false);
		createConnectors(modelFunction.getOutConnectors(), true);

		/*
		 * all functions are required to dispatch back to the pipeline,
		 * depending on tool
		 */
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				MouseEvent mainFrameEvent = SwingUtilities.convertMouseEvent(
						PipelineFunction.this, e, pipeline);

				switch (pipeline.getActiveTool()) {
				case DEFAULT_MAGIC_TOOL:
				case VIEW_TOOL:
				case SELECTION_TOOL:
					pipeline.dispatchEvent(mainFrameEvent);
					break;
				case CONNECTION_TOOL:
					break;
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				MouseEvent mainFrameEvent = SwingUtilities.convertMouseEvent(
						PipelineFunction.this, e, pipeline);

				switch (pipeline.getActiveTool()) {
				case DEFAULT_MAGIC_TOOL:
				case SELECTION_TOOL:
					pipeline.selected(PipelineFunction.this);
					pipeline.setDraggingFrom(mainFrameEvent.getPoint());
					break;
				case VIEW_TOOL:
					pipeline.dispatchEvent(mainFrameEvent);
					break;
				case CONNECTION_TOOL:
					pipeline.connect(PipelineFunction.this);
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

		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				MouseEvent mainFrameEvent = SwingUtilities.convertMouseEvent(
						PipelineFunction.this, e, pipeline);

				switch (pipeline.getActiveTool()) {
				case DEFAULT_MAGIC_TOOL:
				case VIEW_TOOL:
				case SELECTION_TOOL:
					pipeline.dispatchEvent(mainFrameEvent);
					break;
				}
			}
		});
	}

	/**
	 * Creates & adds connectors from connectorList for this function
	 * 
	 * @param connectorList
	 *            all the connectors that shall be created & added
	 * @param areOut
	 *            whether the connectors are out or in pipes
	 */
	private void createConnectors(AbstractConnector[] connectorList,
			boolean areOut) {
		int size = connectorList.length;
		for (int i = 0; i < size; i++) {
			PipelineConnector pc = new PipelineConnector(connectorList[i],
					pipeline, areOut, i, size);
			connectors.add(pc);
		}
	}

	/**
	 * Arranges the connectors, if necessary
	 */
	public void arrangeConnectors() {
		Point funcTopLeft = this.getLocation();

		for (PipelineConnector pc : connectors) {
			// standard stuff per connector
			Point offset = pipeline.objToWindowDelta(new Point2D.Double(-pc
					.getPreferredSize().width / 2.0, 0));
			Point size = pipeline.objToWindowDelta(new Point(pc
					.getPreferredSize().width, pc.getPreferredSize().height));

			// actual new position
			int newX = pc.isOutpipes() ? getWidth() : 0;
			double startY = ((getHeight() - pc.getAmount() * size.y) / 2.0);
			double offsetThisY = (pc.getId() * size.y);

			Point newPosition = new Point(newX, (int) (startY + offsetThisY));
			pc.setLocation(funcTopLeft.x + newPosition.x + offset.x,
					funcTopLeft.y + newPosition.y + offset.y);
			pc.setSize(size.x, size.y);

			pc.arrangeLinks();
			pc.repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		highlighted = this.equals(pipeline.getSelected());

		super.paintComponent(g);
	}

	/**
	 * @return the model function
	 */
	public AbstractFunction getModelFunction() {
		return this.modelFunction;
	}

	/**
	 * @return the location this function's model function has saved
	 */
	public Point2D getModelLocation() {
		return this.modelFunction.getCoordinate();
	}

	/**
	 * @return the connectors this function has
	 */
	public List<PipelineConnector> getConnectors() {
		return connectors;
	}
	
	/**
	 * @return all the outgoing links from all the connectors 
	 */
	public List<PipelineLink> getAllOutLinks() {
		ArrayList<PipelineLink> result = new ArrayList<PipelineLink>();
		
		for (PipelineConnector pc : connectors) {
			result.addAll(pc.getLinks());
		}
		
		return result;
	}

}
