package daripher.skilltree;

import daripher.skilltree.compat.apotheosis.ApotheosisCompatibility;
import daripher.skilltree.compat.attributeslib.AttributesLibCompatibility;
import daripher.skilltree.config.ClientConfig;
import daripher.skilltree.config.ServerConfig;
import daripher.skilltree.init.*;
import daripher.skilltree.network.ModNetwork;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SkillTreeMod.MOD_ID)
public class SkillTreeMod {
  public static final String MOD_ID = "skilltree";
  public static final Logger LOGGER = LogManager.getLogger(SkillTreeMod.MOD_ID);

  public SkillTreeMod() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    PSTItems.REGISTRY.register(eventBus);
    PSTAttributes.REGISTRY.register(eventBus);
    PSTRecipeSerializers.REGISTRY.register(eventBus);
    PSTMobEffects.REGISTRY.register(eventBus);
    PSTCreativeTabs.REGISTRY.register(eventBus);
    PSTSkillBonuses.REGISTRY.register(eventBus);
    PSTLivingConditions.REGISTRY.register(eventBus);
    PSTLivingMultipliers.REGISTRY.register(eventBus);
    PSTDamageConditions.REGISTRY.register(eventBus);
    PSTItemBonuses.REGISTRY.register(eventBus);
    PSTItemConditions.REGISTRY.register(eventBus);
    PSTEnchantmentConditions.REGISTRY.register(eventBus);
    PSTLootPoolEntries.REGISTRY.register(eventBus);
    PSTGemBonuses.REGISTRY.register(eventBus);
    PSTEventListeners.REGISTRY.register(eventBus);
    PSTLootModifiers.REGISTRY.register(eventBus);
    PSTEnchantments.REGISTRY.register(eventBus);
    PSTNumericValueProviders.REGISTRY.register(eventBus);
    PSTPotions.REGISTRY.register(eventBus);
    ModNetwork.register();
    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, daripher.skilltree.config.ModConfig.COMMON_SPEC, "skilltree-common.toml");
    addCompatibilities();
  }

  public static boolean apotheosisEnabled() {
    return ModList.get().isLoaded("apotheosis")
        && ApotheosisCompatibility.INSTANCE.adventureModuleEnabled();
  }

  protected void addCompatibilities() {
    if (ModList.get().isLoaded("apotheosis")) ApotheosisCompatibility.INSTANCE.register();
    if (ModList.get().isLoaded("attributeslib")) AttributesLibCompatibility.INSTANCE.register();
  }
}
