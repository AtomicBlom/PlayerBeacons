package kihira.playerbeacons.common.command;

import cpw.mods.fml.common.network.NetworkRegistry;
import kihira.playerbeacons.api.BeaconDataHelper;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.network.PlayerPhasedMessage;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;

public class CommandPlayerPhase extends CommandBase
{

    @Override
    public String getCommandName() {
        return "playerphased";
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender) {
        return "commands.playerphased.usage";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {


        if (args != null) {
            if (args.length < 1) {
                throw new WrongUsageException("commands.playerphased.usage", args);
            }
            boolean isPhased;
            EntityPlayer player = commandSender.getEntityWorld().getPlayerEntityByName(commandSender.getCommandSenderName());
            if (args.length >= 2) {
                player = commandSender.getEntityWorld().getPlayerEntityByName(args[0]);
                isPhased = Boolean.parseBoolean(args[1]);
            } else {
                isPhased = Boolean.parseBoolean(args[0]);
            }

            if (player == null) {
                throw new WrongUsageException("commands.playerhead.usage", args);
            }

            BeaconDataHelper.setPlayerPhased(player, isPhased);
            PlayerBeacons.networkWrapper.sendToAllAround(new PlayerPhasedMessage(player.getCommandSenderName(), isPhased), new NetworkRegistry.TargetPoint(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, 64));
        }
    }
}
