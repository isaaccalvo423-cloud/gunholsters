package com.gunholsters;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String PROTO = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(GunHolsters.MODID, "main"), () -> PROTO, PROTO::equals, PROTO::equals);

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, DrawRequestPacket.class, DrawRequestPacket::encode, DrawRequestPacket::decode, DrawRequestPacket::handle);
        CHANNEL.registerMessage(id++, AnimPacket.class, AnimPacket::encode, AnimPacket::decode, AnimPacket::handle);
    }
}
