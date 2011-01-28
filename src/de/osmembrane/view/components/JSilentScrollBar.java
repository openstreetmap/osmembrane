package de.osmembrane.view.components;

import java.awt.Adjustable;

import javax.swing.JScrollBar;

/**
 * A JScrollBar with the ability to not continously call its own
 * attributeChanged-Listener after setValue().
 * 
 * <b> DOES NOT WORK YET! </b>
 * 
 * Kind of a dirty hack, but this seems to be a bug in Swing ({@link Adjustable}
 * specifies that an AdjustmentEvent should *NOT* be thrown!) and there is not
 * really an alternative.
 * 
 * @see {@link Adjustable#setValue(int)}, {@link JScrollBar#setValue(int)}
 * 
 * @author tobias_kuhn
 * 
 */
public class JSilentScrollBar extends JScrollBar {

	private static final long serialVersionUID = -8976816464093270469L;

	/**
	 * Whether to suppress the next unwanted setValue() property change fire and
	 * call the fire brigade.
	 */
	private boolean shouldIgnoreAdjustmentEvent;

	/**
	 * @see {@link JScrollBar#JScrollBar()}
	 */
	public JSilentScrollBar() {
		super();
		shouldIgnoreAdjustmentEvent = false;
	}

	/**
	 * @see {@link JScrollBar#JScrollBar(int)}
	 */
	public JSilentScrollBar(int orientation) {
		super(orientation);
		shouldIgnoreAdjustmentEvent = false;
	}

	/**
	 * @see {@link JScrollBar#JScrollBar(int, int, int, int, int)}
	 */
	public JSilentScrollBar(int orientation, int value, int extent, int min,
			int max) {
		super(orientation, value, extent, min, max);
		shouldIgnoreAdjustmentEvent = false;
	}

	/**
	 * @see JScrollBar#setValue(int), only silently
	 */
	public void setValueSilently(int value) {
		shouldIgnoreAdjustmentEvent = true;
		setValue(value);
		shouldIgnoreAdjustmentEvent = false;
	}

	public boolean shouldIgnoreAdjustmentEvent() {
		return this.shouldIgnoreAdjustmentEvent;
	}

}
