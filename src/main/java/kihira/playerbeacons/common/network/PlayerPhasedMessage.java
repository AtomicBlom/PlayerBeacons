package kihira.playerbeacons.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

public class PlayerPhasedMessage implements IMessage {

    String playerName;
    boolean isPhased;

    public PlayerPhasedMessage() {}
    public PlayerPhasedMessage(String playerName, boolean isPhased) {

        this.playerName = playerName;
        this.isPhased = isPhased;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerName = ByteBufUtils.readUTF8String(buf);
        this.isPhased = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.playerName);
        buf.writeBoolean(this.isPhased);
    }

}
