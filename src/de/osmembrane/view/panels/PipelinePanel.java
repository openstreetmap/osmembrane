package de.osmembrane.view.panels;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import de.osmembrane.Application;
import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.controller.actions.AddConnectionAction;
import de.osmembrane.controller.actions.AddFunctionAction;
import de.osmembrane.controller.actions.DeleteSelectionAction;
import de.osmembrane.controller.actions.DuplicateFunctionAction;
import de.osmembrane.controller.actions.MoveFunctionAction;
import de.osmembrane.controller.events.ConnectingFunctionsEvent;
import de.osmembrane.controller.events.ContainingLocationEvent;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.PipelineObserverObject;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.ViewRegistry;

/**
 * This is the pipeline view, i.e. the panel that shows the entire pipeline with
 * all functions and connectors. It contains some really fancy zoom & move
 * stuff.
 * 
 * @author tobias_kuhn
 * 
 */
public class PipelinePanel extends JPanel implements Observer {

	private static final long serialVersionUID = 2544369818627179591L;

	/**
	 * list of functions currently being present on the panel
	 */
	private List<PipelineFunction> functions;

	/**
	 * map of connectors currently being present on the panel
	 */
	private Map<AbstractConnector, PipelineConnector> connectors;

	/**
	 * The transformation which transforms the object coordinates to window
	 * coordinates, depending on the view port and the zooming level
	 */
	private AffineTransform objectToWindow;

	/**
	 * A temporary transformation to be used after objectToWindow to represent
	 * the current changes of the display (move dragging, zoom animation)
	 */
	private AffineTransform currentDisplay;

	/**
	 * Saves the point in object coordinates when a drag and drop action occurs
	 * *inside* the pipeline panel (i.e. not from the library)
	 */
	private Point2D draggingFrom;

	/**
	 * The standard zoom values.
	 */
	private final static double STANDARD_ZOOM_IN = 1.25;
	private final static double STANDARD_ZOOM_OUT = 0.80;
	private final static double PIXEL_PER_ZOOM_LEVEL = 100.00;

	/**
	 * The links to the inspector used for communication between these two
	 * components.
	 */
	private InspectorPanel functionInspector;

	/**
	 * The vertical and horizontal scroll bar that can be used for moving the
	 * view.
	 */
	private JScrollBar verticalScroll;
	private JScrollBar horizontalScroll;

	/**
	 * The upper-left-most and the bottom-right-most point on the whole pipeline
	 * in object space.
	 */
	private Point2D objTopLeft;
	private Point2D objBottomRight;

	/**
	 * The currently selected tool. Must be one of the _TOOL constants.
	 */
	private Tool activeTool;

	/**
	 * The currently selected object (either a PipelineFunction or a
	 * PipelineConnector)
	 */
	private JPanel selected;

	/**
	 * 
	 */
	private PipelineFunction connectionStart;

	/**
	 * Initializes a new pipeline view
	 * 
	 * @param functionLibrary
	 * @param functionInspector
	 */
	public PipelinePanel(InspectorPanel functionInspector) {

		// best decision ever <- do not touch
		setLayout(null);

		// internal values
		this.functions = new ArrayList<PipelineFunction>();
		this.connectors = new HashMap<AbstractConnector, PipelineConnector>();
		this.functionInspector = functionInspector;

		this.verticalScroll = new JScrollBar(JScrollBar.VERTICAL, 0, 0, 0, 0);
		this.horizontalScroll = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0,
				0);
		AdjustmentListener al = new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				/*
				 * if (e.getSource() == verticalScroll) {
				 * moveTo(objectToWindow.getTranslateX(), e.getValue());
				 * arrange(); } else if (e.getSource() == horizontalScroll) {
				 * moveTo(e.getValue(), objectToWindow.getTranslateY());
				 * arrange(); }
				 */
				/* closed down, that ***n bar sends those too often */
			}
		};
		this.verticalScroll.addAdjustmentListener(al);
		this.horizontalScroll.addAdjustmentListener(al);

		this.objTopLeft = new Point2D.Double();
		this.objBottomRight = new Point2D.Double();

		this.activeTool = Tool.DEFAULT_MAGIC_TOOL;
		this.selected = null;
		this.connectionStart = null;

		this.objectToWindow = new AffineTransform();
		this.currentDisplay = new AffineTransform();

		// register as observer
		ViewRegistry.getInstance().addObserver(this);

		// all listeners for all kind of events

		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// zoom with mouse wheel
				if (e.getWheelRotation() < 0) {
					zoom(e.getPoint(), STANDARD_ZOOM_IN);
				} else {
					zoom(e.getPoint(), STANDARD_ZOOM_OUT);
				}
			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// view tool and magic
				switch (activeTool) {
				case DEFAULT_MAGIC_TOOL:
					if (selected != null) {
						break;
					}
				case VIEW_TOOL:
					if (e.isControlDown()) {
						// zoom
						objectToWindow.preConcatenate(currentDisplay);
						currentDisplay.setToIdentity();
					} else {
						// move
						objectToWindow.preConcatenate(currentDisplay);
						currentDisplay.setToIdentity();
					}
					arrange();
					break;
				}

				// select tool and magic
				switch (activeTool) {
				case DEFAULT_MAGIC_TOOL:
				case SELECTION_TOOL:
					if ((selected != null)
							&& (selected instanceof PipelineFunction)
							&& (draggingFrom != null)) {
						PipelineFunction pf = (PipelineFunction) selected;

						// getCoordinate - draggingFrom
						Point2D objOffset = new Point2D.Double(pf
								.getModelLocation().getX()
								- draggingFrom.getX(), pf.getModelLocation()
								.getY() - draggingFrom.getY());

						Point2D objPosition = windowToObj(e.getPoint());

						Point2D newObjPosition = new Point2D.Double(
								objPosition.getX() + objOffset.getX(),
								objPosition.getY() + objOffset.getY());

						// set position
						Action a = ActionRegistry.getInstance().get(
								MoveFunctionAction.class);
						ContainingLocationEvent cle = new ContainingLocationEvent(
								this, pf.getModelFunction(), newObjPosition);
						a.actionPerformed(cle);
					}
				}
				draggingFrom = null;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				switch (activeTool) {
				case SELECTION_TOOL:
					selected(null);
					break;
				case DEFAULT_MAGIC_TOOL:
				case VIEW_TOOL:
					selected(null);
					// start dragging
					draggingFrom = windowToObj(e.getPoint());
					currentDisplay.setToIdentity();
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

				// view tool and magic
				switch (activeTool) {
				case DEFAULT_MAGIC_TOOL:
					if (selected != null) {
						break;
					}
				case VIEW_TOOL:
					if (draggingFrom != null) {
						Point2D draggingTo = windowToObjFixed(e.getPoint());

						if (e.isControlDown()) {
							// zoom
							double winDist = objToWindow(draggingFrom)
									.distance(e.getPoint())
									* Math.signum(draggingFrom.getY()
											- draggingTo.getY());
							zoomTemp(
									draggingFrom,
									Math.exp(winDist * Math.log(2.0)
											/ PIXEL_PER_ZOOM_LEVEL));
						} else {
							// move
							currentDisplay.setToTranslation(
									objectToWindow.getScaleX()
											* (draggingTo.getX() - draggingFrom
													.getX()),
									objectToWindow.getScaleY()
											* (draggingTo.getY() - draggingFrom
													.getY()));
							arrange();
						}
					}
					break;
				}

				// selection tool and magic
				switch (activeTool) {
				case DEFAULT_MAGIC_TOOL:
				case SELECTION_TOOL:
					if ((selected != null)
							&& (selected instanceof PipelineFunction)
							&& (draggingFrom != null)) {
						PipelineFunction pf = (PipelineFunction) selected;

						// getCoordinate - draggingFrom
						Point2D objOffset = new Point2D.Double(pf
								.getModelLocation().getX()
								- draggingFrom.getX(), pf.getModelLocation()
								.getY() - draggingFrom.getY());
						Point winOffset = objToWindowDelta(objOffset);

						// translate
						e.translatePoint(winOffset.x, winOffset.y);
						pf.setLocation(e.getPoint());
						pf.arrangeConnectors();
					}
				}

			} /* mouseDragged */
		});
	}

	/**
	 * Makes object to window exactly the translation to a point
	 * 
	 * @param translateX
	 * @param translateY
	 */
	private void moveTo(double translateX, double translateY) {
		double translatedX = objectToWindow.getTranslateX();
		double translatedY = objectToWindow.getTranslateY();
		objectToWindow.translate(translateX - translatedX, translateY
				- translatedY);

	}

	/**
	 * Translates window coordinates to object coordinates
	 * 
	 * @param window
	 *            window coordinates
	 * @return window in object coordinates, null if there is an error with the
	 *         transformations which should theoretically never be the case
	 */
	protected Point2D windowToObj(Point window) {
		Point2D result = new Point2D.Double();

		try {
			currentDisplay.inverseTransform(window, result);
			objectToWindow.inverseTransform(result, result);
		} catch (NoninvertibleTransformException e) {
			Application.handleException(e);
		}

		return result;
	}

	/**
	 * Translates window coordinates to object coordinates based on only the
	 * object to window transformation, not the temporary display
	 * transformation. This is necessary for dragging operations to transform
	 * only by the part of the transformation which is currently newly
	 * determined.
	 * 
	 * @param window
	 *            window coordinates
	 * @return window in object coordinates, only of basic transformation, null
	 *         if there is an error with the transformations which should
	 *         theoretically never be the case
	 */
	protected Point2D windowToObjFixed(Point window) {
		Point2D result = new Point2D.Double();

		try {
			objectToWindow.inverseTransform(window, result);
		} catch (NoninvertibleTransformException e) {
			Application.handleException(e);
		}

		return result;
	}

	/**
	 * Translates object coordinates to window coordinates
	 * 
	 * @param object
	 *            object coordinates
	 * @return object in window coordinates
	 */
	protected Point objToWindow(Point2D object) {
		Point2D result = new Point2D.Double();

		objectToWindow.transform(object, result);
		currentDisplay.transform(result, result);

		return new Point((int) result.getX(), (int) result.getY());
	}

	/**
	 * Translates object delta coordinates to window delta coordinates. Typical
	 * delta coordinates are object sizes.
	 * 
	 * @param objectDelta
	 *            object delta coordinates
	 * @return object in window delta coordinates
	 */
	protected Point objToWindowDelta(Point2D objectDelta) {
		Point2D result = new Point2D.Double();

		objectToWindow.deltaTransform(objectDelta, result);
		currentDisplay.deltaTransform(result, result);

		return new Point((int) result.getX(), (int) result.getY());
	}

	/**
	 * Zooms in
	 */
	public void zoomIn() {
		zoom(new Point(getWidth() / 2, getHeight() / 2), STANDARD_ZOOM_IN);
	}

	/**
	 * Zooms in
	 */
	public void zoomOut() {
		zoom(new Point(getWidth() / 2, getHeight() / 2), STANDARD_ZOOM_OUT);
	}

	/**
	 * Zooms (in the object space to window space transformation).
	 * 
	 * @param winCenter
	 *            center of the zooming operation in window space
	 * @param factor
	 *            zooming value. if < 1 zooms out, if > 1 zooms in
	 */
	public void zoom(Point winCenter, double factor) {
		// translate the center
		Point2D objCenter = windowToObj(winCenter);

		objectToWindow.translate(+objCenter.getX(), +objCenter.getY());
		objectToWindow.scale(factor, factor);
		objectToWindow.translate(-objCenter.getX(), -objCenter.getY());

		arrange();
	}

	/**
	 * Zooms temporary (in the current display transformation).
	 * 
	 * @param objCenter
	 *            center of the zooming operation in object space
	 * @param factor
	 *            zooming value. if < 1 zooms out, if > 1 zooms in
	 */
	public void zoomTemp(Point2D objCenter, double factor) {
		currentDisplay.setToIdentity();
		currentDisplay.translate(+objCenter.getX(), +objCenter.getY());
		currentDisplay.scale(factor, factor);
		currentDisplay.translate(-objCenter.getX(), -objCenter.getY());

		arrange();
	}

	/**
	 * Resets the view to standard
	 */
	public void resetView() {
		objectToWindow.setToIdentity();
		arrange();
	}

	/**
	 * Shows the entire pipeline
	 */
	public void showEntireView() {
		if (functions.size() < 1) {
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.WARNING, I18N.getInstance().getString(
							"View.Pipeline.NoFunctionsForEntireView")));
		}

		double left = objTopLeft.getX();
		double top = objTopLeft.getY();
		double right = objBottomRight.getX();
		double bottom = objBottomRight.getY();

		/*
		 * Construct an affine transformation so that (left,top) |-> (0,0) and
		 * (right, bottom) |-> (getWidth(),getHeight())
		 * 
		 * respect the aspect, tho
		 */

		double objAspect = (right - left) / (bottom - top);
		double winAspect = getWidth() / (double) getHeight();
		double zoom = 1.0;
		if (objAspect > winAspect) {
			zoom = getWidth() / (right - left);
		} else {
			zoom = getHeight() / (bottom - top);
		}

		objectToWindow.setToIdentity();
		objectToWindow.scale(zoom, zoom);
		objectToWindow.translate(-left, -top);

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
				PipelineFunction pfAdd = new PipelineFunction(
						poo.getChangedFunction(), this);
				functions.add(pfAdd);

				add(pfAdd);
				for (PipelineConnector pc : pfAdd.getConnectors()) {
					connectors.put(pc.getModelConnector(), pc);
					add(pc);
				}

				arrange(pfAdd);
				break;

			// properties of a function changed
			case CHANGE:
				for (PipelineFunction pfChange : functions) {
					if (pfChange.getModelFunction().equals(
							poo.getChangedFunction())) {
						arrange(pfChange);
						repaint();
					}
				}
				break;

			// a function got removed
			case DELETE:
				for (int i = 0; i < functions.size(); i++) {
					PipelineFunction pfDelete = functions.get(i);

					if (pfDelete.getModelFunction().equals(
							poo.getChangedFunction())) {

						remove(pfDelete);
						for (PipelineConnector pc : pfDelete.getConnectors()) {
							connectors.remove(pc.getModelConnector());
							remove(pc);
						}

						functions.remove(i);
						repaint();
						break;
					}
				}
				break;

			// the whole pipeline was exchanged
			case FULLCHANGE:
				functions.clear();
				removeAll();

				for (AbstractFunction af : ModelProxy.getInstance()
						.accessPipeline().getFunctions()) {
					PipelineFunction pfFullChange = new PipelineFunction(af,
							this);

					functions.add(pfFullChange);

					add(pfFullChange);
					for (PipelineConnector pc : pfFullChange.getConnectors()) {
						connectors.put(pc.getModelConnector(), pc);
						add(pc);
					}
				}
				arrange();
				break;
			}
		}

		// recreate topleft and bottomright
		calculateEdges();
		updateScrollbars(true);

	}

	/**
	 * Calculates objTopLeft and objBottomRight.
	 */
	private void calculateEdges() {
		if (functions.size() > 0) {
			double left = Double.MAX_VALUE;
			double top = Double.MAX_VALUE;
			double right = Double.MIN_VALUE;
			double bottom = Double.MIN_VALUE;

			for (PipelineFunction pf : functions) {
				double thisX = pf.getModelLocation().getX();
				double thisY = pf.getModelLocation().getY();

				left = Math.min(left, thisX);
				top = Math.min(top, thisY);
				right = Math.max(right, thisX + pf.getPreferredSize().width);
				bottom = Math.max(bottom, thisY + pf.getPreferredSize().height);
			}

			objTopLeft.setLocation(left, top);
			objBottomRight.setLocation(right, bottom);
		} else {
			objTopLeft.setLocation(0, 0);
			objBottomRight.setLocation(0, 0);
		}
	}

	/**
	 * Updates the valid value ranges of the scroll bars.
	 * 
	 * @param range
	 *            whether to update the ranges as well (only do so, if
	 *            objTopLeft and/or objBottomRight could have changed)
	 */
	private void updateScrollbars(boolean range) {
		if (range) {
			horizontalScroll.setMinimum((int) objTopLeft.getX());
			horizontalScroll.setMaximum((int) Math.max(0.0,
					(objBottomRight.getX() - objTopLeft.getX()) - getWidth()));

			verticalScroll.setMinimum((int) objTopLeft.getY());
			verticalScroll.setMaximum((int) Math.max(0.0,
					(objBottomRight.getY() - objTopLeft.getY()) - getHeight()));
		}

		// Point2D objWindowZero = windowToObj(new Point(0, 0));
		// horizontalScroll.setValue((int) objWindowZero.getX());
		// verticalScroll.setValue((int) objWindowZero.getY());
	}

	/**
	 * Arranges all the functions after a move/zoom change
	 */
	private void arrange() {
		for (PipelineFunction pf : functions) {
			arrange(pf);
		}
		updateScrollbars(false);
	}

	/**
	 * Arrange a specific function after any change
	 * 
	 * @param pf
	 *            the function to arrange
	 */
	private void arrange(PipelineFunction pf) {
		Point location = objToWindow(pf.getModelLocation());
		pf.setLocation(location);

		Point size = new Point(pf.getPreferredSize().width,
				pf.getPreferredSize().height);
		size = objToWindowDelta(size);
		pf.setSize(size.x, size.y);

		pf.arrangeConnectors();
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
	 * @param libraryFunction
	 *            The new function to add
	 */
	public void draggedOnto(LibraryFunction libraryFunction, Point at) {

		// drag & drop functionality : add function
		Action a = ActionRegistry.getInstance().get(AddFunctionAction.class);
		Point2D newPosition = windowToObj(at);

		ContainingLocationEvent cle = new ContainingLocationEvent(this,
				libraryFunction.getModelFunctionPrototype(), newPosition);
		a.actionPerformed(cle);
	}

	/**
	 * @return the currently selected object
	 */
	public Object getSelected() {
		return selected;
	}

	/**
	 * Called when a child object thinks it got selected
	 * 
	 * @param pipelineFunction
	 */
	public void selected(PipelineFunction pipelineFunction) {
		selected = pipelineFunction;

		if (selected != null) {
			for (PipelineFunction pf : functions) {
				pf.repaint();
			}

			// enable deleting & duplicating
			ActionRegistry.getInstance().get(DeleteSelectionAction.class)
					.setEnabled(selected != null);
			ActionRegistry
					.getInstance()
					.get(DuplicateFunctionAction.class)
					.setEnabled(
							(selected != null)
									&& (selected instanceof PipelineFunction));

			// edit in inspector panel
			functionInspector.inspect(pipelineFunction.getModelFunction());
		} else {
			functionInspector.inspect(null);
		}
	}

	/**
	 * @param activeTool
	 *            the activeTool to set
	 * @param newCursor
	 *            the cursor associated with the new tool, or null if no change
	 */
	public void setActiveTool(Tool activeTool, Cursor newCursor) {
		this.activeTool = activeTool;
		if (newCursor != null) {
			this.setCursor(newCursor);
		}
	}

	/**
	 * @return the activeTool
	 */
	public Tool getActiveTool() {
		return activeTool;
	}

	/**
	 * Sets the dragging point manually for objects that catch the MouseEvents.
	 * MouseListeners should be forwarded though. (Forwarding mouse events here
	 * would cause a deselection of the dragged object)
	 * 
	 * @param draggingFrom
	 *            point dragging started from (window space) to set
	 */
	public void setDraggingFrom(Point winDraggingFrom) {
		this.draggingFrom = windowToObj(winDraggingFrom);
	}

	/**
	 * @return the verticalScroll
	 */
	public JScrollBar getVerticalScroll() {
		return verticalScroll;
	}

	/**
	 * @return the horizontalScroll
	 */
	public JScrollBar getHorizontalScroll() {
		return horizontalScroll;
	}

	/**
	 * @param lookFor
	 *            model connector to look for
	 * @return the pipeline, i.e. view, connector associated with that model
	 *         connector, null if none is found
	 */
	protected PipelineConnector findConnector(AbstractConnector lookFor) {
		return connectors.get(lookFor);
	}

	/**
	 * Adds the connectionPoint as an endpoint to the current connection (with
	 * at most 2 points). This means: First call - start of connection. Second
	 * call - end of connection.
	 * 
	 * @param connectionPoint
	 *            point in the connection
	 */
	public void connect(PipelineFunction connectionPoint) {
		if (this.connectionStart == null) {
			this.connectionStart = connectionPoint;
		} else {
			ConnectingFunctionsEvent cfe = new ConnectingFunctionsEvent(this,
					connectionStart.getModelFunction(),
					connectionPoint.getModelFunction());
			ActionRegistry.getInstance().get(AddConnectionAction.class).actionPerformed(cfe);
		}
	}

}