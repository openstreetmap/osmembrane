package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to edit a parameter which is a file path and therefore open the file
 * path dialog. Receives a {@link ContainingEvent}.
 * 
 * @author tobias_kuhn
 * 
 */
public class EditFilePropertyAction extends AbstractAction {

	private static final long serialVersionUID = 1481319711002406388L;

	/**
	 * Creates a new {@link EditFilePropertyAction}
	 */
	public EditFilePropertyAction() {
		putValue(Action.NAME, "Edit File Property");
		putValue(Action.SMALL_ICON, new IconLoader("file_property_edit.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader(
				"file_property_edit.png", Size.NORMAL).get());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO implement
		throw new UnsupportedOperationException();
	}
}