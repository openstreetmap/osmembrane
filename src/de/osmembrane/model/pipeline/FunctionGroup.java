package de.osmembrane.model.pipeline;

import java.util.ArrayList;
import java.util.List;

import de.osmembrane.model.pipeline.Function;
import de.osmembrane.model.xml.XMLFunction;
import de.osmembrane.model.xml.XMLFunctionGroup;
import de.osmembrane.tools.I18N;

/**
 * This represents the implementation of a simple FunctionGroup for the XML-Functions.
 * 
 * @author jakob_jarosch
 */
public class FunctionGroup extends AbstractFunctionGroup {

	private static final long serialVersionUID = 2010123022400001L;
	
	private XMLFunctionGroup xmlGroup;
	private List<Function> functions = new ArrayList<Function>();

	private final String comparator;

	/**
	 * Creates a new FunctionGroup with a given XMLFunctionGroup.
	 * 
	 * @param xmlFunction XMLFunctionGroup belongs to this FunctionGroup
	 */
	public FunctionGroup(XMLFunctionGroup xmlGroup) {
		this.xmlGroup = xmlGroup;
		for (XMLFunction xmlFunction : xmlGroup.getFunction()) {
			functions.add(new Function(this, xmlFunction));
		}

		comparator = this.xmlGroup.getId();
	}

	@Override
	public String getId() {
		return xmlGroup.getId();
	}

	@Override
	public String getFriendlyName() {
		return xmlGroup.getFriendlyName();
	}

	@Override
	public String getDescription() {
		return I18N.getInstance().getDescription(xmlGroup);
	}

	@Override
	public AbstractFunction[] getFunctions() {
		Function[] returnFunctions = new Function[functions.size()]; 
		returnFunctions = functions.toArray(returnFunctions);
		return (AbstractFunction[]) returnFunctions;
	}

	@Override
	public boolean same(AbstractFunctionGroup group) {
		if (group instanceof FunctionGroup) {
			FunctionGroup oFG = (FunctionGroup) group;
			return (oFG.getComparatorString().equals(this.getComparatorString()));
		}
		
		return false;
	}

	protected String getComparatorString() {
		return comparator;
	}
}