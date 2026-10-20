package com.gunholsters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public final class GunHelper {
    public static final String SLOT_WAIST = "holster_waist";
    public static final String SLOT_BACK = "holster_back";
    public enum Kind { SIDEARM, LONG }

    private static final Map<Class<?>, Boolean> CACHE = new ConcurrentHashMap<>();

    /** Cualquier clase que herede de Point Blank "GunItem" (incluye armas del Civilian Gun Pack). */
    public static boolean isGun(Item item) {
        return CACHE.computeIfAbsent(item.getClass(), c -> {
            for (Class<?> k = c; k != null; k = k.getSuperclass())
                if (k.getSimpleName().equals("GunItem")) return true;
            return false;
        });
    }

    public static Kind kind(Item item) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        if (id == null) return Kind.LONG;
        String p = id.getPath().toLowerCase();
        for (String s : Config.SIDEARM_KEYWORDS.get()) if (p.contains(s.toLowerCase())) return Kind.SIDEARM;
        return Kind.LONG;
    }

    public static Kind kind(ItemStack s) { return kind(s.getItem()); }
    public static String slotFor(Kind k) { return k == Kind.SIDEARM ? SLOT_WAIST : SLOT_BACK; }
    public static int slotType(String slotId) { return SLOT_WAIST.equals(slotId) ? 0 : 1; }
}
