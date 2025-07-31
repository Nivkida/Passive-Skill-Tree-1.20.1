package daripher.skilltree.mixin;

import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SmithingMenu.class)
public interface SmithingMenuAccessor {
  @Accessor @Nullable
  SmithingRecipe getSelectedRecipe();
}
