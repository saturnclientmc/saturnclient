package org.saturnclient.saturnclient.cosmetics;

import org.saturnclient.saturnclient.auth.SaturnPlayer;
import org.saturnclient.saturnclient.auth.Auth;

import org.saturnclient.saturnclient.SaturnClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.Identifier;

public class HatFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    public HatFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        ItemStack headItem = state.equippedHeadStack;
        String uuid = Auth.playerNames.get(state.name);
        if (headItem.isEmpty() && uuid != null && !state.invisible) {
            SaturnPlayer player = Auth.players.get(uuid);
            if (player == null || player.hat == null || player.hat.isEmpty()) {
                return;
            }

            matrices.push();

            this.getContextModel().head.rotate(matrices);
            matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(180.0f));
            matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));

            ItemStack customHat = new ItemStack(Items.STICK);
            customHat.set(DataComponentTypes.ITEM_MODEL,
                    Identifier.of("saturnclient:hat_" + player.hat));
            customHat.set(DataComponentTypes.EQUIPPABLE,
                    EquippableComponent.builder(EquipmentSlot.HEAD).build());

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