package io.github.ottermc.pvp.modules.visual;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.pvp.listeners.RenderArmorEffectListener;
import io.github.ottermc.pvp.listeners.RenderEffectListener;
import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.Category;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.Writable;
import io.github.ottermc.modules.setting.BooleanSetting;
import io.github.ottermc.modules.setting.ColorSetting;
import io.github.ottermc.modules.setting.FloatSetting;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.render.Icon;

public class EnchantmentGlint extends Module implements RenderEffectListener, RenderArmorEffectListener {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/armor_icon.png");
	
	private final ColorSetting color = new ColorSetting("Color", Color.DEFAULT, false);
	private final FloatSetting opacity = new FloatSetting("Opacity", 1.0f, 0.0f, 1.0f);
	private final BooleanSetting theme = new BooleanSetting("Use Theme", true);

	public EnchantmentGlint() {
		super("Tool Glint", Category.VISUAL);
	}
	
	@Override
	public void onEnable() {
		EventBus.add(this);
	}
	
	@Override
	public void onDisable() {
		EventBus.remove(this);
	}
	
	@Override
	public void onRenderEffect(RenderEffectEvent event) {
		event.setColor(getColorSettingValue());
	}
	
	@Override
	public void onRenderArmorEffect(RenderArmorEffectEvent event) {
		event.setColor(getColorSettingValue());
	}
	
	private int getColorSettingValue() {
		return ((theme.getValue() && ColorTheme.isModActive()) ? ColorTheme.getColorTheme() : this.color.getValue()).getValue((int) (opacity.getValue() * 255));
	}
	
	@Override
	public Writable<ByteBuf>[] getWritables() {
		return new Storable<?>[] { color, opacity, theme };
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
}
