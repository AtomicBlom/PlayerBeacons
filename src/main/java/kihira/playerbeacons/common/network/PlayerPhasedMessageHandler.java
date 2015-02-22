package kihira.playerbeacons.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import kihira.playerbeacons.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

public class PlayerPhasedMessageHandler implements IMessageHandler<PlayerPhasedMessage, IMessage>
{

    @Override
    public IMessage onMessage(PlayerPhasedMessage message, MessageContext ctx) {
        AbstractClientPlayer player = (AbstractClientPlayer) Minecraft.getMinecraft().theWorld.getPlayerEntityByName(message.playerName);

        if (player != null) {
            if (Minecraft.getMinecraft().thePlayer.getCommandSenderName().equals(message.playerName)) {
                ClientProxy.playerPhased = message.isPhased;
            }
        }
        return null;
    }
}
