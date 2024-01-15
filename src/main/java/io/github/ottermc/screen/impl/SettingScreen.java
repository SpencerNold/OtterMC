package io.github.ottermc.screen.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import io.github.ottermc.Client;
import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.settings.Setting;
import io.github.ottermc.modules.settings.Writable;
import io.github.ottermc.modules.settings.setting.BooleanSetting;
import io.github.ottermc.modules.settings.setting.ColorSetting;
import io.github.ottermc.modules.settings.setting.EnumSetting;
import io.github.ottermc.modules.settings.setting.FloatSetting;
import io.github.ottermc.modules.settings.setting.IntSetting;
import io.github.ottermc.modules.settings.setting.KeyboardSetting;
import io.github.ottermc.modules.settings.setting.StringSetting;
import io.github.ottermc.modules.visual.ColorTheme;
import io.github.ottermc.screen.AbstractScreen;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.screen.render.DrawableHelper;
import io.github.ottermc.screen.render.Icon;
import structures.MutableReference;

public class SettingScreen extends AbstractScreen {
	
	private final List<SettingComponent> components = new ArrayList<>();
	
	private final float scale = 2.0f;
	
	private final Module module;
	
	public SettingScreen(Module module) {
		this.module = module;
	}

	@Override
	public void onScreenOpen() {
		getDrawable().setScale(scale);
		BlurShaderProgram.setActive(true);
		Writable<ByteBuf>[] writables = module.getWritables();
		if (writables != null) {
			for (Writable<ByteBuf> writable : writables) {
				if (!(writable instanceof Setting<?>))
					continue;
				Setting<?> setting = (Setting<?>) writable;
				switch (setting.getType()) {
				case BOOLEAN:
					components.add(new BooleanSettingComponent((BooleanSetting) setting));
					break;
				case COLOR:
					components.add(new ColorSettingComponent((ColorSetting) setting));
					break;
				case ENUM:
					components.add(new EnumSettingComponent((EnumSetting<?>) setting));
					break;
				case INT:
					components.add(new IntSettingComponent((IntSetting) setting));
					break;
				case KEYBOARD:
					components.add(new KeyboardSettingComponent((KeyboardSetting) setting));
					break;
				case STRING:
					components.add(new StringSettingComponent((StringSetting) setting));
					break;
				case FLOAT:
					components.add(new FloatSettingComponent((FloatSetting) setting));
					break;
				}
			}
		}
	}

	@Override
	public void onScreenClose() {
		BlurShaderProgram.setActive(false);
		try {
			Client.getClientStorage().write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void renderScreen(int mouseX, int mouseY, float partialTicks) {
		if (!Display.isFullscreen()) {
			String text = "Please activate fullscreen to use this menu...";
			int x = getDrawable().middle(getDisplayWidth(), getDrawable().getStringWidth(text));
			int y = getDrawable().middle(getDisplayHeight(), getDrawable().getStringHeight());
			getDrawable().drawString(text, x, y, -1);
			return;
		}
		
		int color = ColorTheme.isModActive() ? ColorTheme.getColorTheme().getValue() : -1;
		
		int width = (int) (getDisplayWidth() * 0.625f);
		int height = (int) (getDisplayHeight() * 0.625f);
		int centerX = getDrawable().middle(getDisplayWidth(), width);
		int centerY = getDrawable().middle(getDisplayHeight(), height);
		
		getDrawable().fillRoundedRectangle(centerX, centerY, width, height, 8, backgroundColor);
		
		int height2 = (int) (height * 0.1f);
		getDrawable().fillTopRoundedRectangle(centerX, centerY, width, height2, 8, backgroundColor);
		getDrawable().drawString(module.getName(), centerX + 4, centerY + (height2 / 2) - 6, -1);
		
		if (components.isEmpty()) {
			// TODO Draw descriptions
		} else {
			MutableReference<Integer> offset = new MutableReference<>(4);
			int x = centerX + 8;
			int y = centerY + height2 + 6;
			for (SettingComponent component : components)
				component.draw(this, x, y, offset, color);
		}
		
		String text = String.format("%s %s", Client.NAME, Client.VERSION);
		getDrawable().drawString(text, getDisplayWidth() - getDrawable().getStringWidth(text) - 4, getDisplayHeight() - getDrawable().getStringHeight() - 4, -1);
	}
	
	private static class BooleanSettingComponent extends SettingComponent {
		
		protected BooleanSettingComponent(BooleanSetting setting) {
			super(setting);
		}
		
		@Override
		protected int draw(AbstractScreen screen, int x, int y, MutableReference<Integer> offset, int color) {
			String name = setting.getName();
			screen.getDrawable().drawRectangle(x, y + offset.get(), 16, 16, color);
			screen.getDrawable().drawIcon(Icon.CHECK, x, y + offset.get(), 0.25f, color);
			screen.getDrawable().drawString(name, x + 18, y + offset.get() + (7 - (screen.getDrawable().getStringHeight(0.75f) / 2)), 0.75f, -1);
			offset.set(offset.get() + 24);
			return screen.getDrawable().getStringWidth(name, 0.75f) + 18;
		}
	}
	
	private static class ColorSettingComponent extends SettingComponent {
		
		protected ColorSettingComponent(ColorSetting setting) {
			super(setting);
		}
		
		@Override
		protected int draw(AbstractScreen screen, int x, int y, MutableReference<Integer> offset, int color) {
			String name = setting.getName();
			int nw = screen.getDrawable().getStringWidth(name);
			screen.getDrawable().drawString(name, x, y + offset.get(), -1);
			screen.getDrawable().fillRoundedRectangle(x + nw + 8, y + offset.get(), 128, 20, 6, screen.getBackgroundColor());
			screen.getDrawable().drawRoundedRectangle(x + nw + 8, y + offset.get(), 128, 20, 6, 0xFFC6C6C6); // Color theme color if selected
			screen.getDrawable().drawString("#________", x + nw + 12, y + offset.get() + 3, 0.75f, 0xFFC6C6C6); // Gray if is empty, white, if full
			offset.set(offset.get() + 24);
			return nw + 132;
		}
	}
	
	private static class EnumSettingComponent extends SettingComponent {
		
		private EnumSettingComponent(EnumSetting<? extends Enum<?>> setting) {
			super(setting);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected int draw(AbstractScreen screen, int x, int y, MutableReference<Integer> offset, int color) {
			DrawableHelper drawable = screen.getDrawable();
			EnumSetting<? extends Enum<?>> setting = ((EnumSetting<? extends Enum<?>>) this.setting);
			String name = setting.getName() + ": " + getDisplayString(setting.getEnumValue());
			drawable.drawString(name, x, y + offset.get(), color);
			drawable.fillRectangle(x + 8, y + offset.get() + 29, 120, 4, 0xFFC6C6C6);
			drawable.drawIcon(Icon.CIRCLE, x, y + offset.get() + 23, 0.25f, color);
			offset.set(offset.get() + 15 + 20);
			return Math.max(drawable.getStringWidth(name, 0.75f), 128);
		}
		
		public String getDisplayString(Enum<?> e) {
			String[] array = e.name().split("_");
			for (int i = 0; i < array.length; i++)
				array[i] = format(array[i]);
			return String.join(" ", array);
		}
		
		private String format(String str) {
			return str.charAt(0) + str.substring(1).toLowerCase();
		}
	}
	
	private static class IntSettingComponent extends SettingComponent {
		
		private IntSettingComponent(IntSetting setting) {
			super(setting);
		}

		@Override
		protected int draw(AbstractScreen screen, int x, int y, MutableReference<Integer> offset, int color) {
			DrawableHelper drawable = screen.getDrawable();
			IntSetting setting = (IntSetting) this.setting;
			String name = setting.getName() + ": " + setting.getValue();
			drawable.drawString(name, x, y + offset.get(), color);
			drawable.fillRectangle(x + 8, y + offset.get() + 29, 120, 4, 0xFFC6C6C6);
			drawable.drawIcon(Icon.CIRCLE, x, y + offset.get() + 23, 0.25f, color);
			offset.set(offset.get() + 15 + 20);
			return Math.max(drawable.getStringWidth(name, 0.75f), 128);
		}
	}
	
	private static class KeyboardSettingComponent extends SettingComponent {
		
		private KeyboardSettingComponent(KeyboardSetting setting) {
			super(setting);
		}

		@Override
		protected int draw(AbstractScreen screen, int x, int y, MutableReference<Integer> offset, int color) {
			DrawableHelper drawable = screen.getDrawable();
			drawable.drawRectangle(x, y + offset.get(), 32, 32, 0xFFC6C6C6); // color if selected
			drawable.fillRectangle(x + 1, y + offset.get() + 1, 30, 30, screen.getBackgroundColor());
			String value = ((KeyboardSetting) setting).getKeyName();
			drawable.drawString(value, x + drawable.middle(32, drawable.getStringWidth(value, 0.75f)) - 4, y + offset.get() + drawable.middle(32, drawable.getStringHeight(0.75f)) - 4, 0xFFC6C6C6); // color if selected
			String name = setting.getName();
			drawable.drawString(name, x + 36, y + offset.get() + 10, 0.75f, -1);
			offset.set(offset.get() + 36);
			return 20 + drawable.getStringWidth(name, 0.75f);
		}
	}
	
	private static class StringSettingComponent extends SettingComponent {
		
		private StringSettingComponent(StringSetting setting) {
			super(setting);
		}

		@Override
		protected int draw(AbstractScreen screen, int x, int y, MutableReference<Integer> offset, int color) {
			StringSetting setting = (StringSetting) this.setting;
			DrawableHelper drawable = screen.getDrawable();
			int height = (drawable.getStringHeight(0.75f) + 2) * setting.getLines() + 2;
			drawable.fillRoundedRectangle(x, y + offset.get(), 156, height, 8, screen.getBackgroundColor());
			drawable.drawRoundedRectangle(x, y + offset.get(), 156, height, 8, 0xFFC6C6C6); // color if selected
			// TODO Render string
			offset.set(offset.get() + height);
			return 256;
		}
	}
	
	private static class FloatSettingComponent extends SettingComponent {

		protected FloatSettingComponent(Setting<?> setting) {
			super(setting);
		}
		
		@Override
		protected int draw(AbstractScreen screen, int x, int y, MutableReference<Integer> offset, int color) {
			// TODO Render component
			return 0;
		}
	}
	
	private static abstract class SettingComponent {
		
		protected Setting<?> setting;
		
		protected SettingComponent(Setting<?> setting) {
			this.setting = setting;
		}
		
		protected abstract int draw(AbstractScreen screen, int x, int y, MutableReference<Integer> offset, int color);
	}
}
