package com.gunholsters.client;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class ClientAnimations {
    public static class Anim {
        public final int slotType, duration; public final boolean draw; public final ItemStack stack; public final long start;
        Anim(int s, boolean d, ItemStack st, long start, int dur) { slotType = s; draw = d; stack = st; this.start = start; duration = dur; }
    }
    private static final Map<Integer, Anim> ANIMS = new HashMap<>();

    public static void start(int entityId, int slotType, boolean draw, ItemStack stack, int dur) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        ANIMS.put(entityId, new Anim(slotType, draw, stack, mc.level.getGameTime(), dur));
    }
    public static Anim get(int entityId) { return ANIMS.get(entityId); }
    public static boolean isActive(int entityId, int slotType) {
        Anim a = ANIMS.get(entityId); return a != null && a.slotType == slotType;
    }
    public static void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) { ANIMS.clear(); return; }
        long now = mc.level.getGameTime();
        ANIMS.values().removeIf(a -> now - a.start > a.duration + 1);
    }
}
