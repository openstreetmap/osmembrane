package de.osmembrane.view.panels;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.JPanel;

import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.controller.actions.AddFunctionAction;
import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;
import de.osmembrane.model.xml.XMLHasDescription;
import de.osmembrane.view.ViewRegistry;

/**
 * This is the pipeline view, i.e. the panel that shows the entire pipeline with
 * all functions and connectors.
 * 
 * @author tobias_kuhn
 * 
 */
public class PipelinePanel extends JPanel implements Observer {

	/**
	 * list of functions currently being present on the panel
	 */
	private List<PipelineFunction> functions;
	
	/**
	 * The transformation to handle zooming
	 */
	private AffineTransform zoom;

	/**
	 * The links to the library and to the inspector used for communication
	 * between these components.
	 */
	private LibraryPanel functionLibrary;
	private InspectorPanel functionInspector;

	/**
	 * Initializes a new pipeline view
	 * 
	 * @param functionLibrary
	 * @param functionInspector
	 */
	public PipelinePanel(final LibraryPanel functionLibrary,
			InspectorPanel functionInspector) {

		// internal values
		this.functions = new ArrayList<PipelineFunction>();
		this.functionLibrary = functionLibrary;
		this.functionInspector = functionInspector;
		
		this.zoom = new AffineTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0);

		// register as observer
		ViewRegistry.getInstance().addObserver(this);

		// all listeners for all kind of events

		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// zoom with mouse wheel
				if (e.getWheelRotation() < 0) {
					zoomIn();
				} else {
					zoomOut();
				}
			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// drag & drop functionality : add function
				if (functionLibrary.getDragging() != null) {
					Action a = ActionRegistry.getInstance().get(
							AddFunctionAction.class);
					ContainingEvent ce = new ContainingEvent(this,
							functionLibrary.getDragging()
									.getModelFunctionPrototype());
					a.actionPerformed(ce);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				/* here will go a lot of code */
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

	public void zoomIn() {
		zoom.scale(2.0, 2.0);
	}

	public void zoomOut() {
		zoom.scale(0.5, 0.5);
	}

	public void moveView() {
		// TODO Auto-generated method stub

	}

	public void resetView() {
		// TODO Auto-generated method stub

	}

	public void showEntireView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		
	}
	
	/**
	 * Forwards hint display from functions and library under the cursor to the inspector.
	 * @param hintText the hint to display
	 */
	public void setHint(String hintText) {
		functionInspector.setHintText(hintText);
	}

}