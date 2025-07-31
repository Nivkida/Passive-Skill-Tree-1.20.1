package daripher.skilltree.init;

import com.mojang.serialization.Codec;
import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.loot.modifier.AddItemModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PSTLootModifiers {
  public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> REGISTRY =
      DeferredRegister.create(
          ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SkillTreeMod.MOD_ID);

  public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
      REGISTRY.register("add_item", AddItemModifier.CODEC);

  public static void register(IEventBus eventBus) {
    REGISTRY.register(eventBus);
  }
}
