package org.saturnclient.impl.modules;

import org.saturnclient.feature.features.featuresinterfaces.NametagsInterface;
import org.saturnclient.modules.HealthRenderState;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.text.Text;

public class NametagsFabric implements NametagsInterface {

    public static class EntityStateImpl implements EntityState {

        private final LivingEntityRenderState state;

        public EntityStateImpl(LivingEntityRenderState state) {
            this.state = state;
        }

        @Override
        public String getCustomName() {
            Text name = state.customName;
            return name != null ? name.getString() : null;
        }

        @Override
        public float getHealth() {
            if (!(state instanceof HealthRenderState hrs))
                return 0.0f;

            return hrs.saturn$getHealth();
        }

        @Override
        public float getMaxHealth() {
            if (!(state instanceof HealthRenderState hrs))
                return 0.0f;

            return hrs.saturn$getMaxHealth();
        }

        @Override
        public EntityType getEntityType() {
            if (!(state instanceof HealthRenderState hrs))
                return null;

            return hrs.saturn$getEntityType();
        }
    }
}