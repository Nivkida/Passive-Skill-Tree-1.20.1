package daripher.skilltree.item.ring;

import daripher.skilltree.item.ItemBonusProvider;
import daripher.skilltree.skill.bonus.item.ItemBonus;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class RingItem extends Item implements ICurioItem, ItemBonusProvider {
  public RingItem() {
    super(new Properties().stacksTo(1));
  }

  @Override
  public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
    getItemBonuses(stack).stream().map(ItemBonus::getTooltip).forEach(tooltips::add);
    return tooltips;
  }

  @Override
  public @NotNull List<ItemBonus<?>> getItemBonuses(ItemStack itemStack) {
    return List.of();
  }
}
