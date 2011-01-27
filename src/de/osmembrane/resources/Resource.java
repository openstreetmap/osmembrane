package de.osmembrane.resources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import de.osmembrane.tools.IconLoader;

public enum Resource {
	CURSOR_ICON("/de/osmembrane/resources/cursors/", null, false),
	PROGRAM_ICON("/de/osmembrane/resources/images/icons/", "resources/images/icons/", true),
	PRESET_ICON("/de/osmembrane/resources/images/icons/presets/", "resources/images/icons/presets/", true);
	
	
	private String internalPath;
	private String externalPath;
	private boolean externalPrefered;
	private boolean silentLoad = false;
	
	Resource(String internalPath, String externalPath, boolean externalPrefered) {
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
		try {
			if(externalPrefered) {
				File fileObject = new File(externalPath + file);
				if(!fileObject.exists()) {
					throw new MalformedURLException();
				} else {
					url = fileObject.toURI().toURL();
				}
			} else {
				url = this.getClass().getResource(internalPath + file);
				if (url == null) {
					File fileObject = new File(externalPath + file);
					if(fileObject.exists()) {
						url = fileObject.toURI().toURL();
					}
				}
			}
		} catch(MalformedURLException e) {
			url = this.getClass().getResource(internalPath + file);
		}
		
		return url;
	}
	
	public ImageIcon getImageIcon(IconLoader.Size size) {
		return getImageIcon("", size);
	}
	
	public ImageIcon getImageIcon(String filename, IconLoader.Size size) {
		System.out.println(getURL(filename));
		return new IconLoader(getURL(filename), size, silentLoad).get();
	}
}
