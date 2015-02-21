package edu.tuberlin.dima.nerdle.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceManager {

	public static String getWorkingDir() {
		String tmpDir = System.getProperty("java.io.tmpdir");
		return tmpDir + File.separator + "nerdle_resources";
	}

	public static String getResourcePath(String resource) {

		System.err.println("ResourceManager: requested: " + resource);

		String extractedResourceDir = getWorkingDir();
		File dir = new File(extractedResourceDir);
		if (!dir.exists()) {
			System.err.println("ResourceManager: creating "
					+ extractedResourceDir);
			dir.mkdir();
		}

		String[] path = resource.split("\\"+File.separator);
		String fileName = path[path.length - 1];

		File r = new File(extractedResourceDir + File.separator + fileName);
//		System.out.println("ResourceManager: path: " + r.getAbsolutePath());

		// TODO: beim Entpacken Pfad von resource mit einbeziehen, dass es auch
		// moeglich ist Dateien mit dem selben Namen zu haben?
		
		try {
			
			// copy data
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream is = loader.getResourceAsStream(fileName);
			FileOutputStream fos = new FileOutputStream(r.getAbsolutePath());
			byte[] buffer = new byte[4096];
			int bytesRead = 0;
			while ((bytesRead = is.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}
			
			fos.close();
			
		} catch (IOException e) {
			// TODO: handle exception
		}
		
		return r.getAbsolutePath();
	}

}
