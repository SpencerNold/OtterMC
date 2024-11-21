package io.github.ottermc.pvp.transformers;

import agent.transformation.Callback;
import agent.transformation.Injector;
import agent.transformation.Target;
import agent.transformation.Transformer;
import io.github.ottermc.events.EventBus;
import io.github.ottermc.pvp.listeners.AddChatMessageListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

@Transformer(name =  "net/minecraft/client/entity/EntityPlayerSP")
public class EntityPlayerSPTransformer {

	@Injector(name = "addChatMessage(Lnet/minecraft/util/IChatComponent;)V", target = Target.HEAD)
	public void onAddChatMessage(EntityPlayerSP player, IChatComponent component, Callback callback) {
		AddChatMessageListener.AddChatMessageEvent event = new AddChatMessageListener.AddChatMessageEvent(component.getUnformattedText());
		EventBus.fire(event);
		component = new ChatComponentText(event.getMessage());
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(component);
		callback.setCanceled(true);
	}
	
	@Injector(name = "addChatComponentMessage(Lnet/minecraft/util/IChatComponent;)V", target = Target.HEAD)
	public void onAddChatComponentMessage(EntityPlayerSP player, IChatComponent component, Callback callback) {
		AddChatMessageListener.AddChatMessageEvent event = new AddChatMessageListener.AddChatMessageEvent(component.getUnformattedText());
		EventBus.fire(event);
		component = new ChatComponentText(event.getMessage());
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(component);
		callback.setCanceled(true);
	}
}
