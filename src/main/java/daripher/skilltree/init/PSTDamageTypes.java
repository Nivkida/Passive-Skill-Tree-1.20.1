package daripher.skilltree.init;

import daripher.skilltree.SkillTreeMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class PSTDamageTypes {
  public static ResourceKey<DamageType> POISON = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(SkillTreeMod.MOD_ID, "poison"));
}
