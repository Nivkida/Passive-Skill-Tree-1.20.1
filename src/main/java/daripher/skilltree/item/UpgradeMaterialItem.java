package daripher.skilltree.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UpgradeMaterialItem extends Item {
  public UpgradeMaterialItem() {
    super(new Properties());
  }

  @Override
  public boolean isFoil(@NotNull ItemStack itemStack) {
    return true;
  }

  @Override
  public void appendHoverText(
      @NotNull ItemStack itemStack,
      @Nullable Level level,
      @NotNull List<Component> components,
      @NotNull TooltipFlag isAdvanced) {
    components.add(
        Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GOLD));
    components.add(
        Component.translatable("ancient_material.tooltip").withStyle(ChatFormatting.GOLD));
  }
}
