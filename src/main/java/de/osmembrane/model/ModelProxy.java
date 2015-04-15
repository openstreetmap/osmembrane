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

import java.util.Observable;
import java.util.Observer;

import de.osmembrane.model.pipeline.AbstractFunctionPrototype;
import de.osmembrane.model.pipeline.AbstractPipeline;
import de.osmembrane.model.pipeline.FunctionPrototype;
import de.osmembrane.model.pipeline.Pipeline;
import de.osmembrane.model.preset.AbstractPresetPrototype;
import de.osmembrane.model.preset.PresetPrototype;
import de.osmembrane.model.settings.AbstractSettings;
import de.osmembrane.model.settings.Settings;
import de.osmembrane.model.statusbar.AbstractStatusbar;
import de.osmembrane.model.statusbar.Statusbar;

/**
 * The ModelProxy is the connection to the whole Model of OSMembrane. A instance
 * can got over {@link ModelProxy#getInstance()}.
 * 
 * @author jakob_jarosch
 */
public class ModelProxy extends Observable implements Observer {

    private AbstractSettings settings;
    private AbstractPipeline pipeline;
    private AbstractFunctionPrototype functionPrototype;
    private AbstractPresetPrototype presetPrototype;
    private AbstractStatusbar statusbar;

    /**
     * Implements the Singleton pattern.
     */
    private static ModelProxy instance = new ModelProxy();

    /**
     * Initiates the ModelProxy.
     */
    private ModelProxy() {
        settings = new Settings();
        settings.addObserver(this);

        pipeline = new Pipeline();
        pipeline.addObserver(this);

        functionPrototype = new FunctionPrototype();
        functionPrototype.addObserver(this);

        statusbar = new Statusbar();
        statusbar.addObserver(this);

        presetPrototype = new PresetPrototype();
    }

    /**
     * Getter for the Singleton pattern.
     * 
     * @return the one and only instance of ModelProxy
     */
    public static ModelProxy getInstance() {
        return instance;
    }

    /**
     * Returns the {@link AbstractPipeline}.
     * 
     * @return give back the active {@link AbstractPipeline}
     */
    public AbstractPipeline getPipeline() {
        return pipeline;
    }

    /**
     * Returns the {@link AbstractSettings}.
     * 
     * @return give back the active {@link AbstractSettings}
     */
    public AbstractSettings getSettings() {
        return settings;
    }

    /**
     * Returns the {@link AbstractFunctionPrototype}.
     * 
     * @return give back the active {@link AbstractFunctionPrototype}
     */
    public AbstractFunctionPrototype getFunctions() {
        return functionPrototype;
    }

    /**
     * Returns the {@link AbstractStatusbar}.
     * 
     * @return give back the active {@link AbstractStatusbar}
     */
    public AbstractStatusbar getStatusbar() {
        return statusbar;
    }

    /**
     * Returns the {@link AbstractPresetPrototype}.
     * 
     * @return give back the active {@link AbstractPresetPrototype}
     */
    public AbstractPresetPrototype getPreset() {
        return presetPrototype;
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
