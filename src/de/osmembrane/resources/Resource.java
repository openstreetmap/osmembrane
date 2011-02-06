package de.osmembrane.resources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.swing.ImageIcon;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.tools.IconLoader;

public enum Resource {
	CURSOR_ICON("/de/osmembrane/resources/cursors/", null, false), PROGRAM_ICON(
			"/de/osmembrane/resources/images/icons/",
			new String[] { "resources/images/icons/" }, false), PRESET_ICON(
			"/de/osmembrane/resources/images/icons/presets/",
			new String[] { "resources/images/icons/presets/" }, true),

	OSMEMBRANE_XML("/de/osmembrane/resources/xml/osmosis-structure.xml",
			new String[] { "resources/xml/osmosis-structure.xml" }, true), PRESET_XML(
			"/de/osmembrane/resources/xml/defaultpresets.xml",
			new String[] { "resources/xml/defaultpresets.xml" }, true),

	QUICKSTART_IMAGE("/de/osmembrane/resources/images/quickstart/",
			new String[] { "resources/images/quickstart/" }, true);

	private String internalPath;
	private String[] externalPath;
	private boolean externalPrefered;
	private boolean silentLoad = false;

	Resource(String internalPath, String[] externalPath,
			boolean externalPrefered) {
		this.internalPath = internalPath;
		this.externalPath = externalPath;
		this.externalPrefered = externalPrefered;
	}

	public void setExternalPerfered(boolean externalPrefered) {
		this.externalPrefered = externalPrefered;
	}

	public void setLoadSilent(boolean silentLoad) {
		this.silentLoad = silentLoad;
	}

	public URL getURL() {
		return getURL("");
	}

	public URL getURL(String file) {
		URL url = null;
		url = selectURL(getLocalizedFile(file));
		if (url == null) {
			url = selectURL(file);
		}
		return url;
	}

	private URL selectURL(String file) {
		URL url = null;
		if (externalPrefered) {
			url = getExternalUrl(file);
			if (url == null) {
				url = this.getClass().getResource(internalPath + file);
			}
		} else {
			url = this.getClass().getResource(internalPath + file);
			if (url == null) {
				url = getExternalUrl(file);
			}
		}

		return url;
	}

	private String getLocalizedFile(String file) {
		Locale locale = (Locale) ModelProxy.getInstance().getSettings()
				.getValue(SettingType.ACTIVE_LANGUAGE);
		String newFile;
		int lastDot = file.lastIndexOf(".");

		if (lastDot > 0) {
			newFile = file.substring(0, lastDot);
			newFile += "." + locale.getLanguage() + file.substring(lastDot);
		} else {
			newFile = file;
		}

		return newFile;
	}

	private URL getExternalUrl(String file) {
		if (externalPath == null) {
			return null;
		}
		for (String externalPath : this.externalPath) {
			File fileObject = new File(externalPath + file);
			if (fileObject.exists()) {
				try {
					return fileObject.toURI().toURL();
				} catch (MalformedURLException e) {
					/* try silently another one */
				}
			}
		}
		return null;
	}

	public ImageIcon getImageIcon(IconLoader.Size size) {
		return getImageIcon("", size);
	}

	public ImageIcon getImageIcon(String filename, IconLoader.Size size) {
		return new IconLoader(getURL(filename), size, silentLoad).get();
	}
}
