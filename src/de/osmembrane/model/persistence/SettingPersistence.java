package de.osmembrane.model.persistence;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;

import de.osmembrane.model.AbstractSettings;
import de.osmembrane.model.ObserverObject;
import de.osmembrane.model.Settings;
import de.osmembrane.view.ExceptionType;
import de.osmembrane.view.ViewRegistry;

/**
 * Saves the {@see AbstractSettings} in a file.
 * 
 * @author jakob_jarosch
 */
public class SettingPersistence extends AbstractPersistence {

	@Override
	public void save(String file, Object data) throws IOException {
		if (!(data instanceof Settings)) {
			ViewRegistry.getInstance().pushException(
					this.getClass(),
					ExceptionType.ABNORMAL_BEHAVIOR,
					new Exception("SettingsPersistence#save() got a wrong"
							+ " object, object is the following instance:\n"
							+ data.getClass()));
		}
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		ObjectOutputStream oos = new ObjectOutputStream(bos);

		oos.writeObject(data);
		oos.close();
	}

	@Override
	public Object load(String file) throws IOException, ClassNotFoundException,
			ClassCastException {
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		ObjectInputStream ois = new ObjectInputStream(bis);

		AbstractSettings object = (AbstractSettings) ois.readObject();
		ois.close();

		return object;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Settings) {
			String file = ((ObserverObject) arg).getString();
			Object data = ((ObserverObject) arg).getObject();

			try {
				save(file, data);
			} catch (IOException e) {
				/* forward the exception to the view */
				ViewRegistry.getInstance().pushException(this.getClass(),
						ExceptionType.SAVE_SETTINGS_FAILED, e);
			}
		}
	}

}
