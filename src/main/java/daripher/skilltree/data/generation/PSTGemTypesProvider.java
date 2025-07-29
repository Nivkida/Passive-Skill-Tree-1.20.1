package daripher.skilltree.data.generation;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.compat.apotheosis.gem.PSTGemBonus;
import daripher.skilltree.data.reloader.GemTypesReloader;
import daripher.skilltree.init.PSTAttributes;
import daripher.skilltree.init.PSTTags;
import daripher.skilltree.item.gem.GemType;
import daripher.skilltree.item.gem.bonus.GemBonusProvider;
import daripher.skilltree.item.gem.bonus.GemRemovalBonusProvider;
import daripher.skilltree.item.gem.bonus.RandomGemBonusProvider;
import daripher.skilltree.item.gem.bonus.SimpleGemBonusProvider;
import daripher.skilltree.skill.bonus.SkillBonus;
import daripher.skilltree.skill.bonus.condition.damage.FallDamageCondition;
import daripher.skilltree.skill.bonus.condition.damage.MeleeDamageCondition;
import daripher.skilltree.skill.bonus.condition.damage.ProjectileDamageCondition;
import daripher.skilltree.skill.bonus.condition.item.*;
import daripher.skilltree.skill.bonus.event.AttackEventListener;
import daripher.skilltree.skill.bonus.event.BlockEventListener;
import daripher.skilltree.skill.bonus.item.ItemBonus;
import daripher.skilltree.skill.bonus.item.ItemDurabilityBonus;
import daripher.skilltree.skill.bonus.item.ItemSkillBonus;
import daripher.skilltree.skill.bonus.player.*;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemClass;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.bonus.GemBonus;
import dev.shadowsoffire.placebo.codec.PlaceboCodecs;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

public class PSTGemTypesProvider implements DataProvider {
  private final Map<ResourceLocation, GemType> gemTypes = new HashMap<>();
  private final Map<ResourceLocation, ApothGem> gems = new HashMap<>();
  private final PackOutput packOutput;
  private static final Codec<ApothGem> APOTH_GEM_CODEC =
      RecordCodecBuilder.create(
          (instance) ->
              instance
                  .group(
                      Codec.intRange(0, Integer.MAX_VALUE)
                          .fieldOf("weight")
                          .forGetter(ApothGem::weight),
                      PlaceboCodecs.nullableField(
                              Codec.floatRange(0.0F, Float.MAX_VALUE), "quality", 0.0F)
                          .forGetter(ApothGem::quality),
                      PlaceboCodecs.nullableField(
                              PlaceboCodecs.setOf(Codec.STRING),
                              "dimensions",
                              Collections.emptySet())
                          .forGetter(ApothGem::dimensions),
                      PlaceboCodecs.nullableField(Codec.STRING, "min_rarity")
                          .forGetter(ApothGem::minRarity),
                      PlaceboCodecs.nullableField(Codec.STRING, "max_rarity")
                          .forGetter(ApothGem::maxRarity),
                      GemBonus.CODEC.listOf().fieldOf("bonuses").forGetter(ApothGem::bonuses))
                  .apply(instance, ApothGem::new));
  private static final String[] APOTH_RARITIES = {
    "common", "uncommon", "rare", "epic", "mythic", "ancient"
  };

  public PSTGemTypesProvider(DataGenerator dataGenerator) {
    this.packOutput = dataGenerator.getPackOutput();
  }

  private void addGemTypes() {
    addSimpleGems(
        "sapphire",
        createGemBonuses(
            new ItemSkillBonus(new DamageBonus(0.01f, AttributeModifier.Operation.MULTIPLY_BASE)),
            new ItemSkillBonus(
                new DamageTakenBonus(-0.01f, AttributeModifier.Operation.MULTIPLY_BASE)),
            new ItemSkillBonus(
                new AttributeBonus(
                    Attributes.ARMOR,
                    "Sapphire",
                    0.01f,
                    AttributeModifier.Operation.MULTIPLY_BASE)),
            new ItemSkillBonus(
                new AttributeBonus(
                    Attributes.KNOCKBACK_RESISTANCE,
                    "Sapphire",
                    0.01f,
                    AttributeModifier.Operation.ADDITION)),
            new ItemDurabilityBonus(10, AttributeModifier.Operation.ADDITION),
            new ItemSkillBonus(
                new AttributeBonus(
                    Attributes.ARMOR_TOUGHNESS,
                    "Sapphire",
                    0.01f,
                    AttributeModifier.Operation.MULTIPLY_BASE))));
    addSimpleGems(
        "jade",
        createGemBonuses(
            new ItemSkillBonus(
                new AttributeBonus(
                    Attributes.ATTACK_SPEED,
                    "Jade",
                    0.01f,
                    AttributeModifier.Operation.MULTIPLY_BASE)),
            new ItemSkillBonus(new DamageAvoidanceBonus(0.01f)),
            new ItemSkillBonus(
                new DamageAvoidanceBonus(0.01f)
                    .setDamageCondition(new ProjectileDamageCondition())),
            new ItemSkillBonus(
                new DamageAvoidanceBonus(0.1f).setDamageCondition(new FallDamageCondition())),
            new ItemSkillBonus(
                new DamageAvoidanceBonus(0.01f).setDamageCondition(new MeleeDamageCondition())),
            new ItemSkillBonus(
                new GainedExperienceBonus(0.05f, GainedExperienceBonus.ExperienceSource.MOBS))));
    addSimpleGems(
        "ruby",
        createGemBonuses(
            new ItemSkillBonus(
                new HealingBonus(
                    1f, 0.5f, new AttackEventListener().setTarget(SkillBonus.Target.PLAYER))),
            new ItemSkillBonus(new IncomingHealingBonus(0.01f)),
            new ItemSkillBonus(
                new AttributeBonus(
                    Attributes.MAX_HEALTH,
                    "Ruby",
                    0.01f,
                    AttributeModifier.Operation.MULTIPLY_BASE)),
            new ItemSkillBonus(
                new AttributeBonus(
                    PSTAttributes.REGENERATION.get(),
                    "Ruby",
                    0.1f,
                    AttributeModifier.Operation.ADDITION)),
            new ItemSkillBonus(
                new HealingBonus(
                    1f, 0.1f, new BlockEventListener().setTarget(SkillBonus.Target.PLAYER))),
            new ItemSkillBonus(new IncomingHealingBonus(0.005f))));
    addSimpleGems(
        "citrine",
        createGemBonuses(
            new ItemSkillBonus(new CritChanceBonus(0.01f)),
            new ItemSkillBonus(
                new AttributeBonus(
                    Attributes.LUCK, "Citrine", 0.5f, AttributeModifier.Operation.ADDITION)),
            new ItemSkillBonus(
                new LootDuplicationBonus(0.01f, 2f, LootDuplicationBonus.LootType.GEMS)),
            new ItemSkillBonus(new JumpHeightBonus(0.01f)),
            new ItemSkillBonus(
                new AttributeBonus(
                    Attributes.LUCK, "Citrine", 0.025f, AttributeModifier.Operation.MULTIPLY_BASE)),
            new ItemSkillBonus(
                new LootDuplicationBonus(0.01f, 1f, LootDuplicationBonus.LootType.MOBS))));
    Map<Integer, List<GemBonusProvider>> irisciteBonuses = new HashMap<>();
    gemTypes.forEach(
        (key, value) -> {
          String gemId = key.getPath();
          int gemTier = Integer.parseInt(gemId.substring(gemId.length() - 1));
          List<GemBonusProvider> providers =
              irisciteBonuses.getOrDefault(gemTier, new ArrayList<>());
          providers.addAll(value.bonuses().values());
          irisciteBonuses.put(gemTier, providers);
        });
    for (int i = 0; i < 6; i++) {
      ResourceLocation id = new ResourceLocation(SkillTreeMod.MOD_ID, "iriscite_" + i);
      Map<ItemCondition, GemBonusProvider> bonuses =
          Map.of(NoneItemCondition.INSTANCE, new RandomGemBonusProvider(irisciteBonuses.get(i)));
      gemTypes.put(id, new GemType(id, bonuses));
    }
    ResourceLocation vacuciteId = new ResourceLocation(SkillTreeMod.MOD_ID, "vacucite_3");
    Map<ItemCondition, GemBonusProvider> vacuciteBonuses =
        Map.of(NoneItemCondition.INSTANCE, new GemRemovalBonusProvider());
    gemTypes.put(vacuciteId, new GemType(vacuciteId, vacuciteBonuses));
  }

  private void addSimpleGems(
      String name, List<Triple<ItemCondition, GemClass, ItemBonus<?>>> bonuses) {
    for (int i = 0; i < 6; i++) {
      ResourceLocation id = new ResourceLocation(SkillTreeMod.MOD_ID, "%s_%d".formatted(name, i));
      HashMap<ItemCondition, GemBonusProvider> bonusProviders = new HashMap<>();
      int tier = i;
      bonuses.forEach(
          t -> {
            SimpleGemBonusProvider bonusProvider =
                new SimpleGemBonusProvider(t.getRight().copy().multiply(tier < 5 ? 1 + tier : 10));
            bonusProviders.put(t.getLeft(), bonusProvider);
          });
      gemTypes.put(id, new GemType(id, bonusProviders));
      Set<String> dimensions = Set.of("skilltree:fake_dimension");
      List<GemBonus> gemBonuses = new ArrayList<>();
      bonuses.forEach(
          t -> {
            GemBonus gemBonus =
                new PSTGemBonus(
                    t.getMiddle(), t.getRight().copy().multiply(tier < 5 ? 1 + tier : 10));
            gemBonuses.add(gemBonus);
          });
      Optional<String> rarity = Optional.of(APOTH_RARITIES[i]);
      gems.put(id, new ApothGem(0, 0, dimensions, rarity, rarity, gemBonuses));
    }
  }

  private List<Triple<ItemCondition, GemClass, ItemBonus<?>>> createGemBonuses(
      ItemBonus<?> weaponBonus,
      ItemBonus<?> chestplateBonus,
      ItemBonus<?> helmetBonus,
      ItemBonus<?> bootsBonus,
      ItemBonus<?> shieldBonus,
      ItemBonus<?> jewelryBonus) {
    List<Triple<ItemCondition, GemClass, ItemBonus<?>>> bonuses = new ArrayList<>();
    GemClass weaponClass =
        new GemClass(
            "weapon",
            Set.of(
                LootCategory.HEAVY_WEAPON,
                LootCategory.SWORD,
                LootCategory.BOW,
                LootCategory.CROSSBOW,
                LootCategory.TRIDENT));
    GemClass chestplateClass = new GemClass("chestplate", Set.of(LootCategory.CHESTPLATE));
    GemClass helmetClass = new GemClass("helmet", Set.of(LootCategory.HELMET));
    GemClass bootsClass = new GemClass("boots", Set.of(LootCategory.BOOTS));
    GemClass shieldsClass = new GemClass("shield", Set.of(LootCategory.SHIELD));
    GemClass jewelryClass =
        new GemClass(
            "jewelry",
            Set.of(
                Objects.requireNonNull(LootCategory.byId("curios:necklace")),
                Objects.requireNonNull(LootCategory.byId("curios:ring"))));
    bonuses.add(
        Triple.of(
            new EquipmentCondition(EquipmentCondition.Type.WEAPON), weaponClass, weaponBonus));
    bonuses.add(
        Triple.of(
            new EquipmentCondition(EquipmentCondition.Type.CHESTPLATE),
            chestplateClass,
            chestplateBonus));
    bonuses.add(
        Triple.of(
            new EquipmentCondition(EquipmentCondition.Type.HELMET), helmetClass, helmetBonus));
    bonuses.add(
        Triple.of(new EquipmentCondition(EquipmentCondition.Type.BOOTS), bootsClass, bootsBonus));
    bonuses.add(
        Triple.of(
            new EquipmentCondition(EquipmentCondition.Type.SHIELD), shieldsClass, shieldBonus));
    bonuses.add(
        Triple.of(new ItemTagCondition(PSTTags.Items.JEWELRY.location()), jewelryClass, jewelryBonus));
    return bonuses;
  }

  @Override
  public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
    ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();
    addGemTypes();
    gemTypes.values().forEach(gemType -> futuresBuilder.add(save(output, gemType)));
    gems.forEach((id, gem) -> futuresBuilder.add(save(output, gem, id)));
    return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
  }

  private CompletableFuture<?> save(CachedOutput output, GemType gemType) {
    Path path = packOutput.getOutputFolder().resolve(getPath(gemType));
    JsonElement json = GemTypesReloader.GSON.toJsonTree(gemType);
    return DataProvider.saveStable(output, json, path);
  }

  private CompletableFuture<?> save(CachedOutput output, ApothGem gem, ResourceLocation id) {
    Path path = packOutput.getOutputFolder().resolve(getPath(id));
    JsonElement json =
        APOTH_GEM_CODEC
            .encode(gem, JsonOps.INSTANCE, new JsonObject())
            .result()
            .orElse(new JsonObject());
    return DataProvider.saveStable(output, json, path);
  }

  public String getPath(GemType gemType) {
    ResourceLocation id = gemType.id();
    return "data/%s/gem_types/%s.json".formatted(id.getNamespace(), id.getPath());
  }

  public String getPath(ResourceLocation id) {
    return "data/%s/gems/%s.json".formatted(id.getNamespace(), id.getPath());
  }

  public Map<ResourceLocation, GemType> getGemTypes() {
    return gemTypes;
  }

  @Override
  public @NotNull String getName() {
    return "Gem Types Provider";
  }

  private record ApothGem(
      int weight,
      float quality,
      Set<String> dimensions,
      Optional<String> minRarity,
      Optional<String> maxRarity,
      List<GemBonus> bonuses) {}
}
