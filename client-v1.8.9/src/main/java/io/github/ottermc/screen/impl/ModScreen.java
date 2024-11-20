package io.github.ottermc.screen.impl;

import io.github.ottermc.Client;
import io.github.ottermc.ClientLogger;
import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.AbstractScreen;
import io.github.ottermc.screen.ClientTheme;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.screen.render.Icon;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ModScreen extends AbstractScreen {
	
	private float scale = 2.0f;
	
	private int selectedCategoryIndex = 0;

	@Override
	public void onScreenOpen() {
		updateScale();
		BlurShaderProgram.setActive(true, true);
	}

	@Override
	public void onScreenClose() {
		BlurShaderProgram.setActive(false, false);
		try {
			Client.getClientStorage().write();
		} catch (IOException e) {
			ClientLogger.display(e);
		}
	}
	
	@Override
	public void renderScreen(int mouseX, int mouseY, float partialTicks) {
		Color color = ClientTheme.getColor();
		
		int width = (int) (getDisplayWidth() * 0.625f);
		int height = (int) (getDisplayHeight() * 0.625f);
		int centerX = getDrawable().middle(getDisplayWidth(), width);
		int centerY = getDrawable().middle(getDisplayHeight(), height);
		
		getDrawable().fillRoundedRectangle(centerX, centerY, width, height, 8, backgroundColor);
		
		int height2 = (int) (height * 0.1f);
		getDrawable().fillTopRoundedRectangle(centerX, centerY, width, height2, 8, backgroundColor);
		String title = "Mods & Settings";
		getDrawable().drawString(title, centerX + 4, centerY + (height2 / 2) - 6, -1);
		
		int catX = centerX + 4;
		int catY = centerY + height2 + 4;
		int catWidth = (int) (width * 0.2f) - 8;
		getDrawable().fillRoundedRectangle(catX, catY, catWidth, height -  height2 - 8, 6, backgroundColor);
		for (int i = 0; i < Category.values().length; i++) {
			Category category = Category.values()[i];
			
			int ny = catY + (i * 26) + 2;
			if (i == selectedCategoryIndex)
				getDrawable().fillRoundedRectangle(catX + 2, ny, catWidth - 4, 26, 6, color.getValue());
			
			String display = category.getDisplayName();
			getDrawable().drawString(display, catX + 6, ny + 4, -1);
		}

		int modX = centerX + catWidth + 6;
        int modWidth = (int) (width * 0.8f) - 34;
		int mlen = modWidth / 5;
		int counter = 0;
		Iterator<Module> modules = Client.getModManager().getByCategory(Category.values()[selectedCategoryIndex]).iterator();
		while (modules.hasNext()) {
			for (int i = 0; i < 5; i++) {
				if (!modules.hasNext())
					break;
				Module mod = modules.next();
				
				int mx = modX + 4 + (i * (mlen + 4));
				int my = catY + (counter * (mlen + 4));
				getDrawable().fillRoundedRectangle(mx, my, mlen, mlen, 8, mod.isActive() ? color.getValue(0x60) : backgroundColor);
				
				String mname = mod.getName();
				getDrawable().drawString(mname, mx + (mlen / 2 - getDrawable().getStringWidth(mname, 0.65f) / 2), my + 4, 0.75f, 0xFFC6C6C6);

				int isize = 64;
				Icon icon = mod.getIcon();
				int mid = getDrawable().middle(mlen, isize);
				if (icon != null)
					getDrawable().drawIcon(icon, mx + mid, my + mid, 0xFFC6C6C6);
				getDrawable().drawRectangle(mx + mid + 24, my + mid + isize + 8, 16, 16, 0xFFC6C6C6);
				if (mod.isActive())
					getDrawable().drawIcon(Icon.CHECK, mx + mid + 24, my + mid + isize + 8, 0.25f, 0xFFC6C6C6);
			}
			counter++;
		}
	}
	
	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		mouseX = (int) ((float) mouseX * scale);
		mouseY = (int) ((float) mouseY * scale);
		int width = (int) (getDisplayWidth() * 0.625f);
		int height = (int) (getDisplayHeight() * 0.625f);
		int centerX = getDrawable().middle(getDisplayWidth(), width);
		int centerY = getDrawable().middle(getDisplayHeight(), height);
		
		// Categories
		int theX = centerX + 6;
		if (mouseX > theX && mouseX < (theX + ((int) width * 0.2f) - 8)) {
			int index = (int) ((mouseY - 6 - (height * 0.1f) - centerY) / 26.0f);
			index = clamp(index, 0, Category.values().length - 1);
			selectedCategoryIndex = index;
		}
		
		// Modules
		if (mouseX > centerX + (int) (width * 0.2f) + 6) {
			int mlen = (int) ((width * 0.8 - 34) / 5.0f);
			int i = (int) ((mouseX - centerX - width * 0.2f - 2) / (mlen + 4));
			int counter = (int) ((mouseY - centerY - height * 0.1f - 4) / (mlen + 4));
			int index = counter * 5 + i;
			List<Module> modules = Client.getModManager().getByCategory(Category.values()[selectedCategoryIndex]).collect(Collectors.toList());
			if (index < modules.size()) {
				Module mod = modules.get(index);
				if (button == 0)
					mod.toggle();
				else if (button == 1)
					Minecraft.getMinecraft().displayGuiScreen(new SettingScreen(this, mod));
			}
		}
	}

	@Override
	public void onResize(int sx, int sy) {
		updateScale();
	}

	private void updateScale() {
		float width0 = Display.getDesktopDisplayMode().getWidth();
		float width1 = Display.getWidth();
		scale = 2.0f * (width1 / width0);
		getDrawable().setScale(scale);
	}
	
	private int clamp(int i, int min, int max) {
		if (i < min)
			return min;
        return Math.min(i, max);
    }
}
