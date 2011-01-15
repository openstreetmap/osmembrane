package de.osmembrane.view.panels;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import de.osmembrane.controller.exceptions.ExceptionSeverity;
import de.osmembrane.view.ViewRegistry;

/**
 * The function library panel that lists all the function groups in a register
 * style and gives you the ability to drag & drop them onto the
 * {@link PipelinePanel}.
 * 
 * @author tobias_kuhn
 * 
 */
public class LibraryPanel extends JPanel {

	private static final long serialVersionUID = -865621422748326256L;

	/**
	 * The index of the function group that is currently completely expanded, or
	 * -1 if none is completely expanded.
	 */
	private int expanded;

	/**
	 * The index of the function group that is currently expanding, or -1 if
	 * none
	 */
	private int expanding;

	/**
	 * The index of the function group that is currently contracting, or -1 if
	 * none
	 */
	private int contracting;

	/**
	 * The start of the expandation in System relative time, in milliseconds
	 */
	private long expandingStart;

	/**
	 * The typical duration of an expandation, in milliseconds
	 */
	private final static double expandingDuration = 333.0;

	/**
	 * The thread that performs the expanding/contracting animation
	 */
	private Thread expandingThread;

	/**
	 * The group components listed in this library panel
	 */
	private List<LibraryPanelGroup> groups;

	/**
	 * Initializes the basics of the panel, i.e. logic and display
	 */
	public LibraryPanel() {
		// internal logic
		expanded = -1;
		expanding = -1;
		contracting = -1;
		expandingThread = null;
		groups = new ArrayList<LibraryPanelGroup>();

		// display
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		// best decision ever <- do not touch
		setLayout(null);
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
		rearrange();
	}

	/**
	 * Notify the panel that a group has been clicked
	 * 
	 * @param group
	 */
	public void groupClicked(int group) {

		// wait for the old thread to die before modifying variables
		// possible error in year 292 473 179 (expandingStart is really 0L).
		if ((expandingThread != null) && (expandingThread.isAlive())) {
			expandingStart = 0L;
			try {
				expandingThread.join();
			} catch (InterruptedException e) {
				ViewRegistry.showException(this.getClass(),
						ExceptionSeverity.UNEXPECTED_BEHAVIOR, e);
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
		rearrange();

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

					rearrange();

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
				rearrange();

			}

		});
		expandingThread.start();
	}

	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		rearrange();
	}

	/**
	 * Rearranges the library panel groups to actually look like a library panel
	 * Unlike the "Layout Manager" (incompetent, is a Manager)
	 */
	private void rearrange() {
		int y = 3;
		for (LibraryPanelGroup lpg : groups) {
			// determine top
			lpg.setLocation(3, y);
			// give it the width of the library & the height it needs
			lpg.setSize(this.getWidth() - 6, lpg.getHeight());
			// notify the arrangement
			lpg.rearranged();
		
			y += lpg.getHeight() + 6;
		}
		
		// update for the scroll bar
		setPreferredSize(new Dimension(this.getPreferredSize().width, y));
		setSize(getWidth(), getPreferredSize().height);
		// NB: does not call setSize(Dimension), so no death recursion loop
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

	/**
	 * Used for drag & drop functionality
	 * @return the currently dragged ViewFunction, or null if no dragging
	 */
	public LibraryFunction getDragging() {
		for (LibraryPanelGroup lpg : groups) {
			LibraryFunction dragged = lpg.findDragging();
			if (dragged != null) {
				return dragged;
			}
		}
		
		return null;
	}

}