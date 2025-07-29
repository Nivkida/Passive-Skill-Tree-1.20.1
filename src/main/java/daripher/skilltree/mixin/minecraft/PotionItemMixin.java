package daripher.skilltree.mixin.minecraft;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import daripher.skilltree.init.PSTEnchantments;
import daripher.skilltree.potion.PotionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PotionItem.class)
public class PotionItemMixin extends Item {
  @SuppressWarnings("DataFlowIssue")
  private PotionItemMixin() {
    super(null);
  }

  @Override
  public @NotNull Component getName(@NotNull ItemStack stack) {
    if (PotionHelper.isMixture(stack)) return getMixtureName(stack);
    if (PotionHelper.getDurationMultiplier(stack) != 1f || PotionHelper.getAmplifierBonus(stack) != 0) {
      return getSuperiorPotionName(stack);
    }
    return super.getName(stack);
  }

  protected Component getMixtureName(ItemStack stack) {
    String mixtureId = getMixtureId(stack);
    MutableComponent translatedName = Component.translatable(mixtureId);
    // no special name
    if (translatedName.getString()
        .equals(mixtureId)) {
      return Component.translatable(getDescriptionId() + ".mixture");
    }
    return translatedName;
  }

  protected Component getSuperiorPotionName(ItemStack stack) {
    Component potionName = super.getName(stack);
    return Component.translatable("potion.superior", potionName);
  }

  @Inject(method = "appendHoverText", at = @At("TAIL"))
  public void addAdvancedTooltip(ItemStack itemStack, Level level, List<Component> components, TooltipFlag tooltipFlag, CallbackInfo callbackInfo) {
    if (tooltipFlag != TooltipFlag.Default.ADVANCED) return;
    addAdvancedTooltip(itemStack, components);
  }

  private void addAdvancedTooltip(ItemStack itemStack, List<Component> components) {
    if (PotionHelper.isMixture(itemStack)) {
      PotionUtils.getMobEffects(itemStack)
          .stream()
          .map(MobEffectInstance::getEffect)
          .map(MobEffect::getDescriptionId)
          .map(s -> s.replaceAll("effect.", ""))
          .map(Component::literal)
          .map(c -> c.withStyle(ChatFormatting.DARK_GRAY))
          .forEach(components::add);
    }
  }

  protected String getMixtureId(ItemStack itemStack) {
    StringBuilder name = new StringBuilder(getDescriptionId() + ".mixture");
    PotionUtils.getMobEffects(itemStack)
        .stream()
        .map(MobEffectInstance::getEffect)
        .map(MobEffect::getDescriptionId)
        .map(id -> id.replaceAll("effect.", ""))
        .forEach(id -> name.append(".")
            .append(id));
    return name.toString();
  }

  @WrapWithCondition(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"))
  private boolean shouldConsumePotion(ItemStack itemStack, int decrement) {
    return itemStack.getEnchantmentLevel(PSTEnchantments.BOTTOMLESS_FLASK.get()) == 0;
  }

  @Override
  public boolean isEnchantable(@NotNull ItemStack itemStack) {
    return true;
  }

  @Inject(method = "finishUsingItem", at = @At("HEAD"))
  private void setBottomlessFlaskCooldown(ItemStack itemStack, Level level, LivingEntity user,
                                          CallbackInfoReturnable<InteractionResultHolder<ItemStack>> callbackInfo) {
    if (!(user instanceof Player player)) return;
    if (itemStack.getEnchantmentLevel(PSTEnchantments.BOTTOMLESS_FLASK.get()) > 0) {
      player.getCooldowns()
          .addCooldown(this, 60);
    }
  }
}
