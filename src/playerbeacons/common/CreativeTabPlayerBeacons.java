package playerbeacons.common;

import net.minecraft.creativetab.CreativeTabs;

public class CreativeTabPlayerBeacons extends CreativeTabs {

	public CreativeTabPlayerBeacons() {
		super("playerbeacons");
	}

	public int getTabIconItemIndex() {
		return PlayerBeacons.playerBeaconBlock.blockID;
	}
}