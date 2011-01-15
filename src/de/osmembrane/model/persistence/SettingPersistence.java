package de.osmembrane.model.persistence;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;

import de.osmembrane.controller.exceptions.ExceptionSeverity;
import de.osmembrane.model.AbstractSettings;
import de.osmembrane.model.persistence.FileException.Type;
import de.osmembrane.model.ObserverObject;
import de.osmembrane.model.Settings;
import de.osmembrane.view.ViewRegistry;

/**
 * Saves the {@see AbstractSettings} in a file.
 * 
 * @author jakob_jarosch
 */
public class SettingPersistence extends AbstractPersistence {

	@Override
	public void save(String file, Object data) throws FileException {
		if (!(data instanceof Settings)) {
			ViewRegistry.showException(this.getClass(),
					ExceptionSeverity.UNEXPECTED_BEHAVIOR,
					new Exception("SettingsPersistence#save() got a wrong"
							+ " object, object is the following instance:\n"
							+ data.getClass()));
		}

		try {
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(bos);

			oos.writeObject(data);
			oos.close();
		} catch (IOException e) {
			throw new FileException(Type.NOT_WRITABLE, e);
		}
	}

	@Override
	public Object load(String file) throws FileException {
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(bis);
			
			AbstractSettings object = (AbstractSettings) ois.readObject();
			ois.close();
			
			return object;
		} catch (FileNotFoundException e) {
			throw new FileException(Type.NOT_FOUND, e);
		} catch (IOException e) {
			throw new FileException(Type.NOT_READABLE, e);
		} catch (ClassNotFoundException e) {
			throw new FileException(Type.WRONG_FORMAT, e);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Settings && arg instanceof ObserverObject) {
			String file = ((ObserverObject) arg).getString();
			Object data = ((ObserverObject) arg).getObject();

			try {
				save(file, data);
			} catch (FileException e) {
				/* forward the exception to the view */
				ViewRegistry.showException(this.getClass(),
						ExceptionSeverity.SAVE_SETTINGS_FAILED, e);
			}
		}
	}

}
