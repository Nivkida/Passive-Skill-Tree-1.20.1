package daripher.skilltree.enchantment;

import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.init.PSTEnchantmentCategories;
import daripher.skilltree.skill.bonus.SkillBonus;
import daripher.skilltree.skill.bonus.condition.damage.MagicDamageCondition;
import daripher.skilltree.skill.bonus.condition.damage.NoneDamageCondition;
import daripher.skilltree.skill.bonus.player.DamageConversionBonus;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

public class BottomlessFlaskEnchantment extends CraftableEnchantment {
  public BottomlessFlaskEnchantment() {
    super(1, PSTEnchantmentCategories.POTION, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
  }
}
