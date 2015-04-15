/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */

package de.osmembrane.view.panels;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
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
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
import de.osmembrane.model.pipeline.Connector;
import de.osmembrane.model.pipeline.PipelineObserverObject;
import de.osmembrane.model.pipeline.PipelineObserverObject.ChangeType;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.components.JSilentScrollBar;
import de.osmembrane.view.interfaces.IZoomDevice;

/**
 * This is the pipeline view, i.e. the panel that shows the entire pipeline with
 * all {@link PipelineFunction}s, {@link PipelineConnector}s, and
 * {@link PipelineLink}s.
 * 
 * <b>Note</b>: In order to work with all the fancy zoom and move stuff, the
 * {@link PipelinePanel} defines 2 specific coordinate systems:
 * <ol>
 * <li>The window space - int window coordinates as defined in Swing.</li>
 * <li>The object space - double coordinates that are stored in the model.</li>
 * </ol>
 * Therefore it might be a good idea to always specify when coordinates or
 * points occur whether they are treated as object and window space.
 * 
 * The object space is defined as follows: a length of 1 window coordinate = a
 * length of 1.0 object coordinate in the initial view, window axes = object
 * axes.
 * 
 * In order to produce the viewable output you have to apply
 * {@link AffineTransform}ations from the object space to the window space. In
 * order to translate locations from the window to the object space, you have to
 * do the same vice versa.
 * 
 * There should never be a direct access to perform the transformations. Various
 * winToObj and objToWin transforming functions are provided for this matter.
 * 
 * Additionally, currently happening transformation changes should only change
 * the currentDisplay transformation and, when final, "pre-apply" it (multiply
 * from the left side) to the objectToWindow transformation.
 * 
 * @see "Spezifikation.pdf, chapter 2.1.5 (German)"
 * 
 * @see "your Math reference book, sections 'Linear Algebra', 'Matrices',
 *      'Linear Transformations'"
 * 
 * @author tobias_kuhn
 * 
 */
public class PipelinePanel extends JPanel implements Observer, IZoomDevice {

    private static final long serialVersionUID = 2544369818627179591L;

    /**
     * list of {@link PipelineFunction} currently being present on the panel
     */
    private List<PipelineFunction> functions;

    /**
     * map of {@link PipelineConnector} for the {@link Connector}s in the model,
     * which currently being present on the panel
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
     * The layered pane to show {@link PipelineFunction}s,
     * {@link PipelineConnector}s, {@link PipelineLink}s (in the particular
     * order defined by FUNCTION_LAYER, CONNECTOR_LAYER and LINK_LAYER)
     */
    private JLayeredPane layeredPane;

    /**
     * The constants applicable for the layeredPane.
     */
    private static final Integer FUNCTION_LAYER = new Integer(3);
    private static final Integer CONNECTOR_LAYER = new Integer(2);
    private static final Integer LINK_LAYER = new Integer(1);

    /**
     * Saves the point (in object coordinates) when a drag and drop action
     * occurs *inside* the {@link PipelinePanel} (i.e. not from the
     * {@link LibraryPanel})
     */
    private Point2D draggingFrom;

    /**
     * The standard zoom values.
     */
    private final static double DEFAULT_ZOOM_IN = 1.25;
    private final static double DEFAULT_ZOOM_OUT = 0.80;
    protected static final double DEFAULT_ZOOM = 1.0;
    private final static double PIXEL_PER_ZOOM_LEVEL = 100.00;

    /**
     * The links to the {@link InspectorPanel} used for communication between
     * these two components.
     */
    private InspectorPanel functionInspector;

    /**
     * The vertical and horizontal {@link JScrollBar} that can be used for
     * moving the view.
     */
    private JSilentScrollBar verticalScroll;
    private JSilentScrollBar horizontalScroll;

    /**
     * The upper-left-most and the bottom-right-most point on the whole pipeline
     * in object space.
     */
    private Point2D objTopLeft;
    private Point2D objBottomRight;

    /**
     * The currently selected {@link Tool}.
     */
    private Tool activeTool;

    /**
     * The currently selected object (either a {@link PipelineFunction} or a
     * {@link PipelineLink})
     */
    private Object selected;

    /**
     * The temporary saving slot for creating a two point {@link PipelineLink}.
     */
    private PipelineFunction connectionStart;

    /**
     * The temporary link to show the creation of a new connection
     */
    private PipelinePreviewLink connectionPreview;

    /**
     * Initializes a new {@link PipelinePanel}
     * 
     * @param functionInspector
     *            the {@link InspectorPanel} to handle the edits for the
     *            selected objects
     * @param popup
     *            the Popup to be displayed on right clicks
     */
    public PipelinePanel(InspectorPanel functionInspector,
            final JPopupMenu popup) {

        setLayout(new GridLayout(1, 1));

        // internal values
        this.functions = new ArrayList<PipelineFunction>();
        this.connectors = new HashMap<AbstractConnector, PipelineConnector>();
        this.functionInspector = functionInspector;

        this.verticalScroll = new JSilentScrollBar(Adjustable.VERTICAL, 0, 0,
                0, 0);
        this.horizontalScroll = new JSilentScrollBar(Adjustable.HORIZONTAL, 0,
                0, 0, 0);
        AdjustmentListener al = new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getSource() == verticalScroll) {
                    moveTopTo(e.getValue());
                    arrange(false);
                } else if (e.getSource() == horizontalScroll) {
                    moveLeftTo(e.getValue());
                    arrange(false);
                }
            }
        };
        this.verticalScroll.addAdjustmentListener(al);
        this.horizontalScroll.addAdjustmentListener(al);

        this.objTopLeft = new Point2D.Double();
        this.objBottomRight = new Point2D.Double();

        this.activeTool = Tool.DEFAULT_MAGIC_TOOL;
        this.selected = null;
        this.connectionStart = null;
        this.connectionPreview = new PipelinePreviewLink(this);

        this.objectToWindow = new AffineTransform();
        double zoomFactor = PipelinePanel.DEFAULT_ZOOM
                * (Double) ModelProxy.getInstance().getSettings()
                        .getValue(SettingType.DEFAULT_ZOOM_SIZE);
        this.objectToWindow.setToScale(zoomFactor, zoomFactor);
        this.currentDisplay = new AffineTransform();

        this.layeredPane = new JLayeredPane();
        this.layeredPane.setVisible(true);
        this.layeredPane.setOpaque(true);
        add(this.layeredPane);

        this.layeredPane.add(this.connectionPreview);

        // register as observer
        ViewRegistry.getInstance().addObserver(this);

        // all listeners for all kind of events

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // zoom with mouse wheel
                if (e.getWheelRotation() < 0) {
                    zoom(e.getPoint(), DEFAULT_ZOOM_IN);
                } else {
                    zoom(e.getPoint(), DEFAULT_ZOOM_OUT);
                }
            }
        });

        addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // popup on right click
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }

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
                    arrange(true);
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

                        // getCoordinate - draggingFrom = offset to add
                        Point newWinPos = e.getPoint();
                        Point2D objOffset = new Point2D.Double(pf
                                .getModelLocation().getX()
                                - draggingFrom.getX(), pf.getModelLocation()
                                .getY() - draggingFrom.getY());

                        Point winOffset = objToWindowDelta(objOffset);
                        newWinPos.translate(winOffset.x, winOffset.y);

                        newWinPos = findNextFreePoint(newWinPos, pf);

                        Point2D newObjPosition = windowToObj(newWinPos);

                        // require a minimum distance to drag & drop
                        if (newObjPosition.distance(pf.getModelLocation()) < PipelineFunction.PIPELINE_FUNCTION_MIN_DRAG_DISTANCE) {
                            pf.setLocation(objToWindow(pf.getModelLocation()));
                            pf.arrangeConnectors();
                            pf.arrangeLinks();
                            // return, so we don't create an undo step
                            return;
                        }

                        // set position
                        Action a = ActionRegistry.getInstance().get(
                                MoveFunctionAction.class);
                        ContainingLocationEvent cle = new ContainingLocationEvent(
                                this, pf.getModelFunction(), newObjPosition);
                        a.actionPerformed(cle);
                    }
                }

                // connection tool and magic
                switch (activeTool) {
                case DEFAULT_MAGIC_TOOL:
                case SELECTION_TOOL:
                    abortConnect();
                }

                draggingFrom = null;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                switch (activeTool) {
                case SELECTION_TOOL:
                    selected(null);
                    repaint();
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
                // when the start of new connection is determined, rearrange it
                if (connectionStart != null) {
                    connectionPreview.setTarget(e.getPoint());
                    connectionPreview.regenerateLine();
                    repaint();
                }
                PipelinePanel.this.connectionPreview
                        .setVisible(PipelinePanel.this.connectionStart != null);
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
                            double winDist = objToWindowFixed(draggingFrom)
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
                            arrange(true);
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
                        pf.arrangeLinks();

                        // now arrange all *incoming* links, since connectors
                        // only contain their outgoing connections
                        for (PipelineConnector pfCon : pf.getConnectors()) {
                            if (!pfCon.isOutpipes()) {
                                for (PipelineLink pfInLink : pfCon.getInLinks()) {
                                    pfInLink.getLinkSource().arrangeLinks();
                                }
                            }
                        }

                    } /* if something selected */
                }

            } /* mouseDragged */
        });

        // necessary to initialize with correct model reflection
        update(null, new PipelineObserverObject(ChangeType.FULLCHANGE, null));
    }

    /**
     * Moves objectToWindow in a way, so that the left screen (window x = 0)
     * will result in the object coordinate to.
     * 
     * @param to
     *            the object position to move the left window edge to
     */
    private void moveLeftTo(double to) {
        // if you can't read the Math below properly,
        // some idiot used Eclipse/Java auto code formatter
        /*
         * o2w.scale * to + o2w.translate = 0 <=> o2w.translate = - o2w.scale *
         * to
         */
        double translateToX = -objectToWindow.getScaleX() * to;

        /*
         * o2w * M = o2w_new, where o2w is of the form scale(x,y) and
         * translate(s,t) and M is a translation matrix from translate(u,v) and
         * o2w_new.translate is of the form scale(x,y) and
         * translateTo(translateToX,t) <=> u * x + s = translateToX v * y + t =
         * t <=> u = (translateToX - s) / x v = 0
         */
        objectToWindow.translate(
                (translateToX - objectToWindow.getTranslateX())
                        / (objectToWindow.getScaleX()), 0.0);
    }

    /**
     * Moves objectToWindow in a way, so that the top screen (window y = 0) will
     * result in the object coordinate to.
     * 
     * @param to
     *            the object position to move the top window edge to
     */
    private void moveTopTo(double to) {
        // if you can't read the Math below properly,
        // some idiot used Eclipse/Java auto code formatter
        /*
         * o2w.scale * to + o2w.translate = 0 <=> o2w.translate = - o2w.scale *
         * to
         */
        double translateToY = -objectToWindow.getScaleY() * to;

        /*
         * o2w * M = o2w_new, where o2w is of the form scale(x,y) and
         * translate(s,t) and M is a translation matrix from translate(u,v) and
         * o2w_new.translate is of the form scale(x,y) and
         * translateTo(s,translateToY) <=> u * x + s = s v * y + t =
         * translateToY <=> u = 0 v = (translateToY - t) / y
         */
        objectToWindow.translate(0.0,
                (translateToY - objectToWindow.getTranslateY())
                        / (objectToWindow.getScaleY()));
    }

    /**
     * Fully translates window coordinates to object coordinates.
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
     * Translates window delta coordinates to object delta coordinates. Typical
     * delta coordinates are object sizes.
     * 
     * @param windowDelta
     *            window delta coordinates
     * @return windowDelta in object delta coordinates
     */
    private Point2D windowToObjDelta(Point windowDelta) {
        Point2D result = new Point2D.Double();

        try {
            AffineTransform temp = new AffineTransform(objectToWindow);
            temp.preConcatenate(currentDisplay);
            temp.invert();
            temp.deltaTransform(windowDelta, result);

        } catch (NoninvertibleTransformException e) {
            Application.handleException(e);
        }

        return result;
    }

    /**
     * Translates window coordinates to object coordinates based on only the
     * objectToWindow transformation, not the temporary display transformation.
     * This is necessary for dragging operations to transform only by the part
     * of the transformation which is currently newly determined.
     * 
     * @param windowFixed
     *            fixed window coordinates
     * @return window in object coordinates, only of basic transformation, null
     *         if there is an error with the transformations which should
     *         theoretically never be the case
     */
    protected Point2D windowToObjFixed(Point windowFixed) {
        Point2D result = new Point2D.Double();

        try {
            objectToWindow.inverseTransform(windowFixed, result);
        } catch (NoninvertibleTransformException e) {
            Application.handleException(e);
        }

        return result;
    }

    /**
     * Fully translates object coordinates to window coordinates.
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
     * Translates object coordinates to window coordinates based on only the
     * objectToWindow transformation, not the temporary display transformation.
     * This is necessary for dragging operations to transform only by the part
     * of the transformation which is currently newly determined.
     * 
     * @param objectFixed
     *            fixed object coordinates
     * @return object in window coordinates, only of basic transformation
     */
    protected Point objToWindowFixed(Point2D objectFixed) {
        Point2D result = new Point2D.Double();

        objectToWindow.transform(objectFixed, result);

        return new Point((int) result.getX(), (int) result.getY());
    }

    @Override
    public void zoomIn() {
        zoom(new Point(getWidth() / 2, getHeight() / 2), DEFAULT_ZOOM_IN);
    }

    @Override
    public void zoomOut() {
        zoom(new Point(getWidth() / 2, getHeight() / 2), DEFAULT_ZOOM_OUT);
    }

    /**
     * Zooms (in the objectToWindow transformation).
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

        arrange(true);
        repaint();
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
        AffineTransform objectToWindowInverse = new AffineTransform(
                objectToWindow);
        try {
            objectToWindowInverse.invert();
        } catch (NoninvertibleTransformException e) {
            Application.handleException(e);
        }

        currentDisplay.setToIdentity();

        currentDisplay.concatenate(objectToWindow);

        currentDisplay.translate(+objCenter.getX(), +objCenter.getY());
        currentDisplay.scale(factor, factor);
        currentDisplay.translate(-objCenter.getX(), -objCenter.getY());

        currentDisplay.concatenate(objectToWindowInverse);

        arrange(true);
        repaint();
    }

    @Override
    public void resetView() {
        double zoomFactor = PipelinePanel.DEFAULT_ZOOM
                * (Double) ModelProxy.getInstance().getSettings()
                        .getValue(SettingType.DEFAULT_ZOOM_SIZE);
        objectToWindow.setToScale(zoomFactor, zoomFactor);
        arrange(true);
    }

    @Override
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

        arrange(true);
    }

    /**
     * <b>Note:</b> All addition and removal of objects must be done here.
     */
    @Override
    public void update(Observable o, Object arg) {
        // check for notice from the pipeline model
        if (arg instanceof PipelineObserverObject) {
            PipelineObserverObject poo = (PipelineObserverObject) arg;

            switch (poo.getType()) {

            // new function was added
            case ADD_FUNCTION:
                PipelineFunction pfAdd = new PipelineFunction(
                        poo.getChangedFunction(), this);
                functions.add(pfAdd);

                layeredPane.add(pfAdd, FUNCTION_LAYER);
                for (PipelineConnector pc : pfAdd.getConnectors()) {
                    connectors.put(pc.getModelConnector(), pc);
                    layeredPane.add(pc, CONNECTOR_LAYER);

                    for (PipelineLink pl : pc.getOutLinks()) {
                        layeredPane.add(pl, LINK_LAYER);
                    }
                }

                // automatically select the newly added function
                selected(pfAdd);
                break;

            // properties of a function changed
            case CHANGE_FUNCTION:
                for (PipelineFunction pfChange : functions) {
                    if (pfChange.getModelFunction().equals(
                            poo.getChangedFunction())) {

                        // if this function has links to its in-connectors,
                        // arrange those functions too
                        for (PipelineConnector pc : pfChange.getConnectors()) {
                            if (!pc.isOutpipes()) {
                                for (PipelineLink pl : pc.getInLinks()) {
                                    pl.getLinkSource().arrangeLinks();
                                }
                            }
                        }
                    } /* if pfChange.equals(poo) */
                }
                break;

            // a function got removed
            case DELETE_FUNCTION:
                for (int i = 0; i < functions.size(); i++) {
                    PipelineFunction pfDelete = functions.get(i);

                    if (pfDelete.getModelFunction().equals(
                            poo.getChangedFunction())) {

                        // clean-up on isle three
                        layeredPane.remove(pfDelete);
                        for (PipelineConnector pc : pfDelete.getConnectors()) {
                            // delete in links
                            int j = 0;
                            while (j < pc.getInLinks().size()) {
                                PipelineLink pl = pc.getInLinks().get(j);
                                pl.getLinkSource().removeLinkTo(
                                        pl.getLinkDestination());
                                layeredPane.remove(pl);
                                j++;
                            }

                            // delete out links
                            int k = 0;
                            while (k < pc.getOutLinks().size()) {
                                PipelineLink pl = pc.getOutLinks().get(k);
                                pl.getLinkSource().removeLinkTo(
                                        pl.getLinkDestination());
                                layeredPane.remove(pl);
                                k++;
                            }

                            // delete connector
                            connectors.remove(pc.getModelConnector());
                            layeredPane.remove(pc);
                        }

                        // deselect stuff if necessary
                        if (pfDelete.equals(selected)) {
                            selected(null);
                        }
                        functions.remove(i);
                        break;
                    }
                }
                break;

            // the whole pipeline was exchanged
            case FULLCHANGE:
                // let's pray for the GC to get this right
                functions.clear();
                connectors.clear();
                layeredPane.removeAll();
                System.gc();

                layeredPane.add(connectionPreview);

                for (AbstractFunction af : ModelProxy.getInstance()
                        .getPipeline().getFunctions()) {
                    PipelineFunction pfFullChange = new PipelineFunction(af,
                            this);

                    functions.add(pfFullChange);

                    layeredPane.add(pfFullChange, FUNCTION_LAYER);
                    for (PipelineConnector pc : pfFullChange.getConnectors()) {
                        connectors.put(pc.getModelConnector(), pc);
                        layeredPane.add(pc, CONNECTOR_LAYER);
                    }
                }

                // start linking when all connectors are truly known
                for (PipelineConnector pc : connectors.values()) {
                    pc.generateLinksFromModel();

                    for (PipelineLink pl : pc.getOutLinks()) {
                        layeredPane.add(pl, LINK_LAYER);
                    }
                }

                // deselect stuff
                selected(null);
                break;

            // new connection added
            case ADD_CONNECTION:
                PipelineConnector sourceAdd = findConnector(poo
                        .getChangedConnectors()[0]);
                PipelineLink plAdd = sourceAdd.addLinkTo(findConnector(poo
                        .getChangedConnectors()[1]));
                layeredPane.add(plAdd, LINK_LAYER);
                break;

            // connection deleted
            case DELETE_CONNECTION:
                PipelineConnector sourceDel = findConnector(poo
                        .getChangedConnectors()[0]);
                PipelineLink plDel = sourceDel.removeLinkTo(findConnector(poo
                        .getChangedConnectors()[1]));
                if (plDel != null) {
                    layeredPane.remove(plDel);
                }
                break;
            }
        }

        arrange(true);
        layeredPane.repaint();

        // this is better reset here
        connectionStart = null;

        // recreate topleft and bottomright
        calculateEdges();
        updateScrollbars();

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
     */
    private void updateScrollbars() {
        // current size of the window, not needed to be scrolled
        Point2D winSize = windowToObjDelta(new Point(getWidth(), getHeight()));

        horizontalScroll.setMinimumSilently((int) objTopLeft.getX());
        horizontalScroll.setMaximumSilently((int) Math.max(0.0,
                (objBottomRight.getX() - winSize.getX())));

        verticalScroll.setMinimumSilently((int) objTopLeft.getY());
        verticalScroll.setMaximumSilently((int) Math.max(0.0,
                (objBottomRight.getY() - winSize.getY())));

        Point2D objWindowZero = windowToObj(new Point(0, 0));

        horizontalScroll.setValueSilently((int) objWindowZero.getX());
        verticalScroll.setValueSilently((int) objWindowZero.getY());
    }

    /**
     * Arranges all the {@link PipelineFunction}s after a move/zoom change
     * 
     * @param updateScrolls
     *            whether the scrollbars should be updated. necessary to prevent
     *            infinite recursion
     */
    private void arrange(boolean updateScrolls) {
        for (PipelineFunction pf : functions) {
            arrange(pf);
        }

        // now all connectors are arranged and we can arrange the links
        for (PipelineFunction pf : functions) {
            pf.arrangeLinks();
        }

        calculateEdges();
        if (updateScrolls) {
            updateScrollbars();
        }
    }

    /**
     * Arrange a specific {@link PipelineFunction} after any change
     * 
     * @param pf
     *            the {@link PipelineFunction} to arrange
     */
    private void arrange(PipelineFunction pf) {
        Point location = objToWindow(pf.getModelLocation());
        pf.setLocation(location);

        Point size = new Point(pf.getPreferredSize().width,
                pf.getPreferredSize().height);
        size = objToWindowDelta(size);
        pf.setSize(size.x, size.y);

        if (pf.equals(selected)) {
            pf.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        } else if (!pf.getModelFunction().isComplete()) {
            pf.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        } else {
            pf.setBorder(null);
        }

        pf.arrangeConnectors();
    }

    /**
     * Forwards hint display from {@link PipelineFunction}s and
     * {@link LibraryPanel} under the cursor to the {@link InspectorPanel}.
     * 
     * @param hintText
     *            the hint to display
     */
    public void setHint(String hintText) {
        functionInspector.setHintText(hintText);
    }

    /**
     * Is called when a {@link LibraryFunction} that canDragAndDrop was dragged
     * onto the {@link PipelinePanel}
     * 
     * @param libraryFunction
     *            The new function to add
     * @param at
     *            the position of the new function, in PipelinePanel's window
     *            coordinates
     */
    public void draggedOnto(LibraryFunction libraryFunction, Point at) {

        at = findNextFreePoint(at, null);

        // drag & drop functionality : add function
        Action a = ActionRegistry.getInstance().get(AddFunctionAction.class);
        Point2D newPosition = windowToObj(at);

        ContainingLocationEvent cle = new ContainingLocationEvent(this,
                libraryFunction.getModelFunctionPrototype(), newPosition);
        a.actionPerformed(cle);
    }

    /**
     * @return the currently selected object ({@link PipelineFunction} or
     *         {@link PipelineLink})
     */
    public Object getSelected() {
        return selected;
    }

    /**
     * Called when a child object thinks it got selected. Only defined for
     * {@link PipelineFunction} and {@link PipelineLink}.
     * 
     * @param childObject
     *            child function to be selected
     */
    public void selected(Object childObject) {
        selected = childObject;

        if (selected != null) {
            if (selected instanceof PipelineFunction) {
                repaint();

                // edit in inspector panel
                PipelineFunction pf = (PipelineFunction) childObject;
                functionInspector.inspect(pf.getModelFunction());

            } else if (selected instanceof PipelineLink) {
                repaint();
            } else {
                Application.handleException(new ControlledException(this,
                        ExceptionSeverity.UNEXPECTED_BEHAVIOR, I18N
                                .getInstance().getString(
                                        "View.Pipeline.IllegalSelection",
                                        selected.toString())));
            }
        } else {
            functionInspector.inspect(null);
            repaint();
        }

        arrange(false);

        // enable deleting & duplicating
        ActionRegistry.getInstance().get(DeleteSelectionAction.class)
                .setEnabled(selected != null);
        ActionRegistry
                .getInstance()
                .get(DuplicateFunctionAction.class)
                .setEnabled(
                        (selected != null)
                                && (selected instanceof PipelineFunction));
    }

    /**
     * @param activeTool
     *            the active {@link Tool} to set
     * @param newCursor
     *            the cursor associated with the new tool, or null if no change
     */
    public void setActiveTool(Tool activeTool, Cursor newCursor) {
        abortConnect();
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
     * Sets the dragging point manually for objects that need to catch the
     * {@link MouseEvent} to get dragged around. (Typically,
     * {@link MouseListener}s should be forwarded though. But forwarding mouse
     * events in this case would cause a deselection of the dragged object)
     * 
     * @param winDraggingFrom
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
     *            model {@link Connector} to look for
     * @return the pipeline, i.e. {@link PipelineConnector} associated with that
     *         model {@link Connector}, null if none is found
     */
    protected PipelineConnector findConnector(AbstractConnector lookFor) {
        return connectors.get(lookFor);
    }

    /**
     * Adds the connectionPoint as an end point to the current connection (which
     * has at most 2 points). This means: First call - start of new connection.
     * Second call - end of new connection.
     * 
     * @param connectionPoint
     *            point in the connection
     */
    public void connect(PipelineFunction connectionPoint) {
        if (this.connectionStart == null) {
            this.connectionStart = connectionPoint;
            this.connectionPreview.setSource(connectionPoint);
        } else {
            ConnectingFunctionsEvent cfe = new ConnectingFunctionsEvent(this,
                    connectionStart.getModelFunction(),
                    connectionPoint.getModelFunction());
            ActionRegistry.getInstance().get(AddConnectionAction.class)
                    .actionPerformed(cfe);
            this.connectionStart = null;
        }
    }

    /**
     * Aborts a current try to connect two functions.
     */
    public void abortConnect() {
        this.connectionStart = null;
        this.connectionPreview.setVisible(false);
    }

    /**
     * @return the layeredPane
     */
    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }

    /**
     * Finds out whether you can safely place a function here without occluding
     * another one.
     * 
     * @param newPoint
     *            {@link Point} where the new Function would be situated
     * @param ignore
     *            Ignore this function. Useful if you're currently moving this
     *            one. May be null.
     * @return true, if the new function would collide with an existing one,
     *         false otherwise.
     */
    public boolean wouldCollide(Point newPoint, PipelineFunction ignore) {

        /*
         * number of pixels to subtract from the technically necessity to have
         * functions touching, so that the feature is not too restrictive
         */
        int grace = 0;

        for (PipelineFunction pf : functions) {
            if (pf.equals(ignore)) {
                continue;
            }

            if ((newPoint.x >= pf.getX() - pf.getWidth() + grace)
                    && (newPoint.y >= pf.getY() - pf.getHeight() + grace)
                    && (newPoint.x <= pf.getX() + pf.getWidth() - grace)
                    && (newPoint.y <= pf.getY() + pf.getHeight() - grace)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Finds the next free & usable position on the pipeline panel, based on a
     * preferred location.
     * 
     * @param at
     *            location where to find the nearest free point for
     * @param forFunc
     *            the function for which the place should be found. May be null
     * @return the nearest free point in the area of at
     */
    public Point findNextFreePoint(Point at, PipelineFunction forFunc) {
        Point result = new Point(at);
        double dist = 0.0;

        while (wouldCollide(result, forFunc)) {
            dist += 20.0;

            // angle between x to the right, y downwards in [0, Math.PI / 2]
            for (double angle = 0.0; angle < Math.PI / 2.0; angle += Math.PI / 20.0) {

                result = new Point(at);
                result.translate((int) (+Math.cos(angle) * dist),
                        (int) (+Math.sin(angle) * dist));

                if (!wouldCollide(result, forFunc)) {
                    break;
                }
            } /* for */
        }

        return result;
    }

}
