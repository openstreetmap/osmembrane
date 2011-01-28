package de.osmembrane.resources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import de.osmembrane.tools.IconLoader;

public enum Resource {
	CURSOR_ICON("/de/osmembrane/resources/cursors/", null, false),
	PROGRAM_ICON("/de/osmembrane/resources/images/icons/", new String[]{"resources/images/icons/"}, true),
	PRESET_ICON("/de/osmembrane/resources/images/icons/presets/", new String[]{"resources/images/icons/presets/"}, true);
	
	
	private String internalPath;
	private String[] externalPath;
	private boolean externalPrefered;
	private boolean silentLoad = false;
	
	Resource(String internalPath, String[] externalPath, boolean externalPrefered) {
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
		URL url;
		if(externalPrefered) {
			url = getExternalUrl(file);
			if(url == null) {
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
	
	private URL getExternalUrl(String file) {
		if(externalPath == null) {
			return null;
		}
		for(String externalPath : this.externalPath) {
			File fileObject = new File(externalPath + file);
			if(fileObject.exists()) {
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
