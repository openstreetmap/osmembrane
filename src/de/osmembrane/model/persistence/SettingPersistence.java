package de.osmembrane.model.persistence;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Map;
import java.util.Observable;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.persistence.FileException.Type;
import de.osmembrane.model.settings.AbstractSettings;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.resources.Constants;
import de.osmembrane.tools.I18N;

/**
 * Saves the {@see AbstractSettings} in a file.
 * 
 * @author jakob_jarosch
 */
public class SettingPersistence extends AbstractPersistence {

	@Override
	public void save(URL file, Object data) throws FileException {
		if (!(data instanceof Map)) {
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.UNEXPECTED_BEHAVIOR,
					"SettingPersistence#save() got a wrong"
							+ " object, object is the following instance:\n"
							+ data.getClass()));
		}

		try {
			FileOutputStream fos = new FileOutputStream(file.toString().replace("file:",""));
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(bos);

			oos.writeObject(data);
			oos.close();
		} catch (IOException e) {
			throw new FileException(Type.NOT_WRITABLE, e);
		}
	}

	@Override
	public Object load(URL file) throws FileException {
		try {
			FileInputStream fis = new FileInputStream(file.toString().replace("file:", ""));
			BufferedInputStream bis = new BufferedInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(bis);

			@SuppressWarnings("unchecked")
			Map<SettingType, Object> object = (Map<SettingType, Object>) ois.readObject();
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
		if (arg instanceof Map) {
			try {
				save(Constants.DEFAULT_SETTINGS_FILE, arg);
			} catch (Exception e) {
				/* forward the exception to the view */
				Application
						.handleException(new ControlledException(this,
								ExceptionSeverity.WARNING, e,
								I18N.getInstance().getString(
										"Exception.AutosaveSettingsFailed")));
			}
		}
	}

}
