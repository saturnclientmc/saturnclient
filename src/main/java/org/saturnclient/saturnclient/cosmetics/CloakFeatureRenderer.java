package org.saturnclient.saturnclient.cosmetics;

import org.saturnclient.saturnclient.auth.SaturnSocket;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.equipment.EquipmentModelLoader;
import net.minecraft.client.render.entity.equipment.EquipmentModel.LayerType;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.PlayerCapeModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CloakFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
    private final BipedEntityModel<PlayerEntityRenderState> model;
    private final EquipmentModelLoader equipmentModelLoader;

    public CloakFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context,
            LoadedEntityModels modelLoader, EquipmentModelLoader equipmentModelLoader) {
        super(context);
        this.model = new PlayerCapeModel<PlayerEntityRenderState>(
                modelLoader.getModelPart(EntityModelLayers.PLAYER_CAPE));
        this.equipmentModelLoader = equipmentModelLoader;
    }

    private boolean hasCustomModelForLayer(ItemStack stack, EquipmentModel.LayerType layerType) {
        EquippableComponent equippableComponent = (EquippableComponent) stack.get(DataComponentTypes.EQUIPPABLE);
        if (equippableComponent != null && !equippableComponent.assetId().isEmpty()) {
            EquipmentModel equipmentModel = this.equipmentModelLoader
                    .get((RegistryKey<EquipmentAsset>) equippableComponent.assetId().get());
            return !equipmentModel.getLayers(layerType).isEmpty();
        } else {
            return false;
        }
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
            PlayerEntityRenderState playerEntityRenderState, float f, float g) {
        if (!playerEntityRenderState.invisible && playerEntityRenderState.capeVisible) {
            SkinTextures skinTextures = playerEntityRenderState.skinTextures;

            if (skinTextures.capeTexture() == null) {
                String name = playerEntityRenderState.name;
                String uuid = SaturnSocket.playerNames.get(name);
                if (uuid == null || !SaturnSocket.players.containsKey(uuid)) {
                    return;
                }

                Identifier customCape = Cloaks.getCurrentCloakTexture(uuid);

                if (customCape != null && !this.hasCustomModelForLayer(playerEntityRenderState.equippedChestStack, LayerType.WINGS)) {
                    matrixStack.push();
                    if (this.hasCustomModelForLayer(playerEntityRenderState.equippedChestStack, LayerType.HUMANOID)) {
                        matrixStack.translate(0.0F, -0.053125F, 0.06875F);
                    }

                    VertexConsumer vertexConsumer = vertexConsumerProvider
                            .getBuffer(RenderLayer.getEntityAlpha(customCape));
                    ((PlayerEntityModel) this.getContextModel()).copyTransforms(this.model);
                    this.model.setAngles(playerEntityRenderState);
                    this.model.render(matrixStack, vertexConsumer, 0xF000F0, OverlayTexture.DEFAULT_UV);
                    matrixStack.pop();
                }
            }
        }
    }
}
