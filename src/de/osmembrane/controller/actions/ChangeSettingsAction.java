package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import de.osmembrane.resources.Constants;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;

public class ChangeSettingsAction extends AbstractAction {

	public ChangeSettingsAction() {
		putValue(Action.NAME, "Change Settings");
		putValue(Action.SMALL_ICON, new IconLoader("settings.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("settings.png",
				Size.NORMAL).get());
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		throw new UnsupportedOperationException();
	}
}