package de.osmembrane.model.pipeline;

import java.util.ArrayList;
import java.util.List;

import de.osmembrane.model.xml.XMLEnumValue;
import de.osmembrane.model.xml.XMLParameter;
import de.osmembrane.tools.I18N;

public class Parameter extends AbstractParameter {

	private XMLParameter xmlParam;
	
	private List<EnumValue> enumValues = new ArrayList<EnumValue>();
	
	private ParameterType type;
	
	public Parameter(XMLParameter xmlParam) {
		this.xmlParam = xmlParam;
		
		this.type = ParameterType.parseString(xmlParam.getType());
		
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
		// TODO check Type-Match
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
