package agent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarFile;

public enum Dependency {

	ASM("asm-9.6", "https://repo1.maven.org/maven2/org/ow2/asm/asm/9.6/asm-9.6.jar");
	
	private final String name;
	private final String link;
	
	private Dependency(String name, String link) {
		this.name = name;
		this.link = link;
	}
	
	private File download(File dir) {
		try {
			if (!dir.exists())
				dir.mkdirs();
			File file = new File(dir, name + ".jar");
			URLConnection connection = new URL(link).openConnection();
			InputStream input = connection.getInputStream();
			OutputStream output = new FileOutputStream(file);
			int n;
			byte[] buf = new byte[4096];
			while ((n = input.read(buf)) != -1)
				output.write(buf, 0, n);
			input.close();
			output.close();
			return file;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private boolean load(File file, Instrumentation instrumentation) {
		try {
			instrumentation.appendToSystemClassLoaderSearch(new JarFile(file));
		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}
		return false;
	}
	
	public static void loadAll(File dir, Instrumentation instrumentation) {
		for (Dependency d : values()) {
			File file = d.download(dir);
			boolean success = false;
			if (file != null)
				success = d.load(file, instrumentation);
			if (!success) {
				// TODO Throw some sort of error or something
			}
		}
	}
}
