package com.gunholsters;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class DrawHandler {
    private static final Map<UUID, int[]> PENDING = new HashMap<>(); // uuid -> {slotType, ticksLeft}

    private static IDynamicStackHandler stacks(ServerPlayer p, String id) {
        return CuriosApi.getCuriosInventory(p).resolve()
                .flatMap(h -> h.getStacksHandler(id)).map(ICurioStacksHandler::getStacks).orElse(null);
    }

    private static void send(ServerPlayer p, int slotType, boolean draw, ItemStack stack, int dur) {
        ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> p),
                new AnimPacket(p.getId(), slotType, draw, stack.copy(), dur));
    }

    private static void sound(ServerPlayer p, float pitch) {
        p.level().playSound(null, p.blockPosition(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 0.8f, pitch);
    }

    public static void request(ServerPlayer p, int slotType) {
        if (PENDING.containsKey(p.getUUID())) return;
        GunHelper.Kind kind = slotType == 0 ? GunHelper.Kind.SIDEARM : GunHelper.Kind.LONG;
        IDynamicStackHandler st = stacks(p, GunHelper.slotFor(kind));
        if (st == null) return;
        ItemStack held = p.getMainHandItem();
        ItemStack slot = st.getStackInSlot(0);
        boolean heldFits = GunHelper.isGun(held.getItem()) && GunHelper.kind(held) == kind;
        int dur = Config.DRAW_TICKS.get();

        if (heldFits && slot.isEmpty()) { // ENFUNDAR (inmediato, la animacion lo cubre)
            st.setStackInSlot(0, held.copy());
            p.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            send(p, slotType, false, held, dur);
            sound(p, 1.0f);
            return;
        }
        if (slot.isEmpty()) return;
        // DESENFUNDAR (el cambio real se hace al terminar la animacion)
        PENDING.put(p.getUUID(), new int[]{slotType, dur});
        send(p, slotType, true, slot, dur);
        sound(p, 1.3f);
    }

    public static void tick(ServerPlayer p) {
        int[] pd = PENDING.get(p.getUUID());
        if (pd == null) return;
        if (!p.isAlive()) { PENDING.remove(p.getUUID()); return; }
        if (--pd[1] > 0) return;
        PENDING.remove(p.getUUID());

        GunHelper.Kind kind = pd[0] == 0 ? GunHelper.Kind.SIDEARM : GunHelper.Kind.LONG;
        IDynamicStackHandler st = stacks(p, GunHelper.slotFor(kind));
        if (st == null) return;
        ItemStack slot = st.getStackInSlot(0);
        if (slot.isEmpty()) return;
        ItemStack held = p.getMainHandItem();
        ItemStack toSlot = ItemStack.EMPTY;
        if (!held.isEmpty()) {
            if (GunHelper.isGun(held.getItem()) && GunHelper.kind(held) == kind) toSlot = held.copy();
            else if (!p.getInventory().add(held.copy())) p.drop(held.copy(), false);
        }
        st.setStackInSlot(0, toSlot);
        p.setItemInHand(InteractionHand.MAIN_HAND, slot.copy());
    }

    public static void clear(UUID id) { PENDING.remove(id); }
}
