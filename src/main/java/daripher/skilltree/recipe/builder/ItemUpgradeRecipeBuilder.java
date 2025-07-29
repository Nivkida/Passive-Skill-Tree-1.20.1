package daripher.skilltree.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import daripher.skilltree.data.serializers.SerializationHelper;
import daripher.skilltree.init.PSTRecipeSerializers;
import daripher.skilltree.skill.bonus.condition.item.ItemCondition;
import daripher.skilltree.skill.bonus.condition.item.NoneItemCondition;
import daripher.skilltree.skill.bonus.item.ItemBonus;
import daripher.skilltree.skill.bonus.item.ItemSocketsBonus;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemUpgradeRecipeBuilder {
  private ItemCondition baseCondition = NoneItemCondition.INSTANCE;
  private Ingredient additionalItem = Ingredient.EMPTY;
  private ItemBonus<?> itemBonus = new ItemSocketsBonus(1);
  private int maxUpgrades = 10;
  private float[] upgradeChances = {1f, 0.9f, 0.8f, 0.7f, 0.5f, 0.4f};

  public static ItemUpgradeRecipeBuilder create() {
    return new ItemUpgradeRecipeBuilder();
  }

  public ItemUpgradeRecipeBuilder baseCondition(ItemCondition baseCondition) {
    this.baseCondition = baseCondition;
    return this;
  }

  public ItemUpgradeRecipeBuilder additionalItem(Ingredient additionalItem) {
    this.additionalItem = additionalItem;
    return this;
  }

  public ItemUpgradeRecipeBuilder maxUpgrades(int maxUpgrades) {
    this.maxUpgrades = maxUpgrades;
    return this;
  }

  public ItemUpgradeRecipeBuilder upgradeChances(float... upgradeChances) {
    this.upgradeChances = upgradeChances;
    return this;
  }

  public ItemUpgradeRecipeBuilder itemBonus(ItemBonus<?> itemBonus) {
    this.itemBonus = itemBonus;
    return this;
  }

  public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
    if (additionalItem.isEmpty()) {
      throw new IllegalStateException("Addition ingredient is empty!");
    }
    consumer.accept(
        new Result(id, baseCondition, additionalItem, itemBonus, maxUpgrades, upgradeChances));
  }

  private static class Result implements FinishedRecipe {
    private final ResourceLocation id;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private final ItemCondition baseCondition;
    private final Ingredient addition;
    private final ItemBonus<?> itemBonus;
    private final int maxUpgrades;
    private final float[] upgradeChances;

    private Result(
        ResourceLocation id,
        ItemCondition baseCondition,
        Ingredient addition,
        ItemBonus<?> itemBonus,
        int maxUpgrades,
        float[] upgradeChances) {
      this.id = id;
      this.baseCondition = baseCondition;
      this.addition = addition;
      this.itemBonus = itemBonus;
      this.maxUpgrades = maxUpgrades;
      this.upgradeChances = upgradeChances;
    }

    @Override
    public void serializeRecipeData(@NotNull JsonObject jsonObject) {
      SerializationHelper.serializeItemCondition(jsonObject, baseCondition);
      jsonObject.add("addition", addition.toJson());
      SerializationHelper.serializeItemBonus(jsonObject, itemBonus);
      jsonObject.addProperty("max_upgrades", maxUpgrades);
      JsonArray upgradeChancesJson = new JsonArray();
      for (float upgradeChance : upgradeChances) {
        upgradeChancesJson.add(upgradeChance);
      }
      jsonObject.add("upgrade_chances", upgradeChancesJson);
    }

    @Override
    public @NotNull ResourceLocation getId() {
      return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getType() {
      return PSTRecipeSerializers.ITEM_UPGRADE.get();
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
      return advancement.serializeToJson();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
      return getId().withPrefix("recipes/upgrades/");
    }
  }
}
