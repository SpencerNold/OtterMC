package net.ottermc.window.manifest;

import net.ottermc.window.Main;
import net.ottermc.window.util.FileTool;
import net.ottermc.window.versions.Version;
import net.ottermc.window.versions.impl.FabricVersion;
import net.ottermc.window.versions.impl.VanillaVersion;

import java.io.File;
import java.util.*;

public class Manifest {

    private final List<Version> versions = new ArrayList<>();

    void add(Version version) {
        this.versions.add(version);
    }

    void addAll(Collection<Version> versions) {
        this.versions.addAll(versions);
    }

    private void sort() {
        versions.sort(Comparator.comparing(Version::getLastPlayed).reversed());
    }

    public List<Version> getVersions() {
        return Collections.unmodifiableList(versions);
    }

    public static Manifest read() {
        File file = getManifestPath();
        ManifestReader reader = new ManifestReader(file);
        reader.addToRegistry(VanillaVersion.TYPE, VanillaVersion.class);
        reader.addToRegistry(FabricVersion.TYPE, FabricVersion.class);
        Manifest manifest = reader.read();
        manifest.sort();
        return manifest;
    }

    public static void write(Manifest manifest) {
        File file = getManifestPath();
        ManifestWriter writer = new ManifestWriter(file);
        writer.write(manifest);
    }

    private static File getManifestPath() {
        return FileTool.getFilePath(Main.gameDir, "ottermc", "manifest.json");
    }
}
