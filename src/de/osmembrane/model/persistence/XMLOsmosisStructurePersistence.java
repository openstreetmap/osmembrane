package de.osmembrane.model.persistence;

import java.io.File;
import java.net.URL;
import java.util.Observable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.osmembrane.model.persistence.FileException.Type;
import de.osmembrane.model.xml.XMLOsmosisStructure;

/**
 * Loads the osmosis structure from a given file and returns a
 * 
 * @author jakob_jarosch
 */
public class XMLOsmosisStructurePersistence extends AbstractPersistence {

	@Override
	public Object load(String file) throws FileException {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance("de.osmembrane.model.xml");

			/* XML-Datei mit Osmosis-Task-Beschreibungen einlesen */
			URL xmlTasksFile = getClass().getResource(file);

			if (xmlTasksFile == null) {
				throw new FileException(Type.NOT_FOUND);
			}

			Unmarshaller u = jc.createUnmarshaller();

			XMLOsmosisStructure otd = (XMLOsmosisStructure) u
					.unmarshal(xmlTasksFile);

			return otd;
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