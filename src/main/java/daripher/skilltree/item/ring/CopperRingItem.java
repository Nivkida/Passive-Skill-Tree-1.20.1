package daripher.skilltree.item.ring;

import daripher.skilltree.item.ItemBonusProvider;
import daripher.skilltree.skill.bonus.item.ItemBonus;
import daripher.skilltree.skill.bonus.item.ItemSkillBonus;
import daripher.skilltree.skill.bonus.player.DamageTakenBonus;
import java.util.List;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CopperRingItem extends RingItem implements ItemBonusProvider {
  @Override
  public @NotNull List<ItemBonus<?>> getItemBonuses(ItemStack itemStack) {
    return List.of(new ItemSkillBonus(new DamageTakenBonus(-0.01f, Operation.MULTIPLY_BASE)));
  }
}
