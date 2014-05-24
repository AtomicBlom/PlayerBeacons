package kihira.playerbeacons.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import kihira.playerbeacons.common.tileentity.TileEntityPlayerBeacon;
import kihira.playerbeacons.common.util.BeaconDataHelper;

public class TickHandler {

    private short cycle = 0;

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side.isServer() && event.player.worldObj.getTotalWorldTime() % 20 == 0) {
            TileEntityPlayerBeacon playerBeacon = BeaconDataHelper.getBeaconForDim(event.player, event.player.worldObj.provider.dimensionId);
            if (playerBeacon != null) {
                playerBeacon.checkBeacon();
                if (this.cycle % 2 == 0) {
                    if (!PlayerBeacons.config.disableCorruption) {
                        playerBeacon.calcPylons();
                        playerBeacon.applyCorruption();
                    }
                    if (!playerBeacon.getOwner().equals(" ")) playerBeacon.doEffects();
                    if (this.cycle % 4 == 0) event.player.worldObj.markBlockForUpdate(playerBeacon.xCoord, playerBeacon.yCoord, playerBeacon.zCoord);
                }
                if (this.cycle >= 32000) this.cycle = 0;
            }
        }
        else if (event.side.isClient() && PlayerBeacons.config.enableHideParticleEffects && event.player.worldObj.getTotalWorldTime() % 10 == 0) {
            event.player.getDataWatcher().updateObject(7, 0);
        }
    }
}