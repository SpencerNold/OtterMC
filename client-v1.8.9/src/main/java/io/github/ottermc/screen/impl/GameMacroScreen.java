package io.github.ottermc.screen.impl;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import io.github.ottermc.modules.hypixel.macro.Macro;
import io.github.ottermc.modules.visual.ColorTheme;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.AbstractScreen;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.screen.render.DrawableHelper;
import io.github.ottermc.screen.render.Icon;

public class GameMacroScreen extends AbstractScreen {

	// TODO I'm not really liking how this is turning out, I might just rewrite this or completely just rip it out
	
	private final List<MacroComponent> gameMacroComponents = new ArrayList<>();
	private int selectedIndex = -1;
	
	private final float scale = 2.0f;

	@Override
	public void onScreenOpen() {
		BlurShaderProgram.setActive(true, true);
		getDrawable().setScale(scale);
	}

	@Override
	public void onScreenClose() {
		BlurShaderProgram.setActive(false, false);
	}
	
	@Override
	public void renderScreen(int mouseX, int mouseY, float partialTicks) {
		DrawableHelper drawable = getDrawable();
		if (!Display.isFullscreen()) {
			String text = "Please activate fullscreen to use this menu...";
			int x = drawable.middle(getDisplayWidth(), drawable.getStringWidth(text));
			int y = drawable.middle(getDisplayHeight(), drawable.getStringHeight());
			drawable.drawString(text, x, y, -1);
			return;
		}
		Color color = ColorTheme.isModActive() ? ColorTheme.getColorTheme() : Color.DEFAULT;
		int width = (int) (getDisplayWidth() * 0.625f);
		int height = (int) (getDisplayHeight() * 0.625f);
		int height2 = (int) (height * 0.1f);
		int centerX = drawable.middle(getDisplayWidth(), width);
		int centerY = drawable.middle(getDisplayHeight(), height);
		
		drawable.fillRoundedRectangle(centerX, centerY, width, height, 8, backgroundColor);
		drawable.fillTopRoundedRectangle(centerX, centerY, width, height2, 8, backgroundColor);
		drawable.drawString("Game Macros", centerX + 4, centerY + (height2 / 2) - 6, -1);
		
		int x = centerX + 8;
		int y = centerY + height2 + 8;
		
		for (int i = 0; i < gameMacroComponents.size(); i++) {
			MacroComponent component = gameMacroComponents.get(i);
			drawable.outlineRectangle(x, y, 32, 32, Integer.MIN_VALUE, selectedIndex == i ? color.getValue() : 0xFFC6C6C6); // color if selected
			drawable.fillRectangle(x + 36, y, 256, 32, color.getValue(0xA0));
			drawable.drawIcon(Icon.DROPDOWN, x + 273, y + 8, 0.25f, -1);
			if (component.key != -1) {
				String text = Keyboard.getKeyName(component.key);
				drawable.drawString(text, x - (drawable.getStringWidth(text) / 2) + 14, y + 7, -1);
			}
			drawable.drawString(component.macro.getDisplayName(), x + 38, y + 8, -1);
			drawable.outlineRectangle(x + 300, y, 32, 32, Integer.MIN_VALUE, 0xFFC6C6C6);
			drawable.drawIcon(Icon.CLOSE, x + 300, y, 0.5f, 0xFFC60000);
			
			y += 36;
		}
		
		x = x + 148;
		boolean hovering = drawable.intersects(x, y, 32, 32, mouseX, mouseY);
		drawable.drawIcon(Icon.ADD, x, y, 0.5f, hovering ? color.getValue() : 0xFFC6C6C6);
	}
	
	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		DrawableHelper drawable = getDrawable();
		int width = (int) (getDisplayWidth() * 0.625f);
		int height = (int) (getDisplayHeight() * 0.625f);
		int height2 = (int) (height * 0.1f);
		int centerX = drawable.middle(getDisplayWidth(), width);
		int centerY = drawable.middle(getDisplayHeight(), height);
		int x = centerX + 8;
		int y = centerY + height2 + 8;
		
		// Keybind
		selectedIndex = (int) (((mouseY * scale) - y) / 36);
		if (drawable.intersects(x + 300, y + (selectedIndex * 36), 32, 32, mouseX, mouseY) && selectedIndex >= 0 && selectedIndex < gameMacroComponents.size()) {
			gameMacroComponents.remove(selectedIndex);
			return;
		}
		if (!drawable.intersects(x, y + (selectedIndex * 36), 32, 32, mouseX, mouseY))
			selectedIndex = -1;
		
		// Add
		boolean hovering = drawable.intersects(x + 148, y + gameMacroComponents.size() * 36, 32, 32, mouseX, mouseY);
		if (hovering)
			gameMacroComponents.add(new MacroComponent());
	}
	
	@Override
	public boolean onKeyPressed(char c, int key) {
		if (selectedIndex >= 0 && selectedIndex < gameMacroComponents.size() && key != Keyboard.KEY_ESCAPE) {
			MacroComponent component = gameMacroComponents.get(selectedIndex);
			if (key == Keyboard.KEY_BACK)
				component.key = -1;
			else
				component.key = key;
		}
		return true;
	}
	
	private static class MacroComponent {
		
		private int key;
		private Macro macro;
		
		public MacroComponent() {
			key = -1;
			macro = Macro.values()[0];
		}
	}
}
