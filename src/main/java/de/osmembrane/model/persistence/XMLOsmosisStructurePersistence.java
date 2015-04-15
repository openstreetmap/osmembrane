/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */

package de.osmembrane.model.persistence;

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
    public Object load(URL file) throws FileException {
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance("de.osmembrane.model.xml");

            if (file == null) {
                throw new FileException(Type.NOT_FOUND);
            }

            Unmarshaller u = jc.createUnmarshaller();

            XMLOsmosisStructure otd = (XMLOsmosisStructure) u.unmarshal(file);

            return otd;
        } catch (JAXBException e) {
            throw new FileException(Type.WRONG_FORMAT, e);
        }
    }

    @Deprecated
    @Override
    public void save(URL file, Object data) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void update(Observable o, Object arg) {
        return;
    }
}
