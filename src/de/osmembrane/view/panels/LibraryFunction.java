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

import java.awt.Color;
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
import de.osmembrane.model.pipeline.Function;
import de.osmembrane.resources.Constants;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.frames.MainFrameGlassPane;
import de.osmembrane.view.interfaces.IMainFrame;

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
     * The minimum amount a {@link LibraryFunction} must be dragged to activate
     * the drop event.
     */
    protected static final double LIBRARY_FUNCTION_MIN_DRAG_DISTANCE = 32.0;

    /**
     * The {@link ImageIcon} resource that keeps an image template used to
     * prerender the actual image that will be drawn on this function
     */
    protected static ImageIcon displayTemplate = new ImageIcon(
            LibraryFunction.class
                    .getResource("/de/osmembrane/resources/images/function.png"));

    /**
     * The referenced model {@link Function}
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
     * Initializes a new {@link LibraryFunction} for the given model prototype
     * function
     * 
     * @param pipeline
     *            the {@link PipelinePanel} that shall later be able to identify
     *            drags from this class.
     * @param modelFunctionPrototype
     *            the model's prototype {@link Function} this
     *            {@link LibraryFunction} should represent
     * @param canDragAndDrop
     *            whether this {@link LibraryFunction} is in the
     *            {@link LibraryPanel} and can be dragged onto the pipeline
     *            panel to create a new {@link PipelineFunction}. Additionally,
     *            it gets highlighted when the mouse cursor moves over it. All
     *            non-library descendants are recommended to set this to false.
     */
    public LibraryFunction(final PipelinePanel pipeline,
            final AbstractFunction modelFunctionPrototype,
            final boolean canDragAndDrop) {

        this.modelFunctionPrototype = modelFunctionPrototype;
        /*
         * use DisplayTemplatePanel prerender capabilites in this case we store
         * it for *FUNCTION GROUPS ONLY* if you ever want images/function,
         * change this
         */
        setPreferredSize(new Dimension(
                (int) (displayTemplate.getIconWidth() * Constants.DEFAULT_SIZE_FACTOR),
                (int) (displayTemplate.getIconHeight() * Constants.DEFAULT_SIZE_FACTOR)));

        List<Image> prerender = DisplayTemplatePanel
                .givePrerender(modelFunctionPrototype.getParent());
        if (prerender != null) {
            // images are prerendered, use them
            display = prerender.get(0);
            displayHighlight = prerender.get(1);

        } else {
            // images are not prerendered, create them
            Color color = modelFunctionPrototype.getParent().getColor();
            float[] colorRGB = color.getComponents(null);
            Color highlightColor = new Color(
                    Math.min(1.0f, colorRGB[0] + 0.25f), Math.min(1.0f,
                            colorRGB[1] + 0.25f), Math.min(1.0f,
                            colorRGB[2] + 0.25f));

            display = DisplayTemplatePanel.prerenderDisplay(
                    modelFunctionPrototype.getParent(), displayTemplate, color,
                    modelFunctionPrototype.getIcon());

            displayHighlight = DisplayTemplatePanel.prerenderDisplay(
                    modelFunctionPrototype.getParent(), displayTemplate,
                    highlightColor, modelFunctionPrototype.getIcon());
        }
        highlighted = false;
        dragging = false;
        this.setOpaque(false);

        if (pipeline != null) {
            addMouseListener(new MouseListener() {

                // drag & drop

                @Override
                public void mouseReleased(MouseEvent e) {
                    dragging = false;

                    if (canDragAndDrop) {
                        IMainFrame mainFrame = ViewRegistry.getInstance()
                                .getCasted(MainFrame.class, IMainFrame.class);
                        mainFrame.getMainGlassPane().endDragAndDrop();
                        // necessary to make the glass pane go away

                        // require movement of at least some pixels
                        if (e.getPoint().distance(dragOffset) < LIBRARY_FUNCTION_MIN_DRAG_DISTANCE) {
                            return;
                        }

                        // subtract the offset where it got clicked
                        e.translatePoint(-dragOffset.x, -dragOffset.y);

                        // convert the mouse locations into the mainFrame and
                        // pipeline panel components
                        Point mainFramePoint = SwingUtilities.convertPoint(
                                LibraryFunction.this, e.getPoint(),
                                mainFrame.getMainGlassPane());
                        Point pipelinePoint = SwingUtilities.convertPoint(
                                LibraryFunction.this, e.getPoint(), pipeline);

                        if (mainFrame.isDragAndDropTarget(mainFramePoint)) {
                            pipeline.draggedOnto(LibraryFunction.this,
                                    pipelinePoint);
                        } else {
                            Application
                                    .handleException(new ControlledException(
                                            this,
                                            ExceptionSeverity.WARNING,
                                            I18N.getInstance()
                                                    .getString(
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
                    pipeline.setHint(InspectorPanel.VALID_EMPTY_HINT);

                    if (canDragAndDrop) {
                        highlighted = false;
                        repaint();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    // show hint for this function
                    pipeline.setHint(modelFunctionPrototype.getDescription());

                    if (canDragAndDrop) {
                        highlighted = true;
                        repaint();
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    // these clicks add the function to the pipeline
                    if (e.getClickCount() >= 2) {
                        pipeline.draggedOnto(LibraryFunction.this, new Point(0,
                                0));
                    }
                }
            });
        }

        // nice drag & drop animation
        if (canDragAndDrop) {
            addMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseMoved(MouseEvent e) {
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    IMainFrame mainFrame = ViewRegistry.getInstance()
                            .getCasted(MainFrame.class, IMainFrame.class);

                    // subtract the offset where it got clicked
                    e.translatePoint(-dragOffset.x, -dragOffset.y);

                    // convert the mouse event into the mainFrame and
                    // pipeline panel components
                    MouseEvent mainFrameEvent = SwingUtilities
                            .convertMouseEvent(LibraryFunction.this, e,
                                    mainFrame.getMainGlassPane());

                    mainFrame.getMainGlassPane().dragAndDrop(
                            LibraryFunction.this, mainFrameEvent.getPoint());
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
     *            the {@link Graphics} to draw upon
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

        // get applicable font
        g.setFont(g.getFont().deriveFont(Font.BOLD)
                .deriveFont(g.getFont().getSize() * getHeight() / 90.0f));

        printCenteredString(g, modelFunctionPrototype.getFriendlyName(), at.x,
                at.y + 0.8 * getHeight());
    }

    /**
     * Prints a string centered and with line breaks at spaces fitting into
     * {@link Graphics} g with y coordinate y
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
    protected void printCenteredString(Graphics g, String str, double x,
            double y) {

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
     * @return the model {@link Function} prototype associated with this
     *         {@link LibraryFunction}
     */
    public AbstractFunction getModelFunctionPrototype() {
        return this.modelFunctionPrototype;
    }

    /**
     * @return true, if this {@link LibraryFunction} is currently dragged, false
     *         otherwise
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
