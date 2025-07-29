package daripher.skilltree.recipe.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class StackResultShapedRecipeBuilder extends CraftingRecipeBuilder {
  private final ItemStack result;
  private final int count;
  private final List<String> rows = Lists.newArrayList();
  private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
  @Nullable
  private String group;

  public StackResultShapedRecipeBuilder(ItemStack pResult, int pCount) {
    this.result = pResult;
    this.count = pCount;
  }

  public static StackResultShapedRecipeBuilder create(ItemStack pResult) {
    return create(pResult, 1);
  }

  public static StackResultShapedRecipeBuilder create(ItemStack pResult, int pCount) {
    return new StackResultShapedRecipeBuilder(pResult, pCount);
  }

  public StackResultShapedRecipeBuilder define(Character pSymbol, TagKey<Item> pTag) {
    return this.define(pSymbol, Ingredient.of(pTag));
  }

  public StackResultShapedRecipeBuilder define(Character pSymbol, ItemLike pItem) {
    return this.define(pSymbol, Ingredient.of(pItem));
  }

  public StackResultShapedRecipeBuilder define(Character pSymbol, Ingredient pIngredient) {
    if (this.key.containsKey(pSymbol)) {
      throw new IllegalArgumentException("Symbol '" + pSymbol + "' is already defined!");
    }
    else if (pSymbol == ' ') {
      throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
    }
    else {
      this.key.put(pSymbol, pIngredient);
      return this;
    }
  }

  public StackResultShapedRecipeBuilder pattern(String pPattern) {
    if (!this.rows.isEmpty() && pPattern.length() != this.rows.get(0)
        .length()) {
      throw new IllegalArgumentException("Pattern must be the same width on every line!");
    }
    else {
      this.rows.add(pPattern);
      return this;
    }
  }

  public StackResultShapedRecipeBuilder group(@Nullable String pGroupName) {
    this.group = pGroupName;
    return this;
  }

  public ItemStack getResult() {
    return this.result;
  }

  public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
    this.ensureValid(pRecipeId);
    pFinishedRecipeConsumer.accept(new StackResultShapedRecipeBuilder.Result(pRecipeId, this.result, this.count, this.group == null ? "" :
        this.group, this.rows, this.key));
  }

  private void ensureValid(ResourceLocation pId) {
    if (this.rows.isEmpty()) {
      throw new IllegalStateException("No pattern is defined for shaped recipe " + pId + "!");
    }
    else {
      Set<Character> $$1 = Sets.newHashSet(this.key.keySet());
      $$1.remove(' ');

      for (String $$2 : this.rows) {
        for (int $$3 = 0; $$3 < $$2.length(); ++$$3) {
          char $$4 = $$2.charAt($$3);
          if (!this.key.containsKey($$4) && $$4 != ' ') {
            throw new IllegalStateException("Pattern in recipe " + pId + " uses undefined symbol '" + $$4 + "'");
          }

          $$1.remove($$4);
        }
      }

      if (!$$1.isEmpty()) {
        throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + pId);
      }
      else if (this.rows.size() == 1 && this.rows.get(0)
          .length() == 1) {
        throw new IllegalStateException("Shaped recipe " + pId + " only takes in a single item - should it be a shapeless recipe instead?");
      }
    }
  }

  public static class Result implements FinishedRecipe {
    private final ResourceLocation id;
    private final ItemStack result;
    private final int count;
    private final String group;
    private final List<String> pattern;
    private final Map<Character, Ingredient> key;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    public Result(ResourceLocation pId, ItemStack pResult, int pCount, String pGroup, List<String> pPattern, Map<Character, Ingredient> pKey) {
      this.id = pId;
      this.result = pResult;
      this.count = pCount;
      this.group = pGroup;
      this.pattern = pPattern;
      this.key = pKey;
    }

    public void serializeRecipeData(@NotNull JsonObject pJson) {
      if (!this.group.isEmpty()) {
        pJson.addProperty("group", this.group);
      }

      JsonArray $$1 = new JsonArray();

      for (String $$2 : this.pattern) {
        $$1.add($$2);
      }

      pJson.add("pattern", $$1);
      JsonObject $$3 = new JsonObject();

      for (Map.Entry<Character, Ingredient> characterIngredientEntry : this.key.entrySet()) {
        $$3.add(String.valueOf(characterIngredientEntry.getKey()), characterIngredientEntry.getValue()
            .toJson());
      }

      pJson.add("key", $$3);
      JsonObject $$5 = new JsonObject();
      Item resultItem = this.result.getItem();
      ResourceLocation itemId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(resultItem));
      $$5.addProperty("item", itemId.toString());
      if (this.count > 1) {
        $$5.addProperty("count", this.count);
      }
      if (this.result.getTag() != null) {
        $$5.addProperty("nbt", this.result.getTag()
            .toString());
      }
      pJson.add("result", $$5);
      pJson.addProperty("show_notification", false);
    }

    public @NotNull RecipeSerializer<?> getType() {
      return PSTRecipeSerializers.SHAPED_CRAFTING.get();
    }

    public @NotNull ResourceLocation getId() {
      return this.id;
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
      return advancement.serializeToJson();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
      return getId().withPrefix("recipes/");
    }
  }
}
