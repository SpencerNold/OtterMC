package ottermc;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;

public class KotlinPackageTask extends DefaultTask {

    @TaskAction
    public void execute() {
        Project project = getProject();
        Configuration configuration = project.getConfigurations().getByName("runtimeClasspath");
        Set<ResolvedArtifact> artifacts = configuration.getResolvedConfiguration().getResolvedArtifacts();
        artifacts = artifacts.stream().filter(artifact -> artifact.getName().contains("kotlin-stdlib")).collect(Collectors.toSet());
        try {
            String path = String.join(File.separator, project.getLayout().getBuildDirectory().getAsFile().get().getAbsolutePath(), "libs");
            JarOutputStream output = new JarOutputStream(new FileOutputStream(new File(path, "export-remapped-joined.jar")));
            addJarToFatJar(output, new File(path, "export-remapped.jar"));
            for (ResolvedArtifact artifact : artifacts) {
                File file = artifact.getFile();
                addJarToFatJar(output, file);
            }
            output.flush();
            output.close();
        } catch (IOException e) {
            throw new GradleScriptException("failed to package kotlin stdlib with jar", e);
        }
    }

    private void addJarToFatJar(JarOutputStream output, File file) throws IOException {
        JarInputStream input = new JarInputStream(new FileInputStream(file));
        JarEntry entry;
        while ((entry = input.getNextJarEntry()) != null) {
            output.putNextEntry(new JarEntry(entry.getName()));
            input.transferTo(output);
            output.closeEntry();
        }
        input.close();
    }
}
