package daripher.skilltree.compat.apotheosis;

import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.compat.apotheosis.gem.PSTGemBonus;
import daripher.skilltree.item.ItemHelper;
import daripher.skilltree.skill.bonus.item.ItemBonus;
import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.event.GetItemSocketsEvent;
import dev.shadowsoffire.apotheosis.adventure.loot.GemLootPoolEntry;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.socket.SocketHelper;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.Gem;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemInstance;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemItem;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.GemRegistry;
import dev.shadowsoffire.apotheosis.adventure.socket.gem.bonus.GemBonus;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import java.util.*;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public enum ApotheosisCompatibility {
  INSTANCE;

  public void register() {
    GemBonus.CODEC.register(
        new ResourceLocation(SkillTreeMod.MOD_ID, "gem_bonus"), PSTGemBonus.CODEC);
    IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
    forgeEventBus.addListener(this::addItemSockets);
  }

  public List<? extends ItemBonus<?>> getGemBonuses(ItemStack stack) {
    List<ItemBonus<?>> list = new ArrayList<>();
    for (GemInstance gemInstance : SocketHelper.getGems(stack)) {
      DynamicHolder<Gem> gem = GemItem.getGem(gemInstance.gemStack());
      if (!gem.isBound()) continue;
      List<GemBonus> bonuses = gem.get().getBonuses();
      for (GemBonus gemBonus : bonuses) {
        Set<LootCategory> lootCategories = gemBonus.getGemClass().types();
        if (lootCategories.stream().noneMatch(c -> c.isValid(stack))) continue;
        if (gemBonus instanceof PSTGemBonus aBonus) {
          list.add(aBonus.getBonus(gemInstance.gemStack()));
          break;
        }
      }
    }
    return list;
  }

  public void createGemStack(
      Consumer<ItemStack> consumer, LootContext context, ResourceLocation gemTypeId) {
    Player player = GemLootPoolEntry.findPlayer(context);
    if (player == null) return;
    ItemStack gemStack = getGemStack(gemTypeId);
    if (!gemStack.isEmpty()) {
      consumer.accept(gemStack);
    }
  }

  public ItemStack getGemStack(ResourceLocation gemTypeId) {
    Gem gem = GemRegistry.INSTANCE.getValue(gemTypeId);
    if (gem == null) return ItemStack.EMPTY;
    LootRarity rarity = gem.getMinRarity();
    if (rarity == null) return ItemStack.EMPTY;
    return GemRegistry.createGemStack(gem, rarity);
  }

  public @Nullable ResourceLocation getGemId(ItemStack stack) {
    return GemItem.getGem(stack).getOptional().map(Gem::getId).orElse(null);
  }

  public boolean adventureModuleEnabled() {
    return Apotheosis.enableAdventure;
  }

  //TODO: optimise
  private void addItemSockets(GetItemSocketsEvent event) {
    ItemStack stack = event.getStack();
    if (!ItemHelper.hasSockets(stack)) return;
    int sockets = event.getSockets();
    if (event.getSockets() == 0) {
      int defaultSockets = ItemHelper.getDefaultSockets(stack);
      SocketHelper.setSockets(stack, defaultSockets);
      sockets += defaultSockets;
    }
    sockets += ItemHelper.getAdditionalSockets(stack);
    CompoundTag affixTag = stack.getTagElement(AffixHelper.AFFIX_DATA);
    if (affixTag != null && affixTag.contains(SocketHelper.GEMS)) {
      ListTag gemsTag = affixTag.getList(SocketHelper.GEMS, Tag.TAG_COMPOUND);
      if (sockets < gemsTag.size()) sockets = gemsTag.size();
    }
    event.setSockets(sockets);
  }

  public List<ItemStack> getGemStacks(ItemStack stack) {
    return SocketHelper.getGems(stack).stream().map(GemInstance::gemStack).toList();
  }
}
