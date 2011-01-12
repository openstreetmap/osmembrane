package de.osmembrane.view.panels;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

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
	private final static double expandingDuration = 400.0;

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
		groups = new ArrayList<LibraryPanelGroup>();

		// display
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	/**
	 * Adds a new {@link LibraryPanelGroup} to select from
	 * 
	 * @param lpg
	 */
	public void addGroup(LibraryPanelGroup lpg) {
		groups.add(lpg);
		lpg.setId(groups.size() - 1);
		add(lpg);
	}

	/**
	 * Notify the panel that a group has been clicked
	 * 
	 * @param group
	 */
	public void groupClicked(int group) {
		LibraryPanelGroup lpg;

		// quick finish old animation
		if (expanding != -1) {
			lpg = groups.get(expanding);
			lpg.setHeight(lpg.getFullHeight());
		}
		if (contracting != -1) {
			lpg = groups.get(contracting);
			lpg.setHeight(0);
		}

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

		Runnable animation = new Runnable() {

			@Override
			public void run() {

				LibraryPanelGroup lpg;
				double timeFactor = (System.currentTimeMillis() - expandingStart)
						/ expandingDuration;

				while (timeFactor < 1) {
					timeFactor = (System.currentTimeMillis() - expandingStart)
							/ expandingDuration;

					// actual animation
					lpg = groups.get(expanding);
					lpg.setHeight(getExpandingHeight(timeFactor,
							lpg.getFullHeight(), true));
					lpg = groups.get(contracting);
					lpg.setHeight(getExpandingHeight(timeFactor,
							lpg.getFullHeight(), false));

					// might be inaccurate by several factors, but will still
					// guarantee a fluent animation
					try {
						Thread.sleep(10L);
					} catch (InterruptedException e) {
						// don't really care, we just redraw
					}
				}

				// animation done
				lpg = groups.get(expanding);
				lpg.setHeight(lpg.getFullHeight());
				lpg = groups.get(contracting);
				lpg.setHeight(0);

				expanded = expanding;
				expanding = -1;
				contracting = -1;

			}
		};

		new Thread(animation).start();
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
		if (expanding) {
			return (int) (timeFactor * originalHeight);
		} else {
			return (int) ((1.0 - timeFactor) * originalHeight);
		}
	}
}