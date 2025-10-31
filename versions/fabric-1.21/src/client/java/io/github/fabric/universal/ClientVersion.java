package io.github.fabric.universal;

import io.github.ottermc.universal.UVersion;

public class ClientVersion extends UVersion {
    @Override
    protected int _getClientVersion() {
        return UVersion.V21;
    }
}
