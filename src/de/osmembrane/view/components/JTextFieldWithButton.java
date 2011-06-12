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

package de.osmembrane.view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.osmembrane.view.panels.InspectorPanel;

/**
 * A Panel that contains a {@link JTextField} and a {@link JButton}
 * simultaneously. Is used in the {@link InspectorPanel} to easily access
 * editing dialogs.
 * 
 * @author tobias_kuhn
 * 
 */
public class JTextFieldWithButton extends JPanel {

    private static final long serialVersionUID = -5112638853742176120L;

    private JTextField field;
    private JButton button;

    /**
     * Creates a new {@link JTextFieldWithButton}.
     */
    public JTextFieldWithButton() {
        this(null);
    }

    /**
     * Creates a new {@link JTextFieldWithButton}.
     * 
     * @param value
     *            Sets the contents of the text field to value
     */
    public JTextFieldWithButton(String value) {
        this(value, new String());
    }

    /**
     * Creates a new {@link JTextFieldWithButton}.
     * 
     * @param value
     *            Sets the contents of the text field to value
     * @param caption
     *            Sets the caption of the button to caption
     */
    public JTextFieldWithButton(String value, String caption) {
        field = (value != null) ? new JTextField(value) : new JTextField();
        button = (caption != null) ? new JButton(caption) : new JButton();

        this.setLayout(new BorderLayout());
        this.add(field, BorderLayout.CENTER);
        this.add(button, BorderLayout.EAST);
    }

    /**
     * Creates a new {@link JTextFieldWithButton}.
     * 
     * @param value
     *            Sets the contents of the text field to value
     * @param action
     *            Sets the action of the button to action
     */
    public JTextFieldWithButton(String value, Action action) {
        field = (value != null) ? new JTextField(value) : new JTextField();
        button = (action != null) ? new JButton(action) : new JButton();

        this.setLayout(new BorderLayout());
        this.add(field, BorderLayout.CENTER);
        this.add(button, BorderLayout.EAST);
    }

    /**
     * Unsets the borders of the field
     */
    public void fieldNoBorders() {
        field.setOpaque(true);
        field.setBorder(null);
    }

    /**
     * Sets the content value to value
     * 
     * @param value
     */
    public void setValue(String value) {
        field.setText(value);
    }

    /**
     * Sets the content value to value
     * 
     * @param value
     * @param fixed
     *            whether the value shall be editable
     */
    public void setValue(String value, boolean fixed) {
        field.setEditable(!fixed);
        field.setText(value);
    }

    /**
     * @return the content value
     */
    public String getValue() {
        return field.getText();
    }

    /**
     * Sets the button caption to caption
     * 
     * @param caption
     */
    public void setCaption(String caption) {
        button.setText(caption);
    }

    /**
     * @return the button caption
     */
    public String getCaption() {
        return button.getText();
    }

    /**
     * Adds the {@link ActionListener} al to the button component
     * 
     * @param al
     */
    public void addButtonActionListener(ActionListener al) {
        button.addActionListener(al);
    }

    /**
     * Adds the {@link FocusListener} fl to the field component
     * 
     * @param fl
     */
    public void addFieldFocusListener(FocusListener fl) {
        field.addFocusListener(fl);
    }

    /**
     * @see JTextField#setHorizontalAlignment(int)
     */
    public void setValueHorizontalAlignment(int align) {
        field.setHorizontalAlignment(align);

    }

    /**
     * Adds the {@link java.awt.event.KeyListener} kl to the field component
     * 
     * @param kl
     *            the key listener to add
     */

    public void addFieldKeyListener(KeyListener kl) {
        field.addKeyListener(kl);

    }

    /**
     * Sets the color of the value text field to color.
     * 
     * @param color
     *            the new foreground color
     */
    public void setValueForeground(Color color) {
        field.setForeground(color);
    }

}
