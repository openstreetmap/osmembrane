/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */

package de.osmembrane.model.pipeline;

import java.util.regex.Pattern;

/**
 * Type of a {@link AbstractParameter}.
 * 
 * @author jakob_jarosch
 */
public enum ParameterType {

    /**
     * Normal {@link Integer}.
     */
    INT("Integer", "^-?[0-9]+$", "^$"),

    /**
     * Normal {@link String}.
     */
    STRING("String", "^.+$", "^$"),

    /**
     * Normal {@link Boolean}.
     */
    BOOLEAN("Boolean", "^(true|false|yes|no)$", "^$"),

    /**
     * Enumeration ({@link AbstractEnumValue}).
     * 
     * Important! ENUM can't use the validate method.
     */
    ENUM("Enumeration", null, "^$"),

    /**
     * Path to a file (represented in a {@link String}).
     */
    FILENAME("Filename", "^.+$", "^$"),

    /**
     * Path to a directory (represented in a {@link String}).
     */
    DIRECTORY("Directory", "^.+$", "^$"),

    /**
     * Address to a website.
     */
    URI("URI", "^.+$", "^$"),

    /**
     * The type represents an instant (date and time).<br/>
     * Valid Format: yyyy-MM-dd_HH:mm:ss
     */
    INSTANT("Instant",
            "^[0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}:[0-9]{2}:[0-9]{2}$", "^$"),

    /**
     * BoundingBox Value. The String should be in the following format:
     * double,double,double,double first double is left, second right, third top
     * and fourth is bottom
     */
    BBOX("BoundingBox", "^-?[0-9]+(\\.[0-9]+)?$", "^$"),

    /**
     * A comma sperated list of values.
     */
    LIST("List", "^[^,]+(,[^,]+)*$", "^$");

    /**
     * compile all parameter-types at startup to verify that no pattern is
     * invalid.
     */
    static {
        for (ParameterType type : ParameterType.values()) {
            type.ordinal();
        }
    }

    /**
     * Friendly name of the type.
     */
    private String friendlyName;

    /**
     * The valid matcher.
     */
    private Pattern validPattern;

    /**
     * the null matcher.
     */
    private Pattern nullPattern;

    /**
     * Creates a new {@link ParameterType}.
     * 
     * @param friendlyName
     *            human readable version of the type
     */
    private ParameterType(String friendlyName, String validPattern,
            String nullPattern) {
        this.friendlyName = friendlyName;
        this.validPattern = ((validPattern != null) ? Pattern.compile(
                validPattern, Pattern.CASE_INSENSITIVE) : null);
        this.nullPattern = ((nullPattern != null) ? Pattern.compile(
                nullPattern, Pattern.CASE_INSENSITIVE) : null);
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
     * Returns the ValidMatcherPattern.
     * 
     * @return ValidMatcherPattern
     */
    public Pattern getValidPattern() {
        return validPattern;
    }

    /**
     * Returns the NullMatcherPattern.
     * 
     * @return NullMatcherPattern
     */
    public Pattern getNullPattern() {
        return nullPattern;
    }

    /**
     * Returns the validation result of a given {@link String}.
     * 
     * @param toBeValidated
     *            to be validated string
     * @return true if validation succeeds, false if it fails
     * @throws UnsupportedOperationException
     *             when the {@link ParameterType} does not support the validate
     *             operation.
     */
    protected boolean validate(String toBeValidated) {
        if (getValidPattern() == null || getNullPattern() == null) {
            throw new UnsupportedOperationException(this.toString()
                    + " does not support validation");
        }

        /* the null object is always a valid empty value */
        if (toBeValidated == null) {
            return true;
        }

        if (getValidPattern().matcher(toBeValidated).find()) {
            return true;
        } else if (getNullPattern().matcher(toBeValidated).find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns if a value is empty or not.
     * 
     * @param toBeChecked
     *            to be checked string
     * @return true if validation succeeds, false if it fails
     * @throws UnsupportedOperationException
     *             when the {@link ParameterType} does not support the check
     *             operation.
     */
    protected boolean isStringEmpty(String toBeChecked) {
        if (getNullPattern() == null) {
            throw new UnsupportedOperationException(this.toString()
                    + " does not support check vor empty");
        }

        /* the null object is always a valid empty value */
        if (toBeChecked == null) {
            return true;
        }

        return getNullPattern().matcher(toBeChecked).find();
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
