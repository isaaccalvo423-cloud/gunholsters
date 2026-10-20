package com.gunholsters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

/** Genera curios:tags/items/holster_waist|holster_back con todas las armas GunItem registradas. */
public class GeneratedTagPack implements PackResources {

    private Map<ResourceLocation, byte[]> build() {
        JsonArray waist = new JsonArray(), back = new JsonArray();
        for (Map.Entry<net.minecraft.resources.ResourceKey<Item>, Item> e : ForgeRegistries.ITEMS.getEntries()) {
            Item item = e.getValue();
            if (!GunHelper.isGun(item)) continue;
            String id = e.getKey().location().toString();
            if (GunHelper.kind(item) == GunHelper.Kind.SIDEARM) waist.add(id); else back.add(id);
        }
        Map<ResourceLocation, byte[]> m = new HashMap<>();
        m.put(new ResourceLocation("curios", "tags/items/holster_waist.json"), json(waist));
        m.put(new ResourceLocation("curios", "tags/items/holster_back.json"), json(back));
        return m;
    }

    private static byte[] json(JsonArray values) {
        JsonObject o = new JsonObject();
        o.addProperty("replace", false);
        o.add("values", values);
        return o.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Nullable @Override public IoSupplier<java.io.InputStream> getRootResource(String... p) { return null; }

    @Nullable @Override
    public IoSupplier<java.io.InputStream> getResource(PackType type, ResourceLocation loc) {
        if (type != PackType.SERVER_DATA) return null;
        byte[] b = build().get(loc);
        return b == null ? null : () -> new ByteArrayInputStream(b);
    }

    @Override
    public void listResources(PackType type, String ns, String path, ResourceOutput out) {
        if (type != PackType.SERVER_DATA) return;
        build().forEach((k, v) -> {
            if (k.getNamespace().equals(ns) && k.getPath().startsWith(path)) out.accept(k, () -> new ByteArrayInputStream(v));
        });
    }

    @Override public Set<String> getNamespaces(PackType type) { return type == PackType.SERVER_DATA ? Set.of("curios") : Set.of(); }

    @SuppressWarnings("unchecked")
    @Nullable @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> s) {
        if (s == PackMetadataSection.TYPE) return (T) new PackMetadataSection(Component.literal("GunHolsters generated tags"), 15);
        return null;
    }

    @Override public String packId() { return "gunholsters_generated"; }
    @Override public void close() {}
}
