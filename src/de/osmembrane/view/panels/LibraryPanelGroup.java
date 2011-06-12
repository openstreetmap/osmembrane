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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;
import de.osmembrane.model.pipeline.FunctionGroup;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.interfaces.IMainFrame;

/**
 * A group panel that is placed for each {@link FunctionGroup} on the
 * {@link LibraryPanel}.
 * 
 * @author tobias_kuhn
 * 
 */
public class LibraryPanelGroup extends JPanel {

    private static final long serialVersionUID = -2502154263887966328L;

    /**
     * The id this {@link LibraryPanelGroup} has in its main
     * {@link LibraryPanel} (used for click handling calls)
     */
    private int id;

    /**
     * The header {@link JButton} of this {@link LibraryPanelGroup} that can
     * make it expandable or contractable
     */
    private JButton headerButton;

    /**
     * The height of the contained objects
     */
    private int contentHeight;

    /**
     * The {@link FunctionGroup} represented
     */
    private AbstractFunctionGroup functionGroup;

    /**
     * The contained objects
     */
    private List<LibraryFunction> content;

    /**
     * Initializes a new {@link LibraryPanelGroup} for an
     * {@link AbstractFunctionGroup}.
     * 
     * @param lp
     *            the parent {@link LibraryPanel} on which this group will be
     *            displayed
     * @param pipeline
     *            the {@link PipelinePanel} that shall later be able to identify
     *            drags from the {@link LibraryFunction}s stored here.
     * @param afg
     *            the {@link AbstractFunctionGroup} which this
     *            {@link LibraryPanelGroup} represents
     */
    public LibraryPanelGroup(final LibraryPanel lp, PipelinePanel pipeline,
            AbstractFunctionGroup afg) {
        this.functionGroup = afg;

        // display
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        // best decision ever <- do not touch
        setLayout(null);

        // header button
        headerButton = new JButton();
        headerButton.setText(afg.getFriendlyName());
        Color color = afg.getColor();
        headerButton.setBackground(color);

        // determine size, etc.
        headerButton.setLocation(3, 3);
        headerButton.setSize(headerButton.getPreferredSize());

        // action listener
        headerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lp.groupClicked(id);
            }
        });

        // hint listener for function group
        headerButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // show no hint
                IMainFrame mainFrame = ViewRegistry.getInstance().getCasted(
                        MainFrame.class, IMainFrame.class);
                mainFrame.setHint(InspectorPanel.VALID_EMPTY_HINT);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // show hint for this function group
                IMainFrame mainFrame = ViewRegistry.getInstance().getCasted(
                        MainFrame.class, IMainFrame.class);
                mainFrame.setHint(functionGroup.getDescription());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        add(headerButton);

        content = new ArrayList<LibraryFunction>();
        // all functions available in the function group
        populate(afg.getFunctions(), pipeline);
    }

    /**
     * Initializes a new empty {@link LibraryPanelGroup}.
     * 
     * @param lp
     *            the parent {@link LibraryPanel} on which this group will be
     *            displayed
     * @param pipeline
     *            the {@link PipelinePanel} that shall later be able to identify
     *            drags from the {@link LibraryFunction}s stored here.
     * @param title
     *            title for the group (mainly to display the header button)
     * @param color
     *            color for the group (mainly to display the header button)
     */
    public LibraryPanelGroup(final LibraryPanel lp, PipelinePanel pipeline,
            String title, Color color) {
        this.functionGroup = null;

        // display
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        // best decision ever <- do not touch
        setLayout(null);

        // header button
        headerButton = new JButton();
        headerButton.setText(title);
        headerButton.setBackground(color);

        // determine size, etc.
        headerButton.setLocation(3, 3);
        headerButton.setSize(headerButton.getPreferredSize());

        // action listener
        headerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lp.groupClicked(id);
            }
        });

        add(headerButton);

        content = new ArrayList<LibraryFunction>();
    }

    /**
     * @param id
     *            the id this {@link LibraryPanelGroup} has in its main
     *            {@link LibraryPanel} to set
     */
    protected void setId(int id) {
        this.id = id;
    }

    /**
     * 
     * @return the height of this group's contents, if they are expanded
     */
    protected int getFullContentHeight() {
        return contentHeight;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension headBtn = headerButton.getPreferredSize();
        Dimension result = super.getPreferredSize();

        result.height = (headBtn != null) ? headBtn.height : 0;
        return result;
    }

    /**
     * Sets the height for this group's contents
     * 
     * @param newHeight
     *            0, if contracted, getFullHeight() if expanded
     */
    protected void setContentHeight(int newHeight) {
        Dimension headBtn = headerButton.getPreferredSize();

        setSize(getWidth(), ((headBtn != null) ? headBtn.height : 0) + 6
                + newHeight);
    }

    /**
     * Gets called when the {@link LibraryPanel} has rearranged the
     * {@link LibraryPanelGroup}s
     */
    protected void rearranged() {
        // correct the header button's width
        headerButton.setSize(getWidth() - 6,
                headerButton.getPreferredSize().height);
        // center all the content view functions
        for (LibraryFunction vf : content) {
            vf.setLocation((getWidth() - vf.getWidth()) / 2, vf.getLocation().y);
        }
    }

    /**
     * Removes all functions currently present in this {@link LibraryPanelGroup}
     * and instead uses the functions functions. Also adjusts the size.
     * 
     * @param functions
     *            the functions to display now
     */
    public void populate(AbstractFunction[] functions, PipelinePanel pipeline) {
        for (LibraryFunction lf : content) {
            remove(lf);
        }
        content.clear();

        // find the preferred width by using the maximum of all child objects
        int maxPreferredWidth = headerButton.getPreferredSize().width + 12;
        // running through the current height
        int y = 3 + headerButton.getHeight() + 6;

        // all functions available in the array
        for (AbstractFunction af : functions) {
            LibraryFunction lf = new LibraryFunction(pipeline, af, true);

            // determine the top
            lf.setLocation(3, y);
            // display them fully
            lf.setSize(lf.getPreferredSize());
            // find the maximum necessary width
            maxPreferredWidth = Math.max(maxPreferredWidth,
                    lf.getPreferredSize().width + 12);
            y += lf.getHeight() + 6;

            contentHeight += lf.getPreferredSize().height + 7;

            content.add(lf);
            add(lf);
        }

        setPreferredSize(new Dimension(maxPreferredWidth, 0));
    }

    /**
     * @return the id, i.e. the id this {@link LibraryPanelGroup} has in its
     *         main {@link LibraryPanel}
     */
    protected int getId() {
        return id;
    }

}
