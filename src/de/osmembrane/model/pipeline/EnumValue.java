package de.osmembrane.model.pipeline;

import de.osmembrane.model.xml.XMLEnumValue;
import de.osmembrane.tools.I18N;

public class EnumValue extends AbstractEnumValue {

	private XMLEnumValue xmlEnum;
	
	public EnumValue(XMLEnumValue xmlEnum) {
		this.xmlEnum = xmlEnum;
	}

	@Override
	public String getDescription() {
		return I18N.getInstance().getDescription(xmlEnum);
	}

	@Override
	public String getFriendlyName() {
		return xmlEnum.getFriendlyName();
	}

	@Override
	public String getValue() {
		return xmlEnum.getValue();
	}

}
