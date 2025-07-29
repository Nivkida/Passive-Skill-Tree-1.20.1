package daripher.skilltree.network;

import com.mojang.logging.LogUtils;
import daripher.skilltree.config.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.Map;
import java.util.function.Supplier;

public class RequestSkillTreeOpenPacket {
    public RequestSkillTreeOpenPacket() {}

    public RequestSkillTreeOpenPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    private static final Logger LOGGER = LogUtils.getLogger();

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player = ctx.get().getSender();
        if (player == null) return;

        ctx.get().enqueueWork(() -> {
            CompoundTag persisted = player.getPersistentData().getCompound("PlayerPersisted");
            String selectedClass = persisted.getString("selected_class");

            if (selectedClass == null || selectedClass.isEmpty()) {
                player.displayClientMessage(Component.literal("Вы не выбрали класс!")
                        .withStyle(ChatFormatting.RED), false);
                return;
            }

            Map<String, String> mapping = ModConfig.COMMON.getClassToTree();
            String treeStr = mapping.get(selectedClass);

            if (treeStr == null || treeStr.isEmpty()) {
                player.displayClientMessage(Component.literal("Для класса \"" + selectedClass + "\" не найдено дерево навыков.")
                        .withStyle(ChatFormatting.RED), false);
                return;
            }

            ResourceLocation treeId;
            try {
                treeId = new ResourceLocation(treeStr);
            } catch (Exception e) {
                player.displayClientMessage(Component.literal("Некорректный идентификатор дерева навыков: " + treeStr)
                        .withStyle(ChatFormatting.DARK_RED), false);
                LOGGER.warn("Invalid skill tree ID: {}", treeStr, e);
                return;
            }

            ModNetwork.sendToClient(player, new OpenSkillTreePacket(treeId));
        });

        ctx.get().setPacketHandled(true);
    }
}

