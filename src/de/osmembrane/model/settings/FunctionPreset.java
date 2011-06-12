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

package de.osmembrane.model.settings;

import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.CopyType;

/**
 * Implementation of {@link AbstractFunctionPreset}.
 * 
 * @author jakob_jarosch
 */
public class FunctionPreset extends AbstractFunctionPreset {

    private static final long serialVersionUID = 2011020221170001L;

    private String name;
    private AbstractFunction function;

    /**
     * Creates a new FunctionPreset with given name and function.
     * 
     * @param name
     *            name of the preset
     * @param function
     *            which should be saved in the preset
     */
    public FunctionPreset(String name, AbstractFunction function) {
        this.name = name;
        this.function = function.copy(CopyType.WITHOUT_POSITION);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void loadPreset(AbstractFunction function) {
        /* iterate over all tasks */
        for (int task = 0; task < this.function.getAvailableTasks().length; task++) {
            /* set the active task */
            if (this.function.getAvailableTasks()[task] == this.function
                    .getActiveTask()) {
                function.setActiveTask(function.getAvailableTasks()[task]);
            }
            /* assign all parameters */
            for (int param = 0; param < this.function.getAvailableTasks()[task]
                    .getParameters().length; param++) {

                /* check if parameter values equals */
                try {
                    if (this.function.getAvailableTasks()[task].getParameters()[param]
                            .getName().equals(
                                    function.getAvailableTasks()[task]
                                            .getParameters()[param].getName())) {
                        function.getAvailableTasks()[task].getParameters()[param]
                                .setValue(this.function.getAvailableTasks()[task]
                                        .getParameters()[param].getValue());
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    /* just ignore, not so important */
                }
            }
        }
    }

    @Override
    protected AbstractFunction getInheritedFunction() {
        return function;
    }
}
