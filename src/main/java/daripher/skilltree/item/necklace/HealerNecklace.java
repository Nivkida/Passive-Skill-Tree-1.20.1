package daripher.skilltree.item.necklace;

import daripher.skilltree.skill.bonus.item.ItemBonus;
import daripher.skilltree.skill.bonus.item.ItemSkillBonus;
import daripher.skilltree.skill.bonus.player.IncomingHealingBonus;
import java.util.List;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HealerNecklace extends NecklaceItem {
  @Override
  public @NotNull List<ItemBonus<?>> getItemBonuses(ItemStack itemStack) {
    return List.of(new ItemSkillBonus(new IncomingHealingBonus(0.05f)));
  }
}
