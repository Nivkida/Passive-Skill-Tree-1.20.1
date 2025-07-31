package daripher.skilltree.data.generation;

import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.init.PSTAttributes;
import daripher.skilltree.init.PSTItems;
import daripher.skilltree.init.PSTTags;
import daripher.skilltree.recipe.builder.ItemUpgradeRecipeBuilder;
import daripher.skilltree.skill.bonus.condition.damage.MagicDamageCondition;
import daripher.skilltree.skill.bonus.condition.item.EquipmentCondition;
import daripher.skilltree.skill.bonus.item.ItemSkillBonus;
import daripher.skilltree.skill.bonus.player.AttributeBonus;
import daripher.skilltree.skill.bonus.player.DamageBonus;
import daripher.skilltree.skill.bonus.player.IncomingHealingBonus;
import daripher.skilltree.skill.bonus.player.LootDuplicationBonus;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class PSTRecipesProvider extends RecipeProvider {
  public PSTRecipesProvider(DataGenerator dataGenerator) {
    super(dataGenerator.getPackOutput());
  }

  @Override
  protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
    // rings
    ring(PSTItems.GOLDEN_RING, Tags.Items.NUGGETS_GOLD, consumer);
    ring(PSTItems.COPPER_RING, PSTTags.NUGGETS_COPPER, consumer);
    ring(PSTItems.IRON_RING, Tags.Items.NUGGETS_IRON, consumer);
    // resources
    packing(Items.COPPER_INGOT, PSTTags.NUGGETS_COPPER, consumer);
    unpacking(PSTItems.COPPER_NUGGET, Tags.Items.INGOTS_COPPER, consumer);
    // necklaces
    necklace(PSTItems.ASSASSIN_NECKLACE, Items.BONE, consumer);
    necklace(PSTItems.TRAVELER_NECKLACE, Items.FEATHER, consumer);
    necklace(PSTItems.HEALER_NECKLACE, Items.GHAST_TEAR, consumer);
    necklace(PSTItems.SIMPLE_NECKLACE, consumer);
    necklace(PSTItems.SCHOLAR_NECKLACE, Items.ENDER_PEARL, consumer);
    necklace(PSTItems.ARSONIST_NECKLACE, Items.FIRE_CHARGE, consumer);
    necklace(PSTItems.FISHERMAN_NECKLACE, Items.TROPICAL_FISH, consumer);
    // quviers
    quiver(PSTItems.QUIVER, consumer);
    quiver(PSTItems.ARMORED_QUIVER, Tags.Items.INGOTS_IRON, consumer);
    quiver(PSTItems.DIAMOND_QUIVER, Tags.Items.GEMS_DIAMOND, consumer);
    quiver(PSTItems.FIERY_QUIVER, Items.BLAZE_POWDER, consumer);
    quiver(PSTItems.GILDED_QUIVER, Tags.Items.INGOTS_GOLD, consumer);
    quiver(PSTItems.HEALING_QUIVER, Items.GHAST_TEAR, consumer);
    quiver(PSTItems.TOXIC_QUIVER, Items.FERMENTED_SPIDER_EYE, consumer);
    quiver(PSTItems.SILENT_QUIVER, Items.FEATHER, consumer);
    quiver(PSTItems.BONE_QUIVER, Items.BONE, consumer);
    // upgrades
    ItemUpgradeRecipeBuilder.create()
        .baseCondition(new EquipmentCondition(EquipmentCondition.Type.WEAPON))
        .additionalItem(Ingredient.of(PSTItems.ANCIENT_ALLOY_GILDED.get()))
        .itemBonus(
            new ItemSkillBonus(
                new LootDuplicationBonus(0.02f, 1f, LootDuplicationBonus.LootType.MOBS)))
        .save(consumer, getRecipeId("upgrades/weapons_double_loot"));
    ItemUpgradeRecipeBuilder.create()
        .baseCondition(new EquipmentCondition(EquipmentCondition.Type.BOOTS))
        .additionalItem(Ingredient.of(PSTItems.ANCIENT_ALLOY_LIGHTWEIGHT.get()))
        .itemBonus(
            new ItemSkillBonus(
                new AttributeBonus(
                    Attributes.MOVEMENT_SPEED,
                    "Upgrade",
                    0.01f,
                    AttributeModifier.Operation.MULTIPLY_BASE)))
        .save(consumer, getRecipeId("upgrades/boots_movespeed"));
    ItemUpgradeRecipeBuilder.create()
        .baseCondition(new EquipmentCondition(EquipmentCondition.Type.CHESTPLATE))
        .additionalItem(Ingredient.of(PSTItems.ANCIENT_ALLOY_CURATIVE.get()))
        .itemBonus(new ItemSkillBonus(new IncomingHealingBonus(0.02f)))
        .save(consumer, getRecipeId("upgrades/chestplates_incoming_healing"));
    ItemUpgradeRecipeBuilder.create()
        .baseCondition(new EquipmentCondition(EquipmentCondition.Type.WEAPON))
        .additionalItem(Ingredient.of(PSTItems.ANCIENT_ALLOY_TOXIC.get()))
        .itemBonus(
            new ItemSkillBonus(
                new AttributeBonus(
                    PSTAttributes.POISON_DAMAGE.get(),
                    "Upgrade",
                    0.02f,
                    AttributeModifier.Operation.MULTIPLY_BASE)))
        .save(consumer, getRecipeId("upgrades/weapons_poison_damage"));
    ItemUpgradeRecipeBuilder.create()
        .baseCondition(new EquipmentCondition(EquipmentCondition.Type.WEAPON))
        .additionalItem(Ingredient.of(PSTItems.ANCIENT_ALLOY_ENCHANTED.get()))
        .itemBonus(
            new ItemSkillBonus(
                new DamageBonus(0.02f, AttributeModifier.Operation.MULTIPLY_BASE)
                    .setDamageCondition(new MagicDamageCondition())))
        .save(consumer, getRecipeId("upgrades/weapons_magic_damage"));
    ItemUpgradeRecipeBuilder.create()
        .baseCondition(new EquipmentCondition(EquipmentCondition.Type.PICKAXE))
        .additionalItem(Ingredient.of(PSTItems.ANCIENT_ALLOY_GILDED.get()))
        .itemBonus(
            new ItemSkillBonus(
                new LootDuplicationBonus(0.02f, 1f, LootDuplicationBonus.LootType.GEMS)))
        .save(consumer, getRecipeId("upgrades/pickaxes_double_gems"));
  }

  protected void quiver(
      RegistryObject<Item> result, Item material, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result.get())
        .define('#', material)
        .define('l', Items.LEATHER)
        .define('s', Items.STRING)
        .pattern("#ls")
        .pattern("#ls")
        .pattern("#ls")
        .group(getItemName(result.get()))
        .unlockedBy(getHasName(material), has(material))
        .save(consumer, getRecipeId(result.get()));
  }

  protected void quiver(
      RegistryObject<Item> result, TagKey<Item> material, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result.get())
        .define('#', material)
        .define('l', Items.LEATHER)
        .define('s', Items.STRING)
        .pattern("#ls")
        .pattern("#ls")
        .pattern("#ls")
        .group(getItemName(result.get()))
        .unlockedBy(getHasName(material), has(material))
        .save(consumer, getRecipeId(result.get()));
  }

  protected void quiver(RegistryObject<Item> result, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result.get())
        .define('l', Items.LEATHER)
        .define('s', Items.STRING)
        .pattern("ls")
        .pattern("ls")
        .pattern("ls")
        .group(getItemName(result.get()))
        .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
        .save(consumer, getRecipeId(result.get()));
  }

  protected void necklace(
      RegistryObject<Item> result, Item material, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get())
        .define('#', material)
        .define('n', Tags.Items.NUGGETS_GOLD)
        .pattern("nnn")
        .pattern("n n")
        .pattern("n#n")
        .group(getItemName(result.get()))
        .unlockedBy(getHasName(material), has(material))
        .save(consumer, getRecipeId(result.get()));
  }

  protected void necklace(RegistryObject<Item> result, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get())
        .define('n', Tags.Items.NUGGETS_GOLD)
        .pattern("nnn")
        .pattern("n n")
        .pattern("nnn")
        .group(getItemName(result.get()))
        .unlockedBy(getHasName(Tags.Items.NUGGETS_GOLD), has(Tags.Items.NUGGETS_GOLD))
        .save(consumer, getRecipeId(result.get()));
  }

  protected void ring(
      RegistryObject<Item> result, TagKey<Item> material, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get())
        .define('#', material)
        .pattern(" # ")
        .pattern("# #")
        .pattern(" # ")
        .group(getItemName(result.get()))
        .unlockedBy(getHasName(material), has(material))
        .save(consumer, getRecipeId(result.get()));
  }

  protected void packing(Item result, TagKey<Item> material, Consumer<FinishedRecipe> consumer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
        .define('#', material)
        .pattern("###")
        .pattern("###")
        .pattern("###")
        .group(getItemName(result))
        .unlockedBy(getHasName(material), has(material))
        .save(consumer, getRecipeId(result));
  }

  protected void unpacking(
      RegistryObject<Item> result, TagKey<Item> material, Consumer<FinishedRecipe> consumer) {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), 9)
        .requires(material)
        .group(getItemName(result.get()))
        .unlockedBy(getHasName(material), has(material))
        .save(consumer, getRecipeId(result.get()));
  }

  protected String getHasName(TagKey<Item> material) {
    return "has_" + material.location().getPath().replaceAll("/", "_");
  }

  private ResourceLocation getRecipeId(Item item) {
    ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
    return new ResourceLocation(SkillTreeMod.MOD_ID, Objects.requireNonNull(id).getPath());
  }

  private ResourceLocation getRecipeId(String path) {
    return new ResourceLocation(SkillTreeMod.MOD_ID, path);
  }
}
