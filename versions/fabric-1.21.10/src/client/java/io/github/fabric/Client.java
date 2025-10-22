package io.github.fabric;

import io.github.ottermc.Game;
import net.fabricmc.api.ClientModInitializer;

public class Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Game.game = new Game(new SubClient());
        Game.game.start();
    }
}
