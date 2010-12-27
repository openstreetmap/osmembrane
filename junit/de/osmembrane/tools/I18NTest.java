package de.osmembrane.tools;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import de.osmembrane.model.xml.XMLOsmosisStructure;
import de.osmembrane.model.xml.XMLHasDescription.Description;

/**
 * Simple test for {@link I18N}.
 * 
 * @author jakob_jarosch
 */
public class I18NTest {

	private boolean updated = false;

	/**
	 * Test the geString() method.
	 */
	@Test
	public void testGetString() {
		String testString;
		String testStringEn;

		Observer observer = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				updated = true;
			}
		};

		I18N.getInstance().setLocale(Locale.GERMAN);
		I18N.getInstance().addObserver(observer);

		testString = I18N.getInstance().getString("testString");
		assertEquals("Hallo, das ist OSMembrane v1.0", testString);

		testStringEn = I18N.getInstance().getString("testStringEn");
		assertEquals("I'm only available in English.", testStringEn);

		I18N.getInstance().setLocale(Locale.ENGLISH);
		assertTrue("Observer not updated", updated);

		testString = I18N.getInstance().getString("testString");
		assertEquals("Hello, this is OSMembrane v1.0", testString);

	}

	/**
	 * Test the getDescription() method.
	 */
	@Test
	public void testGetDescription() {
		String descriptionEn = null;
		String descriptionDe = null;
		String structureFile = "src/de/osmembrane/resources/xml/osmosis-structure.xml";

		I18N.getInstance().setLocale(Locale.GERMAN);
		try {
			JAXBContext jc;
			jc = JAXBContext.newInstance("de.osmembrane.model.xml");

			/* XML-Datei mit Osmosis-Task-Beschreibungen einlesen */
			File xmlTasksFile = new File(structureFile);
			Unmarshaller u = jc.createUnmarshaller();
			XMLOsmosisStructure struct = (XMLOsmosisStructure) u
					.unmarshal(xmlTasksFile);

			/* read the description in english format (german is not available) */
			descriptionEn = I18N.getInstance().getDescription(
					struct.getFunctionGroup().get(0).getFunction().get(0)
							.getTask().get(0));

			/* create new german description */
			Description descr = new Description();
			descr.setLang("de");
			descr.setValue("Liest die akutellen Inhalte einer OSM XML Datei aus.");
			struct.getFunctionGroup().get(0).getFunction().get(0).getTask()
					.get(0).getDescription().add(descr);

			/* read the description now in german format */
			descriptionDe = I18N.getInstance().getDescription(
					struct.getFunctionGroup().get(0).getFunction().get(0)
							.getTask().get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals("Reads the current contents of an OSM XML file.",
				descriptionEn);
		assertEquals("Liest die akutellen Inhalte einer OSM XML Datei aus.",
				descriptionDe);
	}

}
