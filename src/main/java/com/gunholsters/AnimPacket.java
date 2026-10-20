package com.gunholsters;

import com.gunholsters.client.ClientAnimations;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

/** Servidor -> clientes: inicia la animacion de desenfundar/enfundar. */
public class AnimPacket {
    final int entityId, slotType, duration;
    final boolean draw;
    final ItemStack stack;

    public AnimPacket(int entityId, int slotType, boolean draw, ItemStack stack, int duration) {
        this.entityId = entityId; this.slotType = slotType; this.draw = draw; this.stack = stack; this.duration = duration;
    }
    public static void encode(AnimPacket m, FriendlyByteBuf b) {
        b.writeVarInt(m.entityId); b.writeVarInt(m.slotType); b.writeBoolean(m.draw); b.writeItem(m.stack); b.writeVarInt(m.duration);
    }
    public static AnimPacket decode(FriendlyByteBuf b) {
        return new AnimPacket(b.readVarInt(), b.readVarInt(), b.readBoolean(), b.readItem(), b.readVarInt());
    }
    public static void handle(AnimPacket m, Supplier<NetworkEvent.Context> s) {
        NetworkEvent.Context ctx = s.get();
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientAnimations.start(m.entityId, m.slotType, m.draw, m.stack, m.duration)));
        ctx.setPacketHandled(true);
    }
}
