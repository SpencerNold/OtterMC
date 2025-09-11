package io.github.ottermc.pvp.modules.visual;

import io.github.ottermc.events.EventBus;
import io.github.ottermc.io.ByteBuf;
import io.github.ottermc.modules.Module;
import io.github.ottermc.modules.Storable;
import io.github.ottermc.modules.Writable;
import io.github.ottermc.modules.setting.ColorSetting;
import io.github.ottermc.modules.setting.FloatSetting;
import io.github.ottermc.pvp.listeners.RenderArmorEffectListener;
import io.github.ottermc.pvp.listeners.RenderEffectListener;
import io.github.ottermc.pvp.modules.CategoryList;
import io.github.ottermc.render.Color;
import io.github.ottermc.screen.render.Icon;

public class EnchantmentGlint extends Module implements RenderEffectListener, RenderArmorEffectListener {
	
	private static final Icon ICON = Icon.getIconIgnoreException("module/armor_icon.png");
	
	private final ColorSetting color = new ColorSetting("Color", Color.DEFAULT, false);
	private final FloatSetting opacity = new FloatSetting("Opacity", 1.0, 0.0, 1.0);

	public EnchantmentGlint() {
		super("Tool Glint", CategoryList.VISUAL);
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
		return color.getValue().getValue();
	}
	
	@Override
	public Writable<ByteBuf>[] getWritables() {
		return new Storable<?>[] { color, opacity };
	}
	
	@Override
	public Icon getIcon() {
		return ICON;
	}
}
