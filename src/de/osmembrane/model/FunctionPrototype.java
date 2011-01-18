package de.osmembrane.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.PersistenceFactory;
import de.osmembrane.model.persistence.XMLOsmosisStructurePersistence;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;
import de.osmembrane.model.pipeline.FunctionGroup;
import de.osmembrane.model.xml.XMLFunctionGroup;
import de.osmembrane.model.xml.XMLOsmosisStructure;

/**
 * Implementation of {@link AbstractFunctionPrototype}.
 * 
 * @author jakob_jarosch
 */
public class FunctionPrototype extends AbstractFunctionPrototype {

	private XMLOsmosisStructure xmlStruct = null;
	private List<FunctionGroup> functionGroups = new ArrayList<FunctionGroup>();

	@Override
	public void initiate(String xmlFilename) {

		try {
			xmlStruct = (XMLOsmosisStructure) PersistenceFactory.getInstance()
					.getPersistence(XMLOsmosisStructurePersistence.class)
					.load(xmlFilename);
		} catch (FileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (XMLFunctionGroup group : xmlStruct.getFunctionGroup()) {
			functionGroups.add(new FunctionGroup(group));
		}
	}

	@Override
	public AbstractFunctionGroup[] getFunctionGroups() {
		FunctionGroup[] groups = new FunctionGroup[functionGroups.size()]; 
		groups = functionGroups.toArray(groups);
		return (FunctionGroup[]) deepCopy(groups);
	}

	@Override
	public AbstractFunctionGroup getFunctionGroup(AbstractFunctionGroup group) {
		for(FunctionGroup internalGroup : functionGroups) {
			if(internalGroup.same(group)) {
				return (AbstractFunctionGroup) deepCopy(internalGroup);
			}
		}
		
		return null;
	}

	@Override
	public AbstractFunction getFunction(AbstractFunction function) {
		/* search recursively through the groups to find the function */
		for(FunctionGroup internalGroup : functionGroups) {
			if(internalGroup.same(function.getParent())) {
				for(AbstractFunction internalFunction : internalGroup.getFunctions()) {
					if(internalFunction.same(function)) {
						return (AbstractFunction) deepCopy(internalFunction);
					}
				}
			}
		}
		
		return null;
	}

	@Override
	public Object duplicate(Serializable object) {
		return deepCopy(object);
	}
	
	/**
	 * Creates a full copy of a given element (group,function,task).
	 * 
	 * @param object which should be copied
	 * @return copy of the given element
	 */
	private Object deepCopy(Serializable object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}