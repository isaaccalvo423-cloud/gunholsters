package com.gunholsters;

import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec COMMON;
    public static final ForgeConfigSpec CLIENT;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SIDEARM_KEYWORDS;
    public static final ForgeConfigSpec.IntValue DRAW_TICKS;
    /** x, y, z, rotX, rotY, rotZ, scale (espacio del cuerpo del jugador, en bloques) */
    public static final ForgeConfigSpec.DoubleValue[] WAIST;
    public static final ForgeConfigSpec.DoubleValue[] BACK;

    static {
        ForgeConfigSpec.Builder c = new ForgeConfigSpec.Builder();
        SIDEARM_KEYWORDS = c.comment("Si el id del item contiene alguna de estas palabras, es arma corta (cintura). El resto va a la espalda.")
                .defineList("sidearmKeywords", List.of("pistol", "revolver", "glock", "1911", "python", "deagle", "desert_eagle",
                        "usp", "p226", "p320", "p250", "beretta", "m92", "makarov", "tt33", "fiveseven", "five_seven", "cz75",
                        "magnum", "handgun", "mk23", "hk45", "luger", "walther", "ppk", "taurus", "g17", "g19", "m9a"),
                        o -> o instanceof String);
        DRAW_TICKS = c.comment("Duracion de la animacion de sacar el arma (ticks)").defineInRange("drawTicks", 10, 2, 60);
        COMMON = c.build();

        ForgeConfigSpec.Builder k = new ForgeConfigSpec.Builder();
        k.comment("Ajusta posicion/rotacion del arma en el cuerpo si no te cuadra con algun arma");
        WAIST = pose(k, "waist", -0.33, 0.62, 0.14, 0, 0, 0, 0.8);
        BACK = pose(k, "back", 0.0, 0.32, 0.24, 0, 0, 35, 0.9);
        CLIENT = k.build();
    }

    private static ForgeConfigSpec.DoubleValue[] pose(ForgeConfigSpec.Builder b, String name, double... d) {
        String[] n = {"x", "y", "z", "rotX", "rotY", "rotZ", "scale"};
        ForgeConfigSpec.DoubleValue[] out = new ForgeConfigSpec.DoubleValue[7];
        b.push(name);
        for (int i = 0; i < 7; i++) out[i] = b.defineInRange(n[i], d[i], -360.0, 360.0);
        b.pop();
        return out;
    }
}
