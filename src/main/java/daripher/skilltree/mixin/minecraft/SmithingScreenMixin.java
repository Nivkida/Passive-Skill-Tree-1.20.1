package daripher.skilltree.mixin.minecraft;

import daripher.skilltree.mixin.SmithingMenuAccessor;
import daripher.skilltree.recipe.upgrade.ChancedUpgradeRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ItemCombinerScreen<SmithingMenu> {
  @SuppressWarnings("DataFlowIssue")
  private SmithingScreenMixin() {
    super(null, null, null, null);
  }

  @Inject(method = "renderOnboardingTooltips", at = @At("TAIL"))
  private void renderUpgradeChance(
      GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo callback) {
    ItemStack baseItem = menu.slots.get(SmithingMenu.BASE_SLOT).getItem();
    ItemStack additionalItem = menu.slots.get(SmithingMenu.ADDITIONAL_SLOT).getItem();
    ItemStack resultItem = menu.slots.get(SmithingMenu.RESULT_SLOT).getItem();
    if (baseItem.isEmpty() || additionalItem.isEmpty() || resultItem.isEmpty()) return;
    SmithingRecipe recipe = ((SmithingMenuAccessor) menu).getSelectedRecipe();
    if (!(recipe instanceof ChancedUpgradeRecipe chancedRecipe)) return;
    int chance = (int) (chancedRecipe.getUpgradeChance(menu) * 100);
    if (chance >= 100) return;
    Component chanceDescription =
        Component.translatable("upgrade_recipe.chance", chance).withStyle(ChatFormatting.DARK_RED);
    int textX = leftPos + titleLabelX;
    int textY = topPos + titleLabelY + 12;
    int textColor = 0x404040;
    graphics.drawString(font, chanceDescription, textX, textY, textColor, false);
  }
}
