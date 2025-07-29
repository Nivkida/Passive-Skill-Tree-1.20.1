package daripher.skilltree.init;

import daripher.skilltree.skill.bonus.condition.item.EquipmentCondition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class PSTEnchantmentCategories {
  public static EnchantmentCategory SHIELD = EnchantmentCategory.create("shield", item -> EquipmentCondition.isShield(new ItemStack(item)));
  public static EnchantmentCategory POTION = EnchantmentCategory.create("potion", item -> item instanceof PotionItem);
}
