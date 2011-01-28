package de.osmembrane.model.pipeline;

/**
 * Type of a {@link AbstractParameter}.
 * 
 * @author jakob_jarosch
 */
public enum ParameterType {

	/**
	 * Normal {@link Integer}.
	 */
	INT("Integer"),

	/**
	 * Normal {@link String}.
	 */
	STRING("String"),

	/**
	 * Normal {@link Boolean}.
	 */
	BOOLEAN("Boolean"),

	/**
	 * Enumeration ({@see AbstractEnumValue}).
	 */
	ENUM("Enumeration"),

	/**
	 * Path to a file (represented in a {@link String}).
	 */
	FILENAME("Filename"),

	/**
	 * Path to a directory (represented in a {@link String}).
	 */
	DIRECTORY("Directory"),

	/**
	 * Address to a website.
	 */
	URI("URI"),

	/**
	 * The type represents an instant (date and time).
	 */
	INSTANT("Instant"),

	/**
	 * BoundingBox Value.
	 * The String should be in the following format: double,double,double,double
	 * first double is left, second right, third top and fourth is bottom
	 */
	BBOX("BoundingBox");

	/**
	 * Friendly name of the type.
	 */
	private String friendlyName;

	/**
	 * Creates a new {@link ParameterType}.
	 * 
	 * @param friendlyName
	 *            human readable version of the type
	 */
	private ParameterType(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	/**
	 * Returns a human readable name of the parameter type.
	 * 
	 * @return human readable name
	 */
	public String getFriendlyName() {
		return friendlyName;
	}

	/**
	 * Creates a new {@link ParameterType} from a {@link String}.
	 * 
	 * @param type
	 *            String which represents a {@link ParameterType}
	 * @return {@link ParameterType} if the type could be parsed, otherwise NULL
	 */
	public static ParameterType parseString(String type) {
		for (ParameterType paramType : ParameterType.values()) {
			if (type.toLowerCase().equals(paramType.toString().toLowerCase())) {
				return paramType;
			}
		}

		return null;
	}
}
