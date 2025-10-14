package io.github.ottermc.universal.hud;

import io.github.ottermc.render.hud.impl.PotionEffectHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientPotionEffectHud extends PotionEffectHud {

    @Override
    public void draw(Object context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.getCameraEntity() instanceof ClientPlayerEntity) {
            LivingEntity player = (LivingEntity) mc.getCameraEntity();
            ArrayList<StatusEffectInstance> effects = new ArrayList<>(player.getStatusEffects());
            int height = mc.getWindow().getScaledHeight();
            drawEffects(mc, (DrawContext) context, effects, (getYOffset() - getDefaultY()) > (height / 2));
        }
    }

    @Override
    public void drawDummyObject(Object context) {
        drawEffects(MinecraftClient.getInstance(), (DrawContext) context, Arrays.asList(
                new StatusEffectInstance(StatusEffects.SPEED, 3600, 2),
                new StatusEffectInstance(StatusEffects.STRENGTH, 3600, 2),
                new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 3600)
        ), false);
    }

    private void drawEffects(MinecraftClient mc, DrawContext context, List<StatusEffectInstance> effects, boolean drawBottomUp) {
        // Commented code is a reflection of what 1.8.9 used to be
        // this was kinda made obsolete in later versions
        // to avoid reinventing the wheel, this version of the mod gives a little better hud info
        int color = io.github.ottermc.modules.impl.hud.PotionEffect.getColor().getValue();
        for (int i = 0; i < effects.size(); i++) {
            StatusEffectInstance effect = effects.get(i);
            if (effect == null)
                continue;
            int x = getDefaultX();
            int y = getDefaultY() + (drawBottomUp ? (getRawHeight() + ((i + 1) * -20)) : (i * 20));
            //drawStatusEffectIcon(context, effect, i % 8 * 18, 198 + i / 8 * 18, false);
            String text = getPotionDisplayString(effect);
            //context.drawText(mc.textRenderer, text, x + 20, y + 6, color, false);
            context.drawText(mc.textRenderer, text, x, y + 6, color, false);
        }
    }

    /*
    private static final Identifier EFFECT_BACKGROUND_LARGE_TEXTURE = Identifier.ofVanilla("container/inventory/effect_background_large");
    private static final Identifier EFFECT_BACKGROUND_SMALL_TEXTURE = Identifier.ofVanilla("container/inventory/effect_background_small");
    private void drawStatusEffectIcon(DrawContext context, StatusEffectInstance effect, int x, int y, boolean wide) {
        if (wide)
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_LARGE_TEXTURE, x, y, 120, 32);
        else
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_SMALL_TEXTURE, x, y, 32, 32);
        RegistryEntry<StatusEffect> lv2 = effect.getEffectType();
        Identifier lv3 = InGameHud.getEffectTexture(lv2);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, lv3, x + (wide ? 6 : 7), y + 7, 18, 18);
    }
    */

    private String getPotionDisplayString(StatusEffectInstance effect) {
        String time = StatusEffectUtil.getDurationText(effect, 1.0f, 20.0f).getString();
        String name = time + " " + I18n.translate(effect.getTranslationKey());
        if (effect.getAmplifier() == 1)
            name = name + " " + I18n.translate("enchantment.level.2");
        else if (effect.getAmplifier() == 2)
            name = name + " " + I18n.translate("enchantment.level.3");
        else if (effect.getAmplifier() == 3)
            name = name + " " + I18n.translate("enchantment.level.4");
        return name;
    }
}

