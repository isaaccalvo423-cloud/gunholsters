package com.gunholsters.client;

import com.gunholsters.GunHelper;
import com.gunholsters.GunHolsters;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = GunHolsters.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent e) {
        e.enqueueWork(() -> {
            for (Item item : ForgeRegistries.ITEMS)
                if (GunHelper.isGun(item)) CuriosRendererRegistry.register(item, HolsterRenderer::new);
        });
    }

    @SubscribeEvent
    public static void keys(RegisterKeyMappingsEvent e) {
        e.register(ClientEvents.DRAW_SIDEARM);
        e.register(ClientEvents.DRAW_LONG);
    }

    @SubscribeEvent
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void layers(EntityRenderersEvent.AddLayers e) {
        for (String skin : new String[]{"default", "slim"}) {
            Object r = e.getSkin(skin);
            if (r instanceof PlayerRenderer pr) pr.addLayer(new DrawAnimLayer(pr));
        }
    }
}
