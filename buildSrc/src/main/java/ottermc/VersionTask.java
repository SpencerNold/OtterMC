package ottermc;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VersionTask extends DefaultTask {
    // Set LatestVersion.NAME
    // Upload Mapping
    // Upload Clean Jar

    private String ver;
    private File mapping;
    private File jar;
    private String latest;

    @Option(option = "ver", description = "Version to be updated")
    public void setVersion(String version) {
        this.ver = version;
    }

    @Input
    public String getVersion() {
        return ver;
    }

    @Option(option = "mapping", description = "Path to mapping file")
    public void setMapping(String mapping) {
        this.mapping = new File(mapping);
    }

    @InputFile
    public File getMapping() {
        return mapping;
    }

    @Option(option = "jar", description = "Path to the clean version of the game client")
    public void setJar(String jar) {
        this.jar = new File(jar);
    }

    @InputFile
    public File getJar() {
        return jar;
    }

    @Option(option = "latest", description = "If using latest version, set new latest value here")
    public void setLatest(String latest) {
        this.latest = latest;
    }

    @Input
    @Optional
    public String getLatest() {
        return latest;
    }

    @TaskAction
    public void execute() {
        if (!VersionRegistry.getVersionNames().contains(ver)) {
            throw new GradleScriptException("unknown version: " + ver, new RuntimeException("unknown version: " + ver));
        }
        if (!mapping.exists()) {
            throw new GradleScriptException("mapping file does not exist", new RuntimeException("mapping file does not exist"));
        }
        if (!jar.exists()) {
            throw new GradleScriptException("clean jar file does not exist", new RuntimeException("clean jar file does not exist"));
        }
        if (ver.equals("latest") && latest == null) {
            throw new GradleScriptException("to set the latest version, add the \"latest\" option", new RuntimeException("to set the latest version, add the \"latest\" option"));
        }
        File dir = getProject().getProjectDir();
        BuildTool.copy(mapping, new File(dir, "buildSrc/src/main/resources/mapping-" + ver + ".txt"));
        BuildTool.copy(mapping, new File(dir, "client-" + ver + "/src/main/resources/mapping-latest.txt"));
        BuildTool.copy(jar, new File(dir, "client-" + ver + "/libs/mc-clean.jar"));
        BuildTool.deleteDirectory(new File(dir, "client-" + ver + "/build"));
        if (latest != null) {
            File target = new File("buildSrc/src/main/java/ottermc/LatestVersion.java");
            try (FileWriter writer = new FileWriter(target, false)) {
                writer.write("package ottermc;public class LatestVersion { public static final String NAME = \"" + latest + "\"; }");
            } catch (IOException e) {
                throw new GradleScriptException("failed to write LatestVersion.java", e);
            }
        }
    }
}
