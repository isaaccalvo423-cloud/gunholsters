package com.gunholsters.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraftforge.common.ForgeConfigSpec;

public class HolsterPose {
    /** Pose aproximada de la mano derecha (espacio del cuerpo) */
    public static final double[] HAND = {-0.36, 0.70, -0.22, 0, 0, 0, 1.0};

    public static double[] values(ForgeConfigSpec.DoubleValue[] c) {
        double[] d = new double[7];
        for (int i = 0; i < 7; i++) d[i] = c[i].get();
        return d;
    }

    public static double[] lerp(double[] a, double[] b, double t) {
        double[] d = new double[7];
        for (int i = 0; i < 7; i++) d[i] = a[i] + (b[i] - a[i]) * t;
        return d;
    }

    /** Se aplica sobre el espacio del cuerpo (tras body.translateAndRotate). */
    public static void apply(PoseStack ps, double[] c) {
        ps.translate(c[0], c[1], c[2]);
        ps.mulPose(Axis.ZP.rotationDegrees(180)); // el espacio de capas del modelo esta invertido
        ps.mulPose(Axis.YP.rotationDegrees((float) c[4]));
        ps.mulPose(Axis.XP.rotationDegrees((float) c[3]));
        ps.mulPose(Axis.ZP.rotationDegrees((float) c[5]));
        float s = (float) c[6];
        ps.scale(s, s, s);
    }
}
