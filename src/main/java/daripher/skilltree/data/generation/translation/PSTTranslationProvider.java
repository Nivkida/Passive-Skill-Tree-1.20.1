package daripher.skilltree.data.generation.translation;

import daripher.skilltree.init.PSTRegistries;
import daripher.skilltree.skill.bonus.SkillBonus;
import daripher.skilltree.skill.bonus.condition.damage.DamageCondition;
import daripher.skilltree.skill.bonus.condition.enchantment.EnchantmentCondition;
import daripher.skilltree.skill.bonus.condition.item.ItemCondition;
import daripher.skilltree.skill.bonus.condition.living.LivingCondition;
import daripher.skilltree.skill.bonus.condition.living.numeric.NumericValueProvider;
import daripher.skilltree.skill.bonus.event.SkillEventListener;
import daripher.skilltree.skill.bonus.item.ItemBonus;
import daripher.skilltree.skill.bonus.multiplier.LivingMultiplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.Arrays;

public abstract class PSTTranslationProvider extends LanguageProvider {
  public PSTTranslationProvider(DataGenerator dataGenerator, String modId, String locale) {
    super(dataGenerator.getPackOutput(), modId, locale);
  }

  protected void addTooltip(Item item, String tooltip) {
    add(item.getDescriptionId() + ".tooltip", tooltip);
  }

  protected void addWarning(Item item, String tooltip) {
    add(item.getDescriptionId() + ".warning", tooltip);
  }

  protected void add(Attribute attribute, String name) {
    add(attribute.getDescriptionId(), name);
  }

  protected void addInfo(Attribute attribute, String name) {
    add(attribute.getDescriptionId() + ".info", "(%s)".formatted(name));
  }

  protected void addCurioSlot(String slotName, String name) {
    add("curio.slot.%s".formatted(slotName), name);
  }

  protected void addCurioSlot(String slotName, String type, String name) {
    add("curio.slot.%s.%s".formatted(slotName, type), name);
  }

  protected void addSkill(int skillId, String name) {
    add("skill.skilltree.new_skill_%d.name".formatted(skillId), name);
  }

  protected void addSkillBranch(String name, int from, int to) {
    for (int i = from; i <= to; i++) {
      addSkill(i, name);
    }
  }

  protected void addMixture(String name, String potionType, MobEffect... effects) {
    StringBuilder potionName = new StringBuilder("item.minecraft." + potionType + ".mixture");
    Arrays.stream(effects).map(MobEffect::getDescriptionId).map(id -> id.replaceAll("effect.", "")).forEach(id -> potionName.append(".").append(id));
    add(potionName.toString(), name);
  }

  protected void add(LivingCondition.Serializer condition, String value) {
    ResourceLocation id = PSTRegistries.LIVING_CONDITIONS.get().getKey(condition);
    assert id != null;
    String key = "living_condition.%s.%s".formatted(id.getNamespace(), id.getPath());
    add(key, value);
  }

  protected void add(LivingCondition.Serializer condition, String type, String value) {
    ResourceLocation id = PSTRegistries.LIVING_CONDITIONS.get().getKey(condition);
    assert id != null;
    String key = "living_condition.%s.%s.%s".formatted(id.getNamespace(), id.getPath(), type);
    add(key, value);
  }

  protected void add(SkillEventListener.Serializer condition, String value) {
    ResourceLocation id = PSTRegistries.EVENT_LISTENERS.get().getKey(condition);
    assert id != null;
    String key = "event_listener.%s.%s".formatted(id.getNamespace(), id.getPath());
    add(key, value);
  }

  protected void add(SkillEventListener.Serializer condition, String type, String value) {
    ResourceLocation id = PSTRegistries.EVENT_LISTENERS.get().getKey(condition);
    assert id != null;
    String key = "event_listener.%s.%s.%s".formatted(id.getNamespace(), id.getPath(), type);
    add(key, value);
  }

  protected void add(DamageCondition.Serializer condition, String type, String value) {
    ResourceLocation id = PSTRegistries.DAMAGE_CONDITIONS.get().getKey(condition);
    assert id != null;
    String key = "damage_condition.%s.%s.%s".formatted(id.getNamespace(), id.getPath(), type);
    add(key, value);
  }

  protected void add(DamageCondition.Serializer condition, String value) {
    ResourceLocation id = PSTRegistries.DAMAGE_CONDITIONS.get().getKey(condition);
    assert id != null;
    String key = "damage_condition.%s.%s".formatted(id.getNamespace(), id.getPath());
    add(key, value);
  }

  protected void add(LivingMultiplier.Serializer multiplier, String value) {
    ResourceLocation id = PSTRegistries.LIVING_MULTIPLIERS.get().getKey(multiplier);
    assert id != null;
    String key = "skill_bonus_multiplier.%s.%s".formatted(id.getNamespace(), id.getPath());
    add(key, value);
  }

  protected void add(LivingMultiplier.Serializer multiplier, String type, String value) {
    ResourceLocation id = PSTRegistries.LIVING_MULTIPLIERS.get().getKey(multiplier);
    assert id != null;
    String key = "skill_bonus_multiplier.%s.%s.%s".formatted(id.getNamespace(), id.getPath(), type);
    add(key, value);
  }

  protected void add(NumericValueProvider.Serializer provider, String value) {
    ResourceLocation id = PSTRegistries.NUMERIC_VALUE_PROVIDERS.get().getKey(provider);
    assert id != null;
    String key = "value_provider.%s.%s".formatted(id.getNamespace(), id.getPath());
    add(key, value);
  }

  protected void add(NumericValueProvider.Serializer provider, String type, String value) {
    ResourceLocation id = PSTRegistries.NUMERIC_VALUE_PROVIDERS.get().getKey(provider);
    assert id != null;
    String key = "value_provider.%s.%s.%s".formatted(id.getNamespace(), id.getPath(), type);
    add(key, value);
  }

  protected void add(SkillBonus.Serializer serializer, String value) {
    ResourceLocation id = PSTRegistries.SKILL_BONUSES.get().getKey(serializer);
    assert id != null;
    String key = "skill_bonus.%s.%s".formatted(id.getNamespace(), id.getPath());
    add(key, value);
  }

  protected void add(SkillBonus.Serializer serializer, String type, String value) {
    ResourceLocation id = PSTRegistries.SKILL_BONUSES.get().getKey(serializer);
    assert id != null;
    String key = "skill_bonus.%s.%s.%s".formatted(id.getNamespace(), id.getPath(), type);
    add(key, value);
  }

  protected void add(ItemBonus.Serializer serializer, String value) {
    ResourceLocation id = PSTRegistries.ITEM_BONUSES.get().getKey(serializer);
    assert id != null;
    String key = "item_bonus.%s.%s".formatted(id.getNamespace(), id.getPath());
    add(key, value);
  }

  protected void add(ItemCondition.Serializer serializer, String type, String value) {
    ResourceLocation id = PSTRegistries.ITEM_CONDITIONS.get().getKey(serializer);
    assert id != null;
    String key = "item_condition.%s.%s.%s".formatted(id.getNamespace(), id.getPath(), type);
    add(key, value);
  }

  protected void add(ItemCondition.Serializer serializer, String value) {
    ResourceLocation id = PSTRegistries.ITEM_CONDITIONS.get().getKey(serializer);
    assert id != null;
    String key = "item_condition.%s.%s".formatted(id.getNamespace(), id.getPath());
    add(key, value);
  }

  protected void add(TagKey<Item> itemTag, String value) {
    ResourceLocation id = itemTag.location();
    String key = "item_tag.%s".formatted(id.toString());
    add(key, value);
  }

  protected void add(TagKey<Item> itemTag, String type, String value) {
    ResourceLocation id = itemTag.location();
    String key = "item_tag.%s.%s".formatted(id.toString(), type);
    add(key, value);
  }

  protected void add(EnchantmentCondition.Serializer serializer, String value) {
    ResourceLocation id = PSTRegistries.ENCHANTMENT_CONDITIONS.get().getKey(serializer);
    assert id != null;
    String key = "enchantment_condition.%s.%s".formatted(id.getNamespace(), id.getPath());
    add(key, value);
  }

  protected void addGem(String type, String name, String... qualities) {
    for (int i = 0; i < qualities.length; i++) {
      add("item.skilltree.gem.skilltree." + type + "_" + i, qualities[i] + " " + name);
      add("item.apotheosis.gem.skilltree:" + type + "_" + i, name);
    }
  }

  protected void deathMessage(String damageType, String deathMessage) {
    add("death.attack." + damageType, deathMessage);
  }
}
