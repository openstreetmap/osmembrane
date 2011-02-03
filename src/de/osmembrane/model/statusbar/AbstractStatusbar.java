package de.osmembrane.model.statusbar;

import java.util.Observable;
import java.util.Observer;

/**
 * Statusbar-Model, for managing all entries in status bar of the view.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractStatusbar extends Observable implements Observer {

	/**
	 * Returns all {@link StatusbarEntry}s, the first one is the most important
	 * one and should be displayed always.
	 * 
	 * @return all {@link StatusbarEntry}s
	 */
	public abstract StatusbarEntry[] getStatusbarEntries();

	/**
	 * Adds a {@link StatusbarEntry} to the model.
	 * 
	 * @param entry entry which should be added
	 */
	public abstract void addStatusbarEntry(StatusbarEntry entry);
	
	/**
	 * Removes a {@link StatusbarEntry}.
	 * 
	 * @param entry {@link StatusbarEntry} which should be removed
	 * @return true if the entry was removed, otherwise false
	 */
	public abstract boolean removeStatusbarEntry(StatusbarEntry entry);

}
