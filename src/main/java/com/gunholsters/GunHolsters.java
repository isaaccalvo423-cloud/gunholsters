package com.gunholsters;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GunHolsters.MODID)
public class GunHolsters {
    public static final String MODID = "gunholsters";

    public GunHolsters() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT);
        bus.addListener((FMLCommonSetupEvent e) -> e.enqueueWork(ModNetwork::register));
        bus.addListener(this::addPacks);
        MinecraftForge.EVENT_BUS.register(new CommonEvents());
    }

    /** Pack de datos generado en tiempo real: mete TODAS las armas GunItem (Point Blank + Civilian Gun Pack) en los tags de Curios. */
    private void addPacks(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.SERVER_DATA) return;
        Pack pack = Pack.readMetaAndCreate("gunholsters_generated", Component.literal("GunHolsters generated tags"),
                true, id -> new GeneratedTagPack(), PackType.SERVER_DATA, Pack.Position.TOP, PackSource.BUILT_IN);
        event.addRepositorySource(consumer -> consumer.accept(pack));
    }
}
