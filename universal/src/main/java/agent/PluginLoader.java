package agent;

import java.io.File;
import java.io.IOException;

public interface PluginLoader {
    void load(File file) throws IOException;
}
