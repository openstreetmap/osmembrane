package de.osmembrane.model.statusbar;

public class StatusbarObserverObject {
	
	private StatusbarEntry entry;

	public StatusbarObserverObject(StatusbarEntry entry) {
		this.entry = entry;
	}
	
	public StatusbarEntry getEntry() {
		return entry;
	}
}
