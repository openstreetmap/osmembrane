package de.osmembrane.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Executable {

	private String binaryPath;

	/**
	 * Creates a new instance of the JOSM Executor.
	 * 
	 * @param binaryPath
	 *            the path to the binary file including the file name; must not
	 *            be <code>null</code>.
	 */
	public Executable(String binaryPath) {
		setBinaryPath(binaryPath);
	}

	/**
	 * Starts the binary subprocess with the given parameters and a maximum heap
	 * size of 256 MB.
	 * 
	 * @param parameters
	 *            the parameters to pass to the JOSM executable; must not be
	 *            <code>null</code>.
	 * @param workingDirectory
	 *            the working directory of the subprocess; if <code>null</code>
	 *            is passed, the current working directory of the parent process
	 *            will be used.
	 * @param environmentExtension
	 *            the environment variables to set; the passed map is merged
	 *            with the environment of the parent process. If the passed map
	 *            contains variables already present in the environment of the
	 *            parent process, the values in the passed map take precedence.
	 *            May be <code>null</code>.
	 * @return the process object representing the subprocess; never
	 *         <code>null</code>.
	 * @throws IOException
	 *             thrown if the creation of the process fails; see
	 *             {@link ProcessBuilder#start()}.
	 */
	public Process execute(List<String> parameters, File workingDirectory,
			Map<String, String> environmentExtension) throws IOException {
		return execute(parameters, workingDirectory, environmentExtension, 256);
	}

	/**
	 * Starts the JOSM editor subprocess with the given parameters.
	 * 
	 * @param parameters
	 *            the parameters to pass to the JOSM executable; must not be
	 *            <code>null</code>.
	 * @param workingDirectory
	 *            the working directory of the subprocess; if <code>null</code>
	 *            is passed, the current working directory of the parent process
	 *            will be used.
	 * @param environmentExtension
	 *            the environment variables to set; the passed map is merged
	 *            with the environment of the parent process. If the passed map
	 *            contains variables already present in the environment of the
	 *            parent process, the values in the passed map take precedence.
	 *            May be <code>null</code>.
	 * @param maxHeapSize
	 *            the maximum heap size of the java process (-Xmx JVM parameter)
	 *            in megabytes
	 * @return the process object representing the subprocess; never
	 *         <code>null</code>.
	 * @throws IOException
	 *             thrown if the creation of the process fails; see
	 *             {@link ProcessBuilder#start()}.
	 */
	public Process execute(List<String> parameters, File workingDirectory,
			Map<String, String> environmentExtension, int maxHeapSize)
			throws IOException {
		List<String> cmdLine = new ArrayList<String>(parameters.size() + 5);
		String javaPath = System.getProperty("java.home") + "/bin/java";
		cmdLine.add(javaPath);
		cmdLine.add(String.format("-Xmx%sm", maxHeapSize));
		cmdLine.add("-jar");
		cmdLine.add(binaryPath);

		cmdLine.addAll(parameters);

		ProcessBuilder processBuilder = new ProcessBuilder(cmdLine);
		if (environmentExtension != null) {
			processBuilder.environment().putAll(environmentExtension);
		}

		return processBuilder.directory(workingDirectory)
				.redirectErrorStream(false).start();
	}

	/**
	 * Sets the path to the JOSM jar file.
	 * 
	 * @param josmJarPath
	 *            the path to the JOSM jar file including the file name; must
	 *            not be <code>null</code>.
	 */
	public void setBinaryPath(String binaryPath) {
		if (binaryPath == null) {
			throw new IllegalArgumentException("binaryPath must not be null");
		}
		File f = new File(binaryPath);
		if (!f.isFile()) {
			throw new IllegalArgumentException("binaryPath must denote a file");
		}
		if (!f.canRead()) {
			throw new IllegalArgumentException("binaryPath must be readable");
		}
		this.binaryPath = binaryPath;
	}
}
