package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.dialogs.ListSelectionDialog;

/**
 * Action to edit a parameter which is a list and therefore open the
 * {@link ListSelectionDialog}. Receives a {@link ContainingEvent}.
 * 
 * @author tobias_kuhn
 * 
 */
public class EditListPropertyAction extends AbstractAction {

	private static final long serialVersionUID = 3473005271005241300L;

	/**
	 * Creates a new {@link EditListPropertyAction}
	 */
	public EditListPropertyAction() {
		putValue(Action.NAME, "Edit List Property");
		putValue(Action.SMALL_ICON, new IconLoader("list_property_edit.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader(
				"list_property_edit.png", Size.NORMAL).get());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO implement
		throw new UnsupportedOperationException();
	}
}