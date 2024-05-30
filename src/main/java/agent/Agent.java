package agent;

import agent.dependencies.Dependency;
import agent.dependencies.DependencyLoader;
import agent.transformation.ClassAdapter;
import io.github.ottermc.Client;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.net.URISyntaxException;

public class Agent {

	private static boolean injectionLoad = false;

	public static void premain(String args, Instrumentation instrumentation) {
		launch(args, instrumentation);
	}

	public static void agentmain(String args, Instrumentation instrumentation) {
		injectionLoad = true;
		launch(args, instrumentation);
	}

	private static void launch(String args, Instrumentation instrumentation) {
		File file = getJarFileDirectory();
		DependencyLoader loader = new DependencyLoader(new File(file, "libs"), instrumentation);
		loader.add(Dependency.getURLDependency("asm-9.6", "https://repo1.maven.org/maven2/org/ow2/asm/asm/9.6/asm-9.6.jar"));
		ClassAdapter adapter = new ClassAdapter(instrumentation);
		Client client = new Client(file, adapter);
		try {
			adapter.execute();
		} catch (UnmodifiableClassException e) {
			throw new RuntimeException(e);
		}
		adapter.clear();
		client.start();
	}
	
	private static File getJarFileDirectory() {
		try {
			return new File(Agent.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isInjectionLoad() {
		return injectionLoad;
	}
}
