package de.osmembrane.view.panels;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.controller.actions.AddFunctionAction;
import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.controller.events.ContainingLocationEvent;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;
import de.osmembrane.model.pipeline.PipelineObserverObject;
import de.osmembrane.model.xml.XMLHasDescription;
import de.osmembrane.view.ExceptionType;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;

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
		
		// best decision ever <- do not touch
		setLayout(null);

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
		
		// try to reserve some space		
		setPreferredSize(new Dimension(400, 400));
	}

	/**
	 * Zooms in
	 */
	public void zoomIn() {
		zoom.scale(2.0, 2.0);
		arrange();
	}

	/**
	 * Zooms out
	 */
	public void zoomOut() {
		zoom.scale(0.5, 0.5);
		arrange();
	}

	/**
	 * Moves the view. Hope this is done by the damn scrollpane
	 */
	public void moveView() {
		// TODO Auto-generated method stub

	}

	/**
	 * Resets the view to standard
	 */
	public void resetView() {
		zoom.setToIdentity();
		arrange();

	}

	/**
	 * Shows the entire pipeline
	 */
	public void showEntireView() {
		// TODO Auto-generated method stub
		arrange();
	}

	@Override
	public void update(Observable o, Object arg) {
		// check for notice from the pipeline model
		if (arg instanceof PipelineObserverObject) {
			PipelineObserverObject poo = (PipelineObserverObject) arg;
			
			switch (poo.getType()) {
			
			// new function was added
			case ADD:
				PipelineFunction pfAdd = new PipelineFunction(poo.getChangedFunction());				
				functions.add(pfAdd);
				add(pfAdd);
				arrange(pfAdd);
				break;
				
			// properties of a function changed
			case CHANGE:
				for (PipelineFunction pfChange : functions) {
					if (pfChange.getModelFunction().equals(poo.getChangedFunction())) {
						arrange(pfChange);
						repaint();
					}
				}
				break;
				
			// a function got removed
			case DELETE:
				for (int i = 0; i < functions.size(); i++) {
					PipelineFunction pfDelete = functions.get(i);
					
					if (pfDelete.getModelFunction().equals(poo.getChangedFunction())) {
						remove(pfDelete);
						functions.remove(i);
						break;
					}
				}
				break;
				
			// the whole pipeline was exchanged
			case FULLCHANGE:
				functions.clear();
				removeAll();
				
				for (AbstractFunction af : ModelProxy.getInstance().accessPipeline().getFunctions()) {
					PipelineFunction pfFullChange = new PipelineFunction(af);				
					functions.add(pfFullChange);
					add(pfFullChange);
				}
				arrange();
				break;
			}
		}
	}

	/**
	 * Arranges all the functions after a move/zoom change
	 */
	private void arrange() {
		for (PipelineFunction pf : functions) {
			arrange(pf);
		}
	}
	
	/**
	 * Arrange a specific function after any change
	 * @param pf the function to arrange
	 */
	private void arrange(PipelineFunction pf) {
		Point2D transformed = new Point();
		// location
		zoom.transform(pf.getModelLocation(), transformed);
		pf.setLocation((int) transformed.getX(), (int) transformed.getY());

		// size
		transformed.setLocation(pf.getPreferredSize().width,
				pf.getPreferredSize().height);
		zoom.transform(transformed, transformed);
		pf.setSize((int) transformed.getX(), (int) transformed.getY());
	}

	/**
	 * Forwards hint display from functions and library under the cursor to the
	 * inspector.
	 * 
	 * @param hintText
	 *            the hint to display
	 */
	public void setHint(String hintText) {
		functionInspector.setHintText(hintText);
	}

	/**
	 * Is called when a ViewFunction that canDragAndDrop was dragged onto the
	 * Pipeline panel
	 * 
	 * @param viewFunction
	 *            The new function to add
	 */
	public void draggedOnto(ViewFunction viewFunction, Point2D at) {
		// drag & drop functionality : add function
		Action a = ActionRegistry.getInstance().get(AddFunctionAction.class);

		Point2D newPosition = new Point2D.Double();
		try {
			zoom.inverseTransform(at, newPosition);
		} catch (NoninvertibleTransformException e1) {
			ViewRegistry.showException(this.getClass(),
					ExceptionType.ABNORMAL_BEHAVIOR, e1);
		}

		ContainingLocationEvent cle = new ContainingLocationEvent(this,
				viewFunction.getModelFunctionPrototype(), newPosition);
		a.actionPerformed(cle);
	}

}