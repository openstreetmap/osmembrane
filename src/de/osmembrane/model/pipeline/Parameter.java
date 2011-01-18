package de.osmembrane.model.pipeline;

import java.util.ArrayList;
import java.util.List;

import de.osmembrane.model.xml.XMLEnumValue;
import de.osmembrane.model.xml.XMLParameter;
import de.osmembrane.tools.I18N;

/**
 * Implemenation of {@link AbstractParameter}.
 * 
 * @author jakob_jarosch
 */
public class Parameter extends AbstractParameter {

	private static final long serialVersionUID = 2011011821310001L;

	/**
	 * The XML counterpart of the parameter.
	 */
	private XMLParameter xmlParam;
	
	/**
	 * The enum values for the parameter (if {@link Parameter#type} is {@link ParameterType#ENUM}).
	 */
	private List<EnumValue> enumValues = new ArrayList<EnumValue>();
	
	/**
	 * Type of the parameter.
	 */
	private ParameterType type;
	
	/**
	 * Constructor for a new {@link Parameter}.
	 * 
	 * @param xmlParam XML counterpart which should be represented by the {@link Parameter}.
	 */
	public Parameter(XMLParameter xmlParam) {
		this.xmlParam = xmlParam;
		
		this.type = ParameterType.parseString(xmlParam.getType());
		
		/* create enum values */
		for(XMLEnumValue xmlEnum : xmlParam.getEnumValue()) {
			enumValues.add(new EnumValue(xmlEnum));
		}
	}

	@Override
	public String getName() {
		return xmlParam.getValue();
	}

	@Override
	public String getFriendlyName() {
		return xmlParam.getFriendlyName();
	}
	
	@Override
	public String getDescription() {
		return I18N.getInstance().getDescription(xmlParam);
	}
	
	@Override
	public ParameterType getType() {
		return type;
	}

	@Override
	public AbstractEnumValue[] getEnumValue() {
		EnumValue[] values = new EnumValue[enumValues.size()];
		return enumValues.toArray(values);
	}

	@Override
	public String getDefaultValue() {
		return xmlParam.getDefaultValue();
	}

	@Override
	public String getValue() {
		return xmlParam.getValue();
	}

	@Override
	public boolean setValue(String value) {
		// TODO check the type match for parameter values
		xmlParam.setValue(value);
		
		setChanged();
		notifyObservers();
		
		return true;
	}

	@Override
	public boolean isRequired() {
		return xmlParam.isRequired();
	}

	@Override
	public boolean isDefaulXMLParameter() {
		return xmlParam.isDefaulXMLParameter();
	}

}
