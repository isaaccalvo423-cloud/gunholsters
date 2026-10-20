package com.gunholsters;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

/** Cliente -> servidor: 0 = cintura, 1 = espalda */
public class DrawRequestPacket {
    final int slotType;
    public DrawRequestPacket(int slotType) { this.slotType = slotType; }
    public static void encode(DrawRequestPacket m, FriendlyByteBuf b) { b.writeVarInt(m.slotType); }
    public static DrawRequestPacket decode(FriendlyByteBuf b) { return new DrawRequestPacket(b.readVarInt()); }
    public static void handle(DrawRequestPacket m, Supplier<NetworkEvent.Context> s) {
        NetworkEvent.Context ctx = s.get();
        ctx.enqueueWork(() -> {
            ServerPlayer p = ctx.getSender();
            if (p != null) DrawHandler.request(p, m.slotType == 0 ? 0 : 1);
        });
        ctx.setPacketHandled(true);
    }
}
