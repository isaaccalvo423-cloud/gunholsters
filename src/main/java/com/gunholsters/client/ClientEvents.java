package com.gunholsters.client;

import com.gunholsters.DrawRequestPacket;
import com.gunholsters.GunHolsters;
import com.gunholsters.ModNetwork;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = GunHolsters.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    public static final KeyMapping DRAW_SIDEARM = new KeyMapping("key.gunholsters.draw_sidearm", GLFW.GLFW_KEY_G, "key.categories.gunholsters");
    public static final KeyMapping DRAW_LONG = new KeyMapping("key.gunholsters.draw_long", GLFW.GLFW_KEY_H, "key.categories.gunholsters");

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        ClientAnimations.tick();
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        while (DRAW_SIDEARM.consumeClick()) ModNetwork.CHANNEL.sendToServer(new DrawRequestPacket(0));
        while (DRAW_LONG.consumeClick()) ModNetwork.CHANNEL.sendToServer(new DrawRequestPacket(1));
    }
}
