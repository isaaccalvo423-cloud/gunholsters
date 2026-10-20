package com.gunholsters.client;

import com.gunholsters.GunHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

/** Dibuja el arma guardada en la cintura/espalda (visible en 3a persona y para otros jugadores). */
public class HolsterRenderer implements ICurioRenderer {

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext ctx, PoseStack ps,
            RenderLayerParent<T, M> parent, MultiBufferSource buf, int light, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingEntity entity = ctx.entity();
        int slotType = GunHelper.slotType(ctx.identifier());
        if (ClientAnimations.isActive(entity.getId(), slotType)) return; // la capa de animacion lo dibuja
        if (!(parent.getModel() instanceof HumanoidModel<?> hm)) return;

        ps.pushPose();
        hm.body.translateAndRotate(ps);
        HolsterPose.apply(ps, HolsterPose.values(slotType == 0 ? com.gunholsters.Config.WAIST : com.gunholsters.Config.BACK));
        renderGun(entity, stack, ps, buf, light);
        ps.popPose();
    }

    public static void renderGun(LivingEntity entity, ItemStack stack, PoseStack ps, MultiBufferSource buf, int light) {
        Minecraft.getInstance().getItemRenderer().renderStatic(entity, stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                false, ps, buf, entity.level(), light, OverlayTexture.NO_OVERLAY, entity.getId());
    }
}
