package daripher.skilltree.mixin.minecraft;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThrownPotion.class)
public abstract class ThrownPotionMixin extends ThrowableItemProjectile implements ItemSupplier {
  @SuppressWarnings("DataFlowIssue")
  private ThrownPotionMixin() {
    super(null, null);
  }

  @Redirect(
      method = "applySplash",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/world/entity/LivingEntity;"
                      + "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"))
  private boolean setAttackerOnHit(
      LivingEntity entity, MobEffectInstance effectInstance, Entity effectSource) {
    if (getOwner() instanceof Player player) {
      entity.setLastHurtByPlayer(player);
    }
    return entity.addEffect(effectInstance, effectSource);
  }
}
