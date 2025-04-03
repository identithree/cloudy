package dev.quinnlane.cloudy.common.reflection;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Utility class for operations related to Java packages and classes.
 */
public class PackageUtils {
	/**
	 * Retrieves all the classes within the specified package without checking subpackages.
	 *
	 * @param packageName The fully qualified name of the package to search for classes.
	 *
	 * @return A list of {@link Class} objects representing all the classes found within the specified package.
	 */
	public static @NotNull List<Class<?>> getClassesInPackage(String packageName) {
		return getClassesInPackage(packageName, false);
	}

	/**
	 * Retrieves all the classes within the specified package. Optionally, it can include classes in sub-packages.
	 *
	 * @param packageName The fully qualified name of the package to search for classes.
	 * @param checkSubpackages If true, the method will recursively search for classes in sub-packages.
	 *
	 * @return A list of {@link Class} objects representing all the classes found within the specified package.
	 *
	 * @throws IllegalArgumentException If the specified package does not exist or is empty.
	 * @throws RuntimeException If a class in the package cannot be loaded.
	 */
	public static @NotNull List<Class<?>> getClassesInPackage(@NotNull String packageName, boolean checkSubpackages) {
		List<Class<?>> classes = new ArrayList<>();
		String path = packageName.replace('.', '/');
		
		try {
			// Get all resources for the path
			Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
			List<String> classNames = new ArrayList<>();
			
			if (!resources.hasMoreElements()) {
				throw new IllegalArgumentException("The specified package " + packageName + " does not exist!");
			}
			
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				
				// If the resource is in a JAR file
				if (resource.getProtocol().equals("jar")) {
					classNames.addAll(getClassNamesFromJar(resource, packageName, checkSubpackages));
				} else {
					// Otherwise, it's a file on the filesystem
					try {
						File directory = new File(resource.toURI());
						classNames.addAll(getClassNamesFromDirectory(directory, packageName, checkSubpackages));
					} catch (URISyntaxException e) {
						throw new RuntimeException("Failed to convert resource URL to URI: " + resource, e);
					}
				}
			}
			
			// Load all the collected class names
			for (String className : classNames) {
				try {
					classes.add(Class.forName(className));
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("Failed to load class " + className + "!", e);
				}
			}
			
			if (classes.isEmpty()) {
				throw new IllegalArgumentException("The specified package " + packageName + " is empty!");
			}
			
		} catch (IOException e) {
			throw new RuntimeException("Error reading package: " + packageName, e);
		}
		
		return classes;
	}
	
	/**
	 * Gets class names from a resource located in a JAR file.
	 *
	 * @param resource The URL of the resource.
	 * @param packageName The package name.
	 * @param checkSubpackages Whether to check subpackages.
	 * @return A list of class names.
	 */
	private static @NotNull List<String> getClassNamesFromJar(URL resource, String packageName, boolean checkSubpackages) throws IOException {
		List<String> classNames = new ArrayList<>();
		String path = packageName.replace('.', '/');
		
		// Connect to the JAR file
		JarURLConnection jarConnection = (JarURLConnection) resource.openConnection();
		JarFile jarFile = jarConnection.getJarFile();
		
		// Get all JAR entries
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryName = entry.getName();
			
			// Check if the entry is a class in the target package
			if (entryName.endsWith(".class") && !entry.isDirectory()) {
				// For classes in the exact package
				if (entryName.startsWith(path + "/") && entryName.lastIndexOf('/') == path.length()) {
					String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
					classNames.add(className);
				}
				// For classes in subpackages (if requested)
				else if (checkSubpackages && entryName.startsWith(path + "/")) {
					String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
					classNames.add(className);
				}
			}
		}
		
		return classNames;
	}
	
	/**
	 * Gets class names from a directory in the filesystem.
	 *
	 * @param directory The directory to search in.
	 * @param packageName The package name.
	 * @param checkSubpackages Whether to check subpackages.
	 * @return A list of class names.
	 */
	private static @NotNull List<String> getClassNamesFromDirectory(File directory, String packageName, boolean checkSubpackages) {
		List<String> classNames = new ArrayList<>();
		
		if (!directory.exists() || !directory.isDirectory()) {
			return classNames;
		}
		
		File[] files = directory.listFiles();
		if (files == null) {
			return classNames;
		}
		
		for (File file : files) {
			if (file.isFile() && file.getName().endsWith(".class")) {
				// Add class from this package
				String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
				classNames.add(className);
			} else if (file.isDirectory() && checkSubpackages) {
				// Add classes from subpackages
				classNames.addAll(getClassNamesFromDirectory(file, packageName + '.' + file.getName(), true));
			}
		}
		
		return classNames;
	}
}
