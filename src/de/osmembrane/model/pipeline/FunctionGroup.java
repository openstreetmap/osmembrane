package de.osmembrane.model.pipeline;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.osmembrane.model.pipeline.Function;
import de.osmembrane.model.xml.XMLFunction;
import de.osmembrane.model.xml.XMLFunctionGroup;
import de.osmembrane.resources.Constants;
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
		/* fallback if friendlyName is not available */
		if (xmlGroup.getFriendlyName() == null) {
			return getId();
		}
		
		return xmlGroup.getFriendlyName();
	}

	@Override
	public String getDescription() {
		return I18N.getInstance().getDescription(xmlGroup);
	}
	
	@Override
	public Color getColor() {
		String colorString = xmlGroup.getColor();
		
		if(colorString == null) {
			return Constants.DEFAULT_FUNCTIONGROUP_COLOR;
		}
		
		String[] color = colorString.split(",");
		
		if(color.length != 3) {
			return Constants.DEFAULT_FUNCTIONGROUP_COLOR;
		}
		
		int r, g, b;
		try {
			r = Integer.parseInt(color[0].trim());
			g = Integer.parseInt(color[1].trim());
			b = Integer.parseInt(color[2].trim());
		} catch (NumberFormatException e) {
			return Constants.DEFAULT_FUNCTIONGROUP_COLOR;
		}
		
		if ((r < 0 || r > 255) && (g < 0 || g > 255) && (b < 0 || b > 255)) {
			return Constants.DEFAULT_FUNCTIONGROUP_COLOR;
		}
		
		return new Color(r, g, b);
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
	
	@Override
	public FunctionGroup copy(CopyType type) {
		FunctionGroup newFG = new FunctionGroup(this.xmlGroup);
		
		/* copy the functions */
		newFG.functions.clear();
		for(Function function : functions) {
			Function newFunction = function.copy(type, newFG);
			newFG.functions.add(newFunction);
		}
		
		return newFG;
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}
}