package de.osmembrane.model.settings;

public class UnparsableFormatException extends Exception {
	
	private SettingType type;

	public UnparsableFormatException(SettingType type) {
		this.type = type;
	}
	
	public SettingType getType() {
		return type;
	}
}
