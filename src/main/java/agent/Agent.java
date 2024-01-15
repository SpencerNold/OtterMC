package agent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.net.URISyntaxException;

import io.github.ottermc.Client;

public class Agent {

	public static void premain(String args, Instrumentation instrumentation) {
		File file = getJarFileDirectory();
		Dependency.loadAll(new File(file, "libs"), instrumentation);
		ClassAdapter adapter = new ClassAdapter(instrumentation);
		Client client = new Client(file, adapter);
		try {
			adapter.execute();
		} catch (UnmodifiableClassException e) {
			e.printStackTrace();
		}
		adapter.clear();
		client.start();
	}
	
	private static File getJarFileDirectory() {
		try {
			return new File(Agent.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
}
