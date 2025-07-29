package daripher.skilltree.item;

import daripher.skilltree.skill.bonus.item.ItemBonus;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface ItemBonusProvider {
  @Nonnull
  List<ItemBonus<?>> getItemBonuses(ItemStack itemStack);

  default UUID getModifiersId(ItemStack itemStack) {
    CompoundTag tag = itemStack.getOrCreateTag();
    String idKey = "ItemBonusProviderId";
    if (!tag.contains(idKey)) {
      tag.putString(idKey, UUID.randomUUID().toString());
    }
    return UUID.fromString(tag.getString(idKey));
  }
}
