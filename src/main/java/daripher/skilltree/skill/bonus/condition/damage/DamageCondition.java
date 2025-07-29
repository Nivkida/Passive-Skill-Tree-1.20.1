package daripher.skilltree.skill.bonus.condition.damage;

import daripher.skilltree.init.PSTRegistries;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public interface DamageCondition {
  boolean met(DamageSource source);

  default String getDescriptionId() {
    ResourceLocation id = PSTRegistries.DAMAGE_CONDITIONS.get().getKey(getSerializer());
    Objects.requireNonNull(id);
    return "damage_condition.%s.%s".formatted(id.getNamespace(), id.getPath());
  }

  default MutableComponent getTooltip() {
    return Component.translatable(getDescriptionId());
  }

  default MutableComponent getTooltip(String type) {
    return Component.translatable(getDescriptionId() + "." + type);
  }

  Serializer getSerializer();

  default DamageSource createDamageSource(Player player) {
    throw new UnsupportedOperationException(
        "Can not create damage source from " + getDescriptionId());
  }

  default boolean canCreateDamageSource() {
    return false;
  }

  interface Serializer extends daripher.skilltree.data.serializers.Serializer<DamageCondition> {
    DamageCondition createDefaultInstance();
  }
}
