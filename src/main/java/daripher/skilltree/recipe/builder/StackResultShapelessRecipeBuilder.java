package daripher.skilltree.recipe.builder;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import daripher.skilltree.init.PSTRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class StackResultShapelessRecipeBuilder extends CraftingRecipeBuilder {
  private final ItemStack result;
  private final int count;
  private final List<Ingredient> ingredients = Lists.newArrayList();
  private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
  @Nullable
  private String group;

  public StackResultShapelessRecipeBuilder(ItemStack pResult, int pCount) {
    this.result = pResult;
    this.count = pCount;
  }

  public static StackResultShapelessRecipeBuilder create(ItemStack pResult) {
    return new StackResultShapelessRecipeBuilder(pResult, 1);
  }

  public static StackResultShapelessRecipeBuilder create(ItemStack pResult, int pCount) {
    return new StackResultShapelessRecipeBuilder(pResult, pCount);
  }

  public StackResultShapelessRecipeBuilder requires(TagKey<Item> pTag) {
    return this.requires(Ingredient.of(pTag));
  }

  public StackResultShapelessRecipeBuilder requires(ItemLike pItem) {
    return this.requires(pItem, 1);
  }

  public StackResultShapelessRecipeBuilder requires(ItemLike pItem, int pQuantity) {
    for (int $$2 = 0; $$2 < pQuantity; ++$$2) {
      this.requires(Ingredient.of(pItem));
    }

    return this;
  }

  public StackResultShapelessRecipeBuilder requires(Ingredient pIngredient) {
    return this.requires(pIngredient, 1);
  }

  public StackResultShapelessRecipeBuilder requires(Ingredient pIngredient, int pQuantity) {
    for (int $$2 = 0; $$2 < pQuantity; ++$$2) {
      this.ingredients.add(pIngredient);
    }

    return this;
  }

  public StackResultShapelessRecipeBuilder group(@Nullable String pGroupName) {
    this.group = pGroupName;
    return this;
  }

  public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
    pFinishedRecipeConsumer.accept(new StackResultShapelessRecipeBuilder.Result(pRecipeId, this.result, this.count, this.group == null ? "" :
        this.group, this.ingredients, this.advancement));
  }

  public static class Result implements FinishedRecipe {
    private final ResourceLocation id;
    private final ItemStack result;
    private final int count;
    private final String group;
    private final List<Ingredient> ingredients;
    private final Advancement.Builder advancement;

    public Result(ResourceLocation pId, ItemStack pResult, int pCount, String pGroup, List<Ingredient> pIngredients,
                  Advancement.Builder pAdvancement) {
      this.id = pId;
      this.result = pResult;
      this.count = pCount;
      this.group = pGroup;
      this.ingredients = pIngredients;
      this.advancement = pAdvancement;
    }

    public void serializeRecipeData(@NotNull JsonObject pJson) {
      if (!this.group.isEmpty()) {
        pJson.addProperty("group", this.group);
      }
      JsonArray ingredientsJson = new JsonArray();
      for (Ingredient ingredient : this.ingredients) {
        ingredientsJson.add(ingredient.toJson());
      }
      pJson.add("ingredients", ingredientsJson);
      JsonObject itemJson = new JsonObject();
      Item resultItem = this.result.getItem();
      ResourceLocation itemId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(resultItem));
      itemJson.addProperty("item", itemId.toString());
      if (this.count > 1) {
        itemJson.addProperty("count", this.count);
      }
      if (this.result.getTag() != null) {
        itemJson.addProperty("nbt", this.result.getTag()
            .toString());
      }
      pJson.add("result", itemJson);
    }

    public @NotNull RecipeSerializer<?> getType() {
      return PSTRecipeSerializers.SHAPELESS_CRAFTING.get();
    }

    public @NotNull ResourceLocation getId() {
      return this.id;
    }

    @Nullable
    public JsonObject serializeAdvancement() {
      return this.advancement.serializeToJson();
    }

    @Nullable
    public ResourceLocation getAdvancementId() {
      return getId().withPrefix("recipes/");
    }
  }
}
