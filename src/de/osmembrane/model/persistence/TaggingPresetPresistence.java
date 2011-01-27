package de.osmembrane.model.persistence;

import java.net.URL;
import java.util.Observable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.openstreetmap.josm.tagging_preset_1.Root;
import de.osmembrane.model.persistence.FileException.Type;

/**
 * Loads the osmosis structure from a given file and returns a
 * 
 * @author jakob_jarosch
 */
public class TaggingPresetPresistence extends AbstractPersistence {

	@Override
	public Object load(String file) throws FileException {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance("de.openstreetmap.josm.tagging_preset_1");

			/* XML-Datei mit Osmosis-Task-Beschreibungen einlesen */
			URL xmlTasksFile = getClass().getResource(file);

			if (xmlTasksFile == null) {
				throw new FileException(Type.NOT_FOUND);
			}

			Unmarshaller u = jc.createUnmarshaller();

			@SuppressWarnings("unchecked")
			JAXBElement<Root> root = (JAXBElement<Root>) u
					.unmarshal(xmlTasksFile);

			return root.getValue();
		} catch (JAXBException e) {
			throw new FileException(Type.WRONG_FORMAT, e);
		}
	}

	@Deprecated
	@Override
	public void save(String file, Object data) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void update(Observable o, Object arg) {
		throw new UnsupportedOperationException();
	}
}