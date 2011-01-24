package de.osmembrane.model.pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.osmembrane.model.Identifier;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.PersistenceFactory;
import de.osmembrane.model.persistence.XMLOsmosisStructurePersistence;
import de.osmembrane.model.xml.XMLEnumValue;
import de.osmembrane.model.xml.XMLFunction;
import de.osmembrane.model.xml.XMLFunctionGroup;
import de.osmembrane.model.xml.XMLOsmosisStructure;
import de.osmembrane.model.xml.XMLParameter;
import de.osmembrane.model.xml.XMLPipe;
import de.osmembrane.model.xml.XMLTask;

/**
 * Implementation of {@link AbstractFunctionPrototype}.
 * 
 * @author jakob_jarosch
 */
public class FunctionPrototype extends AbstractFunctionPrototype {

	private XMLOsmosisStructure xmlStruct = null;
	private List<FunctionGroup> functionGroups = new ArrayList<FunctionGroup>();

	private Map<Identifier, AbstractFunctionGroup> functionGroupMap = new HashMap<Identifier, AbstractFunctionGroup>();
	private Map<AbstractFunctionGroup, Identifier> functionGroupIdentifierMap = new HashMap<AbstractFunctionGroup, Identifier>();

	private Map<Identifier, XMLFunctionGroup> xmlFunctionGroupMap = new HashMap<Identifier, XMLFunctionGroup>();
	private Map<Identifier, XMLFunction> xmlFunctionMap = new HashMap<Identifier, XMLFunction>();
	private Map<Identifier, XMLTask> xmlTaskMap = new HashMap<Identifier, XMLTask>();
	private Map<Identifier, XMLPipe> xmlPipeMap = new HashMap<Identifier, XMLPipe>();
	private Map<Identifier, XMLParameter> xmlParameterMap = new HashMap<Identifier, XMLParameter>();
	private Map<Identifier, XMLEnumValue> xmlEnumValueMap = new HashMap<Identifier, XMLEnumValue>();

	private Map<Object, Identifier> identifiers = new HashMap<Object, Identifier>();

	@Override
	public void initiate(String xmlFilename) {

		try {
			xmlStruct = (XMLOsmosisStructure) PersistenceFactory.getInstance()
					.getPersistence(XMLOsmosisStructurePersistence.class)
					.load(xmlFilename);

			createMaps();
		} catch (FileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (XMLFunctionGroup group : xmlStruct.getFunctionGroup()) {
			FunctionGroup fg = new FunctionGroup(group);
			functionGroups.add(fg);
			
			/* add to the maps */
			Identifier identifier = identifiers.get(group);
			functionGroupIdentifierMap.put(fg, identifier);
		}
	}

	private void createMaps() {
		for (XMLFunctionGroup group : xmlStruct.getFunctionGroup()) {
			Identifier groupIdentifier = new Identifier(group.getId());
			xmlFunctionGroupMap.put(groupIdentifier, group);
			identifiers.put(group, groupIdentifier);

			for (XMLFunction function : group.getFunction()) {
				Identifier functionIdentifier = new Identifier(groupIdentifier
						+ "|" + function.getId());
				xmlFunctionMap.put(functionIdentifier, function);
				identifiers.put(function, functionIdentifier);

				for (XMLTask task : function.getTask()) {
					Identifier taskIdentifier = new Identifier(
							functionIdentifier + "|" + task.getName());
					xmlTaskMap.put(taskIdentifier, task);
					identifiers.put(task, taskIdentifier);

					for (XMLPipe inPipe : task.getInputPipe()) {
						Identifier pipeIdentifier = new Identifier(
								taskIdentifier + "|in|" + inPipe.getType()
										+ inPipe.getIndex());
						xmlPipeMap.put(pipeIdentifier, inPipe);
						identifiers.put(inPipe, pipeIdentifier);
					}
					for (XMLPipe outPipe : task.getOutputPipe()) {
						Identifier pipeIdentifier = new Identifier(
								taskIdentifier + "|out|" + outPipe.getType()
										+ outPipe.getIndex());
						xmlPipeMap.put(pipeIdentifier, outPipe);
						identifiers.put(outPipe, pipeIdentifier);
					}

					for (XMLParameter param : task.getParameter()) {
						Identifier paramIdentifier = new Identifier(
								taskIdentifier + "|" + param.getName());
						xmlParameterMap.put(paramIdentifier, param);
						identifiers.put(param, paramIdentifier);

						for (XMLEnumValue enumValue : param.getEnumValue()) {
							Identifier enumValueIdentifier = new Identifier(
									paramIdentifier + "|enumValue");
							xmlEnumValueMap.put(enumValueIdentifier, enumValue);
							identifiers.put(enumValue, enumValueIdentifier);
						}
					}
				}
			}
		}
	}

	@Override
	public AbstractFunctionGroup[] getFunctionGroups() {
		FunctionGroup[] groups = new FunctionGroup[functionGroups.size()];
		groups = functionGroups.toArray(groups);
		return (FunctionGroup[]) groups;
	}

	@Override
	protected Identifier pushFGToMap(AbstractFunctionGroup fg, XMLFunctionGroup xmlFG) {
		Identifier ident = new Identifier(xmlFG.getId());
		functionGroupMap.put(ident, fg);
		identifiers.put(fg, ident);
		return ident;
	}
	
	@Override
	protected AbstractFunctionGroup getMatchingFunctionGroup(
			Identifier identifier) {
		return functionGroupMap.get(identifier);
	}
	
	@Override
	protected Identifier getMatchingFunctionGroupIdentifier(
			AbstractFunctionGroup identifier) {
		return identifiers.get(identifier);
	}

	@Override
	protected XMLFunction getMatchingXMLFunction(Identifier identifier) {
		return xmlFunctionMap.get(identifier);
	}

	@Override
	protected Identifier getMatchingXMLFunctionIdentifier(XMLFunction identifier) {
		String str = (identifiers.get(identifier) == null ? "never return null! function" : "");
		if (str != "") { System.out.println(str); }
		return identifiers.get(identifier);
	}

	@Override
	protected XMLTask getMatchingXMLTask(Identifier identifier) {
		return xmlTaskMap.get(identifier);
	}

	@Override
	protected Identifier getMatchingXMLTaskIdentifier(XMLTask identifier) {
		String str = (identifiers.get(identifier) == null ? "never return null! task" : "");
		if (str != "") { System.out.println(str); }
		return identifiers.get(identifier);
	}

	@Override
	protected XMLPipe getMatchingXMLPipe(Identifier identifier) {
		return xmlPipeMap.get(identifier);
	}

	@Override
	protected Identifier getMatchingXMLPipeIdentifier(XMLPipe identifier) {
		String str = (identifiers.get(identifier) == null ? "never return null! pipe" : "");
		if (str != "") { System.out.println(str); }
		return identifiers.get(identifier);
	}

	@Override
	protected XMLParameter getMatchingXMLParameter(Identifier identifier) {
		return xmlParameterMap.get(identifier);
	}

	@Override
	protected Identifier getMatchingXMLParameterIdentifier(
			XMLParameter identifier) {
		String str = (identifiers.get(identifier) == null ? "never return null! param" : "");
		if (str != "") { System.out.println(str); }
		return identifiers.get(identifier);
	}

	@Override
	protected XMLEnumValue getMatchingXMLEnumValue(Identifier identifier) {
		return xmlEnumValueMap.get(identifier);
	}

	@Override
	protected Identifier getMatchingXMLEnumValueIdentifier(
			XMLEnumValue identifier) {
		String str = (identifiers.get(identifier) == null ? "never return null! enumValue" : "");
		if (str != "") { System.out.println(str); }
		return identifiers.get(identifier);
	}

}