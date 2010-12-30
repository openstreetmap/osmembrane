package de.osmembrane.model.persistence;

import java.io.File;
import java.util.Observable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.osmembrane.model.xml.XMLOsmosisStructure;

/**
 * Loads the osmosis structure from a given file and returns a
 * 
 * @author jakob_jarosch
 */
public class XMLOsmosisStructurePersistence extends AbstractPersistence {

	@Deprecated
	public void save(String file, Object data) {
		throw new UnsupportedOperationException();
	}

	public Object load(String file) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance("de.osmembrane.model.xml");

			/* XML-Datei mit Osmosis-Task-Beschreibungen einlesen */
			File xmlTasksFile = new File(file); //
			Unmarshaller u = jc.createUnmarshaller();
			XMLOsmosisStructure otd = (XMLOsmosisStructure) u
					.unmarshal(xmlTasksFile);
			
			return otd;
		} catch (JAXBException e) {
			throw new ClassCastException("XML-Importing won't work, like excepted");
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		/* do nothing, never used */
	}
}