package daripher.skilltree.enchantment;

import daripher.skilltree.SkillTreeMod;
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

@Mod.EventBusSubscriber(modid = SkillTreeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MagicFlowEnchantment extends CraftableEnchantment implements SkillBonusEnchantment {
  public MagicFlowEnchantment() {
    super(3, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
  }

  @Override
  protected boolean checkCompatibility(@NotNull Enchantment other) {
    return other != Enchantments.BANE_OF_ARTHROPODS && other != Enchantments.FIRE_ASPECT;
  }

  @Override
  public SkillBonus<?> getBonus(int enchantmentLevel) {
    return new DamageConversionBonus(0.05f * enchantmentLevel, NoneDamageCondition.INSTANCE, new MagicDamageCondition());
  }
}
