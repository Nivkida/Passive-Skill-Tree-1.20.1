package daripher.skilltree.enchantment;

import daripher.skilltree.skill.bonus.SkillBonus;

public interface SkillBonusEnchantment {
  SkillBonus<?> getBonus(int enchantmentLevel);
}
