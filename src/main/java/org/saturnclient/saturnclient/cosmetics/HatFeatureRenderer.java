package org.saturnclient.saturnclient.cosmetics;

import org.saturnclient.saturnclient.auth.SaturnPlayer;
import org.saturnclient.saturnclient.auth.Auth;

import org.saturnclient.saturnclient.SaturnClient;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class HatFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public HatFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta,
            float animationProgress, float headYaw, float headPitch) {
        ItemStack headItem = entity.getInventory().armor.get(0);
        String uuid = entity.getUuidAsString().replace("-", "");
        if (headItem.isEmpty() && uuid != null) {
            SaturnPlayer player = Auth.players.get(uuid);
            if (player == null || player.hat == null || player.hat.isEmpty()) {
                return;
            }

            matrices.push();

            this.getContextModel().head.rotate(matrices);
            matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(180.0f));
            matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));

            ItemStack customHat = new ItemStack(Items.STICK);
            customHat.set(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(4389023));
            ItemRenderer itemRenderer = SaturnClient.client.getItemRenderer();

            itemRenderer.renderItem(
                    customHat,
                    ModelTransformationMode.THIRD_PERSON_RIGHT_HAND,
                    light,
                    OverlayTexture.DEFAULT_UV,
                    matrices,
                    vertexConsumers,
                    null,
                    0);

            matrices.pop();
        }
    }
}