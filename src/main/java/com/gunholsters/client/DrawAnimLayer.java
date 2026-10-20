package com.gunholsters.client;

import com.gunholsters.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;

/** Animacion: el arma viaja de la funda a la mano (o de la mano a la funda). */
public class DrawAnimLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public DrawAnimLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) { super(parent); }

    @Override
    public void render(PoseStack ps, MultiBufferSource buf, int light, AbstractClientPlayer player, float limbSwing,
            float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ClientAnimations.Anim a = ClientAnimations.get(player.getId());
        if (a == null) return;
        float elapsed = (player.level().getGameTime() - a.start) + partialTick;
        float p = Mth.clamp(elapsed / a.duration, 0f, 1f);
        p = p * p * (3 - 2 * p); // smoothstep
        double t = a.draw ? p : 1 - p;            // 0 = funda, 1 = mano

        double[] holster = HolsterPose.values(a.slotType == 0 ? Config.WAIST : Config.BACK);
        double[] pose = HolsterPose.lerp(holster, HolsterPose.HAND, t);
        pose[1] -= Math.sin(Math.PI * t) * 0.12; // pequeno arco hacia arriba

        ps.pushPose();
        getParentModel().body.translateAndRotate(ps);
        HolsterPose.apply(ps, pose);
        HolsterRenderer.renderGun(player, a.stack, ps, buf, light);
        ps.popPose();
    }
}
