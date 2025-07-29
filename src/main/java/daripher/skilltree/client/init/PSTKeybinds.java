package daripher.skilltree.client.init;

import com.mojang.blaze3d.platform.InputConstants;
import daripher.skilltree.SkillTreeMod;
import daripher.skilltree.client.data.SkillTreeClientData;
import daripher.skilltree.client.screen.SkillTreeScreen;
import daripher.skilltree.config.ModConfig;
import daripher.skilltree.data.reloader.SkillTreesReloader;
import daripher.skilltree.skill.PassiveSkillTree;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.Map;

@Mod.EventBusSubscriber(modid = SkillTreeMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PSTKeybinds {
  // регистрируем бинд на O
  public static final KeyMapping SKILL_TREE_KEY = new KeyMapping(
          "key.display_skill_tree",
          InputConstants.Type.KEYSYM,
          GLFW.GLFW_KEY_O,
          "key.categories." + SkillTreeMod.MOD_ID
  );

  @SubscribeEvent
  public static void registerKeybinds(RegisterKeyMappingsEvent event) {
    event.register(SKILL_TREE_KEY);
  }

  // ловим нажатие
  @Mod.EventBusSubscriber(modid = SkillTreeMod.MOD_ID, value = Dist.CLIENT)
  public static class KeyEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
      Minecraft mc = Minecraft.getInstance();
      if (event.getAction() != GLFW.GLFW_PRESS) return;
      if (mc.screen != null || mc.player == null) return;
      if (!SKILL_TREE_KEY.isDown()) return;  // или event.getKey() == SKILL_TREE_KEY.getKey().getValue()

      // 1) Получаем selected_class из NBT
      CompoundTag root = mc.player.getPersistentData();
      CompoundTag forgeData = root.contains("ForgeData", Tag.TAG_COMPOUND)
              ? root.getCompound("ForgeData")
              : new CompoundTag();
      String selectedClass = forgeData.getString("selected_class");
      if (selectedClass.isEmpty()) {
        SkillTreeClientData.printMessage("Класс не выбран!", ChatFormatting.RED);
        return;
      }

      // 2) Берём маппинг из конфига
      Map<String, String> map = ModConfig.COMMON.getClassToTree();
      String treeLocation = map.get(selectedClass);
      if (treeLocation == null || treeLocation.isEmpty()) {
        SkillTreeClientData.printMessage(
                "Нет привязанного дерева для класса «" + selectedClass + "»",
                ChatFormatting.RED
        );
        return;
      }

      // 3) Формируем ResourceLocation и проверяем, что дерево существует
      ResourceLocation treeId = new ResourceLocation(treeLocation);
      PassiveSkillTree tree = SkillTreesReloader.getSkillTreeById(treeId);
      if (tree == null) {
        SkillTreeClientData.printMessage(
                "Дерево «" + treeLocation + "» не найдено!",
                ChatFormatting.DARK_RED
        );
        return;
      }

      // 4) Открываем экран
      mc.setScreen(new SkillTreeScreen(treeId));
    }
  }
}