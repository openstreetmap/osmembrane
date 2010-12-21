package de.osmembrane.model.xml;

import java.util.List;
import java.util.Vector;

public abstract class XMLHasDescription {
	protected List _description;
	public Vector<de.osmembrane.model.xml.XMLHasDescription.XMLDescription> _localized_description = new Vector<XMLDescription>();

	public List getDescription() {
		throw new UnsupportedOperationException();
	}
	public class XMLDescription {
		protected java.lang.String _value;
		protected java.lang.String _lang;
		public XMLHasDescription _localized_description;

		public java.lang.String getValue() {
			throw new UnsupportedOperationException();
		}

		public void setValue(java.lang.String aValue) {
			throw new UnsupportedOperationException();
		}

		public java.lang.String getLang() {
			throw new UnsupportedOperationException();
		}

		public void setLang(java.lang.String aValue) {
			throw new UnsupportedOperationException();
		}
	}
}