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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import de.osmembrane.Application;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.FunctionGroup;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.components.JTextFieldWithButton;

/**
 * The function library panel that lists all the {@link FunctionGroup}s as
 * {@link LibraryPanelGroup}s in a register style and gives you the ability to
 * drag & drop them onto the {@link PipelinePanel}.
 * 
 * Would have been fancy to be generic, but was not possible due to time
 * constraints. Note smelly calls to getMainFrameByPass().getPipeline().
 * 
 * @see "Spezifikation.pdf, chapter 2.1.4 (German)"
 * 
 * @author tobias_kuhn
 * 
 */
public class LibraryPanel extends JPanel {

    private static final long serialVersionUID = -865621422748326256L;

    /**
     * The index of the {@link LibraryPanelGroup} that is currently completely
     * expanded, or -1 if none is completely expanded.
     */
    private int expanded;

    /**
     * The index of the {@link LibraryPanelGroup} that is currently expanding,
     * or -1 if none
     */
    private int expanding;

    /**
     * The index of the {@link LibraryPanelGroup} that is currently contracting,
     * or -1 if none
     */
    private int contracting;

    /**
     * The start of the expandation in System relative time, in milliseconds
     */
    private long expandingStart;

    /**
     * The desired duration of an expandation, in milliseconds
     */
    private final static double expandingDuration = 333.0;

    /**
     * The {@link Thread} that performs the expanding/contracting animation
     */
    private Thread expandingThread;

    /**
     * The {@link LibraryPanelGroup}s listed in this library panel
     */
    private List<LibraryPanelGroup> groups;

    /**
     * Filter text field to filter the functions
     */
    private JTextFieldWithButton editFilter;

    /**
     * {@link LibraryPanelGroup} for the filtered functions by editFilter.
     */
    private LibraryPanelGroup filterGroup;

    /**
     * Initializes a new {@link LibraryPanel}
     * 
     * @param pipeline
     *            pipeline to use for drag & drop
     */
    public LibraryPanel(final PipelinePanel pipeline) {
        // internal logic
        this.expanded = -1;
        this.expanding = -1;
        this.contracting = -1;
        this.expandingThread = null;
        this.groups = new ArrayList<LibraryPanelGroup>();

        final String noFiltering = "("
                + I18N.getInstance().getString("View.Library.NoFiltering")
                + ")";
        this.editFilter = new JTextFieldWithButton();
        editFilter.setLocation(3, 3);
        editFilter.setValue(noFiltering);
        editFilter.setCaption("x");
        editFilter.addButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editFilter.setValue(noFiltering);

                expanded = -1;
                expanding = -1;
                contracting = -1;

                for (LibraryPanelGroup lpg : groups) {
                    lpg.setVisible(true);
                }
                filterGroup.setVisible(false);
                rearrange(true);
            }
        });
        editFilter.setValueHorizontalAlignment(SwingConstants.CENTER);
        editFilter.addFieldFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                if (editFilter.getValue().trim().isEmpty()) {
                    editFilter.setValue(noFiltering);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if (editFilter.getValue().equals(noFiltering)) {
                    editFilter.setValue("");
                }
            }
        });
        editFilter.addFieldKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!editFilter.getValue().trim().isEmpty()
                        && !editFilter.getValue().equals(noFiltering)) {
                    // show filtered state
                    AbstractFunction[] result = ModelProxy.getInstance()
                            .getFunctions()
                            .getFilteredFunctions(editFilter.getValue());
                    filterGroup.populate(result, pipeline);

                    for (LibraryPanelGroup lpg : groups) {
                        lpg.setVisible(false);
                    }
                    filterGroup.setVisible(true);
                    filterGroup.setContentHeight(filterGroup
                            .getFullContentHeight());

                    expanded = filterGroup.getId();
                    expanding = -1;
                    contracting = -1;
                } else {
                    // show normal state
                    for (LibraryPanelGroup lpg : groups) {
                        lpg.setVisible(true);
                    }
                    filterGroup.setVisible(false);

                    expanded = -1;
                    expanding = -1;
                    contracting = -1;
                }

                rearrange(true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
        add(editFilter);

        // display
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        // best decision ever <- do not touch
        setLayout(null);

        filterGroup = new LibraryPanelGroup(this, pipeline, I18N.getInstance()
                .getString("View.Library.Filter"), Color.WHITE);
        filterGroup.setVisible(false);
        addGroup(filterGroup);
    }

    /**
     * Adds a new {@link LibraryPanelGroup} to select from
     * 
     * @param lpg
     */
    public void addGroup(LibraryPanelGroup lpg) {
        groups.add(lpg);
        // set the corresponding id, so the group knows it
        lpg.setId(groups.size() - 1);
        // make it contracted
        lpg.setContentHeight(0);
        add(lpg);
        // check if we have to adjust our preferred width
        if (lpg.getPreferredSize().width > getPreferredSize().width) {
            setPreferredSize(new Dimension(lpg.getPreferredSize().width,
                    getHeight()));
        }
        rearrange(true);
    }

    /**
     * Notify the panel that a {@link LibraryPanelGroup} has been clicked to
     * expand/collapse
     * 
     * @param group
     *            the {@link LibraryPanelGroup} to expand/collapse
     */
    public void groupClicked(int group) {

        // wait for the old thread to die before modifying variables
        // possible error in year 292 473 179 (expandingStart is really 0L).
        if ((expandingThread != null) && (expandingThread.isAlive())) {
            expandingStart = 0L;
            try {
                expandingThread.join();
            } catch (InterruptedException e) {
                Application.handleException(e);
            }
        }

        LibraryPanelGroup lpg;

        // finish old animation, if one was still running
        for (int i = 0; i < groups.size(); i++) {
            lpg = groups.get(i);
            if ((i != expanding) && (i != expanded)) {
                lpg.setContentHeight(0);
            }
        }

        if (expanding != -1) {
            lpg = groups.get(expanding);
            lpg.setContentHeight(lpg.getFullContentHeight());
            expanded = expanding;
        }
        expanding = -1;
        contracting = -1;
        rearrange(true);

        // check whether there is something to expand
        if (expanded == group) {
            expanding = -1;
            contracting = expanded;
            expanded = -1;
        } else {
            expanding = group;
            contracting = expanded;
            expanded = -1;
        }
        expandingStart = System.currentTimeMillis();

        expandingThread = new Thread(new Runnable() {

            @Override
            public void run() {

                LibraryPanelGroup lpg;
                double timeFactor = (System.currentTimeMillis() - expandingStart)
                        / expandingDuration;

                while (timeFactor < 1) {
                    timeFactor = (System.currentTimeMillis() - expandingStart)
                            / expandingDuration;

                    // actual animation
                    if (expanding > -1) {
                        lpg = groups.get(expanding);
                        lpg.setContentHeight(getExpandingHeight(timeFactor,
                                lpg.getFullContentHeight(), true));
                    }
                    if (contracting > -1) {
                        lpg = groups.get(contracting);
                        lpg.setContentHeight(getExpandingHeight(timeFactor,
                                lpg.getFullContentHeight(), false));
                    }

                    // prevents weird flickering... don't know why
                    rearrange(false);

                    // might be inaccurate by several factors, but will still
                    // guarantee a fluent animation
                    try {
                        Thread.sleep(20L);
                    } catch (InterruptedException e) {
                        // don't really care, we just redraw
                    }
                }

                // animation done
                if (expanding > -1) {
                    lpg = groups.get(expanding);
                    lpg.setContentHeight(lpg.getFullContentHeight());
                }
                if (contracting > -1) {
                    lpg = groups.get(contracting);
                    lpg.setContentHeight(0);
                }

                expanded = expanding;
                expanding = -1;
                contracting = -1;
                rearrange(true);

            }

        });
        expandingThread.start();
    }

    /**
     * Makes Swing thread-safe (or so we hope)
     */
    @Override
    public void repaint() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LibraryPanel.super.repaint();
            }
        });
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        rearrange(false);
    }

    /**
     * @see {@link JPanel#setSize}
     */
    private void setSizeNoArrange(Dimension d) {
        super.setSize(d);
    }

    /**
     * Rearranges the {@link LibraryPanelGroup}s to actually look like a
     * {@link LibraryPanel}. Unlike the "LayoutManager" (incompetent, is a
     * Manager)
     */
    private void rearrange(boolean setSize) {
        // bring the filter to its width
        editFilter.setSize(this.getWidth() - 6,
                editFilter.getPreferredSize().height);

        int y = 3 + editFilter.getHeight();
        for (LibraryPanelGroup lpg : groups) {
            if (lpg.isVisible()) {
                // determine top
                lpg.setLocation(3, y);
                // give it the width of the library & the height it needs
                lpg.setSize(this.getWidth() - 6, lpg.getHeight());
                // notify the arrangement
                lpg.rearranged();

                y += lpg.getHeight() + 6;
            }
        }

        // update for the scroll bar
        if (setSize) {
            setPreferredSize(new Dimension(this.getPreferredSize().width, y));
            setSizeNoArrange(getPreferredSize());
        }
    }

    /**
     * Calculates the correct height for a component during an
     * expanding/contracting animation
     * 
     * @param timeFactor
     *            currentTimeMillis() - expandingStart / expandingDuration
     * @param originalHeight
     *            original height of the expanding/contracting component
     * @param expanding
     *            true when component is expanding, false when it is contracting
     * @return the current height of the component during animation
     */
    private int getExpandingHeight(double timeFactor, int originalHeight,
            boolean expanding) {
        // function: timeFactor in [0, 1] ---> arg in [0, 1]
        double arg = 0.5 - 0.5 * Math.cos(timeFactor * Math.PI);

        if (expanding) {
            return (int) (arg * originalHeight);
        } else {
            return (int) ((1.0 - arg) * originalHeight);
        }
    }

}
