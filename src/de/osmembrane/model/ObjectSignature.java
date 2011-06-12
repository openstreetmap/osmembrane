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

package de.osmembrane.model;

import java.util.Random;

/* not yet used! */
/**
 * Comparator for two classes, without checking the real contents.
 * 
 * @author jakob_jarosch.
 */
public abstract class ObjectSignature {

    /**
     * Length of the signature String.
     */
    private static final int SIGNATURE_LENGTH = 32;

    /**
     * Allowed chars in the signature String.
     */
    private static final char[] SIGNATURE_CHARS = { 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9' };

    /**
     * The signature itself.
     */
    private String signature = null;

    /**
     * Returns the signature.
     * 
     * @return signature as String
     */
    public String getSignature() {
        if (signature == null) {
            signature = createSignature();
        }

        return signature;
    }

    /**
     * equals-operation for signature-objects
     */
    @Override
    public boolean equals(Object obj) {
        if (this.getClass() == obj.getClass() && obj instanceof ObjectSignature) {
            ObjectSignature sigObj = (ObjectSignature) obj;
            if (sigObj.getSignature() == getSignature()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Changes the signature (e.g. after copy the object).
     */
    protected void changeSignature() {
        signature = createSignature();
    }

    /**
     * Internal creator for the signature.
     * 
     * @return signature String
     */
    private String createSignature() {
        StringBuilder sb = new StringBuilder();
        Random randomizer = new Random();
        for (int i = 0; i < SIGNATURE_LENGTH; i++) {
            sb.append(SIGNATURE_CHARS[randomizer
                    .nextInt(SIGNATURE_CHARS.length)]);
        }

        return sb.toString();
    }
}
