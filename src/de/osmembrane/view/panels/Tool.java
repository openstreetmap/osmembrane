package de.osmembrane.view.panels;

/**
 * Represents the tool currently in use on the pipeline panel.
 * 
 * @see Specification, 2.1.3
 * 
 * @author tobias_kuhn
 * 
 */
public enum Tool {
	/**
	 * The magic default tool, that behaves as follows:
	 * <ul>
	 * <li>if a function or connection is below the cursor, it behaves like the
	 * selection tool</li>
	 * <li>if nothing is below the cursor, it behaves like the view tool</li>
	 * <li>if a connector is below the cursor, it behaves like the connection
	 * tool</li>
	 * </ul>
	 */
	DEFAULT_MAGIC_TOOL,

	/**
	 * tool to select functions or connections in order to move functions or
	 * delete selections
	 */
	SELECTION_TOOL,

	/**
	 * tool to move or zoom the view
	 */
	VIEW_TOOL,

	/**
	 * tool to create connections
	 */
	CONNECTION_TOOL;
}
