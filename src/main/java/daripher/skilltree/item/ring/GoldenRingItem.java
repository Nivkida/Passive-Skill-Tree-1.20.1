package daripher.skilltree.item.ring;

import daripher.skilltree.item.ItemBonusProvider;
import daripher.skilltree.skill.bonus.item.ItemBonus;
import daripher.skilltree.skill.bonus.item.ItemSkillBonus;
import daripher.skilltree.skill.bonus.player.DamageAvoidanceBonus;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

public class GoldenRingItem extends RingItem implements ItemBonusProvider {
  @NotNull
  @Override
  public List<ItemBonus<?>> getItemBonuses(ItemStack itemStack) {
    return List.of(new ItemSkillBonus(new DamageAvoidanceBonus(0.01f)));
  }

  @Override
  public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
    return true;
  }
}
