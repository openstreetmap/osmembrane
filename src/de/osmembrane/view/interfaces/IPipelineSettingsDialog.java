/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * for more details about the license see
 * http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL: https://www.osmembrane.de/svn/sources/src/de/osmembrane/view/interfaces/ISettingsDialog.java $ ($Revision: 722 $)
 * Last changed: $Date: 2011-02-07 23:10:35 +0100 (Mo, 07 Feb 2011) $
 */


package de.osmembrane.view.interfaces;

/**
 * Interface for {@link PipelineSettingDialog}.
 * 
 * @author tobias_kuhn
 * 
 */
public interface IPipelineSettingsDialog extends IView {

	/**
	 * @return whether or not the changes made in the dialog should be applied
	 *         to the model
	 */
	public boolean shallApplyChanges();
	
	/**
	 * Sets the verbose level of the pipeline, used for osmosis execution.
	 * 
	 * @param mode 0 is default, -1 disables the verbose parameter.
	 */
	public void setVerbose(int mode);
	
	/**
	 * @return the verbose level for osmosis execution
	 */
	public int getVerbose();
	
	/**
	 * Sets the debug level of the pipeline, used for osmosis execution.
	 * 
	 * @param mode 0 is default, -1 disables the debug parameter.
	 */
	public void setDebug(int mode);
	
	/**
	 * @return the debug level for osmosis execution
	 */
	public int getDebug();
	
	/**
	 * Sets a comment for the pipeline.
	 * 
	 * @param comment which should be set
	 */
	public void setComment(String comment);
	
	/**
	 * @return the comment of the pipeline
	 */
	public String getComment();
	
	/**
	 * Sets a name for the pipeline.
	 * 
	 * @param name which should be set
	 */
	public void setName(String name);
	
	/**
	 * @return the name of the pipeline
	 */
	public String getName();

}
