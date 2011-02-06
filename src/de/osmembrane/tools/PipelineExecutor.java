package de.osmembrane.tools;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import de.osmembrane.Application;
import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.view.interfaces.IExecutionStateDialog;

/**
 * Executor for a pipeline
 * 
 * @author jakob_jarosch
 */
public class PipelineExecutor extends Thread implements WindowListener {

	private String osmosisPath;
	private File workingDirectory;
	private List<String> parameters;
	private IExecutionStateDialog dialog;
	
	private Class<? extends Action> callbackEvent;

	/**
	 * Initialize the pipeline executor.
	 * 
	 * @throws IllegalArgumentException
	 *             when the osmosis executable is not runnable.
	 */
	public PipelineExecutor(String osmosisPath, String workingDirectory,
			List<String> parameters, IExecutionStateDialog dialog) {
		setPath(osmosisPath);
		this.workingDirectory = new File(workingDirectory);
		this.parameters = parameters;
		this.dialog = dialog;
		dialog.addWindowListener(this);

		dialog.setState(I18N.getInstance().getString(
				"Tools.PipelineExecutor.ParametersSet"));
		dialog.setProgress(5);
	}

	public void setCallbackAction(Class<? extends Action> event) {
		this.callbackEvent = event;
	}
	
	@Override
	public void run() {
		dialog.setState(I18N.getInstance().getString(
				"Tools.PipelineExecutor.StartingOsmosis"));
		dialog.setProgress(10);

		List<String> cmdLine = new ArrayList<String>(parameters.size() + 1);
		cmdLine.add(osmosisPath);
		cmdLine.addAll(parameters);

		try {
			ProcessBuilder processBuilder = new ProcessBuilder(cmdLine);
			Process process = processBuilder.directory(workingDirectory)
					.redirectErrorStream(true).start();

			final BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));

			String line = null;
			do {
				try {
					ReadThread thread = new ReadThread(reader);
					thread.start();
					thread.join();
					line = thread.getLine();
				} catch (InterruptedException e) {
					reader.close();
					process.destroy();
					interrupt();
					line = null;
				}
				
				if (line != null) {
					dialog.addOutputLine(line);
				}
			} while(line != null);

			if(!interrupted()) {
				int exitValue = -255;
				try {
					exitValue = process.waitFor();
					if(exitValue == 0) {
						dialog.setState(I18N.getInstance().getString(
						"Tools.PipelineExecutor.Finished"));
						dialog.setProgress(100);
						if(callbackEvent != null) {
							ActionRegistry.getInstance().get(callbackEvent).actionPerformed(new ActionEvent(this, 0, "finished"));
						}
					} else {
						dialog.setState(I18N.getInstance().getString(
						"Tools.PipelineExecutor.Failed"));
					}
				} catch (InterruptedException e) {
					reader.close();
					process.destroy();
					interrupt();
				}
			}

			if(interrupted()) {
				/* dialog should already be disposed. */
			}
			
		} catch (IOException e) {
			dialog.setState(I18N.getInstance().getString(
					"Tools.PipelineExecutor.IOExecption"));
			dialog.setProgress(0);
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.WARNING, e, I18N.getInstance().getString(
							"Tools.PipelineExecutor.IOExecption")));
		}
	}

	/**
	 * Sets the osmosisPath and validates it.
	 * 
	 * @param osmosisPath
	 */
	private void setPath(String osmosisPath) {
		if (osmosisPath == null) {
			throw new IllegalArgumentException();
		}
		File f = new File(osmosisPath);
		if (!f.isFile()) {
			throw new IllegalArgumentException();
		}
		if (!f.canRead()) {
			throw new IllegalArgumentException();
		}
		this.osmosisPath = osmosisPath;
	}

	/**
	 * The Windows-closed action of the dialog in the view.
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		this.interrupt();
		dialog.removeWindowListener(this);
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}

/**
 * ReadThread which is able to be interrupted while reading a line.
 * 
 * @author jakob_jarosch
 */
class ReadThread extends Thread {
	
	private BufferedReader reader;
	private String line;
	
	public ReadThread(BufferedReader reader) {
		this.reader = reader;
	}
	
	@Override
	public void run() {
		line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			line = null;
		}
	}
	
	public String getLine() {
		return line;
	}
}
