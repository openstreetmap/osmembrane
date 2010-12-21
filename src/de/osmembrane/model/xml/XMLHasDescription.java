package de.osmembrane.model.xml;

import java.util.List;
import java.util.ArrayList;

public abstract class XMLHasDescription {
	protected List description;
	public ArrayList<de.osmembrane.model.xml.XMLHasDescription.XMLDescription> localized_description = new ArrayList<XMLDescription>();

	public List getDescription() {
		throw new UnsupportedOperationException();
	}
	public class XMLDescription {
		protected java.lang.String value;
		protected java.lang.String lang;
		public XMLHasDescription localized_description;

		public java.lang.String getValue() {
			throw new UnsupportedOperationException();
		}

		public void setValue(java.lang.String value) {
			throw new UnsupportedOperationException();
		}

		public java.lang.String getLang() {
			throw new UnsupportedOperationException();
		}

		public void setLang(java.lang.String value) {
			throw new UnsupportedOperationException();
		}
	}
}