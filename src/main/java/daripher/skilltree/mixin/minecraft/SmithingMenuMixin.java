package daripher.skilltree.mixin.minecraft;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import daripher.itemproduction.block.entity.Interactive;
import daripher.skilltree.entity.player.PlayerExtension;
import daripher.skilltree.init.PSTItems;
import daripher.skilltree.item.ItemHelper;
import daripher.skilltree.item.gem.GemItem;
import daripher.skilltree.item.gem.bonus.GemBonusProvider;
import daripher.skilltree.item.gem.bonus.RandomGemBonusProvider;
import daripher.skilltree.recipe.upgrade.ChancedUpgradeRecipe;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {
  private @Shadow SmithingRecipe selectedRecipe;

  @SuppressWarnings("DataFlowIssue")
  private SmithingMenuMixin() {
    super(null, 0, null, null);
  }

  @Inject(
      method =
          "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
      at = @At("TAIL"))
  private void setPlayerIntoContainer(
      int windowId,
      Inventory inventory,
      ContainerLevelAccess levelAccess,
      CallbackInfo callbackInfo) {
    ((Interactive) inputSlots).setUser(inventory.player);
  }

  @Inject(method = "onTake", at = @At("HEAD"))
  private void changeRainbowJewelInsertionSeed(
      Player player, ItemStack itemStack, CallbackInfo callbackInfo) {
    ItemStack gemStack = inputSlots.getItem(SmithingMenu.ADDITIONAL_SLOT);
    if (gemStack.getItem() != PSTItems.GEM.get()) return;
    GemBonusProvider bonusProvider = GemItem.getGemType(gemStack).getBonusProvider(itemStack);
    if (bonusProvider instanceof RandomGemBonusProvider) {
      ((PlayerExtension) player).updateGemsRandomSeed();
    }
  }

  @Inject(
      method = "createResult",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/world/inventory/ResultContainer;"
                      + "setItem(ILnet/minecraft/world/item/ItemStack;)V",
              shift = At.Shift.BEFORE,
              ordinal = 1))
  private void itemProduced(
      CallbackInfo callbackInfo, @Local(ordinal = 0) LocalRef<ItemStack> stack) {
    ItemHelper.refreshDurabilityBonuses(stack.get());
    ItemHelper.refreshGemsAmount(stack.get());
  }@SuppressWarnings("DataFlowIssue")
  @Override
  public void clicked(
      int slotId, int button, @NotNull ClickType clickType, @NotNull Player player) {
    SmithingMenu menu = (SmithingMenu) (Object) this;
    if (slotId == SmithingMenu.RESULT_SLOT
        && !slots.get(slotId).getItem().isEmpty()
        && selectedRecipe instanceof ChancedUpgradeRecipe recipe
        && hasCraftingFailed(recipe)) {
      recipe.craftingFailed(menu, player);
      return;
    }
    super.clicked(slotId, button, clickType, player);
  }

  @SuppressWarnings("DataFlowIssue")
  private boolean hasCraftingFailed(ChancedUpgradeRecipe recipe) {
    return player.getRandom().nextFloat() >= recipe.getUpgradeChance((SmithingMenu) (Object) this);
  }
}
