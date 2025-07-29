package daripher.skilltree.recipe.upgrade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;

public abstract class ChancedUpgradeRecipe extends SmithingTransformRecipe {
  public ChancedUpgradeRecipe(
      ResourceLocation id,
      Ingredient template,
      Ingredient base,
      Ingredient addition,
      ItemStack result) {
    super(id, template, base, addition, result);
  }

  public abstract float getUpgradeChance(SmithingMenu menu);

  public abstract void craftingFailed(SmithingMenu menu, Player player);
}
