package io.github.ottermc.screen.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import io.github.ottermc.Client;
import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.settings.NumericSetting;
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
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.AbstractScreen;
import io.github.ottermc.screen.render.BlurShaderProgram;
import io.github.ottermc.screen.render.DrawableHelper;
import io.github.ottermc.screen.render.Icon;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class SettingScreen extends AbstractScreen {
	
	private final List<SettingComponent<?>> components = new ArrayList<>();
	
	private final float scale = 2.0f;
	
	private final AbstractScreen parentScreen;
	private final Module module;
	
	public SettingScreen(AbstractScreen parentScreen, Module module) {
		this.parentScreen = parentScreen;
		this.module = module;
	}

	@Override
	public void onScreenOpen() {
		getDrawable().setScale(scale);
		BlurShaderProgram.setActive(true, true);
		components.clear();
		Writable<ByteBuf>[] writables = module.getWritables();
		if (writables != null) {
			for (Writable<?> writable : writables) {
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
				case FLOAT:
					components.add(new FloatSettingComponent((FloatSetting) setting));
					break;	
				case KEYBOARD:
					components.add(new KeyboardSettingComponent((KeyboardSetting) setting));
					break;
				case STRING:
					components.add(new StringSettingComponent((StringSetting) setting));
					break;
				}
			}
			updateSettingComponentPositions();
		}
	}

	@Override
	public void onScreenClose() {
		BlurShaderProgram.setActive(false, false);
		components.forEach(SettingComponent::saveToSetting);
		try {
			Client.getClientStorage().write();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		
		int color = ColorTheme.isModActive() ? ColorTheme.getColorTheme().getValue() : -1;
		
		int width = (int) (getDisplayWidth() * 0.625f);
		int height = (int) (getDisplayHeight() * 0.625f);
		int centerX = drawable.middle(getDisplayWidth(), width);
		int centerY = drawable.middle(getDisplayHeight(), height);
		
		drawable.fillRoundedRectangle(centerX, centerY, width, height, 8, backgroundColor);
		
		int height2 = (int) (height * 0.1f);
		drawable.fillTopRoundedRectangle(centerX, centerY, width, height2, 8, backgroundColor);
		drawable.drawString(module.getName(), centerX + 4, centerY + (height2 / 2) - 6, -1);
		
		components.forEach(c -> c.draw(this, color));
		
		if (components.size() == 0) {
			String text = module.getDescription();
			text = text == null ? "Open the HUD edit menu to modify this mod." : text;
			int x = centerX + 5;
			int y = centerY + height2 + 5;
			int offset = 0;
			int line = 0;
			String[] words = text.split(" ");
			for (String str : words) {
				int sw = drawable.getStringWidth(str) + 4;
				if ((x + offset + sw) > (centerX + width - 10)) {
					offset = 0;
					line++;
				}
				drawable.drawString(str, x + offset, y + (line * 16), -1);
				offset += sw;
			}
		}
	}
	
	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		components.forEach(c -> c.mouseClick(this, mouseX, mouseY, button));
	}
	
	@Override
	public boolean onKeyPressed(char c, int key) {
		components.forEach(m -> m.keyPressed(this, c, key));
		if (key == Keyboard.KEY_ESCAPE)
			Minecraft.getMinecraft().displayGuiScreen(parentScreen);
		return false;
	}
	
	@Override
	public void onClickMove(int mouseX, int mouseY, int button, long dt) {
		components.forEach(c -> c.mouseClickMove(this, mouseX, mouseY, button));
	}
	
	@Override
	public void onRelease(int mouseX, int mouseY, int button) {
		components.forEach(c -> c.mouseReleased(this));
	}
	
	private void updateSettingComponentPositions() {
		int width = (int) (getDisplayWidth() * 0.625f);
		int height = (int) (getDisplayHeight() * 0.625f);
		int height2 = (int) (height * 0.1f);
		int centerX = getDrawable().middle(getDisplayWidth(), width);
		int centerY = getDrawable().middle(getDisplayHeight(), height);
		int x = centerX + 8;
		int offset = 0;
		for (SettingComponent<?> component : components) {
			component.x = x;
			component.y = centerY + height2 + 6 + offset;
			offset += component.getHeight() + 8;
			// TODO Wrap components to another column if there are too many settings
		}
	}
	
	private static class BooleanSettingComponent extends SettingComponent<BooleanSetting> {
		
		protected BooleanSettingComponent(BooleanSetting setting) {
			super(setting);
		}

		@Override
		protected int draw(AbstractScreen screen, int color) {
			String name = setting.getName();
			screen.getDrawable().drawRectangle(x, y, 16, 16, color);
			if (setting.getValue())
				screen.getDrawable().drawIcon(Icon.CHECK, x, y, 0.25f, color);
			screen.getDrawable().drawString(name, x + 18, y + (7 - (screen.getDrawable().getStringHeight(0.75f) / 2)), 0.75f, -1);
			return screen.getDrawable().getStringWidth(name, 0.75f) + 18;
		}

		@Override
		protected void mouseClick(AbstractScreen screen, int mouseX, int mouseY, int button) {
			if (screen.getDrawable().intersects(x, y, 16, getHeight(), mouseX, mouseY))
				setting.setValue(!setting.getValue());
		}

		@Override
		protected int getHeight() {
			return 16;
		}
	}
	
	private static class ColorSettingComponent extends SettingComponent<ColorSetting> {
		
		private static final String hexColorCharacters = "0123456789abcdefABCDEF";
		
		private boolean selected;
		private String colorText;
		private int width;

		protected ColorSettingComponent(ColorSetting setting) {
			super(setting);
			colorText = Integer.toHexString(setting.getValue().getValue());
		}

		@Override
		protected int draw(AbstractScreen screen, int color) {
			String name = setting.getName();
			int nw = screen.getDrawable().getStringWidth(name, 0.75f);
			screen.getDrawable().drawString(name, x, y, 0.75f, -1);
			screen.getDrawable().fillRoundedRectangle(x + nw + 8, y, 128, 20, 6, screen.getBackgroundColor());
			screen.getDrawable().drawRoundedRectangle(x + nw + 8, y, 128, 20, 6, selected ? color : 0xFFC6C6C6);
			screen.getDrawable().drawString(formatDisplayString(), x + nw + 12, y + 3, 0.75f, colorText.length() != 8 ? 0xFFC6C6C6 : -1);
			return width = nw + 132;
		}

		@Override
		protected void mouseClick(AbstractScreen screen, int mouseX, int mouseY, int button) {
			selected = screen.getDrawable().intersects(x + width - 128, y, 128, 20, mouseX, mouseY);
			if (!selected && colorText.length() == 8)
				saveToSetting();
		}
		
		@Override
		protected void keyPressed(AbstractScreen screen, char c, int key) {
			if (selected && key != Keyboard.KEY_ESCAPE) {
				if (key == Keyboard.KEY_BACK && colorText.length() != 0) {
					colorText = colorText.substring(0, colorText.length() - 1);
					return;
				}
				if (colorText.length() < 8 && hexColorCharacters.contains(String.valueOf(c)))
					colorText += c;
			}
		}
		
		@Override
		protected int getHeight() {
			return 20;
		}
		
		@Override
		protected void saveToSetting() {
			if (colorText.length() == 8)
				((ColorSetting) setting).setValue(new Color((int) Long.parseLong(colorText, 16), true));
		}
		
		private String formatDisplayString() {
			byte[] bytes = new byte[8];
			byte[] strBytes = colorText.getBytes(StandardCharsets.UTF_8);
			int i;
			for (i = 0; i < strBytes.length; i++)
				bytes[i] = strBytes[i];
			for (i = strBytes.length; i < 8; i++)
				bytes[i] = '_';
			return "#" + new String(bytes, StandardCharsets.UTF_8).toUpperCase();
		}
	}
	
	private static class EnumSettingComponent extends SliderSettingComponent<EnumSetting<?>> {

		protected EnumSettingComponent(EnumSetting<?> setting) {
			super(setting, setting.getMaximum() - 1);
		}

		@Override
		public String getDisplayString() {
			Enum<?> e = setting.getEnumValue();
			String[] array = e.name().split("_");
			for (int i = 0; i < array.length; i++)
				array[i] = format(array[i]);
			return String.join(" ", array);
		}
		
		private String format(String str) {
			return str.charAt(0) + str.substring(1).toLowerCase();
		}
	}
	
	private static class IntSettingComponent extends SliderSettingComponent<IntSetting> {

		protected IntSettingComponent(IntSetting setting) {
			super(setting, setting.getMaximum());
		}

		@Override
		public String getDisplayString() {
			return String.valueOf(setting.getValue());
		}
	}
	
	private static class FloatSettingComponent extends SliderSettingComponent<FloatSetting> {

		protected FloatSettingComponent(FloatSetting setting) {
			super(setting, setting.getMaximum());
		}

		@Override
		public String getDisplayString() {
			return String.format("%.2f", setting.getValue());
		}
	}
	
	private static abstract class SliderSettingComponent<T extends NumericSetting<? extends Number>> extends SettingComponent<T> {
		
		private final float size;
		private int position;
		private boolean selected;
		
		protected SliderSettingComponent(T setting, float size) {
			super(setting);
			this.size = size;
			position = (int) (((Number) setting.getValue()).floatValue() * 120.0f / size);
		}

		@Override
		protected int draw(AbstractScreen screen, int color) {
			DrawableHelper drawable = screen.getDrawable();
			String name = setting.getName() + ": " + getDisplayString();
			drawable.drawString(name, x, y, -1);
			drawable.fillRectangle(x + 8, y + 29, 120, 4, 0xFFC6C6C6);
			drawable.drawIcon(Icon.CIRCLE, x + position, y + 23, 0.25f, color);
			return Math.max(drawable.getStringWidth(name, 0.75f), 128);
		}
		
		@Override
		protected void mouseClick(AbstractScreen screen, int mouseX, int mouseY, int button) {
			selected = screen.getDrawable().intersects(x + position, y + 23, 16, 16, mouseX, mouseY);
		}
		
		@Override
		protected void mouseReleased(AbstractScreen screen) {
			selected = false;
		}
		
		protected void mouseClickMove(AbstractScreen screen, int mouseX, int mouseY, int button) {
			mouseX *= screen.getDrawable().getScale();
			if (selected) {
				position = MathHelper.clamp_int(mouseX - x, 0, 120);
				float value = (position / 120.0f * size);
				if (setting instanceof FloatSetting)
					((FloatSetting) setting).setValue(value);
				else if (setting instanceof IntSetting)
					((IntSetting) setting).setValue((int) value);
			}
		}

		@Override
		protected int getHeight() {
			return 40;
		}
		
		public abstract String getDisplayString();
	}
	
	private static class KeyboardSettingComponent extends SettingComponent<KeyboardSetting> {
		
		private boolean selected;

		protected KeyboardSettingComponent(KeyboardSetting setting) {
			super(setting);
		}

		@Override
		protected int draw(AbstractScreen screen, int color) {
			DrawableHelper drawable = screen.getDrawable();
			drawable.drawRectangle(x, y, 32, 32, selected ? color : 0xFFC6C6C6);
			drawable.fillRectangle(x + 1, y + 1, 30, 30, screen.getBackgroundColor());
			String value = setting.getKeyName();
			drawable.drawString(value, x + drawable.middle(32, drawable.getStringWidth(value, 0.75f)) - 4, y + drawable.middle(32, drawable.getStringHeight(0.75f)) - 4, selected ? -1 : 0xFFC6C6C6);
			String name = setting.getName();
			drawable.drawString(name, x + 36, y + 10, 0.75f, -1);
			return 20 + drawable.getStringWidth(name, 0.75f);
		}
		
		@Override
		protected void mouseClick(AbstractScreen screen, int mouseX, int mouseY, int button) {
			selected = screen.getDrawable().intersects(x, y, 32, 32, mouseX, mouseY);
		}
		
		@Override
		protected void keyPressed(AbstractScreen screen, char c, int key) {
			if (!selected || key == Keyboard.KEY_ESCAPE)
				return;
			if (key == Keyboard.KEY_BACK) {
				setting.setValue(-1);
				return;
			}
			setting.setValue(key);
		}
		
		@Override
		protected int getHeight() {
			return 32;
		}
	}
	
	private static class StringSettingComponent extends SettingComponent<StringSetting> {
		
		private AbstractScreen screen;
		private boolean selected;

		protected StringSettingComponent(StringSetting setting) {
			super(setting);
		}

		@Override
		protected int draw(AbstractScreen screen, int color) {
			this.screen = screen;
			DrawableHelper drawable = screen.getDrawable();
			int height = getHeight();
			String name = setting.getName();
			drawable.drawString(name, x, y + (height / 2) - (drawable.getStringHeight(0.75f) / 2), 0.75f, -1);
			int sw = drawable.getStringWidth(name, 0.75f) + 8;
			drawable.fillRoundedRectangle(x + sw, y, 156, height, 8, screen.getBackgroundColor());
			drawable.drawRoundedRectangle(x + sw, y, 156, height, 8, selected ? color : 0xFFC6C6C6);
			String[] lines = getLines();
			for (int i = 0; i < lines.length; i++)
				drawable.drawString(lines[i] + (i == (lines.length - 1) && selected ? "_" : ""), x + sw + 2, y + 3 + (i * (drawable.getStringHeight() + 2)), 0.75f, selected ? -1 : 0xFFC6C6C6);
			return 256 + sw;
		}
		
		@Override
		protected void mouseClick(AbstractScreen screen, int mouseX, int mouseY, int button) {
			selected = screen.getDrawable().intersects(x, y, 256, getHeight(), mouseX, mouseY);
		}
		
		@Override
		protected void keyPressed(AbstractScreen screen, char c, int key) {
			if (selected && key != Keyboard.KEY_ESCAPE) {
				String value = setting.getValue();
				if (key == Keyboard.KEY_BACK && value.length() != 0) {
					setting.setValue(value.substring(0, value.length() - 1));
					return;
				}
				boolean bypass = false;
				if (key == Keyboard.KEY_RETURN) {
					c = '\n';
					bypass = true;
				} else if (key == Keyboard.KEY_SPACE) {
					c = ' ';
					bypass = true;
				}
				if (screen.getDrawable().isFontCharSupported(c) || bypass)
					setting.setValue(value + c);
			}
		}

		@Override
		protected int getHeight() {
			 return screen == null ? 0 : (screen.getDrawable().getStringHeight(0.75f) + 2) * setting.getLines() + 6;
		}
		
		private String[] getLines() {
			String[] lines = setting.getValue().split("\n");
			if (setting.getValue().endsWith("\n")) {
				String[] copy = new String[lines.length + 1];
				System.arraycopy(lines, 0, copy, 0, lines.length);
				copy[copy.length - 1] = "";
				lines = copy;
			}
			return lines;
		}
	}
	
	private static abstract class SettingComponent<T extends Setting<?>> {
		
		protected T setting;
		
		protected int x, y;
		
		protected SettingComponent(T setting) {
			this.setting = setting;
		}
		
		protected abstract int draw(AbstractScreen screen, int color);
		protected void mouseClick(AbstractScreen screen, int mouseX, int mouseY, int button) {}
		protected void mouseReleased(AbstractScreen screen) {}
		protected void mouseClickMove(AbstractScreen screen, int mouseX, int mouseY, int button) {}
		protected void keyPressed(AbstractScreen screen, char c, int key) {}
		protected void saveToSetting() {}
		
		protected abstract int getHeight();
	}
}
