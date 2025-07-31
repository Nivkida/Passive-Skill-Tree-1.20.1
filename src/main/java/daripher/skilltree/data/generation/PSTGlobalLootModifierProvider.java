package daripher.skilltree.data.generation;

import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.init.PSTItems;
import daripher.skilltree.loot.modifier.AddItemModifier;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.registries.RegistryObject;

public class PSTGlobalLootModifierProvider extends GlobalLootModifierProvider {
  public PSTGlobalLootModifierProvider(DataGenerator generator) {
    super(generator.getPackOutput(), SkillTreeMod.MOD_ID);
  }

  @Override
  protected void start() {
    upgradeMaterial(
        "zombie_piglins_alloys", "entities/zombified_piglin", PSTItems.ANCIENT_ALLOY_GILDED);
    upgradeMaterial("endermen_alloys", "entities/enderman", PSTItems.ANCIENT_ALLOY_LIGHTWEIGHT);
    upgradeMaterial("creepers_alloys", "entities/creeper", PSTItems.ANCIENT_ALLOY_CURATIVE);
    upgradeMaterial("spiders_alloys", "entities/spider", PSTItems.ANCIENT_ALLOY_TOXIC);
    upgradeMaterial(
        "wither_skeletons_alloys", "entities/creeper", PSTItems.ANCIENT_ALLOY_ENCHANTED);
  }

  private void upgradeMaterial(
      String modifierName, String lootTablePath, RegistryObject<Item> item) {
    addItem(modifierName, lootTablePath, item, 0.05f, 0.05f);
  }

  private void addItem(
      String modifierName,
      String lootTablePath,
      RegistryObject<Item> item,
      float chance,
      float lootingMultiplier) {
    add(
        modifierName,
        new AddItemModifier(
            new ItemStack(item.get()),
            LootTableIdCondition.builder(new ResourceLocation(lootTablePath)).build(),
            LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(
                    chance, lootingMultiplier)
                .build()));
  }
}
