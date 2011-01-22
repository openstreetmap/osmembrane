package de.osmembrane.model.pipeline;

import de.osmembrane.model.xml.XMLEnumValue;
import de.osmembrane.tools.I18N;

/**
 * Implementation of {@link AbstractEnumValue}.
 * 
 * @author jakob_jarosch
 */
public class EnumValue extends AbstractEnumValue {

	private static final long serialVersionUID = 2011011821200001L;
	
	/**
	 * xml value of enum value.
	 */
	private XMLEnumValue xmlEnum;
	
	/**
	 * Creates a new EnumValue with a given {@link XMLEnumValue}.
	 * 
	 * @param xmlEnum which will be represented by {@link EnumValue}
	 */
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

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

}
