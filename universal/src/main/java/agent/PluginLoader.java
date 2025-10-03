package agent;

import java.io.File;
import java.util.function.Consumer;

public class PluginLoader {

    private final Consumer<File> pluginConsumer;

    public PluginLoader(Consumer<File> pluginConsumer) {
        this.pluginConsumer = pluginConsumer;
    }

    public void load(File file) {
        this.pluginConsumer.accept(file);
    }
}
