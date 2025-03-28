package org.saturnclient.saturnclient.cosmetics;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.Identifier;

public class CustomHeadFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    public CustomHeadFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
            PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        ItemStack headItem = state.equippedHeadStack;
        if (headItem.isEmpty()) {
            matrices.push();

            // Apply the head's rotations to align with the player's head orientation
            this.getContextModel().head.rotate(matrices);

            // Scale the item to fit properly
            matrices.scale(0.5f, .5f, 0.5f);

            // Position the item properly
            matrices.translate(0.0f, -0.8f, 0.0f);

            // Rotate the item 180 degrees around the X-axis to fix the upside-down issue
            matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X.rotationDegrees(180.0f));

            // Create an ItemStack for the custom head item
            ItemStack customHat = new ItemStack(Items.STICK);
            customHat.set(DataComponentTypes.ITEM_MODEL, Identifier.of("saturnclient:hat"));

            // Get the ItemRenderer instance
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

            // Render the item as headwear
            itemRenderer.renderItem(
                    customHat,
                    ModelTransformationMode.HEAD,
                    light,
                    OverlayTexture.DEFAULT_UV,
                    matrices,
                    vertexConsumers,
                    null, // World can be null for entity rendering
                    0 // Seed for randomness, not needed here
            );

            // Pop the matrix stack to restore previous transformations
            matrices.pop();
        }
    }
}