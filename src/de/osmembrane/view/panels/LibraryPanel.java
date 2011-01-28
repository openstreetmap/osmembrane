package de.osmembrane.view.panels;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import de.osmembrane.Application;
import de.osmembrane.model.pipeline.FunctionGroup;

/**
 * The function library panel that lists all the {@link FunctionGroup}s as
 * {@link LibraryPanelGroup}s in a register style and gives you the ability to
 * drag & drop them onto the {@link PipelinePanel}.
 * 
 * Would have been fancy to be generic, but was not possible due to time constraints.
 * Note smelly calls to getMainFrameByPass().getPipeline(). 
 * 
 * @see Spezifikation.pdf, chapter 2.1.4
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
	 * Initializes a new {@link LibraryPanel}
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

					rearrange(true);

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
		if (setSize) {
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