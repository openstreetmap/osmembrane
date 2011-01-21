package de.osmembrane.model.persistence;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Observable;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.persistence.FileException.Type;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.PipelineObserverObject;
import de.osmembrane.tools.I18N;

/**
 * Saves the OSMembrane Pipeline into a file.
 * 
 * @author jakob_jarosch
 */
public class OSMembranePersistence extends AbstractPersistence {

	@Override
	public void save(String file, Object data) throws FileException {
		if (!(data instanceof List<?>)) {
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.UNEXPECTED_BEHAVIOR,
					"OSMembranePersistence#save() got a wrong"
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
			
			@SuppressWarnings("unchecked")
			List<AbstractFunction> object = (List<AbstractFunction>) ois.readObject();
			
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
		if (arg instanceof PipelineObserverObject) {
			try {
				((PipelineObserverObject) arg).getPipeline().backupPipeline();
			} catch (FileException e) {
				/* forward the exception to the view */
				Application
						.handleException(new ControlledException(this,
								ExceptionSeverity.WARNING, e,
								I18N.getInstance().getString(
										"Exception.AutosavePipelineFailed")));

			}
		}
	}
}